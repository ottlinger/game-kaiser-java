package de.aikiit.game.kaiser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KaiserEngineCultivateTest {
    private KaiserEngine engine;

    @BeforeEach
    void createNewEngineUnderTest() {
        this.engine = new KaiserEngine();
    }

    @Test
    void cultivateZeroChangesCostForNextRound() {
        BigDecimal costB4 = engine.getCost();

        engine.cultivate(0L);

        assertThat(engine.getCost()).isNotEqualTo(costB4);
    }

    @Test
    void cultivateNegativeInputDoesNotChangeCost() {
        BigDecimal costB4 = engine.getCost();

        engine.cultivate(-1234L);

        assertThat(engine.getCost()).isEqualTo(costB4);
    }

    @Test
    void youCanNotCultivateMoreThanYourArea() {
        BigDecimal areaB4 = engine.getArea();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            engine.cultivate(Long.MAX_VALUE);
        });

        String expectedMessage = "You cannot cultivate more area than you have.";
        assertThat(exception.getMessage()).contains(expectedMessage);
        assertThat(engine.getArea()).isEqualTo(areaB4);
    }

    @Test
    void youNeedToHaveEnoughSupplies() {
        engine.setArea(BigDecimal.valueOf(Long.MAX_VALUE));
        engine.setSupplies(BigDecimal.valueOf(Long.MAX_VALUE).divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP));
        BigDecimal suppliesB4 = engine.getSupplies();
        BigDecimal areaB4 = engine.getArea();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            engine.cultivate(Long.MAX_VALUE);
        });

        String expectedMessage = "You cannot cultivate more than you have.";
        assertThat(exception.getMessage()).contains(expectedMessage);
        assertThat(engine.getSupplies()).isEqualTo(suppliesB4);
        assertThat(engine.getArea()).isEqualTo(areaB4);
    }

    @Test
    void youNeedWorkersForCultivation() {
        engine.setArea(BigDecimal.valueOf(Long.MAX_VALUE));
        engine.setSupplies(BigDecimal.valueOf(Long.MAX_VALUE).divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP));
        BigDecimal suppliesB4 = engine.getSupplies();
        BigDecimal areaB4 = engine.getArea();

        assertThat(engine.getPopulation()).isEqualTo(BigDecimal.valueOf(95L)); // is default value
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            engine.cultivate(BigDecimal.valueOf(1000L).longValue()); // is 10 times more than population
        });

        String expectedMessage = "Not enough workers available.";
        assertThat(exception.getMessage()).contains(expectedMessage);
        assertThat(engine.getSupplies()).isEqualTo(suppliesB4);
        assertThat(engine.getArea()).isEqualTo(areaB4);
    }

    @Test
    void cultivateRegularUseCaseAndSetPriceToOneAfterwards() {
        BigDecimal supplyB4 = engine.getSupplies();
        BigDecimal costB4 = engine.getCost();
        engine.cultivate(4L);

        assertThat(engine.getSupplies()).isNotEqualTo(supplyB4);
        assertThat(engine.getCost()).isNotEqualTo(costB4);
    }
}
