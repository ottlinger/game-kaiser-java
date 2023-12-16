package de.aikiit.game.kaiser;

import java.util.InputMismatchException;
import java.util.Scanner;

public class KaiserActions {

    /**
     * Try to buy land in the current game.
     *
     * @param engine the current game engine
     * @return {@code true} if the buy action succeeded, {@code false} otherwise.
     */
    public static boolean buy(KaiserEngine engine) {
        while (true) {
            try {
                System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Wieviel Land wollen Sie kaufen? 0 = nichts" + KaiserEnginePrinter.ANSI_RESET);
                Long buy = new Scanner(System.in).nextLong();
                return engine.buyLand(buy) > 0;

            } catch (InputMismatchException e) {
                System.out.print(KaiserEnginePrinter.ANSI_PURPLE);
                System.out.println("Gib einen richtigen Wert ein, Du Knalltüte!");
                System.out.println("Überleg doch mal, Du hast nur " + engine.getSupplies() + " Korn zur Verfügung. Nun denn.");
                System.out.print(KaiserEnginePrinter.ANSI_RESET);
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
                System.out.print(KaiserEnginePrinter.ANSI_PURPLE);
                System.out.println("Gib einen richtigen Wert ein, Du Knalltüte!");
                System.out.println("Überleg doch mal, Du hast nur " + engine.getArea() + " Hektar Land.");
                System.out.print(KaiserEnginePrinter.ANSI_RESET);
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
                System.out.print(KaiserEnginePrinter.ANSI_PURPLE);
                System.out.println("Gib einen richtigen Wert ein, Du Knalltüte!");
                System.out.println("Überleg doch mal, Du hast nur " + engine.getSupplies() + " Korn zur Verfügung. Nun denn.");
                System.out.print(KaiserEnginePrinter.ANSI_RESET);
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
