package de.aikiit.game.kaiser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class KaiserEngineTest {

    private KaiserEngine engine;

    @BeforeEach
    void createNewEngineUnderTest() {
        this.engine = new KaiserEngine();
    }

    @Test
    void getAreaPerCapita() {
        assertThat(engine.getAreaPerCapita()).isEqualTo(new BigDecimal("11"));
    }

    @Test
    void famineReducesPopulationDependingOnScaleFactorBeingBelowZero() {
        engine.setQ(new BigDecimal("-0.5"));
        engine.processFamine();
        assertThat(engine.getPopulation()).isEqualTo(new BigDecimal("48"));
        assertThat(engine.getQ()).isNotEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void noPopulationReductionIfScaleFactorIsTooBig() {
        engine.setQ(new BigDecimal("12.34"));
        engine.processFamine();
        assertThat(engine.getPopulation()).isEqualTo(new BigDecimal("95"));
        assertThat(engine.getQ()).isNotEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void buyLandIgnoresNegativeInput() {
// TBD
    }
}