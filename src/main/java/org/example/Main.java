package org.example;
import services.GemeenteService;
import services.AzcService;
import services.LandService;
import java.util.Scanner;



import jakarta.persistence.*;
import model.Asielzoeker;
import model.Land;

public class Main {

    public static void main(String[] args) {
        GemeenteService gemeente= new GemeenteService();
        gemeente.deleteGemeente();
    }
}