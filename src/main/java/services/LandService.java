package services;
import jakarta.persistence.*;
import model.Land;
import java.util.List;
import java.util.Scanner;

public class LandService {
    Scanner s= new Scanner(System.in);
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");

    public void saveLand(String land, Boolean isVeilig){
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Land land1= new Land(land, isVeilig);// Nu correcte parameters
            em.persist(land1);
            em.getTransaction().commit();
            System.out.println("Land opgeslagen!");
        } finally {
            em.close();
        }
    }

    public void updateLand(int id, boolean isVeilig){

        EntityManager em= emf.createEntityManager();
        try {

            Land bestaandLand= em.find(Land.class,id);
            if(bestaandLand!= null){
                // Start transactie
                em.getTransaction().begin();

                // Update velden
                bestaandLand.setVeilig(isVeilig);

                //Commit
                em.getTransaction().commit();

            }
            else {
                System.out.println("Land met ID " + id + " niet gevonden.");
            }

        }
        finally {
            em.close();
        }


    }

    public void deleteLand(){
        getAllLands();
        System.out.println("Geef de ID op van het Land dat u wilt Verwijderen. ");
        int id= s.nextInt();
        s.nextLine(); // consume newline

        EntityManagerFactory emf= Persistence.createEntityManagerFactory("azc-unit");
        EntityManager em= emf.createEntityManager();

        try {
            Land verwijderLand= em.find(Land.class, id);
            if (verwijderLand!= null){
                em.getTransaction().begin();
                em.remove(verwijderLand);
                em.getTransaction().commit();
                em.getTransaction().commit();
                System.out.println("Land succesvol verwijderd.");
            }
            else {
                System.out.println("Geen land gevonden met ID: " + id);
            }
        }
        finally {
            em.close();
        }

    }




    public void getAllLands(){
        EntityManagerFactory emf= Persistence.createEntityManagerFactory("azc-unit");
        EntityManager em= emf.createEntityManager();
        try {
            em.getTransaction().begin();
            List<Land> lijst= em.createQuery ("Select l from Land l",Land.class).getResultList();
            for(Land land : lijst){
                System.out.printf("Land: %d , Naam:%s, IsVeilig:%b\n", land.getId(),land.getNaam(),land.isVeilig());
            }
        }
        finally {
            em.close();
        }
    }
}
