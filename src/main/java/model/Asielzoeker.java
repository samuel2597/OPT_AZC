package model;
import model.Land;
import jakarta.persistence.*;
import model.Observer;

@Entity
@DiscriminatorValue("Asielzoeker")
public class Asielzoeker extends Gebruiker implements Observer{

    private String naam;
    private boolean heeftVerblijfsvergunning = false;
    @ManyToOne
    Land land;

    @OneToOne(cascade = CascadeType.ALL)
    private Dossier dossier;
    private String notificatie;



    // Extra velden specifiek voor Asielzoeker kun je hier toevoegen

    public Asielzoeker(String email, String wachtwoord) {
        super(email, wachtwoord, AccountRol.Asielzoeker);
    }

    public Asielzoeker(String email, String wachtwoord, String naam, Land land, boolean heeftPaspoort) {
        super(email, wachtwoord, AccountRol.Asielzoeker);
        this.naam = naam;
        this.land = land;
        this.heeftVerblijfsvergunning = false;
        this.dossier = new Dossier(heeftPaspoort);
    }


    // JPA vereist een lege constructor
    protected Asielzoeker() {
        super();
    }

    public Dossier getDossier() {
        return dossier;
    }

    public String getNaam() {
        return naam;
    }

    public boolean isHeeftVerblijfsvergunning() {
        return heeftVerblijfsvergunning;
    }

    public Land getLand() {
        return land;
    }
    //observer
    @Override
    public void update(String bericht) {
        this.notificatie = bericht;
    }

    public String getNotificatie() {
        return notificatie;
    }

    public void clearNotificatie() {
        this.notificatie = null;
    }

    @ManyToOne
    private Azc huidigeAzc;

    public Azc getHuidigeAzc() { return huidigeAzc; }
    public void setHuidigeAzc(Azc huidigeAzc) { this.huidigeAzc = huidigeAzc; }
}
