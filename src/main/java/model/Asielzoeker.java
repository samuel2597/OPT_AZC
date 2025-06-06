package model;
public class Asielzoeker extends Gebruiker {



    public Asielzoeker( String email, String wachtwoord){
        super(email, wachtwoord);

    }

    @Override
    public AccountRol getRol() {
        return AccountRol.Asielzoeker;
    }


}