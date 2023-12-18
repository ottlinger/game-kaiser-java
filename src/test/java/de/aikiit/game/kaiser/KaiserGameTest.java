package de.aikiit.game.kaiser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class KaiserGameTest {

    private KaiserGame game;

    @BeforeEach
    void createNewGameUnderTest() {
        this.game = new KaiserGame();
    }

    @Test
    void intro() {
        assertDoesNotThrow(() -> game.intro());
    }

    @Test
    void banner() {
        assertDoesNotThrow(() -> game.banner());
    }

    @Test
    void byeByeBanner() {
        assertDoesNotThrow(() -> game.byeByeBanner());
    }

    @Test
    void finish() {
        assertDoesNotThrow(() -> game.finish());
    }

}