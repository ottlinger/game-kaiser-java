package de.aikiit.game.kaiser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThatThrownBy(() -> engine.sellLand(1000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Not Enough Land");
        assertThatThrownBy(() -> engine.sellLand(Long.MAX_VALUE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Not Enough Land");
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
