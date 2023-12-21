package de.aikiit.game.kaiser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KaiserEngineSellLandTest {
    private KaiserEngine engine;

    @BeforeEach
    void createNewEngineUnderTest() {
        this.engine = new KaiserEngine();
    }

    @Test
    void sellLandDoesNothingIfZeroIsGiven() {
        BigDecimal costB4 = engine.getCost();
        BigDecimal areaB4 = engine.getArea();
        BigDecimal supplyB4 = engine.getSupplies();

        engine.sellLand(0L);

        assertThat(engine.getArea()).isEqualTo(areaB4);
        assertThat(engine.getSupplies()).isEqualTo(supplyB4);
        assertThat(engine.getCost()).isEqualTo(costB4);
    }

    @Test
    void sellLandDoesNothingIfNegativeValueIsGiven() {
        BigDecimal costB4 = engine.getCost();
        BigDecimal areaB4 = engine.getArea();
        BigDecimal supplyB4 = engine.getSupplies();

        engine.sellLand(-1234L);

        assertThat(engine.getArea()).isEqualTo(areaB4);
        assertThat(engine.getSupplies()).isEqualTo(supplyB4);
        assertThat(engine.getCost()).isEqualTo(costB4);
    }
    @Test
    void sellLandWithNotEnoughLandAsArgumentInducesNoChanges() {
        BigDecimal costB4 = engine.getCost();
        BigDecimal areaB4 = engine.getArea();
        BigDecimal supplyB4 = engine.getSupplies();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> engine.sellLand(Long.MAX_VALUE));

        String expectedMessage = "Not Enough Land";
        assertThat(exception.getMessage()).contains(expectedMessage);
        assertThat(engine.getArea()).isEqualTo(areaB4);
        assertThat(engine.getSupplies()).isEqualTo(supplyB4);
        assertThat(engine.getCost()).isEqualTo(costB4);
    }

    @Test
    void sellLandRegularUseCaseAndSetPriceToZeroAfterwards() {
        BigDecimal areaB4 = engine.getArea();
        BigDecimal supplyB4 = engine.getSupplies();
        engine.sellLand(4L);

        assertThat(engine.getArea()).isEqualTo(areaB4.subtract(BigDecimal.valueOf(4L)));
        assertThat(engine.getSupplies()).isEqualTo(supplyB4.add(engine.getYield().multiply(BigDecimal.valueOf(4L))));
        assertThat(engine.getCost()).isEqualTo(BigDecimal.ZERO);
    }
}
