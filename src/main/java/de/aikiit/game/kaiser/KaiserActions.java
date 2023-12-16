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
}
