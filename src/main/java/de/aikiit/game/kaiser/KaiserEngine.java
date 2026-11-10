package de.aikiit.game.kaiser;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

/**
 * Maintains the city's population, land, grain stocks and reign statistics, and
 * applies the rules from KAISER.BAS (09.08.1992) without performing console I/O.
 * The caller controls the dialogue and the order in which the player's actions
 * are requested.
 * <p>
 * A year starts with {@link #startNewRound()}, which adds immigrants and applies
 * any plague determined at the end of the previous year. After reporting that
 * state, the caller invokes {@link #prepareLandTrading()}, offers buying or
 * selling land, allocates food and processes planting. Finally,
 * {@link #finishRoundAfterActions()} determines immigration and starvation and
 * may dismiss the ruler. After ten action years, one further year-start update
 * prepares the final report and marks the reign as completed.
 * <p>
 * BASIC's reused variables are deliberately separated here: Q is food allocated
 * and later a plague roll; Y is harvest yield and later land price; C is several
 * unrelated random factors. Quantities may be fractional. Explicit BASIC
 * {@code INT} operations round down, while fractional statistics retain decimal
 * precision. Negative action quantities abort the game; other invalid quantities
 * are rejected before the action changes resources.
 */
@Getter
public class KaiserEngine {
    private static final MathContext PRECISION = MathContext.DECIMAL128;
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    /** Describes whether further annual updates and player actions are permitted. */
    public enum GameState {
        /** The reign is in progress and the caller may advance the game. */
        RUNNING,
        /** The ten action years and the final year-start update have completed. */
        COMPLETED,
        /** More than 45 percent of the population starved in one year. */
        DISMISSED,
        /** The player or console input ended the game before completion. */
        ABORTED
    }

    /** Final rating based on average starvation and land owned per inhabitant. */
    public enum Regency {
        /** Average starvation exceeds 33 percent, or land per person is below seven hectares. */
        DISASTROUS,
        /** Below the disastrous tier, but starvation exceeds 10 percent or land per person is below nine. */
        TYRANNICAL,
        /** Below the worse tiers, but starvation exceeds three percent or land per person is below ten. */
        MEDIOCRE,
        /** Average starvation is at most three percent and land per person is at least ten hectares. */
        EXCELLENT
    }

    @Getter(lombok.AccessLevel.NONE)
    private final Random random;
    private int year;
    private BigDecimal population;
    private BigDecimal landArea;
    private BigDecimal grainStocks;
    private BigDecimal harvestYield;
    private BigDecimal totalHarvest;
    private BigDecimal ratLoss;
    private BigDecimal immigrants;
    private BigDecimal starvationDeaths;
    private BigDecimal totalStarvationDeaths;
    private BigDecimal averageStarvationPercentage;
    private BigDecimal landPrice;
    private BigDecimal foodAllocated;
    private int plagueRoll;
    private int immigrationFactor;
    private boolean plagueOccurred;
    private GameState state;

    /**
     * Creates a new reign with the BASIC starting resources and a secure random
     * source for prices, harvests, rat losses, immigration and plague events.
     * The first annual population update is deferred until {@link #startNewRound()}.
     */
    public KaiserEngine() {
        this(new SecureRandom());
    }

    /**
     * Creates a new reign using a caller-supplied source of random values. Injecting
     * a known sequence makes the BASIC calculations and random draw order testable
     * without changing the game rules.
     *
     * @param random source supplying values in the interval {@code [0, 1)}
     * @throws NullPointerException if {@code random} is null
     */
    KaiserEngine(Random random) {
        this.random = Objects.requireNonNull(random);
        reset();
    }

