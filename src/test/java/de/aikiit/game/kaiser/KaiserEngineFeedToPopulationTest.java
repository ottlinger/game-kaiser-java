package de.aikiit.game.kaiser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KaiserEngineFeedToPopulationTest {
    private KaiserEngine engine;

    @BeforeEach
    void createNewEngineUnderTest() {
        this.engine = new KaiserEngine();
    }

    @Test
    void feedToPopulationDoesNothingIfZeroIsGiven() {
        int costB4 = engine.getCost();
        BigDecimal supplyB4 = engine.getSupplies();

        engine.feedToPopulation(0L);

        assertThat(engine.getSupplies()).isEqualTo(supplyB4);
        assertThat(engine.getCost()).isEqualTo(costB4);
    }

    @Test
    void feedToPopulationWithNotEnoughSupplyAsArgumentInducesNoChanges() {
        int costB4 = engine.getCost();
        BigDecimal supplyB4 = engine.getSupplies();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            engine.feedToPopulation(Long.MAX_VALUE);
        });

        String expectedMessage = "Not Enough in Stock";
        assertThat(exception.getMessage()).contains(expectedMessage);
        assertThat(engine.getSupplies()).isEqualTo(supplyB4);
        assertThat(engine.getCost()).isEqualTo(costB4);
    }

    @Test
    void feedToPopulationRegularUseCaseAndSetPriceToOneAfterwards() {
        BigDecimal supplyB4 = engine.getSupplies();
        engine.feedToPopulation(4L);

        assertThat(engine.getSupplies()).isEqualTo(supplyB4.subtract(BigDecimal.valueOf(4L)));
        assertThat(engine.getCost()).isEqualTo(1);
    }


}
