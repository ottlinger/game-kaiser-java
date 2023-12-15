package de.aikiit.game.kaiser;

public class KaiserEngine {
    private Long population = 0L;
    private Long area = 0L;
    private Long yield = 0L;
    private Long externalDamage = 0L;
    private Long supplies = 0L;

    private Long humans = 0L;

    ///
    private Integer zYear; // why z in original?
    private Long deathToll;
    private Long deathTollSum; // d1 in original
    private Long percentDeathToll;
    private Long increase; // birthRate?

    /// Missing:
    // q - disaster quote

    /**
     * @return area per capita, called <b>L</b> in original. Land ownership?
     */
    public Long getAreaPerCapita() {
        return Long.divideUnsigned(area, population);
    }

    public KaiserEngine() {
        this.population = 95L;
        this.zYear = 0;
        this.yield = 3L;
        this.supplies = 2800L;
        this.humans = 3000L;
        this.area = Long.divideUnsigned(this.humans, this.supplies);
        this.increase = 5L;
        this.deathToll = 0L;
    }


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public String getStatus() {
        StringBuilder status = new StringBuilder();
        status.append(ANSI_GREEN).append("STATUS").append(ANSI_RESET);
        return status.toString();
    }
}
