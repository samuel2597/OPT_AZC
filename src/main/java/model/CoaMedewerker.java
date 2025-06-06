package model;
public class CoaMedewerker extends Gebruiker {

    public CoaMedewerker(String email, String wachtwoord){
        super(email, wachtwoord);

    }

    @Override
    public AccountRol getRol() {
        return AccountRol.CoaMedewerker;
    }

}