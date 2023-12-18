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
    void buyLandIgnoresNegativeInputAndChangesNothing() {
        BigDecimal costB4 = engine.getCost();
        BigDecimal areaB4 = engine.getArea();
        BigDecimal supplyB4 = engine.getSupplies();

        engine.buyLand(-123L);

        assertThat(engine.getArea()).isEqualTo(areaB4);
        assertThat(engine.getSupplies()).isEqualTo(supplyB4);
        assertThat(engine.getCost()).isEqualTo(costB4);
    }

    @Test
    void calculateCostChangesPrice() {
        BigDecimal costB4 = engine.getCost();

        engine.calculateNewPrice();

        assertThat(engine.getCost()).isNotEqualTo(costB4);
    }

    @Test
    void verifyThingsHappenUponRoundRestart() {
        engine.startNewRound();

        assertThat(engine.getPopulation()).isEqualTo("100");
        assertThat(engine.getCost()).isPositive(); // randomNumber
    }
}