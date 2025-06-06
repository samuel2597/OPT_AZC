package model;
public class Beheerder extends Gebruiker {

    public Beheerder( String email, String wachtwoord){
        super(email, wachtwoord);
    }

    @Override
    public AccountRol getRol() {
        return AccountRol.Beheerder;
    }

}