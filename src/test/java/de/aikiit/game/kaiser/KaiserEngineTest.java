package de.aikiit.game.kaiser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KaiserEngineTest {
    @Test
    void initialReportMatchesBasicWithoutOverwritingHarvestOrRats() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom());
        engine.startNewRound();
        assertThat(engine.getYear()).isEqualTo(1);
        assertThat(engine.getPopulation()).isEqualByComparingTo("100");
        assertThat(engine.getLandArea()).isEqualByComparingTo("1000");
        assertThat(engine.getHarvestYield()).isEqualByComparingTo("3");
        assertThat(engine.getTotalHarvest()).isEqualByComparingTo("3000");
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2800");
        assertThat(engine.getRatLoss()).isEqualByComparingTo("200");
        assertThat(engine.getStarvationDeaths()).isZero();
        assertThat(engine.getImmigrants()).isEqualByComparingTo("5");
    }

    @ParameterizedTest
    @CsvSource({"0,17", "0.999999,26"})
    void landPriceUsesBasicRange(double roll, String expected) {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(roll));
        engine.startNewRound();
        engine.prepareLandTrading();
        assertThat(engine.getLandPrice()).isEqualByComparingTo(expected);
        assertThat(engine.getHarvestYield()).isEqualByComparingTo("3");
    }

    @Test
    void feedingAndImmigrationFollowBasicTraceAndLandPersists() {
        // Price 17, yield 3, rat roll 3 (no damage), immigration factor 1, no plague.
        BasicRandom random = new BasicRandom(0.0, 0.4, 0.4, 0.0, 0.5);
        KaiserEngine engine = new KaiserEngine(random);
        engine.startNewRound();
        engine.prepareLandTrading();
        engine.buyLand(10L);
        engine.feedToPopulation(1800L);
        engine.cultivate(900L);
        engine.finishRoundAfterActions();
        // S=2800-170-1800-450+2700=3080; I=INT((20*1010+3080)/100/100+1)=3.
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("3080");
        assertThat(engine.getImmigrants()).isEqualByComparingTo("3");
        assertThat(engine.getPopulation()).isEqualByComparingTo("90");
        assertThat(engine.getStarvationDeaths()).isEqualByComparingTo("10");
        assertThat(engine.getTotalStarvationDeaths()).isEqualByComparingTo("10");
        assertThat(engine.getAverageStarvationPercentage()).isEqualByComparingTo("10");
        engine.startNewRound();
        assertThat(engine.getPopulation()).isEqualByComparingTo("93");
        assertThat(engine.getLandArea()).isEqualByComparingTo("1010");
        assertThat(engine.getRatLoss()).isZero();
        assertThat(engine.getHarvestYield()).isEqualByComparingTo("3");
        assertThat(random.remaining()).isZero();
    }

    @ParameterizedTest
    @CsvSource({"0,-1,true", "0.03,0,true", "0.129,0,true", "0.131,1,false", "0.999,9,false"})
    void plagueIsRolledAtYearEndAndAppliedIncludingZero(double roll, int plagueRoll, boolean plague) {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0, 0.0, 0.0, roll));
        engine.startNewRound();
        engine.feedToPopulation(2000L);
        engine.cultivate(0L);
        engine.finishRoundAfterActions();
        // S=800, I=3: next year's population is 103, or INT(103/2)=51.
        assertThat(engine.getPlagueRoll()).isEqualTo(plagueRoll);
        engine.startNewRound();
        assertThat(engine.isPlagueOccurred()).isEqualTo(plague);
        assertThat(engine.getPopulation()).isEqualByComparingTo(plague ? "51" : "103");
        assertThat(engine.getTotalStarvationDeaths()).isZero();
    }

    @Test
    void overfeedingDoesNotIncreasePopulationAndSkipsDeathStatistics() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0, 0.0, 0.0, 0.5));
        engine.startNewRound();
        engine.feedToPopulation(2020L);
        engine.cultivate(0L);
        engine.finishRoundAfterActions();
        assertThat(engine.getPopulation()).isEqualByComparingTo("100");
        assertThat(engine.getStarvationDeaths()).isZero();
        assertThat(engine.getAverageStarvationPercentage()).isZero();
    }

    @ParameterizedTest
    @CsvSource({"1100,RUNNING,45", "1080,DISMISSED,46", "0,DISMISSED,100"})
    void dismissalRequiresMoreThanFortyFivePercent(long food, KaiserEngine.GameState state, String deaths) {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0, 0.0, 0.0, 0.5));
        engine.startNewRound();
        engine.feedToPopulation(food);
        engine.cultivate(0L);
        engine.finishRoundAfterActions();
        assertThat(engine.getState()).isEqualTo(state);
        assertThat(engine.getStarvationDeaths()).isEqualByComparingTo(deaths);
        if (state == KaiserEngine.GameState.DISMISSED) {
            assertThat(engine.getTotalStarvationDeaths()).isZero();
            assertThrows(IllegalStateException.class, engine::startNewRound);
        }
    }

    @Test
    void deathPercentagesKeepFractionsAcrossYears() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(
                0.99, 0.4, 0.0, 0.5, 0.99, 0.4, 0.0, 0.5));
        engine.startNewRound();
        engine.feedToPopulation(1980L); // 1/100 dies; next population = 99+3=102.
        engine.cultivate(900L);
        engine.finishRoundAfterActions();
        engine.startNewRound();
        engine.feedToPopulation(2020L); // 1/102 dies.
        engine.cultivate(900L);
        engine.finishRoundAfterActions();
        assertThat(engine.getTotalStarvationDeaths()).isEqualByComparingTo("2");
        assertThat(engine.getAverageStarvationPercentage()).isBetween(new BigDecimal("0.99019"), new BigDecimal("0.99020"));
    }

    @Test
    void foodForJustUnderOneHundredPeopleSupportsOnlyNinetyNine() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0, 0.0, 0.0, 0.5));
        engine.startNewRound();
        engine.feedToPopulation(new BigDecimal("1999.9"));
        engine.cultivate(0L);
        engine.finishRoundAfterActions();
        assertThat(engine.getPopulation()).isEqualByComparingTo("99");
        assertThat(engine.getStarvationDeaths()).isEqualByComparingTo("1");
    }

    @ParameterizedTest
    @CsvSource({"1860,5", "2000,10"})
    void exactFeedingUpdatesAverageButOverfeedingLeavesItUntouched(long food, String average) {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(
                0.99, 0.4, 0.0, 0.5, 0.0, 0.0, 0.0, 0.5));
        engine.startNewRound();
        engine.feedToPopulation(1800L);
        engine.cultivate(900L);
        engine.finishRoundAfterActions();
        assertThat(engine.getAverageStarvationPercentage()).isEqualByComparingTo("10");
        engine.startNewRound();
        engine.feedToPopulation(food); // Population 93: exactly 1860 grain is needed.
        engine.cultivate(0L);
        engine.finishRoundAfterActions();
        assertThat(engine.getPopulation()).isEqualByComparingTo("93");
        assertThat(engine.getStarvationDeaths()).isZero();
        assertThat(engine.getAverageStarvationPercentage()).isEqualByComparingTo(average);
        assertThat(engine.getTotalStarvationDeaths()).isEqualByComparingTo("10");
    }

    @Test
    void landPerPersonIsNotRoundedToWholeHectares() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom());
        assertThat(engine.getLandPerPerson()).isBetween(new BigDecimal("10.5263"), new BigDecimal("10.5264"));
    }

    @Test
    void resetRestoresAllInitialState() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0));
        engine.startNewRound();
        engine.prepareLandTrading();
        engine.buyLand(1L);
        engine.abort();
        engine.reset();
        assertThat(engine.getState()).isEqualTo(KaiserEngine.GameState.RUNNING);
        assertThat(engine.getYear()).isZero();
        assertThat(engine.getPopulation()).isEqualByComparingTo("95");
        assertThat(engine.getLandArea()).isEqualByComparingTo("1000");
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2800");
        assertThat(engine.getLandPrice()).isZero();
    }
}
