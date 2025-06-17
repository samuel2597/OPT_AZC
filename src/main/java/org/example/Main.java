package org.example;
import controller.BeheerderController;
import jakarta.persistence.*;
import model.*;
import services.*;
import controller.AsielzoekerController;
import controller.CoaMedewerkerController;
import view.AsielzoekerView;
import view.BeheerderView;
import view.CoaMedewerkerView;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("azc-unit");
        try {
            BeheerderController beheerderController= new BeheerderController();
            CoaMedewerkerController coaMedewerkerController= new CoaMedewerkerController();
            AsielzoekerController asielzoekerController= new AsielzoekerController();
            LoginService loginService = new LoginService(); // of via constructor
            loginService.login(); // Vraag om e-mail & wachtwoord

            AccountRol rol = Session.getInstance().getAccountRol();

            if (rol == null) {
                System.out.println("Er is geen geldige sessie. Log eerst in.");
                return;
            }

            switch (rol) {
                case Asielzoeker -> new AsielzoekerView().start();
                case CoaMedewerker -> new CoaMedewerkerView().start();
                case Beheerder -> new BeheerderView().start();
                default -> System.out.println("Onbekende rol.");
            }

        }
        finally {
            emf.close();
        }

    }

    }

