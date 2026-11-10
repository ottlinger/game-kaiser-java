package de.aikiit.game.kaiser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KaiserEngineBuyLandTest {
    private KaiserEngine engine;

    @BeforeEach
    void setup() {
        engine = new KaiserEngine(new BasicRandom(0.0));
        engine.startNewRound();
        engine.prepareLandTrading();
    }

    @Test
    void buyingUsesLandPriceNotHarvestYield() {
        assertThat(engine.buyLand(4L)).isEqualTo(4);
        assertThat(engine.getLandArea()).isEqualByComparingTo("1004");
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2732");
        assertThat(engine.getLandPrice()).isEqualByComparingTo("17");
    }

    @Test
    void zeroChangesNothing() {
        assertThat(engine.buyLand(0L)).isZero();
        assertThat(engine.getLandArea()).isEqualByComparingTo("1000");
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2800");
    }

    @Test
    void unaffordablePurchaseChangesNothing() {
        assertThrows(IllegalArgumentException.class, () -> engine.buyLand(Long.MAX_VALUE));
        assertThat(engine.getLandArea()).isEqualByComparingTo("1000");
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2800");
    }

    @Test
    void spendingExactlyAllStocksIsAllowed() {
        // Price 20 divides the initial 2800 units exactly.
        KaiserEngine exact = new KaiserEngine(new BasicRandom(0.3));
        exact.startNewRound();
        exact.prepareLandTrading();
        exact.buyLand(140L);
        assertThat(exact.getGrainStocks()).isZero();
    }

    @Test
    void negativeInputEndsGame() {
        engine.buyLand(-1L);
        assertThat(engine.getState()).isEqualTo(KaiserEngine.GameState.ABORTED);
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2800");
    }
}
