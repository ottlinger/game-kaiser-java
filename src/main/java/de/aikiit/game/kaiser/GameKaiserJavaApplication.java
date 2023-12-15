package de.aikiit.game.kaiser;

//@SpringBootApplication
public class GameKaiserJavaApplication {

    public static void main(String[] args) {
        //SpringApplication.run(GameKaiserJavaApplication.class, args);
        new KaiserGame().run();
    }

}
