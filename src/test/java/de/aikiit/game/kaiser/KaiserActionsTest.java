package de.aikiit.game.kaiser;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import uk.org.webcompere.systemstubs.SystemStubs;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class KaiserActionsTest {

    @SneakyThrows
    @Test
    void buyWithPositiveInput() {
        SystemStubs.withTextFromSystemIn("11")
                .execute(() -> {
                    assertThat(KaiserActions.buy(new KaiserEngine()))
                            .isTrue();
                });
    }

    @SneakyThrows
    @Test
    void buyWithZeroInputWillTriggerSell() {
        SystemStubs.withTextFromSystemIn("0")
                .execute(() -> {
                    assertThat(KaiserActions.buy(new KaiserEngine()))
                            .isFalse();
                });
    }


    @SneakyThrows
    @Test
    void sell() {
        SystemStubs.withTextFromSystemIn("0")
                .execute(() -> {
                    assertDoesNotThrow(() -> KaiserActions.sell(new KaiserEngine()));
                });
    }

    @Test
    @SneakyThrows
    void feed() {
        SystemStubs.withTextFromSystemIn("0")
                .execute(() -> {
                    assertDoesNotThrow(() -> KaiserActions.feed(new KaiserEngine()));
                });
    }

    @Test
    @SneakyThrows
    void cultivate() {
        SystemStubs.withTextFromSystemIn("0")
                .execute(() -> {
                    assertDoesNotThrow(() -> KaiserActions.cultivate(new KaiserEngine()));
                });

    }
}