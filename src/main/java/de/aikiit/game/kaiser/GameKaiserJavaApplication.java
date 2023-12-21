package de.aikiit.game.kaiser;

/**
 * This is the main class to start the game.
 * You can launch it directly or start the shaded JAR.
 */
//@SpringBootApplication
public class GameKaiserJavaApplication {

    public static void main(String[] args) {
        //SpringApplication.run(GameKaiserJavaApplication.class, args);
        new KaiserGame().run();
    }

}
