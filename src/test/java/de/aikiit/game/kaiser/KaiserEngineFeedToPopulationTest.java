package de.aikiit.game.kaiser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KaiserEngineFeedToPopulationTest {
    private KaiserEngine engine;

    @BeforeEach
    void setup() {
        engine = new KaiserEngine(new BasicRandom());
        engine.startNewRound();
    }

    @Test
    void feedingStoresAllocationAndReducesStocks() {
        engine.feedToPopulation(2000L);
        assertThat(engine.getFoodAllocated()).isEqualByComparingTo("2000");
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("800");
        assertThat(engine.getPlagueRoll()).isEqualTo(1);
    }

    @Test
    void zeroFoodIsAValidAllocation() {
        engine.feedToPopulation(0L);
        assertThat(engine.getFoodAllocated()).isZero();
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2800");
    }

    @Test
    void allStocksCanBeDistributed() {
        engine.feedToPopulation(2800L);
        assertThat(engine.getFoodAllocated()).isEqualByComparingTo("2800");
        assertThat(engine.getGrainStocks()).isZero();
    }

    @Test
    void insufficientStocksDoNotChangeState() {
        assertThatThrownBy(() -> engine.feedToPopulation(2801L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Not Enough in Stock");
        assertThat(engine.getFoodAllocated()).isZero();
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2800");
    }

    @Test
    void negativeInputEndsGame() {
        engine.feedToPopulation(-1L);
        assertThat(engine.getState()).isEqualTo(KaiserEngine.GameState.ABORTED);
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2800");
    }
}
