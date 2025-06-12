package services;
import jakarta.persistence.*;
import model.Gemeente;

import java.util.List;
import java.util.Scanner;
public class GemeenteService {

    Scanner s= new Scanner(System.in);

//    private final String naam;
//    private int aantalInwoners;
//    private int aangebodenPlaatsen;

    public void saveGemeente() {
        System.out.println("Voer de naam van de gemeente in:");
        String naamGemeente = s.nextLine();

        System.out.println("Voer het aantal inwoners in:");
        int aantalInwoners = s.nextInt();
        s.nextLine(); // consume newline

        System.out.println("Voer het aantal aangeboden plaatsen in:");
        int aangebodenPlaatsen = s.nextInt();
        s.nextLine(); // consume newline

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Gemeente nieuweGemeente = new Gemeente(naamGemeente, aantalInwoners, aangebodenPlaatsen);
            em.persist(nieuweGemeente);
            em.getTransaction().commit();
            System.out.println("Gemeente succesvol opgeslagen.");
        } finally {
            em.close();
            emf.close();
        }
    }

    public void updateGemeente(){
        getAllGemeente();
        System.out.println("Geef de ID op van het Gemeente dat u wilt bewerken.");
        int id= s.nextInt();
        s.nextLine();

        EntityManagerFactory emf= Persistence.createEntityManagerFactory("azc-unit");
        EntityManager em= emf.createEntityManager();
        try {
            Gemeente bestaandGemeente= em.find(Gemeente.class,id);

            if (bestaandGemeente != null) {
                em.getTransaction().begin();
                System.out.println("Voer de nieuwe naam van de gemeente in:");
                String nieuweNaam = s.nextLine();

                System.out.println("Voer het nieuwe aantal inwoners in:");
                int nieuweInwoners = s.nextInt();
                s.nextLine();

                System.out.println("Voer het nieuwe aantal aangeboden plaatsen in:");
                int nieuweAangebodenPlaatsen = s.nextInt();
                s.nextLine();

                bestaandGemeente.setNaam(nieuweNaam);
                bestaandGemeente.setAantalInwoners(nieuweInwoners);
                bestaandGemeente.setAangebodenPlaatsen(nieuweAangebodenPlaatsen);
                em.getTransaction().commit();
            }
            else {
                System.out.println("Geen gemeente gevonden met ID: " + id);
            }
        }
        finally {
            em.close();
            emf.close();
        }

    }

    public void deleteGemeente(){
        getAllGemeente();
        System.out.println("Geef de ID op van het Gemeente dat u wilt verwijder.");
        int id= s.nextInt();
        s.nextLine();

        EntityManagerFactory emf= Persistence.createEntityManagerFactory("azc-unit");
        EntityManager em= emf.createEntityManager();
        try {
            Gemeente verwijderGemeente= em.find(Gemeente.class, id);
            if(verwijderGemeente!= null){
            em.getTransaction().begin();

            em.remove(verwijderGemeente);
            em.getTransaction().commit();
                System.out.println("Gemeente succesvol verwijderd.");
            }
            else {
                System.out.println("Geen Gemeente gevonden met ID: " + id);
            }

        }
        finally {
            em.close();
            emf.close();
        }



    }
    public void getAllGemeente() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");
        EntityManager em = emf.createEntityManager();

        try {
            List<Gemeente> lijst = em.createQuery("SELECT g FROM Gemeente g", Gemeente.class).getResultList();
            for (Gemeente gemeente : lijst) {
                System.out.printf(
                        "Gemeente: %d, Naam: %s, AantalInwoners: %d, AangebodenPlaatsen: %d%n", gemeente.getId(),
                        gemeente.getNaam(),
                        gemeente.getAantalInwoners(),
                        gemeente.getAangebodenPlaatsen()
                );
            }
        } finally {
            em.close();
            emf.close();
        }
    }


}
