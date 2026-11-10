package de.aikiit.game.kaiser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

class KaiserGameTest {
    @ParameterizedTest
    @ValueSource(strings = {"n", "N", ""})
    void startConfirmationCanStopBeforeFirstYear(String input) {
        KaiserEngine engine = new KaiserEngine(new BasicRandom());
        String output = run(engine, input);
        assertThat(engine.getState()).isEqualTo(KaiserEngine.GameState.ABORTED);
        assertThat(engine.getYear()).isZero();
        assertThat(output).contains("Start (J/N)?");
        assertThat(output).doesNotContain("Im Jahr 1", "Kode-Zahl", "20-53");
    }

    @Test
    void starvationEndsGameImmediatelyWithoutFinalTenYearEvaluation() {
        BasicRandom random = new BasicRandom(0.0, 0.0, 0.0, 0.0, 0.5);
        KaiserEngine engine = new KaiserEngine(random);
        String output = run(engine, "j 0 0 0 0");
        assertThat(engine.getState()).isEqualTo(KaiserEngine.GameState.DISMISSED);
        assertThat(engine.getYear()).isEqualTo(1);
        assertThat(output).contains("100 Menschen", "Versager des Jahres");
        assertThat(output).doesNotContain("Im Jahr 2", "Bewertung Ihrer Herrschaft", "Nochmal", "Kode-Zahl", "20-53");
        assertThat(random.remaining()).isZero();
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "0 -1", "0 0 -1", "0 0 2000 -1", ""})
    void negativeInputOrEofStopsRemainingActions(String actions) {
        KaiserEngine engine = new KaiserEngine(new BasicRandom(0.0));
        String output = run(engine, "j " + actions);
        assertThat(engine.getState()).isEqualTo(KaiserEngine.GameState.ABORTED);
        assertThat(engine.getYear()).isEqualTo(1);
        assertThat(output).contains("Das kann man nicht mit mir machen");
        assertThat(output).doesNotContain("Bewertung Ihrer Herrschaft");
    }

    @Test
    void tenActionYearsAreFollowedByEleventhReportAndThenEvaluation() {
        BasicRandom random = tenYearRandom();
        KaiserEngine engine = new KaiserEngine(random);
        String output = run(engine, tenYearInput() + "n");
        assertThat(engine.getState()).isEqualTo(KaiserEngine.GameState.COMPLETED);
        assertThat(engine.getYear()).isEqualTo(11);
        assertThat(engine.getRegency()).isEqualTo(KaiserEngine.Regency.MEDIOCRE);
        assertThat(engine.getTotalStarvationDeaths()).isZero();
        assertThat(engine.getPopulation()).isEqualByComparingTo("137");
        assertThat(engine.getLandArea()).isEqualByComparingTo("1330");
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("26050");
        assertThat(engine.getImmigrants()).isEqualByComparingTo("4");
        assertThat(output.split("Wieviel Land wollen Sie kaufen", -1)).hasSize(11);
        assertThat(output).contains("Im Jahr 11", "glücklicher", "Nochmal (J/N)?");
        assertThat(output.indexOf("Im Jahr 11")).isLessThan(output.indexOf("Bewertung Ihrer Herrschaft"));
        assertThat(output).doesNotContain("Im Jahr 0", "11-jährigen Amtszeit");
        assertThat(random.remaining()).isEqualTo(1); // No land-price roll without a replay.
    }

    @Test
    void finalReportStillAppliesImmigrationAndPlagueBeforeRating() {
        List<Double> values = new ArrayList<>();
        for (int year = 1; year <= 10; year++) {
            values.addAll(List.of(0.0, 0.99, 0.0, 0.0, year == 10 ? 0.0 : 0.99));
        }
        BasicRandom random = new BasicRandom(values.toArray(Double[]::new));
        KaiserEngine engine = new KaiserEngine(random);
        String output = run(engine, tenYearInput());
        assertThat(engine.getYear()).isEqualTo(11);
        assertThat(engine.getPopulation()).isEqualByComparingTo("68"); // INT((133+4)/2).
        assertThat(engine.getTotalStarvationDeaths()).isZero();
        assertThat(engine.getRegency()).isEqualTo(KaiserEngine.Regency.EXCELLENT);
        assertThat(output).contains("fürchterliche Seuche", "wunderbare Regierungszeit");
        assertThat(output).doesNotContain("Nochmal");
        assertThat(random.remaining()).isZero();
    }

    @Test
    void replayResetsGameAndStartsWithoutCodePrompt() {
        KaiserEngine engine = new KaiserEngine(tenYearRandom());
        String output = run(engine, tenYearInput() + "j j -1");
        assertThat(engine.getState()).isEqualTo(KaiserEngine.GameState.ABORTED);
        assertThat(engine.getYear()).isEqualTo(1);
        assertThat(engine.getPopulation()).isEqualByComparingTo("100");
        assertThat(engine.getLandArea()).isEqualByComparingTo("1000");
        assertThat(engine.getGrainStocks()).isEqualByComparingTo("2800");
        assertThat(output.split("Start \\(J/N\\)\\?", -1)).hasSize(3);
        assertThat(output).doesNotContain("Kode-Zahl", "20-53");
    }

    private static BasicRandom tenYearRandom() {
        List<Double> values = new ArrayList<>();
        for (int year = 1; year <= 10; year++) {
            // Price 17, yield 5, no rats, factor 1, no plague.
            values.addAll(List.of(0.0, 0.99, 0.0, 0.0, 0.99));
        }
        values.add(0.5); // Line 961: residents unhappy with the mediocre reign.
        values.add(0.0); // Land price if the player chooses to play again.
        return new BasicRandom(values.toArray(Double[]::new));
    }

    /** A solvent ten-year strategy; keep roughly ten hectares per person. */
    private static String tenYearInput() {
        StringBuilder input = new StringBuilder("j ");
        long population = 100;
        long area = 1000;
        long stocks = 2800;
        for (int year = 1; year <= 10; year++) {
            long buy = population * 10 - area;
            input.append(buy).append(' ');
            if (buy == 0) {
                input.append("0 "); // Selling is offered only after buying zero.
            }
            stocks -= buy * 17;
            area += buy;
            long food = population * 20;
            long planted = area - 1; // BASIC's worker limit is strict.
            input.append(food).append(' ').append(planted).append(' ');
            stocks = stocks - food - planted / 2 + planted * 5;
            population += (20 * area + stocks) / population / 100 + 1;
        }
        return input.toString();
    }

    private static String run(KaiserEngine engine, String input) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream original = System.out;
        try (PrintStream output = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(output);
            new KaiserGame(engine, new Scanner(input)).run();
        } finally {
            System.setOut(original);
        }
        return bytes.toString(StandardCharsets.UTF_8);
    }
}