    /**
     * Restores all resources, annual figures and accumulated statistics to their
     * starting values, and makes the game runnable again. The city starts with
     * 95 inhabitants, 1000 hectares and 2800 DZ of grain. Its initial report refers
     * to a harvest of 3000 DZ at three DZ per hectare, 200 DZ lost to rats and five
     * immigrants who will arrive at the first year start.
     * <p>
     * The supplied random source is retained rather than reseeded, so a replay
     * continues drawing new values from the same source. Corresponds to BASIC
     * lines 90–110 and is also used when the player starts again.
     */
    void reset() {
        year = 0;
        population = BigDecimal.valueOf(95);
        grainStocks = BigDecimal.valueOf(2800);
        totalHarvest = BigDecimal.valueOf(3000);
        harvestYield = BigDecimal.valueOf(3);
        landArea = BigDecimal.valueOf(1000);
        ratLoss = BigDecimal.valueOf(200);
        immigrants = BigDecimal.valueOf(5);
        starvationDeaths = BigDecimal.ZERO;
        totalStarvationDeaths = BigDecimal.ZERO;
        averageStarvationPercentage = BigDecimal.ZERO;
        landPrice = BigDecimal.ZERO;
        foodAllocated = BigDecimal.ZERO;
        plagueRoll = 1;
        immigrationFactor = 0;
        plagueOccurred = false;
        state = GameState.RUNNING;
    }

    /**
     * Advances to the next report year, adds the previously calculated immigrants
     * and applies the stored plague roll. A roll of zero or less halves the updated
     * population, rounding down; plague deaths do not enter starvation statistics.
     * Land ownership, harvest figures and rat losses remain unchanged for reporting.
     * <p>
     * On entering year 11, the same population update still takes place, but the
     * game becomes {@link GameState#COMPLETED}: the caller should report and evaluate
     * the reign without offering another action phase. This method prepares the
     * report data but does not print it. Corresponds to BASIC lines 220–270.
     *
     * @throws IllegalStateException if the game has already ended
     */
    public void startNewRound() {
        requireRunning();
        year++;
        population = population.add(immigrants);
        plagueOccurred = plagueRoll <= 0;
        if (plagueOccurred) {
            population = floor(population.divide(BigDecimal.valueOf(2), PRECISION));
        }
        if (year == KaiserGame.MAX_ROUNDS + 1) {
            state = GameState.COMPLETED;
        }
    }

    /**
     * Draws the grain price for one hectare of land, uniformly from 17 through 26 DZ,
     * and stores it for both buying and selling during the current year. It does not
     * replace the previous harvest yield or consume grain.
     * <p>
     * The caller invokes this after reporting the previous harvest and before
     * asking for a trade, matching BASIC line 310.
     *
     * @throws IllegalStateException if the game has already ended
     */
    public void prepareLandTrading() {
        requireRunning();
        landPrice = BigDecimal.valueOf((int) Math.floor(10 * random.nextDouble()) + 17);
    }

    /**
     * Buys a whole-number quantity of land using the same validation, grain payment
     * and abort handling as {@link #buyLand(BigDecimal)}.
     *
     * @param hectares hectares requested; zero skips buying and a negative value aborts
     * @return the requested quantity, including zero or a negative abort request
     * @throws IllegalArgumentException if the purchase costs more grain than is available
     * @throws IllegalStateException if the game has already ended
     */
    public long buyLand(long hectares) {
        buyLand(BigDecimal.valueOf(hectares));
        return hectares;
    }

    /**
     * Increases owned land by the requested hectares and pays for it from grain stocks
     * at the current land price. Spending exactly all available grain is allowed.
     * A zero quantity changes no resources and lets the caller offer selling instead.
     * A negative quantity aborts the game without changing land or stocks.
     * <p>
     * The caller must first establish the year's price with
     * {@link #prepareLandTrading()}. Corresponds to BASIC lines 320–334.
     *
     * @param hectares requested land area, which may be fractional
     * @return the requested quantity; a negative return denotes an abort, not a purchase
     * @throws IllegalArgumentException if the purchase costs more grain than is available
     * @throws IllegalStateException if the game has already ended
     */
    public BigDecimal buyLand(BigDecimal hectares) {
        requireRunning();
        if (abortOnNegative(hectares)) {
            return hectares;
        }
        BigDecimal price = landPrice.multiply(hectares);
        if (price.compareTo(grainStocks) > 0) {
            throw new IllegalArgumentException("Not Enough Supplies");
        }
        landArea = landArea.add(hectares);
        grainStocks = grainStocks.subtract(price);
        return hectares;
    }

    /**
     * Sells a whole-number quantity of land using the same price, ownership checks
     * and abort handling as {@link #sellLand(BigDecimal)}.
     *
     * @param hectares hectares offered; zero skips selling and a negative value aborts
     * @throws IllegalArgumentException if the quantity equals or exceeds owned land
     * @throws IllegalStateException if the game has already ended
     */
    public void sellLand(long hectares) {
        sellLand(BigDecimal.valueOf(hectares));
    }

