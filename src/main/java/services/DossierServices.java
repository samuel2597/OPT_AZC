// Herschreven DossierServices.java
package services;

import jakarta.persistence.*;
import model.Asielzoeker;
import model.Dossier;
import model.Land;
import java.util.*;

public class DossierServices {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");

    public void registreerAsielzoeker(String naam, String email, String wachtwoord, Land gekozenLand, boolean heeftPaspoort) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Asielzoeker asielzoeker = new Asielzoeker(email, wachtwoord, naam, gekozenLand, heeftPaspoort);
            em.persist(asielzoeker);
            em.getTransaction().commit();
        } finally {
            em.close();
            emf.close();
        }
    }

    public void updateDossier(long asielzoekerId, boolean paspoortGetoond, boolean aanvraagCompleet, boolean rechterToegewezen, boolean uitspraakGedaan, String uitspraak, boolean teruggekeerd, String plaatsingStatus) {
        EntityManager em = emf.createEntityManager();

        try {
            Asielzoeker asielzoeker = em.find(Asielzoeker.class, asielzoekerId);
            if (asielzoeker == null || asielzoeker.getDossier() == null) {
                System.out.println("Dossier of asielzoeker niet gevonden.");
                return;
            }

            Dossier dossier = asielzoeker.getDossier();

            em.getTransaction().begin();
            dossier.setPaspoortGetoond(paspoortGetoond);
            dossier.setAanvraagCompleet(aanvraagCompleet);
            dossier.setRechterToegewezen(rechterToegewezen);
            dossier.setUitspraakGedaan(uitspraakGedaan);
            dossier.setUitspraak(uitspraak);
            dossier.setTeruggekeerd(teruggekeerd);
            dossier.setPlaatsingStatus(plaatsingStatus);
            em.merge(dossier);
            em.getTransaction().commit();

            System.out.println("Dossier succesvol bijgewerkt.");
        } finally {
            em.close();
            emf.close();
        }
    }
}
