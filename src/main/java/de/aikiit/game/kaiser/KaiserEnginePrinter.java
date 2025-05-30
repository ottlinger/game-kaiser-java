package de.aikiit.game.kaiser;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.apache.commons.lang3.compare.ComparableUtils.is;

/**
 * This class contains static helper methods to print
 * results or calculation or status information of the currently running game.
 */
public class KaiserEnginePrinter {

    // inspired by https://talyian.github.io/ansicolors/
    /**
     * Reset anything in console.
     */
    public static final String ANSI_RESET = "\u001B[0m";
    /**
     * Colour BLACK.
     */
    public static final String ANSI_BLACK = "\u001B[30m";
    /**
     * Colour RED.
     */
    public static final String ANSI_RED = "\u001B[31m";
    /**
     * Colour GREEN.
     */
    public static final String ANSI_GREEN = "\u001B[32m";
    /**
     * Colour YELLOW.
     */
    public static final String ANSI_YELLOW = "\u001B[33m";
    /**
     * Colour BLUE.
     */
    public static final String ANSI_BLUE = "\u001B[34m";
    /**
     * Colour PURPLE.
     */
    public static final String ANSI_PURPLE = "\u001B[35m";
    /**
     * Colour CYAN.
     */
    public static final String ANSI_CYAN = "\u001B[36m";
    /**
     * Colour WHITE.
     */
    public static final String ANSI_WHITE = "\u001B[37m";
    /**
     * Colour ORANGE.
     */
    public static final String ORANGE = "\u001B[38;5;202m";

    private final KaiserEngine engine;

    public KaiserEnginePrinter(KaiserEngine engine) {
        this.engine = engine;
    }

    /**
     * Show status information of the underlying game engine for the given round.
     *
     * @param round current round/year of the game.
     * @return colour-encoded status.
     */
    public String getStatus(int round) {
        StringBuilder status = new StringBuilder();
        status.append(ANSI_GREEN).append("### STATUS nach Runde ").append(round).append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_CYAN).append("Die Einwohnerzahl beträgt jetzt   ").append(StringUtils.leftPad(String.valueOf(this.engine.getPopulation()), 10)).append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_CYAN).append("Die Stadt besitzt an Land (Hektar)").append(StringUtils.leftPad(String.valueOf(this.engine.getArea()), 10)).append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_CYAN).append("Die Ernte betrug (dzt/Hektar)     ").append(StringUtils.leftPad(String.valueOf(this.engine.getYield()), 10)).append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_RED).append("Ratten haben gefressen (dzt)      ").append(StringUtils.leftPad(String.valueOf(this.engine.getExternalDamage()), 10)).append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_RED).append("Eure Vorräte betragen (dzt)       ").append(StringUtils.leftPad(String.valueOf(this.engine.getSupplies()), 10)).append(ANSI_RESET).append(System.lineSeparator());
        return status.toString();
    }

    /**
     * Show population result of the current round.
     *
     * @param round current round/year of the game.
     * @return colour-encoded population statistics.
     */
    public String getYearResult(int round) {
        return ANSI_GREEN + "Im Jahr " + round + ANSI_RESET + System.lineSeparator() +
                ANSI_CYAN + "Es starben         " + StringUtils.leftPad(String.valueOf(this.engine.getDeathToll()), 10) + " Einwohner" + ANSI_RESET + System.lineSeparator() +
                ANSI_CYAN + "hinzugekommen sind " + StringUtils.leftPad(String.valueOf(this.engine.getIncrease()), 10) + " Einwohner" + ANSI_RESET + System.lineSeparator();
    }

    /**
     * After the game ends and the maximum number of years is reached an overall statistics is printed out.
     *
     * @return colour-encoded overall statistics.
     */
    public String getResults() {
        return "In Ihrer " + KaiserGame.MAX_ROUNDS + "-jährigen Amtszeit sind durchschnittlich " +
                this.engine.getPercentDeathToll() + " % im Jahr verstorben." + System.lineSeparator() +
                "Insgesamt sind damit " + this.engine.getDeathTollSum() + " Einwohner verstorben." + System.lineSeparator() +
                System.lineSeparator() +
                "Anfangs hatten Sie 10 Hektar pro Einwohner, jetzt haben Sie " + this.engine.getAreaPerCapita() + " Hektar/Einwohner." + System.lineSeparator();
    }

    /**
     * Depending on global rules the player's legacy is described/classified depending on the engine's results.
     *
     * @return colour-encoded legacy/regency output.
     */
    public String evaluateRegency() {
        StringBuilder result = new StringBuilder();
        result.append(ANSI_BLUE).append(">>>> Bewertung Ihrer Herrschaft <<<<").append(System.lineSeparator());

        result.append("In ihrer ").append(engine.getZYear()).append("-jährigen Amtszeit sind ");
        result.append(engine.getPercentDeathToll()).append("% der Bevölkerung im Jahr verstorben.").append(System.lineSeparator());
        result.append("Insgesamt sind ").append(engine.getDeathTollSum()).append(" Einwohner gestorben.").append(System.lineSeparator());

        BigDecimal legacy = engine.getArea().divide(engine.getPopulation(), RoundingMode.HALF_UP);
        result.append("Zu Beginn hatten sie 10 Hektar pro Einwohner,").append(System.lineSeparator());
        result.append("jetzt sind es ").append(legacy).append(" Hektar/Einwohner.").append(System.lineSeparator());

        // TODO: check and fix legacy calculation - message should match criteria
        if (is(engine.getPercentDeathToll()).greaterThan(BigDecimal.valueOf(33)) || is(legacy).lessThan(BigDecimal.valueOf(7))) {
            result.append("Auf Grund dieser extremen Misswirtschaft, werden Sie nicht nur aus Amt und Würden gejagt,").append(System.lineSeparator());
            result.append("sondern auch zum Versager des Jahres erklärt.").append(System.lineSeparator());
        } else {

            if (is(engine.getPercentDeathToll()).greaterThan(BigDecimal.TEN) || is(legacy).lessThan(BigDecimal.valueOf(9))) {
                result.append("Ihre hartherzige Regierungsmethode erinnert an Nero und Iwan den Schrecklichen.");
                result.append(System.lineSeparator());
                result.append("Die (verbliebenen) Einwohner würden Sie gerne zum Teufel jagen!");
            } else if (is(engine.getPercentDeathToll()).greaterThan(BigDecimal.TEN) || is(legacy).lessThan(BigDecimal.valueOf(9))) {
                result.append("Sie hätten glücklicher agieren können, aber es war nicht übel.");
                result.append(System.lineSeparator());
                result.append(this.engine.getPopulation().multiply(new BigDecimal("0.8"))).append(" Einwohner möchten Sie zwar hängen sehen, aber kleine Probleme hat ja jeder.");
            } else {
                result.append("Eine wunderbare Regierungszeit! Karl, Konrad und Willi (die Großen) hätten es nicht besser machen können.");
            }
        }

        result.append(ANSI_RESET);
        result.append(System.lineSeparator());
        result.append(System.lineSeparator());
        return result.toString();
    }
}
