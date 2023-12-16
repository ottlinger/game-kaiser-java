package de.aikiit.game.kaiser;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class KaiserEngineTest {

    @Test
    void getAreaPerCapita() {
        KaiserEngine k = new KaiserEngine();
        assertThat(k.getAreaPerCapita()).isEqualTo(new BigDecimal("11"));
    }

    @Test
    void famineReducesPopulationDependingOnScaleFactorBeingBelowZero() {
        KaiserEngine k = new KaiserEngine();
        k.setQ(new BigDecimal("-0.5"));
        k.processFamine();
        assertThat(k.getPopulation()).isEqualTo(new BigDecimal("48"));
        assertThat(k.getQ()).isNotEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void noPopulationReductionIfScaleFactorIsTooBig() {
        KaiserEngine k = new KaiserEngine();
        k.setQ(new BigDecimal("12.34"));
        k.processFamine();
        assertThat(k.getPopulation()).isEqualTo(new BigDecimal("95"));
        assertThat(k.getQ()).isNotEqualByComparingTo(BigDecimal.ONE);
    }
}