package de.aikiit.game.kaiser;

/**
 * This is the main class to start the game.
 * You can launch it directly or start via the shaded JAR.
 */
//@SpringBootApplication
public class GameKaiserJavaApplication {

    /**
     * Start the actual game directly.
     * @param args no parameters required at the moment.
     */
    public static void main(String[] args) {
        new KaiserGame().run();
    }

}
