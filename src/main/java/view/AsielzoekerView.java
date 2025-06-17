package view;

import controller.AsielzoekerController;
import java.util.Scanner;

public class AsielzoekerView {
    private final AsielzoekerController controller = new AsielzoekerController();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n1. Bekijk dossier");
            System.out.println("2. Registreer adres");
            System.out.println("0. Exit");

            int keuze = scanner.nextInt();
            scanner.nextLine();

            switch (keuze) {
                case 1 -> controller.toonDossier();
                case 2 -> controller.registreerAdres();
                case 0 -> System.exit(0);
                default -> System.out.println("Ongeldige keuze.");
            }
        }
    }
}
