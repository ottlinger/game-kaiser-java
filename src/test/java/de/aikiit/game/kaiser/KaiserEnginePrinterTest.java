package de.aikiit.game.kaiser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class KaiserEnginePrinterTest {
    @Test
    void reportDisplaysPreviousHarvestRatherThanLandPrice() {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0));
        engine.startNewRound();
        engine.prepareLandTrading();
        KaiserEnginePrinter printer = new KaiserEnginePrinter(engine);
        assertThat(printer.getStatus(1)).contains("3").doesNotContain("17");
        assertThat(printer.getYearResult(1)).contains("Im Jahr 1", "5");
        assertThat(printer.getResults()).contains("10-jährigen", "10 Hektar");
    }

    @ParameterizedTest
    @CsvSource({"0,EXCELLENT", "3,EXCELLENT", "4,MEDIOCRE", "10,MEDIOCRE", "11,TYRANNICAL",
            "33,TYRANNICAL", "34,DISASTROUS"})
    void starvationRatingThresholdsMatchBasic(int deaths, KaiserEngine.Regency rating) {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0, 0.0, 0.0, 0.5));
        engine.startNewRound();
        engine.feedToPopulation(2000L - 20L * deaths);
        engine.cultivate(0L);
        engine.finishRoundAfterActions();
        assertThat(engine.getRegency()).isEqualTo(rating);
    }

    @ParameterizedTest
    @CsvSource({"699,DISASTROUS", "700,TYRANNICAL", "899,TYRANNICAL", "900,MEDIOCRE",
            "999,MEDIOCRE", "1000,EXCELLENT"})
    void landRatingUsesUnroundedRatios(int area, KaiserEngine.Regency rating) {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0));
        engine.startNewRound();
        engine.prepareLandTrading();
        engine.sellLand(1000L - area);
        assertThat(engine.getRegency()).isEqualTo(rating);
    }

    @ParameterizedTest
    @CsvSource({"0,wunderbare", "4,glücklicher", "11,hartherzige", "34,Misswirtschaft"})
    void allFourEvaluationMessagesAreReachable(int deaths, String message) {
        BasicRandom random = new BasicRandom(0.0, 0.0, 0.0, 0.5, 0.5);
        KaiserEngine engine = new KaiserEngine(random);
        engine.startNewRound();
        engine.feedToPopulation(2000L - 20L * deaths);
        engine.cultivate(0L);
        engine.finishRoundAfterActions();
        String evaluation = new KaiserEnginePrinter(engine).evaluateRegency();
        assertThat(evaluation).contains(message);
        if (deaths == 4) {
            // INT(96 * .8 * .5), not a fixed 80% of the population.
            assertThat(evaluation).contains("38 Einwohner");
            assertThat(random.remaining()).isZero();
        } else {
            assertThat(random.remaining()).isEqualTo(1);
        }
    }
}
