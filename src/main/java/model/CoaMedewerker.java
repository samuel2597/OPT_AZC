package model;
import jakarta.persistence.*;
@Entity
@DiscriminatorValue("CoaMedewerker")
public class CoaMedewerker extends Gebruiker {

    public CoaMedewerker(String email, String wachtwoord){
        super(email, wachtwoord, AccountRol.CoaMedewerker);
    }

    protected CoaMedewerker(){
        super();
    }
    @Override
    public AccountRol getRol() {
        return AccountRol.CoaMedewerker;
    }

}