module TP6 {

    //Lib
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;


    //Version 1
    opens motCroisee.v1;

    opens motCroisee.v1.controller;
    opens motCroisee.v1.modele;
    opens motCroisee.v1.view;

    //Version 2
    opens motCroisee.v2;

    opens motCroisee.v2.controller;
    opens motCroisee.v2.modele;
    opens motCroisee.v2.view;
    opens motCroisee.v2.ressources.fonts;

}