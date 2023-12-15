package de.aikiit.game.kaiser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KaiserEngineTest {

    @Test
    void getAreaPerCapita() {
        KaiserEngine k = new KaiserEngine();
        assertThat(k.getAreaPerCapita()).isZero();
    }
}