package de.aikiit.game.kaiser;

public class KaiserGame {

    public static final int MAX_ROUNDS = 10;
    private final KaiserEngine engine;
    private final KaiserEnginePrinter printer;
    private int round = 0;

    public KaiserGame() {
        this.engine = new KaiserEngine();
        this.printer = new KaiserEnginePrinter(this.engine);
    }

    public void run() {
        intro();
        while (round < MAX_ROUNDS) {
            status();
            actions();
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
        System.out.println(printer.getStatus(++round));
    }

    public void actions() {
        System.out.println();
        // TODO implement actions with error handling and act on engine variable
        System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "#+#+#+#+ Was möchten Sie tun?" + KaiserEnginePrinter.ANSI_RESET);
        System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Wieviel Land wollen Sie kaufen?" + KaiserEnginePrinter.ANSI_RESET);
        System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Wieviel Land wollen Sie verkaufen?" + KaiserEnginePrinter.ANSI_RESET);
        System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Wieviel dzt wollen Sie an Ihr Volk verteilen?" + KaiserEnginePrinter.ANSI_RESET);
        System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Wieviel Land wollen Sie bebauen?" + KaiserEnginePrinter.ANSI_RESET);
        System.out.println();
    }

    public void finish() {
        System.out.println(printer.getResults());
        System.out.println(printer.evaluateRegency());
    }
}
