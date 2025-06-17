// Plaats deze klasse in: src/main/java/services/PlaatsingService.java
package services;
import jakarta.persistence.*;
import model.*;
import java.util.*;

public class PlaatsingService {
    private final Scanner s = new Scanner(System.in);
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");

    public void plaatsAsielzoekerMetKeuze(long asielzoekerId, long azcId) {
        EntityManager em = emf.createEntityManager();

        try {
            Asielzoeker asielzoeker = em.find(Asielzoeker.class, asielzoekerId);
            if (asielzoeker == null) {
                System.out.println("Asielzoeker niet gevonden.");
                return;
            }

            em.getTransaction().begin();

            // Stap 1: Kies automatisch een gemeente (of via spreidingswet)
            Gemeente gekozenGemeente = zoekGemeenteMetMeesteBeschikbarePlekken(em);
            if (gekozenGemeente == null) {
                gekozenGemeente = zoekGemeenteOpBasisVanSpreidingswet(em);
                System.out.println("Geen gemeente met vrije plekken. Spreidingswet toegepast.");
            }

            // Stap 2: Valideer of het gekozen AZC binnen die gemeente valt
            Azc nieuwAzc = em.find(Azc.class, azcId);
            if (nieuwAzc == null || !nieuwAzc.getGemeente().getId().equals(gekozenGemeente.getId())) {
                System.out.println("AZC ID komt niet overeen met automatisch gekozen gemeente (" + gekozenGemeente.getNaam() + ").");
                return;
            }

            // Stap 3: Huidige AZC verwijderen
            Azc huidigAzc = asielzoeker.getHuidigeAzc();
            if (huidigAzc != null) {
                huidigAzc.verwijderAsielzoeker(asielzoeker);
                Gemeente oudeGemeente = huidigAzc.getGemeente();
                oudeGemeente.setAantalInwoners(oudeGemeente.getAantalInwoners() - 1);
                oudeGemeente.setAangebodenPlaatsen(oudeGemeente.getAangebodenPlaatsen() + 1);
                em.merge(huidigAzc);
                em.merge(oudeGemeente);
            }

            // Stap 4: Nieuwe AZC toewijzen
            nieuwAzc.voegAsielzoekerToe(asielzoeker);
            asielzoeker.setHuidigeAzc(nieuwAzc);

            gekozenGemeente.setAangebodenPlaatsen(gekozenGemeente.getAangebodenPlaatsen() - 1);
            gekozenGemeente.setAantalInwoners(gekozenGemeente.getAantalInwoners() + 1);

            em.merge(nieuwAzc);
            em.merge(gekozenGemeente);
            em.merge(asielzoeker);

            em.getTransaction().commit();

            System.out.println("Asielzoeker succesvol geplaatst in AZC (" + nieuwAzc.getStraat() + ") van gemeente " + gekozenGemeente.getNaam());
        } finally {
            em.close();
        }
    }


    public void verplaatsAsielzoeker(long asielzoekerId, long gemeenteId, long azcId) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Zoek de asielzoeker
            Asielzoeker asielzoeker = em.find(Asielzoeker.class, asielzoekerId);
            if (asielzoeker == null) {
                throw new IllegalArgumentException("Asielzoeker met ID " + asielzoekerId + " niet gevonden.");
            }

            // Zoek de gemeente
            Gemeente gekozenGemeente = em.find(Gemeente.class, gemeenteId);
            if (gekozenGemeente == null) {
                throw new IllegalArgumentException("Gemeente met ID " + gemeenteId + " niet gevonden.");
            }

            // Zoek het nieuwe AZC
            Azc nieuwAzc = em.find(Azc.class, azcId);
            if (nieuwAzc == null) {
                throw new IllegalArgumentException("AZC met ID " + azcId + " niet gevonden.");
            }

            // Huidig AZC verwerken (indien aanwezig)
            Azc huidigAzc = asielzoeker.getHuidigeAzc();
            if (huidigAzc != null) {
                Gemeente oudeGemeente = huidigAzc.getGemeente();
                huidigAzc.verwijderAsielzoeker(asielzoeker);
                oudeGemeente.setAantalInwoners(oudeGemeente.getAantalInwoners() - 1);
                oudeGemeente.setAangebodenPlaatsen(oudeGemeente.getAangebodenPlaatsen() + 1);

                em.merge(huidigAzc);
                em.merge(oudeGemeente);
            }

            // Nieuwe AZC en gemeente verwerken
            nieuwAzc.voegAsielzoekerToe(asielzoeker);
            asielzoeker.setHuidigeAzc(nieuwAzc);

            Gemeente nieuweGemeente = nieuwAzc.getGemeente();
            nieuweGemeente.setAantalInwoners(nieuweGemeente.getAantalInwoners() + 1);
            nieuweGemeente.setAangebodenPlaatsen(nieuweGemeente.getAangebodenPlaatsen() - 1);

            // Persist alle aanpassingen
            em.merge(nieuwAzc);
            em.merge(nieuweGemeente);
            em.merge(asielzoeker);

            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Fout bij verplaatsen asielzoeker: " + e.getMessage(), e);
        } finally {
            em.close();
            emf.close();
        }
    }

    public Gemeente getGemeenteMetAutomatischeKeuze() {
        EntityManager em = emf.createEntityManager();
        try {
            Gemeente g = zoekGemeenteMetMeesteBeschikbarePlekken(em);
            if (g == null) {
                g = zoekGemeenteOpBasisVanSpreidingswet(em);
            }
            return g;
        } finally {
            em.close();
        }
    }




    private Gemeente zoekGemeenteMetMeesteBeschikbarePlekken(EntityManager em) {
        List<Gemeente> gemeenten = em.createQuery("SELECT g FROM Gemeente g", Gemeente.class).getResultList();
        Gemeente beste = null;
        int maxBeschikbaar = -1;

        for (Gemeente g : gemeenten) {
            int bezet = telAantalAsielzoekersInGemeente(em, g);
            int beschikbaar = g.getAangebodenPlaatsen() - bezet;
            if (beschikbaar > maxBeschikbaar) {
                maxBeschikbaar = beschikbaar;
                beste = g;
            }
        }
        return (maxBeschikbaar > 0) ? beste : null;
    }

    private Gemeente zoekGemeenteOpBasisVanSpreidingswet(EntityManager em) {
        List<Gemeente> gemeenten = em.createQuery("SELECT g FROM Gemeente g", Gemeente.class).getResultList();
        Gemeente minst = null;
        double laagsteRatio = Double.MAX_VALUE;

        for (Gemeente g : gemeenten) {
            double ratio = (double) g.getAangebodenPlaatsen() / g.getAantalInwoners();
            if (ratio < laagsteRatio) {
                laagsteRatio = ratio;
                minst = g;
            }
        }
        return minst;
    }

    private int telAantalAsielzoekersInGemeente(EntityManager em, Gemeente g) {
        List<Azc> azcs = em.createQuery("SELECT a FROM Azc a WHERE a.gemeente = :gemeente", Azc.class)
                .setParameter("gemeente", g)
                .getResultList();
        int totaal = 0;
        for (Azc azc : azcs) {
            totaal += azc.getAsielzoekers().size();
        }
        return totaal;
    }


}
