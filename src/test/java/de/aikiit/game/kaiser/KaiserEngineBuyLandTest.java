package de.aikiit.game.kaiser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KaiserEngineBuyLandTest {
    private KaiserEngine engine;

    @BeforeEach
    void createNewEngineUnderTest() {
        this.engine = new KaiserEngine();
    }

    @Test
    void buyLandDoesNothingIfZeroIsGiven() {
        int costB4 = engine.getCost();
        BigDecimal areaB4 = engine.getArea();
        BigDecimal supplyB4 = engine.getSupplies();

        engine.buyLand(0L);

        assertThat(engine.getArea()).isEqualTo(areaB4);
        assertThat(engine.getSupplies()).isEqualTo(supplyB4);
        assertThat(engine.getCost()).isEqualTo(costB4);
    }

    @Test
    void buyLandWithMoreThanCashAsArgumentInducesNoChanges() {
        int costB4 = engine.getCost();
        BigDecimal areaB4 = engine.getArea();
        BigDecimal supplyB4 = engine.getSupplies();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            engine.buyLand(Long.MAX_VALUE);
        });

        String expectedMessage = "Not Enough Supplies";
        assertThat(exception.getMessage()).contains(expectedMessage);
        assertThat(engine.getArea()).isEqualTo(areaB4);
        assertThat(engine.getSupplies()).isEqualTo(supplyB4);
        assertThat(engine.getCost()).isEqualTo(costB4);
    }

    @Test
    void buyLandRegularUseCaseAndSetPriceToZeroAfterwards() {
        BigDecimal areaB4 = engine.getArea();
        BigDecimal supplyB4 = engine.getSupplies();
        engine.buyLand(4L);

        assertThat(engine.getArea()).isEqualTo(areaB4.add(BigDecimal.valueOf(4L)));
        assertThat(engine.getSupplies()).isEqualTo(supplyB4.subtract(engine.getYield().multiply(BigDecimal.valueOf(4L))));
        assertThat(engine.getCost()).isEqualTo(0);
    }


}
