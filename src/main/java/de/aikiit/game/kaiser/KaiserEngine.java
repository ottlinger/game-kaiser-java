package de.aikiit.game.kaiser;

import lombok.Getter;
import org.assertj.core.util.VisibleForTesting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import static org.apache.commons.lang3.compare.ComparableUtils.is;

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
    private Integer cost = 0;

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

    public void startNewRound() {
        this.area = this.humans.divide(this.yield, RoundingMode.HALF_UP);
        this.externalDamage = this.humans.subtract(this.supplies);
        this.zYear++;
        this.population = this.population.add(this.increase);

        processFamine();
        this.cost = getRandomNumberUntil(10);
        this.yield = BigDecimal.valueOf(Long.valueOf(cost) + 17L);
    }

    int getRandomNumberUntil(int threshold) {
        return new Random().nextInt(threshold + 1) + 1;
    }

    public void processFamine() {
        if (is(q).lessThan(BigDecimal.ZERO)) {
            this.population = this.population.divide(BigDecimal.valueOf(2L), RoundingMode.HALF_UP);
            System.out.println(KaiserEnginePrinter.ORANGE);
            System.out.println("Eine fürchterliche Seuche hat die halbe Stadt dahingerafft!");
            System.out.println(KaiserEnginePrinter.ANSI_RESET);
        }
        this.q = new BigDecimal(getRandomNumberUntil(10)).divide(new BigDecimal("10"), RoundingMode.HALF_UP).
                subtract(new BigDecimal("0.3"));
    }

    @VisibleForTesting
    void setQ(BigDecimal q) {
        this.q = q;
    }

    @VisibleForTesting
    void setSupplies(BigDecimal supplies) {
        this.supplies = supplies;
    }


    @VisibleForTesting
    void setArea(BigDecimal area) {
        this.area = area;
    }

    /**
     * @return area per capita, called <b>L</b> in original. Land ownership?
     */
    public BigDecimal getAreaPerCapita() {
        return area.divide(population, RoundingMode.HALF_UP);
    }

    public long buyLand(Long buy) {
        if (buy < 0) {
            System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Ignoriere negative Eingaben - Du willst mich wohl verkackeiern." + KaiserEnginePrinter.ANSI_RESET);
        }

        if (buy > 0) {
            if (is(this.yield.multiply(BigDecimal.valueOf(buy))).lessThanOrEqualTo(this.supplies)) {
                this.area = this.area.add(BigDecimal.valueOf(buy));
                this.supplies = this.supplies.subtract(this.yield.multiply(BigDecimal.valueOf(buy)));
                this.cost = 0; // price is recalculated per round
            } else {
                throw new IllegalArgumentException("Not Enough Supplies");
            }
        }
        return buy;
    }

    public void sellLand(Long sell) {
        if (sell < 0) {
            System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Ignoriere negative Eingaben - Du willst mich wohl verkackeiern." + KaiserEnginePrinter.ANSI_RESET);
            return;
        }

        if (is(BigDecimal.valueOf(sell)).lessThan(this.area)) {
            this.area = this.area.subtract(BigDecimal.valueOf(sell));
            this.supplies = this.supplies.add(this.yield.multiply(BigDecimal.valueOf(sell)));
            this.cost = 0; // price is recalculated per round
        } else {
            throw new IllegalArgumentException("Not Enough Land");
        }
    }

    public void feedToPopulation(Long feed) {
        if (feed < 0) {
            System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Ignoriere negative Eingaben - Du willst mich wohl verkackeiern." + KaiserEnginePrinter.ANSI_RESET);
            return;
        }

        if (feed != 0) {
            if (is(BigDecimal.valueOf(feed)).lessThanOrEqualTo(this.supplies)) {
                this.supplies = this.supplies.subtract(BigDecimal.valueOf(feed));
                this.cost = 1; // price is recalculated per round
            } else {
                throw new IllegalArgumentException("Not Enough in Stock");
            }
        }
    }

    public void cultivate(Long cultivate) {
        if (cultivate == 0) {
            this.cost = getRandomNumberUntil(5);
            return;
        }

        if (cultivate < 0) {
            System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Ignoriere negative Eingaben - Du willst mich wohl verkackeiern." + KaiserEnginePrinter.ANSI_RESET);
            return;
        }

        if (is(this.area).lessThan(BigDecimal.valueOf(cultivate))) {
            throw new IllegalArgumentException("You cannot cultivate more area than you have.");
        }

        if (is(this.supplies).lessThan(BigDecimal.valueOf(cultivate).divide(BigDecimal.valueOf(2L), RoundingMode.HALF_UP))) {
            throw new IllegalArgumentException("You cannot cultivate more than you have.");
        }

        if (is(this.supplies).lessThan(BigDecimal.valueOf(cultivate).divide(BigDecimal.valueOf(2L), RoundingMode.HALF_UP))) {
            throw new IllegalArgumentException("Not enough workers available.");
        }
// TBD
    }

    public void finishRoundAfterActions() {
        // TBD
    }
}
