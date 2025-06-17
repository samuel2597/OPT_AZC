package view;

import controller.BeheerderController;
import java.util.Scanner;

public class BeheerderView {
    private final BeheerderController controller = new BeheerderController();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n1. Land toevoegen");
            System.out.println("2. Veiligheidsstatus land wijzigen");
            System.out.println("3. Gemeente toevoegen");
            System.out.println("4. Aangeboden plaatsen wijzigen");
            System.out.println("5. AZC toevoegen");
            System.out.println("6. AZC wijzigen");
            System.out.println("7. AZC verwijderen");
            System.out.println("8. Uitkeringsrapportage");
            System.out.println("0. Exit");

            int keuze = scanner.nextInt();
            scanner.nextLine();

            switch (keuze) {
                case 1 -> controller.landToevoegen();
                case 2 -> controller.landVeiligheidsstatusWijzigen();
                case 3 -> controller.gemeenteToevoegen();
                case 4 -> controller.wijzigAangebodenPlaatsenGemeente();
                case 5 -> controller.azcToevoegen();
                case 6 -> controller.azcWijzigen();
                case 7 -> controller.azcVerwijderen();
                case 8 -> controller.genereerUitkeringsrapportage();
                case 0 -> System.exit(0);
                default -> System.out.println("Ongeldige keuze.");
            }
        }
    }
}
