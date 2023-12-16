package de.aikiit.game.kaiser;

import lombok.Getter;

import java.util.Random;

@Getter
public class KaiserEngine {
    private final Long externalDamage = 0L;
    private final Long deathToll;
    private final Long increase; // i in original-  birthRate?
    ///
    private Integer zYear; // why z in original?
    private Long population = 0L; // h in original
    private Long area = 0L;
    private Long yield = 0L;
    private Long supplies = 0L;
    private Long humans = 0L;
    private Long deathTollSum; // d1 in original
    private Long percentDeathToll; // p1 in original
    private Long e = 0L;
    private Long q = 0L;
    private Integer c = 0;

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
        // base values
        this.q = 1L;
    }

    public void play() {
        this.area = this.humans / this.supplies;
        this.e = this.population - this.supplies;
        this.zYear++;
        this.population += this.increase;

        if (q < 0) {
            this.population /= 2;
            System.out.println("\u001B[38;5;202m");
            System.out.println("Eine fürchterliche Seuche hat die halbe Stadt dahingerafft!");
            System.out.println(KaiserEnginePrinter.ANSI_RESET);
        }

        this.c = new Random().nextInt(11) + 1;
        this.yield = Long.valueOf(c) + 17;
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

    public void sellLand(Long sell) {
        System.out.println("TODO: Würde versuchen " + sell + " zu verkaufen.");
    }

    public void feedToPopulation(Long feed) {
        System.out.println("TODO: Würde versuchen " + feed + " an Bevölkerung zu geben.");
    }

    public void cultivate(Long cultivate) {
        System.out.println("TODO: Würde versuchen " + cultivate + " anzubauen.");
    }
}
