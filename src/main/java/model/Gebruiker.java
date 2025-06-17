package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Gebruiker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    protected String email;

    @NotBlank
    @Size(min = 8)
    @Column(nullable = false)
    protected String wachtwoord;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_rol", nullable = false)
    protected AccountRol rol;

    // Constructor
    public Gebruiker(String email, String wachtwoord, AccountRol rol) {
        this.email = email;
        this.wachtwoord = wachtwoord;
        this.rol = rol;
    }

    // JPA vereist een lege constructor
    protected Gebruiker() {}

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public AccountRol getRol() {
        return rol;
    }
}
