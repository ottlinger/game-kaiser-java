package de.aikiit.game.kaiser;

import java.util.InputMismatchException;
import java.util.Scanner;

public class KaiserActions {

    public static void buy(KaiserEngine engine) {
        while (true) {
            try {
                System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Wieviel Land wollen Sie kaufen?" + KaiserEnginePrinter.ANSI_RESET);
                Long buy = new Scanner(System.in).nextLong();
                engine.buyLand(buy);
                return;

            } catch (InputMismatchException e) {
                System.out.println("Gib einen richtigen Wert ein, Du Knalltüte!");
            }
        }
    }

    public static void sell(KaiserEngine engine) {
        while (true) {
            try {
                System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Wieviel Land wollen Sie verkaufen?" + KaiserEnginePrinter.ANSI_RESET);
                Long sell = new Scanner(System.in).nextLong();
                engine.sellLand(sell);
                return;

            } catch (InputMismatchException e) {
                System.out.println("Gib einen richtigen Wert ein, Du Knalltüte!");
            }
        }
    }

    public static void feed(KaiserEngine engine) {
        while (true) {
            try {
                System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Wieviel dzt wollen Sie an Ihr Volk verteilen?" + KaiserEnginePrinter.ANSI_RESET);
                Long feed = new Scanner(System.in).nextLong();
                engine.feedToPopulation(feed);
                return;

            } catch (InputMismatchException e) {
                System.out.println("Gib einen richtigen Wert ein, Du Knalltüte!");
            }
        }
    }

    public static void cultivate(KaiserEngine engine) {
        while (true) {
            try {
                System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Wieviel Land wollen Sie bebauen?" + KaiserEnginePrinter.ANSI_RESET);
                Long cultivate = new Scanner(System.in).nextLong();
                engine.cultivate(cultivate);
                return;

            } catch (InputMismatchException e) {
                System.out.println("Gib einen richtigen Wert ein, Du Knalltüte!");
            }
        }
    }
}
