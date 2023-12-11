package de.aikiit.game.kaiser;

public class Kaiser {
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
    private Long increase; // birthRate

    /// Missing:
    // q - disaster quote

    /**
     * @return area per capita, called <b>L</b> in original.
     */
    public Long getAreaPerCapita() {
        return Long.divideUnsigned(area, population);
    }

    public Kaiser() {
        this.population = 95L;
        this.zYear = 0;
        this.yield = 3L;
        this.supplies = 2800L;
        this.humans = 3000L;
        this.area = Long.divideUnsigned(this.humans, this.supplies);
        this.increase = 5L;
        this.deathToll = 0L;
    }
}
