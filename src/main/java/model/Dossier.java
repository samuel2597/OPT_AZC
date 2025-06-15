package model;

import jakarta.persistence.*;

@Entity
public class Dossier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean paspoortGetoond;
    private boolean aanvraagCompleet;
    private boolean rechterToegewezen;
    private boolean uitspraakGedaan;
    private String uitspraak;
    private boolean teruggekeerd;
    private String plaatsingStatus; // "nee", "gestart", "afgerond
    private String straat;
    private int huisnummer;
    private String postcode;

    public Dossier(boolean paspoortGetoond, boolean aanvraagCompleet, boolean rechterToegewezen,
                   boolean uitspraakGedaan, String uitspraak, boolean teruggekeerd, String plaatsingStatus) {
        this.paspoortGetoond = paspoortGetoond;
        this.aanvraagCompleet = aanvraagCompleet;
        this.rechterToegewezen = rechterToegewezen;
        this.uitspraakGedaan = uitspraakGedaan;
        this.uitspraak = uitspraak;
        this.teruggekeerd = teruggekeerd;
        this.plaatsingStatus = plaatsingStatus;
    }
    public Dossier(boolean paspoortGetoond) {
        this.paspoortGetoond = paspoortGetoond;
        this.aanvraagCompleet = false;
        this.rechterToegewezen = false;
        this.uitspraakGedaan = false;
        this.uitspraak = null;
        this.teruggekeerd = false;
        this.plaatsingStatus = null;
    }


    public boolean isPaspoortGetoond() {
        return paspoortGetoond;
    }

    public Dossier() {
    }

    public void setPaspoortGetoond(boolean paspoortGetoond) {
        this.paspoortGetoond = paspoortGetoond;
    }

    public void setAanvraagCompleet(boolean aanvraagCompleet) {
        this.aanvraagCompleet = aanvraagCompleet;
    }

    public void setRechterToegewezen(boolean rechterToegewezen) {
        this.rechterToegewezen = rechterToegewezen;
    }

    public void setUitspraakGedaan(boolean uitspraakGedaan) {
        this.uitspraakGedaan = uitspraakGedaan;
    }

    public void setUitspraak(String uitspraak) {
        this.uitspraak = uitspraak;
    }

    public void setTeruggekeerd(boolean teruggekeerd) {
        this.teruggekeerd = teruggekeerd;
    }

    public void setPlaatsingStatus(String plaatsingStatus) {
        this.plaatsingStatus = plaatsingStatus;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public void setHuisnummer(int huisnummer) {
        this.huisnummer = huisnummer;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public boolean isAanvraagCompleet() {
        return aanvraagCompleet;
    }

    public boolean isRechterToegewezen() {
        return rechterToegewezen;
    }

    public boolean isUitspraakGedaan() {
        return uitspraakGedaan;
    }

    public String getUitspraak() {
        return uitspraak;
    }

    public boolean isTeruggekeerd() {
        return teruggekeerd;
    }

    public String getPlaatsingStatus() {
        return plaatsingStatus;
    }


    // Getters en setters
}
