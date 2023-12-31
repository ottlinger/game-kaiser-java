package de.aikiit.game.kaiser;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class contains all available actions that have to be taken by the player of the game.
 * At the moment this is done via the operating system's console.
 */
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
                System.out.print(KaiserEnginePrinter.ANSI_RESET);
            } catch (IllegalArgumentException e) {
                System.out.print(KaiserEnginePrinter.ANSI_PURPLE);
                System.out.println("Überleg doch mal, Du hast nur " + engine.getSupplies() + " Korn zur Verfügung. Nun denn, versuch es nochmals.");
                System.out.print(KaiserEnginePrinter.ANSI_RESET);
            }
        }
    }

    /**
     * Perform a sell operation in the current game.
     *
     * @param engine the current game engine
     */
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
                System.out.print(KaiserEnginePrinter.ANSI_RESET);
            } catch (IllegalArgumentException e) {
                System.out.print(KaiserEnginePrinter.ANSI_PURPLE);
                System.out.println("Überleg doch mal, Du hast nur " + engine.getArea() + " Hektar Land. Probier es erneut.");
                System.out.print(KaiserEnginePrinter.ANSI_RESET);
            }
        }
    }

    /**
     * Perform a feeding operation in the current game, that means give food to the population.
     *
     * @param engine the current game engine
     */
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
                System.out.print(KaiserEnginePrinter.ANSI_RESET);
            } catch (IllegalArgumentException e) {
                System.out.print(KaiserEnginePrinter.ANSI_PURPLE);
                System.out.println("Überleg doch mal, Du hast nur " + engine.getSupplies() + " Korn zur Verfügung. Nun denn, probier es erneut.");
                System.out.print(KaiserEnginePrinter.ANSI_RESET);
            }
        }
    }

    /**
     * In order to feed your population you need to plant crops and cultivate your available area/land.
     *
     * @param engine the current game engine
     */
    public static void cultivate(KaiserEngine engine) {
        while (true) {
            try {
                System.out.println(KaiserEnginePrinter.ANSI_PURPLE + "Wieviel Land wollen Sie bebauen?" + KaiserEnginePrinter.ANSI_RESET);
                Long cultivate = new Scanner(System.in).nextLong();
                engine.cultivate(cultivate);
                return;
            } catch (InputMismatchException e) {
                System.out.print(KaiserEnginePrinter.ANSI_PURPLE);
                System.out.println("Gib einen richtigen Wert ein, Du Knalltüte!");
                System.out.print(KaiserEnginePrinter.ANSI_RESET);
            } catch (IllegalArgumentException e) {
                System.out.print(KaiserEnginePrinter.ANSI_PURPLE);
                switch (e.getMessage()) {
                    case "You cannot cultivate more area than you have." ->
                            System.out.println("Überleg doch mal, Du hast nur " + engine.getArea() + " Hektar Land. Probier es nochmals.");
                    case "You cannot cultivate more than you have." ->
                            System.out.println("Nun denk doch mal nach, Du hast nur " + engine.getSupplies() + " Korn zur Verfügung. Nun denn, versuche es erneut.");
                    case "Not enough workers available." ->
                            System.out.println("Sie haben aber nur " + engine.getPopulation() + " Arbeiter. Noch einmal.");
                }
                System.out.print(KaiserEnginePrinter.ANSI_RESET);
            }
        }
    }
}
