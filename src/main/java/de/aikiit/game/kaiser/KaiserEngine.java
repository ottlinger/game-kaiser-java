package de.aikiit.game.kaiser;

import lombok.Getter;
import org.assertj.core.util.VisibleForTesting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Getter
public class KaiserEngine {
    private final BigDecimal externalDamage = BigDecimal.ZERO;
    private final BigDecimal deathToll;
    private final BigDecimal increase; // i in original-  birthRate?
    ///
    private Integer zYear; // why z in original?
    private BigDecimal population = BigDecimal.ZERO; // h in original
    private BigDecimal area = BigDecimal.ZERO;
    private BigDecimal yield = BigDecimal.ZERO;
    private BigDecimal supplies = BigDecimal.ZERO;
    private BigDecimal humans = BigDecimal.ZERO;
    private BigDecimal deathTollSum; // d1 in original
    private BigDecimal percentDeathToll; // p1 in original
    private BigDecimal e = BigDecimal.ZERO;
    private BigDecimal q = BigDecimal.ZERO;
    private Integer c = 0;

    /// Missing:
    // q - disaster quote

    public KaiserEngine() {
        this.population = BigDecimal.valueOf(95L);
        this.zYear = 0;
        this.yield = BigDecimal.valueOf(3L);
        this.supplies = BigDecimal.valueOf(2800L);
        this.humans = BigDecimal.valueOf(3000L);
        this.area = this.humans.divide(this.supplies, RoundingMode.HALF_UP);
        this.increase = BigDecimal.valueOf(5L);
        this.deathToll = BigDecimal.ZERO;
        this.percentDeathToll = BigDecimal.ZERO;
        this.deathTollSum = BigDecimal.ZERO;
        // base values
        this.q = BigDecimal.ONE;
    }

    public void play() {
        this.area = this.humans.divide(this.supplies, RoundingMode.HALF_UP);
        this.e = this.population.subtract(this.supplies);
        this.zYear++;
        this.population = this.population.add(this.increase);

        processFamine();
        this.c = new Random().nextInt(11) + 1;
        this.yield = BigDecimal.valueOf(Long.valueOf(c) + 17L);
    }

    public void processFamine() {
        if (q.compareTo(BigDecimal.ZERO) < 0) {
            this.population = this.population.divide(BigDecimal.valueOf(2L), RoundingMode.HALF_UP);
            System.out.println("\u001B[38;5;202m");
            System.out.println("Eine fürchterliche Seuche hat die halbe Stadt dahingerafft!");
            System.out.println(KaiserEnginePrinter.ANSI_RESET);
        }
    }

    @VisibleForTesting
    void setQ(BigDecimal q) {
        this.q = q;
    }

    /**
     * @return area per capita, called <b>L</b> in original. Land ownership?
     */
    public BigDecimal getAreaPerCapita() {
        return area.divide(population, RoundingMode.HALF_UP);
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
