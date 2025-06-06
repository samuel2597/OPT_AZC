package model;
public enum AccountRol {
    Beheerder(0),
    CoaMedewerker(1),
    Asielzoeker(2);

    private final int waarde;

    AccountRol(int waarde) {
        this.waarde = waarde;
    }

    public int getWaarde() {
        return waarde;
    }
}