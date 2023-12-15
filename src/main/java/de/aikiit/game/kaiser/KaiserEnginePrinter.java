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

        status.append(ANSI_GREEN).append("STATUS nach Runde ").append(round).append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_CYAN).append("Die Einwohnerzahl ist jetzt ").append(this.engine.getPopulation()).append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_CYAN).append("Die Stadt besitzt an Land ").append(this.engine.getArea()).append(" Hektar").append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_CYAN).append("Die Ernte betrug ").append(this.engine.getYield()).append(" DZ/Hektar").append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_RED).append("Ratten haben gefressen ").append(this.engine.getYield()).append(" DZ").append(ANSI_RESET).append(System.lineSeparator());
        status.append(ANSI_RED).append("Die Vorräte betragen ").append(this.engine.getSupplies()).append(" DZ").append(ANSI_RESET).append(System.lineSeparator());
        return status.toString();
    }

    public String getResults() {
        StringBuilder result = new StringBuilder();

        result.append("In Ihrer ").append(KaiserGame.MAX_ROUNDS).append("-jährigen Amtszeit sind ");
        result.append(this.engine.getPercentDeathToll()).append(System.lineSeparator());

        return result.toString();

    }


}
