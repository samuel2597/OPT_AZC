package services;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Asielzoeker;
import model.Azc;
import model.Dossier;

public class AsielzoekerService {
    private final Scanner s = new Scanner(System.in);
    public void toonDossierStatus() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");
        EntityManager em = emf.createEntityManager();

        try {
            System.out.print("Voer je asielzoekers-ID in: ");
            long id = s.nextLong();
            s.nextLine();

            Asielzoeker asielzoeker = em.find(Asielzoeker.class, id);
            if (asielzoeker == null || asielzoeker.getDossier() == null) {
                System.out.println("Geen dossier gevonden.");
                return;
            }


            System.out.println("------ Persoonlijke gegevens ------");
            System.out.println("Naam: " + asielzoeker.getNaam());
            System.out.println("E-mail: " + asielzoeker.getEmail());
            System.out.println("Land van herkomst: " + asielzoeker.getLand().getNaam());


            Azc huidigAzc = asielzoeker.getHuidigeAzc();
            if (huidigAzc != null) {
                System.out.println("Verblijft in AZC: " + huidigAzc.getStraat() + ", " + huidigAzc.getPostcode());
            } else {
                System.out.println("Verblijft momenteel niet in een AZC.");
            }

            Dossier d = asielzoeker.getDossier();

            System.out.println("------ Dossierstatus ------");
            System.out.println("Paspoort getoond: " + (d.isPaspoortGetoond() ? "ja" : "nee"));
            System.out.println("Asielaanvraag is compleet: " + (d.isAanvraagCompleet() ? "ja" : "nee"));
            System.out.println("Rechter toegewezen: " + (d.isRechterToegewezen() ? "ja" : "nee"));

            String uitspraak = d.isUitspraakGedaan()
                    ? (d.getUitspraak().equalsIgnoreCase("Toegelaten") ? "geaccepteerd" : "afgewezen")
                    : "nee";
            System.out.println("Rechter heeft uitspraak gedaan: " + uitspraak);

            String woningStatus = d.getPlaatsingStatus(); // waarden: "nee", "gestart", "afgerond"
            System.out.println("Plaatsing in eigen woning: " + woningStatus);

        } finally {
            em.close();
            emf.close();
        }
    }

    public void registreerNieuwAdres() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");
        EntityManager em = emf.createEntityManager();

        try {
            System.out.print("Voer je asielzoekers-ID in: ");
            long id = s.nextLong();
            s.nextLine();

            Asielzoeker asielzoeker = em.find(Asielzoeker.class, id);
            if (asielzoeker == null || asielzoeker.getDossier() == null) {
                System.out.println("Geen dossier gevonden.");
                return;
            }

            Dossier dossier = asielzoeker.getDossier();

            // Voorwaarde controleren
            boolean uitspraakOK = dossier.isUitspraakGedaan()
                    && dossier.getUitspraak().equalsIgnoreCase("Toegelaten");
            boolean plaatsingGestart = dossier.getPlaatsingStatus().equalsIgnoreCase("gestart");

            if (!uitspraakOK || !plaatsingGestart) {
                System.out.println("Je komt nog niet in aanmerking om een adres te registreren.");
                return;
            }

            System.out.print("Voer je nieuwe straatnaam in: ");
            String straat = s.nextLine();

            System.out.print("Voer je huisnummer in: ");
            int nummer = s.nextInt();
            s.nextLine();

            System.out.print("Voer je postcode in: ");
            String postcode = s.nextLine();

            // Sla op in het dossier (of apart Adres-object indien gewenst)
            em.getTransaction().begin();
            dossier.setStraat(straat);
            dossier.setHuisnummer(nummer);
            dossier.setPostcode(postcode);
            dossier.setPlaatsingStatus("afgerond");
            em.merge(dossier);
            em.getTransaction().commit();

            System.out.println("Adres succesvol geregistreerd.");

        } finally {
            em.close();
            emf.close();
        }
    }

}
