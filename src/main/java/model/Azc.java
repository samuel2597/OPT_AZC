package model;


import jakarta.persistence.*;
import services.GemeenteService;

import java.util.ArrayList;

import java.util.List;

@Entity
public class Azc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String straat;
    private int nummer;
    private String postcode;
    @ManyToOne
    private Gemeente gemeente;

    @OneToMany(mappedBy = "huidigeAzc")
    private List<Asielzoeker> asielzoekers = new ArrayList<>();


    public Azc(String straat, int nummer, String postcode, Gemeente gemeente) {
        this.straat = straat;
        this.nummer = nummer;
        this.postcode = postcode;
        this.gemeente = gemeente;

    }

    public Azc() {}


    public void setStraat(String straat) {
        this.straat = straat;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getStraat() {return straat;    }

    public int getNummer() {return nummer;   }

    public String getPostcode() {return postcode;  }

    public Long getId() {return id;}

    public Gemeente getGemeente() {
        return gemeente;
    }

    public List<Asielzoeker> getAsielzoekers() {return asielzoekers; }

    public void voegAsielzoekerToe(Asielzoeker a) {asielzoekers.add(a); }

    public void verwijderAsielzoeker(Asielzoeker a) {asielzoekers.remove(a); }
}