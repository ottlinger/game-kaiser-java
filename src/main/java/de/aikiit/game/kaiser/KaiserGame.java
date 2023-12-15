package de.aikiit.game.kaiser;

public class KaiserGame {

    private static final int MAX_ROUNDS = 10;
    private final int round = 0;
    private final KaiserEngine engine;

    public KaiserGame(KaiserEngine engine) {
        this.engine = engine;
    }

    public void start() {
    }

    public void finish() {
        // TODO print results
    }

    public void status() {
        System.out.println("Runde" + round);
        System.out.println(engine.getStatus());
    }

}
