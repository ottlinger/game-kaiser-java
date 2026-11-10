package de.aikiit.game.kaiser;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Handles the console dialogue for buying and selling land, feeding the population
 * and planting crops. Each action requests a quantity and delegates validation and
 * changes to the game state to {@link KaiserEngine}.
 * <p>
 * Invalid numbers and quantities rejected by the engine produce an explanation and
 * another prompt. Negative quantities end the game according to the BASIC rules;
 * exhausted input also aborts the game rather than prompting indefinitely.
 * All actions use the caller's shared scanner so buffered input is preserved between
 * prompts. The scanner is not closed here because its lifetime belongs to the caller.
 */
public final class KaiserActions {
    private KaiserActions() {
    }

    /**
     * Asks how many hectares to buy and retries until the engine accepts the quantity
     * or the game is aborted. A positive purchase spends grain at the current land
     * price; entering zero leaves stocks unchanged and allows the caller to offer
     * selling land instead.
     *
     * @param engine game whose land and grain stocks are updated
     * @param input shared console scanner; decimal quantities use a point
     * @return {@code true} after an accepted positive purchase; {@code false} for
     *         zero, a negative quantity or exhausted input. The caller must also
     *         check {@link KaiserEngine#isGameOver()} before offering another action.
     */
    public static boolean buy(KaiserEngine engine, Scanner input) {
        return perform(engine, input, "Wieviel Land wollen Sie kaufen? 0 = nichts", engine::buyLand).signum() > 0;
    }

    /**
     * Asks how many hectares to sell in exchange for grain at the current land price.
     * Selling all owned land or more is rejected with a new prompt; zero is accepted
     * without changing ownership or stocks. A negative quantity or exhausted input
     * aborts the game and returns control to the caller.
     *
     * @param engine game whose land and grain stocks are updated
     * @param input shared console scanner; decimal quantities use a point
     */
    public static void sell(KaiserEngine engine, Scanner input) {
        perform(engine, input, "Wieviel Land wollen Sie verkaufen?", engine::sellLand);
    }

    /**
     * Asks how much stored grain to distribute to the population. The accepted amount
     * is removed from stocks and retained by the engine for the later starvation
     * calculation; this dialogue does not immediately change the population.
     * Allocating more grain than is available causes another prompt. Zero is valid,
     * while a negative quantity or exhausted input aborts the game.
     *
     * @param engine game whose stocks and food allocation are updated
     * @param input shared console scanner; quantities are in DZ of grain
     */
    public static void feed(KaiserEngine engine, Scanner input) {
        perform(engine, input, "Wieviele DZ wollen Sie an Ihr Volk verteilen?", engine::feedToPopulation);
    }

    /**
     * Asks how many hectares to plant and delegates seed consumption, harvest and
     * rat losses to the engine. Rejected quantities produce an explanation about
     * available land, seed grain or workers before asking again.
     * Entering zero still processes the season's harvest and possible rat losses;
     * a negative quantity or exhausted input aborts the game instead.
     *
     * @param engine game whose planting and harvest are processed
     * @param input shared console scanner; quantities are in hectares
     */
    public static void cultivate(KaiserEngine engine, Scanner input) {
        perform(engine, input, "Wieviel Land wollen Sie bebauen?", engine::cultivate);
    }

    /**
     * Runs the common prompt/read/validate loop for one action. Numeric input uses
     * a locale-independent decimal point. Non-numeric tokens are consumed and
     * retried; resource validation errors from the engine are translated into
     * player-facing messages. Negative numbers are passed through to the engine,
     * which owns the BASIC abort rule.
     *
     * @param engine game used for resource explanations and end-of-input handling
     * @param input shared scanner, whose locale is set to {@link Locale#ROOT}
     * @param prompt question displayed before each attempt
     * @param action engine operation that validates and applies the entered quantity
     * @return accepted quantity, including a negative abort request, or zero when
     *         input ends; the game state distinguishes an abort from a valid zero
     */
    private static BigDecimal perform(KaiserEngine engine, Scanner input, String prompt, Consumer<BigDecimal> action) {
        input.useLocale(Locale.ROOT); // BASIC uses a decimal point, independent of the host locale.
        while (true) {
            System.out.println(KaiserEnginePrinter.ANSI_PURPLE + prompt + KaiserEnginePrinter.ANSI_RESET);
            if (!input.hasNextBigDecimal()) {
                if (!input.hasNext()) {
                    engine.abort();
                    return BigDecimal.ZERO;
                }
                input.next();
                System.out.println("Bitte eine Zahl eingeben (Dezimalpunkt statt Komma).");
                continue;
            }
            BigDecimal amount = input.nextBigDecimal();
            try {
                action.accept(amount);
                return amount;
            } catch (IllegalArgumentException e) {
                switch (e.getMessage()) {
                    case "Not Enough Land", "You cannot cultivate more area than you have." ->
                            System.out.println("Überlegen Sie doch, Sie haben nur " + engine.getLandArea() + " Hektar Land.");
                    case "Not enough workers available." ->
                            System.out.println("Sie haben aber nur " + engine.getPopulation() + " Arbeiter. Noch einmal.");
                    default -> System.out.println("Nun denken Sie doch mal nach, Sie haben nur "
                            + engine.getGrainStocks() + " Korn zur Verfügung. Nun denn.");
                }
            }
        }
    }
}
