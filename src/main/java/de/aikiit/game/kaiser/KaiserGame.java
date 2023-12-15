package de.aikiit.game.kaiser;

public class KaiserGame {

    private static final int MAX_ROUNDS = 10;
    private final KaiserEngine engine;
    private final KaiserEnginePrinter printer;
    private int round = 0;

    public KaiserGame() {
        this.engine = new KaiserEngine();
        this.printer = new KaiserEnginePrinter();
    }

    public void start() {
        System.out.println(KaiserEnginePrinter.ANSI_RED + "*** KAISER ***" + KaiserEnginePrinter.ANSI_RESET);
        System.out.println("Versuchen Sie die antike Stadt " + KaiserEnginePrinter.ANSI_YELLOW + ">SUMERIA<" + KaiserEnginePrinter.ANSI_RESET + " zu regieren!");
        System.out.println("Ihre Regierungszeit beträgt " + MAX_ROUNDS + " Jahre.");
        System.out.println("Nach jeweils einem Jahr erhalten Sie einen Bericht über die Entwicklung in der Stadt.");
        System.out.println("Dann werden wir weitersehen .......");

        while (round <= MAX_ROUNDS) {
            System.out.println("Starte in Runde " + ++round + "....");
            status();
        }
    }

    public void status() {
        System.out.println("Status nach Runde " + round);
        System.out.println(printer.getStatus(this.engine));
    }


}
