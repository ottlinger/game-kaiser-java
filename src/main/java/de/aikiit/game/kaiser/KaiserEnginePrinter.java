package de.aikiit.game.kaiser;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.apache.commons.lang3.compare.ComparableUtils.is;

public class KaiserEnginePrinter {

    // inspired by https://talyian.github.io/ansicolors/
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ORANGE = "\u001B[38;5;202m";

    private final KaiserEngine engine;

    public KaiserEnginePrinter(KaiserEngine engine) {
        this.engine = engine;
    }

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

    public String getYearResult(int round) {
        StringBuilder status = new StringBuilder();

        status.append(ANSI_GREEN).append("Im Jahr ").append(round).append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_CYAN).append("Es starben         ").append(StringUtils.leftPad(String.valueOf(this.engine.getDeathToll()), 10)).append(" Einwohner").append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_CYAN).append("hinzugekommen sind ").append(StringUtils.leftPad(String.valueOf(this.engine.getIncrease()), 10)).append(" Einwohner").append(ANSI_RESET).append(System.lineSeparator());
        return status.toString();

    }

    public String getResults() {
        StringBuilder result = new StringBuilder();

        result.append("In Ihrer ").append(KaiserGame.MAX_ROUNDS).append("-jährigen Amtszeit sind durchschnittlich ");
        result.append(this.engine.getPercentDeathToll()).append(" % im Jahr verstorben.").append(System.lineSeparator());
        result.append("Insgesamt sind damit ").append(this.engine.getDeathTollSum()).append(" Einwohner verstorben.").append(System.lineSeparator());

        result.append(System.lineSeparator());
        result.append("Anfangs hatten Sie 10 Hektar pro Einwohner, jetzt haben Sie ").append(this.engine.getAreaPerCapita()).append(" Hektar/Einwohner.").append(System.lineSeparator());

        return result.toString();
    }

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
