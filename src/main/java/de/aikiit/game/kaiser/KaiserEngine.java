package de.aikiit.game.kaiser;

import lombok.Getter;

@Getter
public class KaiserEngine {
    private final Long externalDamage = 0L;
    ///
    private final Integer zYear; // why z in original?
    private final Long deathToll;
    private final Long increase; // birthRate?
    private Long population = 0L;
    private Long area = 0L;
    private Long yield = 0L;
    private Long supplies = 0L;
    private Long humans = 0L;
    private Long deathTollSum; // d1 in original
    private Long percentDeathToll; // p1 in original

    /// Missing:
    // q - disaster quote

    public KaiserEngine() {
        this.population = 95L;
        this.zYear = 0;
        this.yield = 3L;
        this.supplies = 2800L;
        this.humans = 3000L;
        this.area = this.humans / this.supplies;
        this.increase = 5L;
        this.deathToll = 0L;
        this.percentDeathToll = 0L;
        this.deathTollSum = 0L;
    }

    /**
     * @return area per capita, called <b>L</b> in original. Land ownership?
     */
    public Long getAreaPerCapita() {
        return Long.divideUnsigned(area, population);
    }

    public void buyLand(Long buy) {
        System.out.println("TODO: Würde versuchen " + buy + " zu kaufen.");
    }
}