    /**
     * Reduces owned land by the offered hectares and credits grain stocks at the
     * current land price. The city must retain some land: selling exactly the owned
     * area is rejected just like selling more. Zero leaves resources unchanged;
     * a negative quantity aborts the game without a trade.
     * <p>
     * The caller offers this action only when the player bought zero hectares and
     * must first establish the year's price with {@link #prepareLandTrading()}.
     * Corresponds to BASIC lines 340–350, including the strict comparison at line 342.
     *
     * @param hectares offered land area, which may be fractional
     * @throws IllegalArgumentException if the quantity equals or exceeds owned land
     * @throws IllegalStateException if the game has already ended
     */
    public void sellLand(BigDecimal hectares) {
        requireRunning();
        if (abortOnNegative(hectares)) {
            return;
        }
        if (hectares.compareTo(landArea) >= 0) {
            throw new IllegalArgumentException("Not Enough Land");
        }
        landArea = landArea.subtract(hectares);
        grainStocks = grainStocks.add(landPrice.multiply(hectares));
    }

    /**
     * Allocates a whole-number amount of grain for feeding using the same stock
     * checks, deferred starvation calculation and abort handling as
     * {@link #feedToPopulation(BigDecimal)}.
     *
     * @param grain grain to distribute in DZ; zero is valid and a negative value aborts
     * @throws IllegalArgumentException if the allocation exceeds available grain stocks
     * @throws IllegalStateException if the game has already ended
     */
    public void feedToPopulation(long grain) {
        feedToPopulation(BigDecimal.valueOf(grain));
    }

    /**
     * Removes the requested grain from stocks and remembers it as this year's food
     * allocation. Population does not change immediately: at year end, every complete
     * 20 DZ will support one inhabitant. This separates BASIC's food variable Q from
     * the later plague roll so that a random event cannot overwrite the allocation.
     * <p>
     * Zero is a valid allocation, distributing all available stocks is allowed, and
     * a negative quantity aborts without consuming grain. The caller invokes feeding
     * once after trading and before planting. Corresponds to BASIC lines 411–430;
     * the stored allocation is evaluated at line 540.
     *
     * @param grain grain to distribute in DZ, which may be fractional
     * @throws IllegalArgumentException if the allocation exceeds available grain stocks
     * @throws IllegalStateException if the game has already ended
     */
    public void feedToPopulation(BigDecimal grain) {
        requireRunning();
        if (abortOnNegative(grain)) {
            return;
        }
        if (grain.compareTo(grainStocks) > 0) {
            throw new IllegalArgumentException("Not Enough in Stock");
        }
        foodAllocated = grain;
        grainStocks = grainStocks.subtract(grain);
    }

    /**
     * Plants a whole-number area and processes the harvest using the same resource
     * checks, random draws and abort handling as {@link #cultivate(BigDecimal)}.
     *
     * @param hectares area to plant; zero still processes the season and a negative value aborts
     * @throws IllegalArgumentException if the land, seed or worker limits are violated
     * @throws IllegalStateException if the game has already ended
     */
    public void cultivate(long hectares) {
        cultivate(BigDecimal.valueOf(hectares));
    }

