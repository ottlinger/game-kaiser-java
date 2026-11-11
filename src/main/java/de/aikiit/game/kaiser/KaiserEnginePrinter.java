package de.aikiit.game.kaiser;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

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
        status.append(ANSI_CYAN).append("Die Stadt besitzt an Land (Hektar)").append(StringUtils.leftPad(String.valueOf(this.engine.getLandArea()), 10)).append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_CYAN).append("Die Ernte betrug (dt/Hektar)      ").append(StringUtils.leftPad(String.valueOf(this.engine.getHarvestYield()), 10)).append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_RED).append("Ratten haben gefressen (dt)       ").append(StringUtils.leftPad(String.valueOf(this.engine.getRatLoss()), 10)).append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_RED).append("Eure Vorräte betragen (dt)        ").append(StringUtils.leftPad(String.valueOf(this.engine.getGrainStocks()), 10)).append(ANSI_RESET).append(System.lineSeparator());
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
                ANSI_CYAN + "Es starben         " + StringUtils.leftPad(String.valueOf(this.engine.getStarvationDeaths()), 10) + " Einwohner" + ANSI_RESET + System.lineSeparator() +
                ANSI_CYAN + "hinzugekommen sind " + StringUtils.leftPad(String.valueOf(this.engine.getImmigrants()), 10) + " Einwohner" + ANSI_RESET + System.lineSeparator();
    }

    /**
     * After the game ends and the maximum number of years is reached an overall statistics is printed out.
     *
     * @return colour-encoded overall statistics.
     */
    public String getResults() {
        return "In Ihrer " + KaiserGame.MAX_ROUNDS + "-jährigen Amtszeit sind durchschnittlich " +
                format(this.engine.getAverageStarvationPercentage()) + " % im Jahr verstorben." + System.lineSeparator() +
                "Insgesamt sind damit " + this.engine.getTotalStarvationDeaths() + " Einwohner verstorben." + System.lineSeparator() +
                System.lineSeparator() +
                "Anfangs hatten Sie 10 Hektar pro Einwohner, jetzt haben Sie " + format(this.engine.getLandPerPerson()) + " Hektar/Einwohner." + System.lineSeparator();
    }

    /**
     * Depending on global rules the player's legacy is described/classified depending on the engine's results.
     *
     * @return colour-encoded legacy/regency output.
     */
    public String evaluateRegency() {
        StringBuilder result = new StringBuilder();
        result.append(ANSI_BLUE).append(">>>> Bewertung Ihrer Herrschaft <<<<").append(System.lineSeparator());

        switch (engine.getRegency()) {
            case DISASTROUS -> result.append(dismissalMessage());
            case TYRANNICAL -> result.append("Ihre hartherzige Regierungsmethode erinnert an Nero und Iwan den Schrecklichen.")
                    .append(System.lineSeparator())
                    .append("Die (verbliebenen) Einwohner würden Sie gerne zum Teufel jagen!");
            case MEDIOCRE -> result.append("Sie hätten glücklicher agieren können, aber es war nicht übel.")
                    .append(System.lineSeparator())
                    .append(engine.countDiscontentedResidents())
                    .append(" Einwohner möchten Sie zwar hängen sehen, aber kleine Probleme hat ja jeder.");
            case EXCELLENT -> result.append("Eine wunderbare Regierungszeit! Karl, Konrad und Willi (die Großen) hätten es nicht besser machen können.");
        }

        result.append(ANSI_RESET);
        result.append(System.lineSeparator());
        result.append(System.lineSeparator());
        return result.toString();
    }

    public String getDismissal() {
        return ANSI_YELLOW + "Sie haben " + engine.getStarvationDeaths()
                + " Menschen in nur einem Jahr verhungern lassen!" + System.lineSeparator()
                + dismissalMessage() + ANSI_RESET;
    }

    private static String dismissalMessage() {
        return "Auf Grund dieser extremen Misswirtschaft werden Sie nicht nur aus Amt und Würden gejagt,"
                + System.lineSeparator() + "sondern auch zum Versager des Jahres erklärt.";
    }

    private static String format(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }
}
