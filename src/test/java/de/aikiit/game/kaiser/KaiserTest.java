package de.aikiit.game.kaiser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class KaiserTest {

    @Test
    void getAreaPerCapita() {
        Kaiser k = new Kaiser();
        assertThat(k.getAreaPerCapita()).isZero();
    }
}