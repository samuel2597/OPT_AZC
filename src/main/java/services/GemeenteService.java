package services;
import jakarta.persistence.*;
import model.Gemeente;

import java.util.List;
import java.util.Scanner;
public class GemeenteService {

    Scanner s= new Scanner(System.in);
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");


    public void saveGemeente(String naamGemeente, int aantalInwoners, int aangebodenPlaatsen) {

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

    public void updateGemeente(int id, int nieuweInwoners, int nieuweAangebodenPlaatsen){

        EntityManager em= emf.createEntityManager();
        try {
            Gemeente bestaandGemeente= em.find(Gemeente.class,id);

            if (bestaandGemeente != null) {

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
