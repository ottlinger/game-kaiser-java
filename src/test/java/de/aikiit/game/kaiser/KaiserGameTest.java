package de.aikiit.game.kaiser;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import uk.org.webcompere.systemstubs.SystemStubs;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KaiserGameTest {

    private KaiserGame game;

    @Mock
    private KaiserEngine mockedEngine;

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

    @Test
    void status() {
        assertDoesNotThrow(() -> game.status());
    }

    @SneakyThrows
    @Test
    void actionsWithMockedEngine() {
        KaiserGame gameWithMockedEngine = new KaiserGame(mockedEngine);

        SystemStubs.withTextFromSystemIn("0")
                .execute(() -> {
                    // as we can only stub one call to System.in, we get an exception from the second call
                    assertThrows(NoSuchElementException.class, () -> gameWithMockedEngine.actions());
                });
    }

    @SneakyThrows
    @Test
    void runWithMockedEngine() {
        KaiserGame gameWithMockedEngine = new KaiserGame(mockedEngine);

        SystemStubs.withTextFromSystemIn("0")
                .execute(() -> {
                    // as we can only stub one call to System.in, we get an exception from the second call
                    assertThrows(NoSuchElementException.class, () -> gameWithMockedEngine.run());
                });
    }
}