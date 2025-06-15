package controller;
import model.Land;
import services.LandService;
import services.GemeenteService;
import services.AzcService;
import java.util.Scanner;
public class BeheerderController {
    //beheren van Land, Gemeente, AZC, rapportage
    LandService landService = new LandService();
    GemeenteService gemeenteService = new GemeenteService();
    AzcService azcService = new AzcService();
    Scanner scanner = new Scanner(System.in);

    // LAND
    public void landToevoegen() {
        System.out.print("Voer naam van het land in: ");
        String naam = scanner.nextLine();

        System.out.print("Is het land veilig? (ja/nee): ");
        boolean isVeilig = scanner.nextLine().trim().equalsIgnoreCase("ja");

        landService.saveLand(naam, isVeilig);
    }

    public void landVeiligheidsstatusWijzigen() {
        gemeenteService.getAllGemeente();
        System.out.print("Voer het ID van het land in: ");
        int landId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nieuwe veiligheidsstatus? (ja/nee): ");
        boolean nieuweStatus = scanner.nextLine().trim().equalsIgnoreCase("ja");

        landService.updateLand(landId,nieuweStatus);
    }

    // GEMEENTE
    public void gemeenteToevoegen() {
        System.out.print("Voer naam van de gemeente in: ");
        String naam = scanner.nextLine();

        System.out.print("Aantal inwoners: ");
        int aantalInwoners = scanner.nextInt();

        System.out.print("Aangeboden plaatsen: ");
        int aangebodenPlaatsen = scanner.nextInt();
        scanner.nextLine();

        gemeenteService.saveGemeente(naam, aantalInwoners, aangebodenPlaatsen);
    }

    public void wijzigAangebodenPlaatsenGemeente() {
        gemeenteService.getAllGemeente();
        System.out.print("Voer gemeente-ID in: ");
        int gemeenteId = scanner.nextInt();

        System.out.println("Voer het nieuwe aantal inwoners in:");
        int nieuweInwoners = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nieuwe waarde voor aangeboden plaatsen: ");
        int nieuweWaarde = scanner.nextInt();
        scanner.nextLine();

        gemeenteService.updateGemeente(gemeenteId, nieuweInwoners, nieuweWaarde);
    }

    // AZC
    public void azcToevoegen() {
        System.out.print("Voer straat in: ");
        String straat = scanner.nextLine();

        System.out.print("Voer nummer in: ");
        int nummer = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Voer postcode in: ");
        String postcode = scanner.nextLine();

        System.out.println("Toon beschikbare Gemeente:");
        gemeenteService.getAllGemeente();

        System.out.println("Voer land-ID in:");
        long gemeenteId = scanner.nextLong();
        scanner.nextLine();

        azcService.saveAzc(straat, nummer, postcode, gemeenteId);
    }

    public void azcWijzigen() {
        gemeenteService.getAllGemeente();
        System.out.println("Geef de ID op van het AZC dat u wilt bewerken.");
        int azcId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Nieuwe straatnaam: ");
        String nieuweStraat = scanner.nextLine();

        System.out.print("Nieuw huisnummer: ");
        int nieuwNummer = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nieuwe postcode: ");
        String nieuwePostcode = scanner.nextLine();

        azcService.updateAzc(azcId, nieuweStraat, nieuwNummer, nieuwePostcode);
    }

    public void azcVerwijderen() {
        azcService.getAllAzcs();
        System.out.print("AZC-ID dat je wil verwijderen: ");
        long azcId = scanner.nextLong();
        scanner.nextLine();

        azcService.deleteAzcById(azcId);
    }

    // RAPPORTAGE
    public void genereerUitkeringsrapportage() {
        gemeenteService.uitkeringsRapportage();
    }
}
