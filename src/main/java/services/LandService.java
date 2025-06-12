package services;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Land;

import java.util.List;
import java.util.Scanner;
public class LandService {
    Scanner s= new Scanner(System.in);

    public void saveLand(){
        System.out.println("Voer land in:");
        String land= s.nextLine();
        System.out.println("Status land: Veilig? (ja/nee)");
        String invoer = s.nextLine().trim().toLowerCase();
        boolean isVeilig = invoer.equals("ja");


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Land land1= new Land(land, isVeilig);// Nu correcte parameters
            em.persist(land1);
            em.getTransaction().commit();
            System.out.println("Land opgeslagen!");
        } finally {
            em.close();
            emf.close();
        }
    }

    public void updateLand(){
        getAllLands();
        System.out.println("Geef de ID op van het Land dat u wilt bewerken. ");
        int id= s.nextInt();
        s.nextLine(); // consume newline

        EntityManagerFactory emf= Persistence.createEntityManagerFactory("azc-unit");
        EntityManager em= emf.createEntityManager();
        try {

            Land bestaandLand= em.find(Land.class,id);
            if(bestaandLand!= null){
                // Start transactie
                em.getTransaction().begin();
                System.out.println("Voer land in:");
                String nieuweNaam= s.nextLine();
                System.out.println("Status land: Veilig? (ja/nee)");
                String invoer = s.nextLine().trim().toLowerCase();
                boolean isVeilig = invoer.equals("ja");


                // Update velden
                bestaandLand.setNaam(nieuweNaam);
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
            emf.close();
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
            emf.close();
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
            emf.close();
        }
    }
}
