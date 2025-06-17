package model;

public class Session {
    private static Session instance;
    private long gebruikerId;
    private AccountRol accountRol; // gebruik enum!

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void login(long gebruikerId, AccountRol rol) {
        this.gebruikerId = gebruikerId;
        this.accountRol = rol;
    }

    public void logout() {
        this.gebruikerId = 0;
        this.accountRol = null;
    }



    public AccountRol getAccountRol() {
        return accountRol;
    }

    public boolean isIngelogdAls(AccountRol rol) {
        return this.accountRol == rol;
    }

    public long getGebruikerId() {
        return gebruikerId;
    }

}
