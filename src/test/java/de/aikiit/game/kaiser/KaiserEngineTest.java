package de.aikiit.game.kaiser;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class KaiserEngineTest {

    @Test
    void getAreaPerCapita() {
        KaiserEngine k = new KaiserEngine();
        assertThat(k.getAreaPerCapita()).isZero();
    }

    @Test
    void famineReducesPopulation() {
        KaiserEngine k = new KaiserEngine();
        k.setQ(new BigDecimal("0.5"));
        k.processFamine();
        assertThat(k.getPopulation()).isEqualTo(new BigDecimal("95"));
    }
}