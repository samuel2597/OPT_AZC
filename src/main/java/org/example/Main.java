package org.example;
import controller.BeheerderController;
import services.*;
import services.AzcService;
import services.LandService;
import java.util.Scanner;
import controller.AsielzoekerController;



import jakarta.persistence.*;
import model.Asielzoeker;
import model.Land;

public class Main {

    public static void main(String[] args) {

//        DossierServices dossierServices= new DossierServices();
//        dossierServices.registreerAsielzoeker();
        BeheerderController beheerderController= new BeheerderController();
        beheerderController.azcVerwijderen();



    }
}