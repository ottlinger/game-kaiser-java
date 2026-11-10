package de.aikiit.game.kaiser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KaiserEngineSellLandTest {
    private KaiserEngine engine;

    @BeforeEach
    void setup() {
        engine = new KaiserEngine(new BasicRandom(0.0));
        engine.startNewRound();
        engine.prepareLandTrading();
    }

    @Test
    void sellingUsesCurrentLandPrice() {
        engine.sellLand(4L);
        assertThat(engine.getLandArea()).isEqualByComparingTo("996");
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2868");
    }

    @Test
    void zeroChangesNothing() {
        engine.sellLand(0L);
        assertThat(engine.getLandArea()).isEqualByComparingTo("1000");
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2800");
    }

    @Test
    void cannotSellAllLandOrMore() {
        assertThrows(IllegalArgumentException.class, () -> engine.sellLand(1000L));
        assertThrows(IllegalArgumentException.class, () -> engine.sellLand(Long.MAX_VALUE));
        assertThat(engine.getLandArea()).isEqualByComparingTo("1000");
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2800");
    }

    @Test
    void negativeInputEndsGame() {
        engine.sellLand(-1L);
        assertThat(engine.getState()).isEqualTo(KaiserEngine.GameState.ABORTED);
        assertThat(engine.getLandArea()).isEqualByComparingTo("1000");
    }
}
