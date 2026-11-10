package de.aikiit.game.kaiser;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.Random;

/** A finite BASIC RND trace: unexpected draws fail the test immediately. */
final class BasicRandom extends Random {
    private final Queue<Double> values = new ArrayDeque<>();
    private int draws;

    BasicRandom(Double... values) {
        this.values.addAll(Arrays.asList(values));
    }

    @Override
    public double nextDouble() {
        draws++;
        if (values.isEmpty()) {
            throw new AssertionError("Unexpected RND call " + draws);
        }
        return values.remove();
    }

    int remaining() {
        return values.size();
    }
}
