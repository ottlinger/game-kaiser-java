package de.aikiit.game.kaiser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KaiserEnginePrinterTest {
    private KaiserEnginePrinter printer;

    @BeforeEach
    void createNewPrinterUnderTest() {
        this.printer = new KaiserEnginePrinter(new KaiserEngine());
    }

    @Test
    void getStatus() {
        assertThat(printer.getStatus(4712)).contains("4712");
    }

    @Test
    void getYearResult() {
    }

    @Test
    void getResults() {
    }

    @Test
    void evaluateRegency() {
    }
}