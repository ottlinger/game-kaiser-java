package de.aikiit.game.kaiser;

import org.assertj.core.util.VisibleForTesting;

public class KaiserGame {

    public static final int MAX_ROUNDS = 10;
    private final KaiserEngine engine;
    private final KaiserEnginePrinter printer;
    private int round = 0;

    public KaiserGame() {
        this(new KaiserEngine());
    }

    @VisibleForTesting
    KaiserGame(KaiserEngine engine) {
        this.engine = new KaiserEngine();
        this.printer = new KaiserEnginePrinter(this.engine);
    }

    private void incrementRoundCounter() {
        round++;
    }

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
                                                                                                    \s                                                                            """);
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
