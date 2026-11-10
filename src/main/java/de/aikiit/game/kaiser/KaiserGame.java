package de.aikiit.game.kaiser;

import java.util.Objects;
import java.util.Scanner;

/**
 * This class encapsulates the actual game flow.
 * A player
 * <ul>
 *     <li>is shown current statistics and</li>
 *     <li>can start to act</li>
 *     <li>until the maximum number of rounds is reached and the game ends.</li>
 * </ul>
 */
public class KaiserGame {

    /**
     * The player can play at most this number of rounds.
     */
    public static final int MAX_ROUNDS = 10;

    private final KaiserEngine engine;
    private final KaiserEnginePrinter printer;
    private final Scanner input;

    /**
     * Initializes a game with default parameters.
     */
    public KaiserGame() {
        this(new KaiserEngine(), new Scanner(System.in));
    }

    /**
     * Allows setting an explicit engine for testing purposes.
     * @param engine initialize with the given engine.
     */
    KaiserGame(KaiserEngine engine, Scanner input) {
        this.engine = Objects.requireNonNull(engine);
        this.input = Objects.requireNonNull(input);
        this.printer = new KaiserEnginePrinter(this.engine);
    }

    /**
     * Shows the introduction and start confirmation without the original BASIC code
     * gate, then runs the reign until completion, dismissal or an abort. A replay
     * starts again with the introduction and start confirmation.
     */
    public void run() {
        boolean playAgain;
        do {
            playAgain = false;
            intro();
            System.out.println("Start (J/N)?");
            if ("n".equalsIgnoreCase(nextAnswer())) {
                engine.abort();
                break;
            }
            while (!engine.isGameOver()) {
                engine.startNewRound();
                status();
                if (engine.isGameOver()) {
                    break; // BASIC line 270: final report, but no eleventh action phase.
                }
                engine.prepareLandTrading();
                System.out.println("Ein Hektar Land kostet " + engine.getLandPrice() + " DZ Getreide.");
                actions();
                if (!engine.isGameOver()) {
                    engine.finishRoundAfterActions();
                }
            }
            switch (engine.getState()) {
                case DISMISSED -> System.out.println(printer.getDismissal());
                case ABORTED -> System.out.println("Das kann man nicht mit mir machen! Suchen Sie sich einen anderen zum Mitspielen!");
                case COMPLETED -> {
                    finish();
                    // Only the mediocre branch reaches the replay prompt in lines 959–966.
                    if (engine.getRegency() == KaiserEngine.Regency.MEDIOCRE) {
                        System.out.println("Nochmal (J/N)?");
                        playAgain = "j".equalsIgnoreCase(nextAnswer());
                        if (playAgain) {
                            engine.reset();
                        }
                    }
                }
                default -> throw new IllegalStateException("Game ended without a final state.");
            }
        } while (playAgain);
        byeByeBanner();
    }

    /**
     * Shows information about the game flow and welcomes the player.
     */
    public void intro() {
        banner();
        System.out.println("Versuchen Sie die antike Stadt " + KaiserEnginePrinter.ANSI_YELLOW + ">SUMERIA<" + KaiserEnginePrinter.ANSI_RESET + " zu regieren!");
        System.out.println("Ihre Regierungszeit beträgt " + MAX_ROUNDS + " Jahre.");
        System.out.println("Nach jeweils einem Jahr erhalten Sie einen Bericht über die Entwicklung in der Stadt.");
        System.out.println("Dann werden wir weitersehen .......");
        System.out.println();
    }

    /**
     * Shows the game's banner.
     */
    public void banner() {
        // generated with the help of https://manytools.org/hacker-tools/ascii-banner/
        // font: banner
        System.out.println(KaiserEnginePrinter.ANSI_RED);
        System.out.println("""
                #    #                              \s
                #   #    ##   #  ####  ###### ##### \s
                #  #    #  #  # #      #      #    #\s
                ###    #    # #  ####  #####  #    #\s
                #  #   ###### #      # #      ##### \s
                #   #  #    # # #    # #      #   # \s
                #    # #    # #  ####  ###### #    #\s
                                                    \s
                """);
        System.out.println(KaiserEnginePrinter.ANSI_RESET);
    }

    /**
     * Shows console banner after the game is over.
     */
    public void byeByeBanner() {
        // generated with the help of https://manytools.org/hacker-tools/ascii-banner/
        // font: banner
        System.out.println(KaiserEnginePrinter.ANSI_RED);
        System.out.println("""
                ######                  ######                  #    #                              \s
                #     # #   # ######    #     # #   # ######    #   #    ##   #  ####  ###### ##### \s
                #     #  # #  #         #     #  # #  #         #  #    #  #  # #      #      #    #\s
                ######    #   #####     ######    #   #####     ###    #    # #  ####  #####  #    #\s
                #     #   #   #         #     #   #   #         #  #   ###### #      # #      ##### \s
                #     #   #   #         #     #   #   #         #   #  #    # # #    # #      #   # \s
                ######    #   ######    ######    #   ######    #    # #    # #  ####  ###### #    #\s
                                                                                                    \n
                """);
        System.out.println(KaiserEnginePrinter.ANSI_RESET);
    }

    /**
     * Shows the underlying status of the current round the player is in.
     */
    public void status() {
        System.out.println(printer.getYearResult(engine.getYear()));
        if (engine.isPlagueOccurred()) {
            System.out.println(KaiserEnginePrinter.ORANGE
                    + "Eine fürchterliche Seuche hat die halbe Stadt dahingerafft!"
                    + KaiserEnginePrinter.ANSI_RESET);
        }
        System.out.println(printer.getStatus(engine.getYear()));
    }

    /**
     * Performs user interactions.
     * The player can either
     * <ul>
     *     <li>buy or sell land</li>
     *     <li>feed the population</li>
     *     <li>perform agricultural operations per round.</li>
     * </ul>
     */
    public void actions() {
        System.out.println();
        System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "#+#+#+#+ Was möchten Sie tun?" + KaiserEnginePrinter.ANSI_RESET);

        // only buy or sell is allowed
        if (!KaiserActions.buy(engine, input) && !engine.isGameOver()) {
            KaiserActions.sell(engine, input);
        }
        if (!engine.isGameOver()) {
            KaiserActions.feed(engine, input);
        }
        if (!engine.isGameOver()) {
            KaiserActions.cultivate(engine, input);
        }

        System.out.println();
    }

    /**
     * Shows results after the game is over.
     */
    public void finish() {
        System.out.println(printer.getResults());
        System.out.println(printer.evaluateRegency());
    }

    private String nextAnswer() {
        return input.hasNext() ? input.next() : "n";
    }
}
