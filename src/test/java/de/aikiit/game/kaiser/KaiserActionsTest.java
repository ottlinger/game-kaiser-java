package de.aikiit.game.kaiser;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

class KaiserActionsTest {
    @Test
    void sharedScannerPreservesAllFourActionsAndRetriesBadInput() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0, 0.99, 0.4, 0.0));
        engine.startNewRound();
        engine.prepareLandTrading();
        Scanner input = new Scanner("abc 99999999999999999999999999999 999999 0 1000 5 9999 2000 1001 900");
        assertThat(KaiserActions.buy(engine, input)).isFalse();
        KaiserActions.sell(engine, input);
        KaiserActions.feed(engine, input);
        KaiserActions.cultivate(engine, input);
        assertThat(engine.getLandArea()).isEqualByComparingTo("995");
        assertThat(engine.getFoodAllocated()).isEqualByComparingTo("2000");
        assertThat(engine.getTotalHarvest()).isEqualByComparingTo("4500");
        assertThat(input.hasNext()).isFalse();
    }

    @Test
    void basicDecimalInputsAreAcceptedAndIntOperationsStillFloor() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0, 0.4, 0.4, 0.0));
        engine.startNewRound();
        engine.prepareLandTrading();
        Scanner input = new Scanner("1.5 1999.9 3.9");
        assertThat(KaiserActions.buy(engine, input)).isTrue();
        KaiserActions.feed(engine, input);
        KaiserActions.cultivate(engine, input);
        assertThat(engine.getLandArea()).isEqualByComparingTo("1001.5");
        assertThat(engine.getFoodAllocated()).isEqualByComparingTo("1999.9");
        assertThat(engine.getTotalHarvest()).isEqualByComparingTo("11.7");
        // S=2800-25.5-1999.9-INT(3.9/2)+11.7.
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("785.3");
    }

    @Test
    void positivePurchaseSkipsSelling() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0));
        engine.startNewRound();
        engine.prepareLandTrading();
        assertThat(KaiserActions.buy(engine, new Scanner("11"))).isTrue();
        assertThat(engine.getLandArea()).isEqualByComparingTo("1011");
    }

    @Test
    void negativeInputAndEndOfInputAbortInsteadOfRetryingForever() {
        KaiserEngine negative = new KaiserEngine(new BasicRandom());
        KaiserActions.feed(negative, new Scanner("-1"));
        assertThat(negative.getState()).isEqualTo(KaiserEngine.GameState.ABORTED);
        KaiserEngine eof = new KaiserEngine(new BasicRandom());
        KaiserActions.buy(eof, new Scanner(""));
        assertThat(eof.getState()).isEqualTo(KaiserEngine.GameState.ABORTED);
    }
}
