package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Gemeente {
    private  String naam;
    private int aantalInwoners;
    private int aangebodenPlaatsen;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Gemeente(String naam, int aantalInwoners, int aangebodenPlaatsen){
        this.naam= naam;
        this.aantalInwoners= aantalInwoners;
        this.aangebodenPlaatsen= aangebodenPlaatsen;
    }

    public Gemeente(){

    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setAantalInwoners(int aantalInwoners) {
        this.aantalInwoners = aantalInwoners;
    }

    public void setAangebodenPlaatsen(int aangebodenPlaatsen) {
        this.aangebodenPlaatsen = aangebodenPlaatsen;
    }



    public Long getId() {
        return id;
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