    /**
     * Validates the planted area, consumes seed grain and processes this year's
     * harvest and rat damage. For a positive area, the city must own at least that
     * much land, have strictly more grain than {@code floor(hectares / 2)}, and
     * plant strictly fewer than ten hectares per inhabitant. Validation failures
     * change no resources and consume no random values.
     * <p>
     * After consuming the seed, a roll from one through five determines harvest
     * yield per hectare. A second such roll determines rat losses: even rolls remove
     * {@code floor(remaining stocks / roll)} grain, while odd rolls cause no damage.
     * Rat losses are deducted before the new harvest is added. A third roll is
     * retained as the factor for the year-end immigration calculation.
     * <p>
     * Planting zero bypasses resource checks and seed consumption but still performs
     * all three draws and stock updates, with a harvest of zero. Negative input
     * aborts without processing the season. Corresponds to BASIC lines 441–531.
     *
     * @param hectares area to plant, which may be fractional
     * @throws IllegalArgumentException if the land, seed or worker limits are violated
     * @throws IllegalStateException if the game has already ended
     */
    public void cultivate(BigDecimal hectares) {
        requireRunning();
        if (abortOnNegative(hectares)) {
            return;
        }
        BigDecimal plantedArea = hectares;
        BigDecimal seed = plantedArea.divide(BigDecimal.valueOf(2), 0, RoundingMode.FLOOR);
        if (hectares.signum() != 0) {
            if (plantedArea.compareTo(landArea) > 0) {
                throw new IllegalArgumentException("You cannot cultivate more area than you have.");
            }
            // These two comparisons are strictly '<' in the printed BASIC.
            if (seed.compareTo(grainStocks) >= 0) {
                throw new IllegalArgumentException("You cannot cultivate more than you have.");
            }
            if (plantedArea.compareTo(population.multiply(BigDecimal.TEN)) >= 0) {
                throw new IllegalArgumentException("Not enough workers available.");
            }
            grainStocks = grainStocks.subtract(seed);
        }

        harvestYield = BigDecimal.valueOf(rollOneToFive());
        totalHarvest = plantedArea.multiply(harvestYield);
        int ratRoll = rollOneToFive();
        // Line 522 skips damage for odd C; only even rolls cause rat losses.
        ratLoss = ratRoll % 2 == 0
                ? grainStocks.divide(BigDecimal.valueOf(ratRoll), 0, RoundingMode.FLOOR)
                : BigDecimal.ZERO;
        grainStocks = grainStocks.subtract(ratLoss).add(totalHarvest);
        immigrationFactor = rollOneToFive();
    }

    /**
     * Resolves the year's demographic consequences after trading, feeding and
     * planting. Immigration for the next year is calculated from owned land,
     * remaining grain, current population and the factor drawn during planting.
     * The food allocation supports {@code floor(foodAllocated / 20)} inhabitants;
     * only after calculating that number is next year's plague roll drawn.
     * <p>
     * If food supports more people than currently live in the city, starvation
     * deaths become zero, but population and accumulated starvation statistics
     * remain unchanged. Otherwise, unsupported inhabitants count as starvation
     * deaths. Losing more than 45 percent of the population immediately sets
     * {@link GameState#DISMISSED}, without updating population or accumulated statistics.
     * <p>
     * For a surviving reign, the running annual starvation percentage and total
     * deaths are updated, and population is reduced to the supported number.
     * Immigrants and any plague take effect only at the next year start; plague
     * victims are never counted as starvation deaths. Corresponds to BASIC lines 533–556.
     *
     * @throws IllegalStateException if the game has already ended
     */
    public void finishRoundAfterActions() {
        requireRunning();
        BigDecimal immigrationNumerator = BigDecimal.valueOf(immigrationFactor)
                .multiply(BigDecimal.valueOf(20).multiply(landArea).add(grainStocks));
        immigrants = immigrationNumerator.divide(population.multiply(HUNDRED), 0, RoundingMode.FLOOR)
                .add(BigDecimal.ONE);
        BigDecimal supportedPopulation = foodAllocated.divide(BigDecimal.valueOf(20), 0, RoundingMode.FLOOR);
        plagueRoll = (int) Math.floor(10 * random.nextDouble() - 0.3);

        if (population.compareTo(supportedPopulation) < 0) {
            // BASIC jumps through line 210: D=0, without updating P1 or D1.
            starvationDeaths = BigDecimal.ZERO;
            return;
        }
        starvationDeaths = population.subtract(supportedPopulation);
        if (starvationDeaths.compareTo(population.multiply(new BigDecimal("0.45"))) > 0) {
            state = GameState.DISMISSED;
            return;
        }
        averageStarvationPercentage = averageStarvationPercentage.multiply(BigDecimal.valueOf(year - 1L))
                .add(starvationDeaths.multiply(HUNDRED).divide(population, PRECISION))
                .divide(BigDecimal.valueOf(year), PRECISION);
        population = supportedPopulation;
        totalStarvationDeaths = totalStarvationDeaths.add(starvationDeaths);
    }

    /**
     * Calculates owned hectares per current inhabitant without rounding the result
     * to a whole number. Evaluation uses this ratio so fractional differences near
     * the seven-, nine- and ten-hectare thresholds remain significant.
     *
     * @return land area divided by population, using the engine's decimal precision
     */
    public BigDecimal getLandPerPerson() {
        return landArea.divide(population, PRECISION);
    }

