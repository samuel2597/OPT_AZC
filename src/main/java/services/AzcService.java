package services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Azc;

import java.util.List;
import java.util.Scanner;

public class AzcService {
    Azc azc;
    Scanner s = new Scanner(System.in);

    public void saveAzc() {


        System.out.print("Voer straat in: ");
        String straat = s.nextLine();

        System.out.print("Voer nummer in: ");
        int nummer = s.nextInt();
        s.nextLine(); // consume newline

        System.out.print("Voer postcode in: ");
        String postcode = s.nextLine();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Azc azc = new Azc(straat, nummer, postcode);  // Nu correcte parameters
            em.persist(azc);
            em.getTransaction().commit();
            System.out.println("AZC opgeslagen!");
        } finally {
            em.close();
            emf.close();
        }
    }

    public void updateAzc() {
        getAllAzcs();
        System.out.println("Geef de ID op van het AZC dat u wilt bewerken.");
        int id = s.nextInt();
        s.nextLine(); // consume newline

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");
        EntityManager em = emf.createEntityManager();

        try {
            Azc bestaandAzc = em.find(Azc.class, id);

            if (bestaandAzc != null) {
                // Start transactie
                em.getTransaction().begin();

                System.out.print("Voer straat in: ");
                String nieuweStraat = s.nextLine();

                System.out.print("Voer nummer in: ");
                int nieuwNummer = s.nextInt();
                s.nextLine(); // consume newline

                System.out.print("Voer postcode in: ");
                String nieuwePostcode = s.nextLine();

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
            emf.close();
        }
    }


    public void deleteAzcById(){

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
            emf.close();
        }


    }


}
