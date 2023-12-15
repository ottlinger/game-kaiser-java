package de.aikiit.game.kaiser;

public class KaiserGame {

    private static final int MAX_ROUNDS = 10;
    private int round = 0;
    private final KaiserEngine engine;
    private final KaiserEnginePrinter printer;

    public KaiserGame() {
        this.engine = new KaiserEngine();
        this.printer = new KaiserEnginePrinter();
    }

    public void start() {
        while(round <= MAX_ROUNDS) {
            System.out.println("Starte in Runde " + ++round + "....");
            status();
        }
    }

    public void status() {
        System.out.println("Status nach Runde " + round);
        System.out.println(printer.getStatus(this.engine));
    }


}
