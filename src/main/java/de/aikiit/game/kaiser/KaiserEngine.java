package de.aikiit.game.kaiser;

import lombok.Getter;
import org.assertj.core.util.VisibleForTesting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Getter
public class KaiserEngine {
    private BigDecimal externalDamage = BigDecimal.ZERO; // e
    private BigDecimal deathToll; // d
    private BigDecimal increase; // i in original-  birthRate?
    private Integer zYear; // why z in original?
    private BigDecimal population = BigDecimal.ZERO; // h in original
    private BigDecimal area = BigDecimal.ZERO;
    private BigDecimal yield = BigDecimal.ZERO;
    private BigDecimal supplies = BigDecimal.ZERO;
    private BigDecimal humans = BigDecimal.ZERO;
    private BigDecimal deathTollSum; // d1 in original
    private BigDecimal percentDeathToll; // p1 in original
    private BigDecimal q = BigDecimal.ONE; // q - disaster/famineQuotient
    private Integer c = 0;

    public KaiserEngine() {
        this.population = BigDecimal.valueOf(95L);
        this.zYear = 0;
        this.yield = BigDecimal.valueOf(3L);
        this.supplies = BigDecimal.valueOf(2800L);
        this.humans = BigDecimal.valueOf(3000L);
        this.area = this.humans.divide(this.yield, RoundingMode.HALF_UP);
        this.increase = BigDecimal.valueOf(5L);
        this.deathToll = BigDecimal.ZERO;
        this.percentDeathToll = BigDecimal.ZERO;
        this.deathTollSum = BigDecimal.ZERO;
        this.externalDamage = this.humans.subtract(this.supplies);
    }

    public void play() {
        this.area = this.humans.divide(this.supplies, RoundingMode.HALF_UP);
        this.externalDamage = this.population.subtract(this.supplies);
        this.zYear++;
        this.population = this.population.add(this.increase);

        processFamine();
        this.c = getRandomNumberUntil(10);
        this.yield = BigDecimal.valueOf(Long.valueOf(c) + 17L);
    }

    int getRandomNumberUntil(int threshold) {
        return new Random().nextInt(threshold + 1) + 1;
    }

    public void processFamine() {
        if (BigDecimal.ZERO.compareTo(q) <= 0) {
            this.population = this.population.divide(BigDecimal.valueOf(2L), RoundingMode.HALF_UP);
            System.out.println("\u001B[38;5;202m");
            System.out.println("Eine fürchterliche Seuche hat die halbe Stadt dahingerafft!");
            System.out.println(KaiserEnginePrinter.ANSI_RESET);
        }
        this.q = new BigDecimal(10L * getRandomNumberUntil(2)).subtract(new BigDecimal("0.3"));
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

    public void applyActions() {
//        this.increase =
//        this.c =
    }
}
