package de.aikiit.game.kaiser;

import lombok.Getter;
import org.assertj.core.util.VisibleForTesting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import static org.apache.commons.lang3.compare.ComparableUtils.is;

/**
 * This class encapsulates the game's logics and operations on its attributes.
 * As the game is based on rounds {@link #startNewRound()} starts a new round and performs calculations based on the player's actions.
 * <br />
 * In each round there is a chance for famine-induced loss,
 * which is handled in {@link #processFamine()} based on an underlying random probability factor.
 * <br />
 * Apart from these automated things the player can:
 * <ul>
 *     <li>{@link #buyLand(Long)} buy new land</li>
 *     <li>{@link #sellLand(Long)} sell existing land</li>
 *     <li>{@link #cultivate(Long)} cultivate land in order to achieve new yields</li>
 *     <li>{@link #feedToPopulation(Long)} give food to your population</li>
 * </ul>
 * After user interactions the round is finished in {@link #finishRoundAfterActions()}.
 */
@Getter
public class KaiserEngine {
    /**
     * e - External damage, e.g. loss due to rats.
     */
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
    private BigDecimal cost = BigDecimal.ZERO;

    /**
     * Default constructor to start a game with the given default settings.
     */
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

    /**
     * Starts a new round in performs initial calculations before user actions are taken into account.
     */
    public void startNewRound() {
        this.area = this.humans.divide(this.yield, 0, RoundingMode.HALF_UP);
        this.externalDamage = this.humans.subtract(this.supplies);
        this.zYear++;
        this.population = this.population.add(this.increase);

        processFamine();
        this.cost = getRandomNumberUntil(10);
        this.yield = cost.add(BigDecimal.valueOf(17L));
    }

    /**
     * Helper method to retrieve a new random number without any comma (scale=0).
     *
     * @param threshold number is greater than 0 and at most threshold.
     * @return a new random number.
     */
    BigDecimal getRandomNumberUntil(int threshold) {
        return BigDecimal.valueOf(new Random().nextInt(threshold + 1) + 1).setScale(0, RoundingMode.HALF_EVEN);
    }

    /**
     * Evaluate internally, if a famine is happening in the current round.
     * If so this method performs all necessary calculations/reductions within the currently running game.
     */
    public void processFamine() {
        if (is(q).lessThan(BigDecimal.ZERO)) {
            this.population = this.population.divide(BigDecimal.valueOf(2L), 0, RoundingMode.HALF_UP);
            System.out.println(KaiserEnginePrinter.ORANGE);
            System.out.println("Eine fürchterliche Seuche hat die halbe Stadt dahingerafft!");
            System.out.println(KaiserEnginePrinter.ANSI_RESET);
        }
        refreshFamineQuotient();
    }

