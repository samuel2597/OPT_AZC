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
    private String plaatsingStatus;

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
    public Dossier(){

    }

    public boolean isPaspoortGetoond() {
        return paspoortGetoond;
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
