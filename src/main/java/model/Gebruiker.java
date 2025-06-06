package model;
public abstract class Gebruiker {
    protected String email;
    protected String wachtwoord;

    public Gebruiker(String email, String wachtwoord) {
        this.email = email;
        this.wachtwoord = wachtwoord;
    }

    public String getEmail() {
        return email;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }


    public abstract AccountRol getRol();
}