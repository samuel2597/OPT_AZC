package model;

public class Gemeente {
    private final String naam;
    private int aantalInwoners;
    private int aangebodenPlaatsen;

    public Gemeente(String naam, int aantalInwoners, int aangebodenPlaatsen){
        this.naam= naam;
        this.aantalInwoners= aantalInwoners;
        this.aangebodenPlaatsen= aangebodenPlaatsen;
    }

    public String getNaam() {
        return naam;
    }

    public int getAantalInwoners() {
        return aantalInwoners;
    }

    public int getAangebodenPlaatsen() {
        return aangebodenPlaatsen;
    }



}