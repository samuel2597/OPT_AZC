package services;
import jakarta.persistence.*;
import model.Gemeente;
import model.Azc;
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
        }

    }



    public void getAllGemeente() {
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
        }
    }

    public void uitkeringsRapportage() {
        EntityManager em = emf.createEntityManager();

        try {
            List<Gemeente> gemeenten = em.createQuery("SELECT g FROM Gemeente g", Gemeente.class).getResultList();

            System.out.println("\nUitkeringsrapportage per Gemeente");
            System.out.println("═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════");
            System.out.println("║ Gemeente               ║ Inwoners   ║ Plekken    ║ Asielzoekers       ║ Bezettingsgraad    ║ Verplichte opvang ║ Uitkering (€)      ║");
            System.out.println("╠════════════════════════╬════════════╬════════════╬════════════════════╬════════════════════╬════════════════════╬════════════════════╣");

            for (Gemeente g : gemeenten) {
                // Ophalen van alle AZC's binnen deze gemeente
                List<Azc> azcs = em.createQuery("SELECT a FROM Azc a WHERE a.gemeente = :gemeente", Azc.class)
                        .setParameter("gemeente", g)
                        .getResultList();

                int totaalAsielzoekers = azcs.stream().mapToInt(a -> a.getAsielzoekers().size()).sum();
                double bezettingsgraad = g.getAangebodenPlaatsen() > 0
                        ? (double) totaalAsielzoekers / g.getAangebodenPlaatsen() * 100
                        : 0;
                long verplichteOpvang = Math.round(g.getAantalInwoners() * 0.005);
                long extra = totaalAsielzoekers - verplichteOpvang;

                int uitkering = 0;
                if (extra > 100) {
                    uitkering = 2000;
                } else if (extra > 0) {
                    uitkering = 1000;
                }

                System.out.printf("║ %-22s ║ %-10d ║ %-10d ║ %-18d ║ %-18.1f%% ║ %-18d ║ %-18d ║%n",
                        g.getNaam(),
                        g.getAantalInwoners(),
                        g.getAangebodenPlaatsen(),
                        totaalAsielzoekers,
                        bezettingsgraad,
                        verplichteOpvang,
                        uitkering);
            }

            System.out.println("╚════════════════════════╩════════════╩════════════╩════════════════════╩════════════════════╩════════════════════╩════════════════════╝");

        } finally {
            em.close();
        }
    }


}
