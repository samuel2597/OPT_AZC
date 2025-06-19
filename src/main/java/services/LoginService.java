package services;
import jakarta.persistence.*;
import model.Asielzoeker;
import model.Gebruiker;
import model.Session;
import java.util.Scanner;

public class LoginService {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");
    Scanner scanner= new Scanner(System.in);


public void login() {
    System.out.print("Voer je e-mail in: ");
    String email = scanner.nextLine();

    System.out.print("Voer je wachtwoord in: ");
    String wachtwoord = scanner.nextLine();

    EntityManager em = emf.createEntityManager();
    try {
        Gebruiker gebruiker = em.createQuery("SELECT g FROM Gebruiker g WHERE g.email = :email", Gebruiker.class)
                .setParameter("email", email)
                .getSingleResult();

        // Controleer wachtwoord
        if (!gebruiker.getWachtwoord().equals(wachtwoord)) {
            System.out.println("❌ Ongeldig wachtwoord.");
            return;
        }

        // Inloggen
        Session.getInstance().login(gebruiker.getId(), gebruiker.getRol());
        System.out.println("✅ Ingelogd als: " + gebruiker.getRol());
        if (gebruiker instanceof Asielzoeker asielzoeker) {
            if (asielzoeker.getNotificatie() != null) {
                System.out.println("📬 Bericht: " + asielzoeker.getNotificatie());
                asielzoeker.clearNotificatie(); // eenmalig tonen
            }
        }


    } catch (NoResultException e) {
        System.out.println("❌ Geen gebruiker gevonden met dat e-mailadres.");
    } finally {
        em.close();
    }
}



}