    /**
     * Explicitly trigger the recalculation of the given internal famine calculation factor.
     */
    void refreshFamineQuotient() {
        this.q = getRandomNumberUntil(10).divide(BigDecimal.TEN, 0, RoundingMode.HALF_UP).subtract(new BigDecimal("0.3"));

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
     * Calculates the available area per person in the current game.
     *
     * @return area per capita, called <b>L</b> in original. Land ownership?
     */
    public BigDecimal getAreaPerCapita() {
        return area.divide(population, 0, RoundingMode.HALF_UP);
    }

    public long buyLand(Long buy) {
        if (buy < 0) {
            System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Ignoriere negative Eingaben - Du willst mich wohl verkackeiern." + KaiserEnginePrinter.ANSI_RESET);
        }

        if (buy > 0) {
            if (is(this.yield.multiply(BigDecimal.valueOf(buy))).lessThanOrEqualTo(this.supplies)) {
                this.area = this.area.add(BigDecimal.valueOf(buy));
                this.supplies = this.supplies.subtract(this.yield.multiply(BigDecimal.valueOf(buy)));
                this.cost = BigDecimal.ZERO; // price is recalculated per round
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
            this.cost = BigDecimal.ZERO; // price is recalculated per round
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
                this.cost = BigDecimal.ONE; // price is recalculated per round
            } else {
                throw new IllegalArgumentException("Not Enough in Stock");
            }
        }
    }

    public void cultivate(Long cultivate) {
        if (cultivate == 0) {
            calculateNewPrice();
            return;
        }

        if (cultivate < 0) {
            System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Ignoriere negative Eingaben - Du willst mich wohl verkackeiern." + KaiserEnginePrinter.ANSI_RESET);
            return;
        }

        if (is(this.area).lessThan(BigDecimal.valueOf(cultivate))) {
            throw new IllegalArgumentException("You cannot cultivate more area than you have.");
        }

        BigDecimal halfCultivate = BigDecimal.valueOf(cultivate).divide(BigDecimal.valueOf(2L), 0, RoundingMode.HALF_UP);
        if (is(this.supplies).lessThan(halfCultivate)) {
            throw new IllegalArgumentException("You cannot cultivate more than you have.");
        }

        if (is(BigDecimal.valueOf(cultivate)).greaterThan(getPopulation().multiply(BigDecimal.TEN))) {
            throw new IllegalArgumentException("Not enough workers available.");
        }

        // perform seeding
        this.supplies = this.supplies.subtract(halfCultivate);
        calculateNewPrice();

        // yields after cultivation and population increase
        this.yield = this.cost;
        this.humans = this.yield.multiply(BigDecimal.valueOf(cultivate));

        // cultivation kills rats ;)
        this.externalDamage = BigDecimal.ZERO;
        calculateNewPrice();

        // but add some external damage in some cases in a naiive manner
        // original condition stated: if int(c/2) <> c/2
        if (this.cost.divide(BigDecimal.valueOf(2L), 0, RoundingMode.DOWN).intValue() == this.cost.divide(BigDecimal.valueOf(2L), 0, RoundingMode.UP).intValue()) {
            this.externalDamage = this.supplies.divide(this.cost, 0, RoundingMode.HALF_UP);
        }
        this.supplies = this.supplies.subtract(this.externalDamage).add(this.humans);
        calculateNewPrice();
    }

    @VisibleForTesting
    /**
     * Change price for next round.
     * @see #cost
     */
    void calculateNewPrice() {
        this.cost = getRandomNumberUntil(5);
    }

    public void finishRoundAfterActions() {
        BigDecimal factor = BigDecimal.valueOf(20L).multiply(this.area).add(this.supplies);
        this.increase = cost.multiply(factor).divide(this.population, 0, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(100).add(BigDecimal.ONE), 0, RoundingMode.HALF_UP);

        this.cost = this.q.divide(BigDecimal.valueOf(20L), 0, RoundingMode.HALF_UP);
        refreshFamineQuotient();

        if (is(this.population).lessThan(this.cost)) {
            this.deathToll = BigDecimal.ZERO;
            return; // start new round without any deaths
        }

        // calculate deaths
        this.deathToll = this.population.subtract(this.cost);
        if (is(this.deathToll).greaterThan(this.population.multiply(BigDecimal.valueOf(0.45)))) {
            System.out.println(KaiserEnginePrinter.ANSI_YELLOW);
            System.out.println("Sie haben " + this.deathToll + " Menschen in nur einem Jahr verhungern lassen!");
            System.out.println("Auf Grund dieser extremen Misswirtschaft, werden Sie nicht nur aus Amt und Würden gejagt,");
            System.out.println("sondern auch zum Versager des Jahres erklärt.");
            System.out.println(KaiserEnginePrinter.ANSI_RESET);
            return; // TODO stop the game here!
        }

        // calc death statistics
        // p1 = ((z-1)*p1+D*100/p)/z
        BigDecimal tempQuotient = this.percentDeathToll.multiply(BigDecimal.valueOf(this.zYear - 1)).add(this.deathToll.multiply(BigDecimal.valueOf(100)).divide(this.population, 0, RoundingMode.HALF_UP));
        this.percentDeathToll = tempQuotient.divide(BigDecimal.valueOf(this.zYear), 0, RoundingMode.HALF_UP);

        this.population = this.cost; // TODO why? shouldn't this somehow be added up?
        this.deathTollSum = this.deathTollSum.add(this.deathToll);
    }
}
