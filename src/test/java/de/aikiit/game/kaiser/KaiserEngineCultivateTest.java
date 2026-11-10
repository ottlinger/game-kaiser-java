package de.aikiit.game.kaiser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KaiserEngineCultivateTest {
    @Test
    void zeroStillCalculatesHarvestRatsAndImmigrationFactor() {
        BasicRandom random = new BasicRandom(0.99, 0.2, 0.99);
        KaiserEngine engine = new KaiserEngine(random);
        engine.startNewRound();
        engine.cultivate(0L);
        assertThat(engine.getHarvestYield()).isEqualByComparingTo("5");
        assertThat(engine.getTotalHarvest()).isZero();
        assertThat(engine.getRatLoss()).isEqualByComparingTo("1400");
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("1400");
        assertThat(engine.getImmigrationFactor()).isEqualTo(5);
        assertThat(random.remaining()).isZero();
    }

    @ParameterizedTest
    @CsvSource({"0,0", "0.2,1400", "0.4,0", "0.6,700", "0.8,0"})
    void onlyEvenRatRollsCauseDamage(double roll, String loss) {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0, roll, 0.0));
        engine.cultivate(0L);
        assertThat(engine.getRatLoss()).isEqualByComparingTo(loss);
    }

    @Test
    void seedAndRatLossUseFloorNotRounding() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0, 0.2, 0.0));
        engine.startNewRound();
        engine.cultivate(3L);
        // S=2800-INT(3/2)=2799; E=INT(2799/2)=1399; H=3.
        assertThat(engine.getRatLoss()).isEqualByComparingTo("1399");
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("1403");
    }

    @Test
    void exactSeedStockIsRejectedButZeroPlantingBypassesValidation() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0, 0.0, 0.0));
        engine.startNewRound();
        engine.feedToPopulation(2798L);
        assertThrows(IllegalArgumentException.class, () -> engine.cultivate(4L));
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2");
        engine.cultivate(0L);
    }

    @Test
    void exactWorkerCapacityIsRejectedButPlantingAllLandIsAllowed() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0, 0.0, 0.0));
        // Initial P=95, so 950 is forbidden; 949 is allowed.
        assertThrows(IllegalArgumentException.class, () -> engine.cultivate(950L));
        engine.cultivate(949L);

        KaiserEngine allLand = new KaiserEngine(new BasicRandom(0.99, 0.4, 0.0, 0.5,
                0.0, 0.0, 0.0));
        allLand.startNewRound();
        allLand.feedToPopulation(2000L);
        allLand.cultivate(900L);
        allLand.finishRoundAfterActions();
        allLand.startNewRound(); // P=103: 1000 hectares now strictly below capacity.
        allLand.cultivate(1000L);
        assertThat(allLand.getTotalHarvest()).isEqualByComparingTo("1000");
    }

    @Test
    void moreLandThanOwnedIsRejectedWithoutRandomDraws() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom());
        assertThrows(IllegalArgumentException.class, () -> engine.cultivate(1001L));
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2800");
    }

    @Test
    void negativeInputEndsGameWithoutHarvest() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom());
        engine.cultivate(-1L);
        assertThat(engine.getState()).isEqualTo(KaiserEngine.GameState.ABORTED);
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2800");
    }
}
