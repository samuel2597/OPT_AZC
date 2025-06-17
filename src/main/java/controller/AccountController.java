package controller;
import jakarta.persistence.*;
import model.*;

import java.awt.desktop.SystemSleepEvent;
import java.util.List;
import java.util.Scanner;

public class AccountController  {
    EntityManagerFactory emf= Persistence.createEntityManagerFactory("azc-unit");
    Scanner s= new Scanner(System.in);

    public void createAccount() {
        EntityManager em = emf.createEntityManager();

        System.out.println("Voer e-mail in:");
        String email = s.nextLine();

        System.out.println("Voer wachtwoord in:");
        String wachtwoord = s.nextLine();

        System.out.println("Kies de rol:");
        for (AccountRol r : AccountRol.values()) {
            System.out.println(r.getWaarde() + " - " + r.name());
        }

        int keuze = s.nextInt();
        s.nextLine(); // enter afvangen

        AccountRol gekozenRol = null;
        for (AccountRol r : AccountRol.values()) {
            if (r.getWaarde() == keuze) {
                gekozenRol = r;
                break;
            }
        }

        if (gekozenRol == null) {
            System.out.println("❌ Ongeldige keuze.");
            return;
        }

        try {
            em.getTransaction().begin();
            switch (gekozenRol) {
                case Asielzoeker -> em.persist(new Asielzoeker(email, wachtwoord));
                case Beheerder -> em.persist(new Beheerder(email, wachtwoord));
                case CoaMedewerker -> em.persist(new CoaMedewerker(email, wachtwoord));
            }
            em.getTransaction().commit();
            System.out.println("✅ Account aangemaakt.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("❌ Fout: " + e.getMessage());
        } finally {
            em.close();
        }
    }


    public void updateAccount() {
        EntityManager em = emf.createEntityManager();

        System.out.print("Voer het e-mailadres in van het account dat je wilt wijzigen: ");
        String email = s.nextLine();

        try {
            Gebruiker gebruiker = em.createQuery("SELECT g FROM Gebruiker g WHERE g.email = :email", Gebruiker.class)
                    .setParameter("email", email)
                    .getSingleResult();

            System.out.print("Voer het nieuwe wachtwoord in: ");
            String nieuwWachtwoord = s.nextLine();

            em.getTransaction().begin();
            gebruiker.setWachtwoord(nieuwWachtwoord);
            em.merge(gebruiker);
            em.getTransaction().commit();

            System.out.println("✅ Wachtwoord succesvol bijgewerkt.");
        } catch (NoResultException e) {
            System.out.println("❌ Geen gebruiker gevonden met dat e-mailadres.");
        } finally {
            em.close();
        }
    }

    public void readAccountbyrolAsiel() {
        EntityManager em = emf.createEntityManager();

        AccountRol gekozenRol = null;
        for (AccountRol r : AccountRol.values()) {
            if (r.getWaarde() == 2) {
                gekozenRol = r;
                break;
            }
        }

        if (gekozenRol == null) {
            System.out.println("❌ Rol niet gevonden.");
            return;
        }

        try {
            TypedQuery<model.Asielzoeker> query = em.createQuery(
                    "SELECT a FROM Asielzoeker a WHERE a.rol = :rol", model.Asielzoeker.class);
            query.setParameter("rol", gekozenRol);
            List<Asielzoeker> resultaten = query.getResultList();

            if (resultaten.isEmpty()) {
                System.out.println("⚠️ Geen accounts gevonden met rol: " + gekozenRol);
            } else {
                System.out.println(" Geregistreerde asielzoekers:");
                for (model.Asielzoeker a : resultaten) {
                    System.out.println( a.getId()+ " | Email: "+ a.getEmail() + " | Rol: " + a.getRol());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Fout tijdens het ophalen: " + e.getMessage());
        } finally {
            em.close();
        }
    }


    public void readAccountbyrol() {
        EntityManager em = emf.createEntityManager();

        System.out.println("Kies een rol om te bekijken:");
        for (AccountRol r : AccountRol.values()) {
            System.out.println(r.getWaarde() + " - " + r.name());
        }

        int keuze = s.nextInt();
        s.nextLine();

        AccountRol gekozenRol = null;
        for (AccountRol r : AccountRol.values()) {
            if (r.getWaarde() == keuze) {
                gekozenRol = r;
                break;
            }
        }

        if (gekozenRol == null) {
            System.out.println("❌ Ongeldige keuze.");
            return;
        }

        try {
            TypedQuery<Gebruiker> query = em.createQuery("SELECT g FROM Gebruiker g WHERE g.rol = :rol", Gebruiker.class);
            query.setParameter("rol", gekozenRol);
            var resultaten = query.getResultList();

            if (resultaten.isEmpty()) {
                System.out.println("⚠️ Geen accounts gevonden voor deze rol.");
            } else {
                for (Gebruiker g : resultaten) {
                    System.out.println("📧 " + g.getId()+ " | Rol: "+ g.getEmail() + " | Rol: " + g.getRol());
                }
            }
        } finally {
            em.close();
        }
    }

    public void deleteAccount(){

    }
}
