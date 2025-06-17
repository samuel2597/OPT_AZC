package view;

import controller.CoaMedewerkerController;
import java.util.Scanner;

public class CoaMedewerkerView {
    private final CoaMedewerkerController controller = new CoaMedewerkerController();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n1. Registreer asielzoeker");
            System.out.println("2. Verplaats asielzoeker");
            System.out.println("3. Plaats asielzoeker automatisch");
            System.out.println("4. Update dossier");
            System.out.println("0. Exit");

            int keuze = scanner.nextInt();
            scanner.nextLine();

            switch (keuze) {
                case 1 -> controller.Registreer();
                case 2 -> controller.VerplaatsAsiel();
                case 3 -> controller.plaatsAsielzoeker();
                case 4 -> controller.UpdateDossier();
                case 0 -> System.exit(0);
                default -> System.out.println("Ongeldige keuze.");
            }
        }
    }
}
