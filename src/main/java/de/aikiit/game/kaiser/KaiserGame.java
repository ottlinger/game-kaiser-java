package de.aikiit.game.kaiser;

import org.assertj.core.util.VisibleForTesting;

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
    /**
     * The current round of the game.
     */
    private int round = 0;

    public KaiserGame() {
        this(new KaiserEngine());
    }

    @VisibleForTesting
    KaiserGame(KaiserEngine engine) {
        this.engine = engine;
        this.printer = new KaiserEnginePrinter(this.engine);
    }

    private void incrementRoundCounter() {
        round++;
    }

    /**
     * Start the game and allow player interactions until the maximum number of rounds is reached.
     */
    public void run() {
        intro();
        status();
        while (round < MAX_ROUNDS) {
            incrementRoundCounter();
            engine.startNewRound();
            status();
            actions();
            engine.finishRoundAfterActions();
        }

        finish();
        byeByeBanner();
    }

    public void intro() {
        banner();
        System.out.println("Versuchen Sie die antike Stadt " + KaiserEnginePrinter.ANSI_YELLOW + ">SUMERIA<" + KaiserEnginePrinter.ANSI_RESET + " zu regieren!");
        System.out.println("Ihre Regierungszeit beträgt " + MAX_ROUNDS + " Jahre.");
        System.out.println("Nach jeweils einem Jahr erhalten Sie einen Bericht über die Entwicklung in der Stadt.");
        System.out.println("Dann werden wir weitersehen .......");
        System.out.println();
    }

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
                                                                                                    \n                                                                           """);
        System.out.println(KaiserEnginePrinter.ANSI_RESET);
    }

    public void status() {
        System.out.println(printer.getStatus(round));
        System.out.println(printer.getYearResult(round));
    }

    public void actions() {
        System.out.println();
        System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "#+#+#+#+ Was möchten Sie tun?" + KaiserEnginePrinter.ANSI_RESET);

        // only buy or sell is allowed
        if (!KaiserActions.buy(this.engine)) {
            KaiserActions.sell(this.engine);
        }

        KaiserActions.feed(this.engine);
        KaiserActions.cultivate(this.engine);

        System.out.println();
    }

    public void finish() {
        System.out.println(printer.getResults());
        System.out.println(printer.evaluateRegency());
    }
}
