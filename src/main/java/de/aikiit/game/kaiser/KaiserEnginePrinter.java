package de.aikiit.game.kaiser;

public class KaiserEnginePrinter {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private final KaiserEngine engine;

    public KaiserEnginePrinter(KaiserEngine engine) {
        this.engine = engine;
    }

    public String getStatus(int round) {
        StringBuilder status = new StringBuilder();

        status.append(ANSI_GREEN).append("### STATUS nach Runde ").append(round).append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_CYAN).append("Die Einwohnerzahl ist jetzt ").append(this.engine.getPopulation()).append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_CYAN).append("Die Stadt besitzt an Land ").append(this.engine.getArea()).append(" Hektar").append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_CYAN).append("Die Ernte betrug ").append(this.engine.getYield()).append(" DZ/Hektar").append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_RED).append("Ratten haben gefressen ").append(this.engine.getYield()).append(" DZ").append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_RED).append("Die Vorräte betragen ").append(this.engine.getSupplies()).append(" DZ").append(ANSI_RESET).append(System.lineSeparator());
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

        // line 900
        result.append("Eine wunderbare Regierungszeit! Karl, Konrad und Willi (die Großen) hätten es nicht besser machen können.");
        result.append(System.lineSeparator());

        // line 940
        result.append("Ihre hartherzige Regierungsmethode erinnert an Nero und Iwan den Schrecklichen.");
        result.append(System.lineSeparator());
        result.append("Die (verbliebenen) Einwohner würden Sie gerne zum Teufel jagen!");
        result.append(System.lineSeparator());

        // line 960
        result.append("Sie hätten glücklicher agieren können, aber es war nicht übel.");
        result.append(System.lineSeparator());
        result.append(this.engine.getPopulation() * .8).append(" Einwohner möchten Sie zwar hängen sehen, aber kleine Probleme hat ja jeder.");
        result.append(System.lineSeparator());

        result.append(System.lineSeparator());
        result.append("Anfangs hatten Sie 10 Hektar pro Einwohner, jetzt haben Sie ").append(this.engine.getAreaPerCapita()).append(" Hektar/Einwohner.").append(System.lineSeparator());

        result.append(ANSI_RESET);
        return result.toString();
    }
}
