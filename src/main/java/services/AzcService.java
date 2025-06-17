package services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Gemeente;
import model.Azc;
import java.util.List;
import java.util.Scanner;

public class AzcService {
    Scanner s = new Scanner(System.in);
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");


    public void saveAzc(String straat, int nummer, String postcode,long gemeenteId) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Gemeente gekozenGemeente = em.find(Gemeente.class, gemeenteId);
            Azc azc = new Azc(straat, nummer, postcode, gekozenGemeente);  // Nu correcte parameters
            em.persist(azc);
            em.getTransaction().commit();
            System.out.println("AZC opgeslagen!");
        } finally {
            em.close();
        }
    }

    public void updateAzc(int id, String nieuweStraat, int nieuwNummer, String nieuwePostcode) {

        EntityManager em = emf.createEntityManager();

        try {
            Azc bestaandAzc = em.find(Azc.class, id);

            if (bestaandAzc != null) {
                // Start transactie
                em.getTransaction().begin();

                // Update velden
                bestaandAzc.setStraat(nieuweStraat);
                bestaandAzc.setNummer(nieuwNummer);
                bestaandAzc.setPostcode(nieuwePostcode);

                // Commit
                em.getTransaction().commit();

                System.out.println("AZC bijgewerkt!");
                // notifyObservers(bestaandAzc); // als je observer pattern gebruikt
            } else {
                System.out.println("AZC met ID " + id + " niet gevonden.");
            }

        } finally {
            em.close();
        }
    }


    public void deleteAzcById(long azcId) {
        EntityManager em = emf.createEntityManager();

        try {
            Azc azc = em.find(Azc.class, azcId);

            if (azc == null) {
                System.out.println("Geen AZC gevonden met ID: " + azcId);
                return;
            }

            if (!azc.getAsielzoekers().isEmpty()) {
                System.out.println("Kan AZC niet verwijderen: er zijn nog asielzoekers gehuisvest.");
                return;
            }

            em.getTransaction().begin();
            em.remove(azc);
            em.getTransaction().commit();
            System.out.println("AZC succesvol verwijderd.");
        } finally {
            em.close();
        }
    }

    public void getAllAzcs(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");
        EntityManager em = emf.createEntityManager();

        try {
            List<Azc> lijst = em.createQuery("SELECT a FROM Azc a", Azc.class).getResultList();
            for (Azc azc : lijst) {
                System.out.println("AZC #" + azc.getId() + " - " + azc.getStraat() + " " + azc.getNummer() + ", " + azc.getPostcode());
            }
        } finally {
            em.close();
        }


    }


}
