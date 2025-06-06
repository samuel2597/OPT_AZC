package model;

public class Dossier {
    private boolean paspoortGetoond;
    private boolean aanvraagCompleet;
    private boolean rechterToegewezen;
    private boolean uitspraakGedaan;
    private String uitspraak;
    private  boolean teruggekeerd;
    private String plaatsingStatus;

    public Dossier(boolean paspoortGetoond,boolean aanvraagCompleet,boolean rechterToegewezen,boolean uitspraakGedaan,String uitspraak,boolean teruggekeerd,String plaatsingStatus){
        this.paspoortGetoond=paspoortGetoond;
        this.aanvraagCompleet=aanvraagCompleet;
        this.rechterToegewezen= rechterToegewezen;
        this.uitspraakGedaan= uitspraakGedaan;
        this.uitspraak= uitspraak;
        this.teruggekeerd= teruggekeerd;
        this.plaatsingStatus= plaatsingStatus;

    }

}