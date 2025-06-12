package model;

import jakarta.persistence.*;

@Entity
public class Asielzoeker extends Gebruiker {

    // Extra velden specifiek voor Asielzoeker kun je hier toevoegen

    public Asielzoeker(String email, String wachtwoord) {
        super(email, wachtwoord, AccountRol.Asielzoeker);
    }

    // JPA vereist een lege constructor
    protected Asielzoeker() {
        super();
    }
}
