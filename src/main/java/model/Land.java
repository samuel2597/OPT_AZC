package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Land {
    private String naam;
    private boolean isVeilig;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Land(String naam, boolean isVeilig){
        this.naam=naam;
        this.isVeilig=isVeilig;

    }

    public Land(){

    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setVeilig(boolean veilig) {
        isVeilig = veilig;
    }

    public Long getId() {
        return id;
    }

    public String getNaam(){
        return naam;
    }

    public boolean isVeilig() {
        return isVeilig;
    }
}