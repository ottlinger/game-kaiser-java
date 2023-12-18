package de.aikiit.game.kaiser;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import uk.org.webcompere.systemstubs.SystemStubs;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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


    @Test
    void sell() {
    }

    @Test
    void feed() {
    }

    @Test
    void cultivate() {
    }
}