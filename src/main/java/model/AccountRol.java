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
    public static AccountRol fromString(String rolNaam) {
        for (AccountRol rol : AccountRol.values()) {
            if (rol.name().equalsIgnoreCase(rolNaam)) {
                return rol;
            }
        }
        throw new IllegalArgumentException("Onbekende rol: " + rolNaam);
    }

}