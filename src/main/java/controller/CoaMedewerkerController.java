package controller;

import model.Asielzoeker;
import model.Azc;
import model.Gemeente;
import services.PlaatsingService;
import services.DossierServices;
import services.GemeenteService;
import services.LandService;
import model.Land;
import java.util.List;
import java.util.Scanner;
import jakarta.persistence.*;
public class CoaMedewerkerController {
    // registreren en verplaatsen van asielzoekers, dossierbeheer

    DossierServices dossierServices = new DossierServices();
    PlaatsingService plaatsingService = new PlaatsingService();
    LandService landService = new LandService();
    AccountController accountController= new AccountController();
    GemeenteService gemeenteService= new GemeenteService();

    private final Scanner scanner = new Scanner(System.in);
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");

    public void Registreer() {

        EntityManager em = emf.createEntityManager();

        try {
            System.out.print("Naam: ");
            String naam = scanner.nextLine();

            System.out.print("E-mail: ");
            String email = scanner.nextLine();

            System.out.print("Wachtwoord: ");
            String wachtwoord = scanner.nextLine();

            System.out.println("Beschikbare landen:");
            landService.getAllLands(); // toont alle landen, bijv. via em.createQuery...

            System.out.print("Voer land-ID in: ");
            long landId = scanner.nextLong();
            scanner.nextLine();

            Land gekozenLand = em.find(Land.class, landId);
            if (gekozenLand == null) {
                System.out.println("Land niet gevonden.");
                return;
            }

            System.out.print("Heeft de asielzoeker een paspoort? (ja/nee): ");
            boolean heeftPaspoort = scanner.nextLine().trim().equalsIgnoreCase("ja");

            dossierServices.registreerAsielzoeker(naam, email, wachtwoord, gekozenLand, heeftPaspoort);

            System.out.println("Asielzoeker succesvol geregistreerd.");
        } finally {
            em.close();
            emf.close();
        }
    }

    public void plaatsAsielzoeker() {
        EntityManager em = emf.createEntityManager();

        try {
            System.out.print("Voer het ID van de asielzoeker in: ");
            long asielzoekerId = scanner.nextLong();
            scanner.nextLine();

            // Automatisch gekozen gemeente, dus we tonen pas hier de AZC’s die erbij horen
            Gemeente gekozenGemeente = plaatsingService.getGemeenteMetAutomatischeKeuze(); // nieuwe helper
            if (gekozenGemeente == null) {
                System.out.println("Geen geschikte gemeente gevonden.");
                return;
            }

            List<Azc> azcs = em.createQuery("SELECT a FROM Azc a WHERE a.gemeente = :gemeente", Azc.class)
                    .setParameter("gemeente", gekozenGemeente)
                    .getResultList();

            if (azcs.isEmpty()) {
                System.out.println("Geen AZC's in deze gemeente (" + gekozenGemeente.getNaam() + ").");
                return;
            }

            System.out.println("AZC’s binnen gemeente " + gekozenGemeente.getNaam() + ":");
            for (Azc azc : azcs) {
                System.out.println("AZC ID: " + azc.getId() + ", Straat: " + azc.getStraat());
            }

            System.out.print("Voer het ID van het AZC in waar deze persoon geplaatst wordt: ");
            long azcId = scanner.nextLong();
            scanner.nextLine();

            plaatsingService.plaatsAsielzoekerMetKeuze(asielzoekerId, azcId);
        } finally {
            em.close();
        }
    }



    public void VerplaatsAsiel() {
        EntityManager em = emf.createEntityManager();
        try {
            System.out.print("Voer het ID van de asielzoeker in: ");
            long asielzoekerId = scanner.nextLong();
            scanner.nextLine();

            // Toon beschikbare gemeentes
            gemeenteService.getAllGemeente();
            System.out.print("Voer het ID van de gemeente in: ");
            long gemeenteId = scanner.nextLong();
            scanner.nextLine();

            // Toon AZC's binnen de gekozen gemeente
            List<Azc> azcs = em.createQuery("SELECT a FROM Azc a WHERE a.gemeente.id = :gemeenteId", Azc.class)
                    .setParameter("gemeenteId", gemeenteId)
                    .getResultList();

            for (Azc azc : azcs) {
                System.out.println("AZC ID: " + azc.getId() + ", Straat: " + azc.getStraat());
            }
            System.out.print("Voer het ID van het AZC in waar deze persoon geplaatst wordt: ");
            long azcId = scanner.nextLong();
            scanner.nextLine();

            plaatsingService.verplaatsAsielzoeker(asielzoekerId, gemeenteId, azcId);
        }
        finally {
            em.close();
        }
    }

    public void UpdateDossier() {
        accountController.readAccountbyrolAsiel();
        System.out.print("Voer het ID van de asielzoeker in: ");
        long asielzoekerId = scanner.nextLong();
        scanner.nextLine();

        EntityManager em = emf.createEntityManager();
        try {
            // Controleer of het opgegeven ID echt bij een Asielzoeker hoort
            Asielzoeker asielzoeker = em.find(Asielzoeker.class, asielzoekerId);

            if (asielzoeker == null) {
                System.out.println("❌ Geen asielzoeker gevonden met dat ID.");
                return;
            }

            System.out.print("Is het paspoort getoond? (ja/nee): ");
            boolean paspoortGetoond = scanner.nextLine().trim().equalsIgnoreCase("ja");

            System.out.print("Is de aanvraag compleet aangeleverd? (ja/nee): ");
            boolean aanvraagCompleet = scanner.nextLine().trim().equalsIgnoreCase("ja");

            System.out.print("Is er een rechter toegewezen? (ja/nee): ");
            boolean rechterToegewezen = scanner.nextLine().trim().equalsIgnoreCase("ja");

            System.out.print("Heeft de rechter een uitspraak gedaan? (ja/nee): ");
            boolean uitspraakGedaan = scanner.nextLine().trim().equalsIgnoreCase("ja");

            System.out.print("Wat is de uitspraak van de rechter (Toegelaten/Niet-toegelaten): ");
            String uitspraak = scanner.nextLine().trim();

            System.out.print("Is de vluchteling teruggekeerd naar het land van herkomst? (ja/nee): ");
            boolean teruggekeerd = scanner.nextLine().trim().equalsIgnoreCase("ja");

            System.out.print("Wat is de plaatsingsstatus? (nee/gestart/afgerond): ");
            String plaatsingStatus = scanner.nextLine().trim();

            // Pas het dossier aan via de service
            dossierServices.updateDossier(asielzoekerId, paspoortGetoond, aanvraagCompleet,
                    rechterToegewezen, uitspraakGedaan, uitspraak, teruggekeerd, plaatsingStatus);
        } finally {
            em.close();
        }
    }

}