    /**
     * Classifies the reign using average annual starvation and current land per
     * inhabitant, checking the worst applicable tier first. More than 33 percent
     * average starvation or fewer than seven hectares per person is disastrous;
     * more than ten percent or fewer than nine hectares is tyrannical; more than
     * three percent or fewer than ten hectares is mediocre. Otherwise the reign
     * is excellent. Threshold comparisons use unrounded values.
     * <p>
     * This method only evaluates the current figures: it does not end the game,
     * print a message or consume randomness. The caller normally uses it after
     * the final report-year update. Corresponds to BASIC lines 880–896.
     *
     * @return the worst rating whose starvation or land condition applies
     */
    public Regency getRegency() {
        BigDecimal landPerPerson = getLandPerPerson();
        if (averageStarvationPercentage.compareTo(BigDecimal.valueOf(33)) > 0
                || landPerPerson.compareTo(BigDecimal.valueOf(7)) < 0) {
            return Regency.DISASTROUS;
        }
        if (averageStarvationPercentage.compareTo(BigDecimal.TEN) > 0
                || landPerPerson.compareTo(BigDecimal.valueOf(9)) < 0) {
            return Regency.TYRANNICAL;
        }
        if (averageStarvationPercentage.compareTo(BigDecimal.valueOf(3)) > 0
                || landPerPerson.compareTo(BigDecimal.TEN) < 0) {
            return Regency.MEDIOCRE;
        }
        return Regency.EXCELLENT;
    }

    /**
     * Draws the number of residents who would like to hang the ruler in the
     * mediocre-reign message. The result is
     * {@code floor(population * 0.8 * randomValue)}, rather than a fixed 80 percent
     * of the population. No population or resource figures are changed.
     * <p>
     * Each call consumes a fresh random value. To preserve the BASIC draw order,
     * the caller invokes this only when rendering that evaluation message, as
     * at BASIC line 961.
     *
     * @return a non-negative, whole-number count of discontented residents
     */
    public BigDecimal countDiscontentedResidents() {
        return floor(population.multiply(new BigDecimal("0.8"))
                .multiply(BigDecimal.valueOf(random.nextDouble())));
    }

    /**
     * Indicates whether the reign completed, the ruler was dismissed or play was
     * aborted. All three terminal states prevent further annual updates and actions.
     *
     * @return {@code true} whenever the state is not {@link GameState#RUNNING}
     */
    public boolean isGameOver() {
        return state != GameState.RUNNING;
    }

    /**
     * Marks the game as aborted without changing resources or demographic figures.
     * Used for negative action quantities and exhausted console input. The caller
     * is responsible for stopping the dialogue and displaying any closing message.
     */
    public void abort() {
        state = GameState.ABORTED;
    }

    /**
     * Applies the shared BASIC rule that a negative action quantity ends the game
     * instead of requesting another quantity. Non-negative values are left for
     * the action's normal validation.
     *
     * @param input quantity supplied to a player action
     * @return whether the game was aborted because the quantity was negative
     */
    private boolean abortOnNegative(BigDecimal input) {
        if (input.signum() < 0) {
            abort();
            return true;
        }
        return false;
    }

    /**
     * Prevents an annual update or player action from modifying an ended reign.
     * This checks the terminal state, not the ordering of actions within a year;
     * the caller remains responsible for that order.
     *
     * @throws IllegalStateException if the game has completed, dismissed the ruler or been aborted
     */
    private void requireRunning() {
        if (isGameOver()) {
            throw new IllegalStateException("The game has ended.");
        }
    }

    /**
     * Consumes one random value and converts it to the inclusive range one through
     * five, matching the BASIC subroutine used for harvest, rat and immigration rolls.
     *
     * @return a uniformly distributed integer from one through five
     */
    private int rollOneToFive() {
        return (int) Math.floor(random.nextDouble() * 5) + 1;
    }

    /**
     * Implements BASIC's {@code INT} operation by rounding toward negative infinity,
     * not toward zero or to the nearest integer.
     *
     * @param value number whose fractional part is to be rounded down
     * @return the greatest integer less than or equal to {@code value}, with scale zero
     */
    private static BigDecimal floor(BigDecimal value) {
        return value.setScale(0, RoundingMode.FLOOR);
    }
}
