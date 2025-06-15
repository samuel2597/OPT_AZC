package model;
import jakarta.persistence.*;
@Entity
@DiscriminatorValue("Beheerder")
public class Beheerder extends Gebruiker {



    public Beheerder(String email, String wachtwoord){
        super(email, wachtwoord, AccountRol.Beheerder);
    }

    protected  Beheerder(){
        super();
    }


    @Override
    public AccountRol getRol() {
        return AccountRol.Beheerder;
    }

}