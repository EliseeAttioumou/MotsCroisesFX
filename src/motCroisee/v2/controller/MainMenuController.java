package motCroisee.v2.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import motCroisee.v2.view.ViewFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MainMenuController extends BaseController{

    @FXML
    private Button chosenGridButton;

    @FXML
    private Button quitButton;

    @FXML
    private Button randomGridButton;

    public MainMenuController(String viewPath, ViewFactory viewFactory) {
        super(viewPath, viewFactory);
    }

    @FXML
    private void initialize(){

    }

    @FXML
    void randomGridAction(ActionEvent event) throws IOException {
        System.out.println("random grid");
        Map<String,String> params = new HashMap<>();
        params.put("gridNumber","-1");
        this.viewFactory.setScene("VueTP6",params);
    }

    @FXML
    void chosenGridAction(ActionEvent event) throws IOException, SQLException {
        System.out.println("Chosen grid");

        //Recuperation des grilles disponibles
        Map<String,Integer> grillesInfo = this.chargerGrille.recupererNumerosMinMaxDesGrilles();

        //boucle tant que la grille n'est pas bonne ou appuis sur annuler
        int userGridNumber = 0;
        do {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setHeaderText("Choisisez une grille entre " + grillesInfo.get("min") +" et " + grillesInfo.get("max"));
            Optional<String> result = inputDialog.showAndWait();

            //Si cancel/ fermeture pressé, donc pas de résultats
            if ( !result.isPresent() ){
                return;
            }

            try {
                userGridNumber = Integer.parseInt(inputDialog.getEditor().getText());

            } catch (NumberFormatException e){
                userGridNumber = 0;
            }

        } while (userGridNumber < grillesInfo.get("min") || userGridNumber > grillesInfo.get("max"));


        //Chargement de vue avec la grille
        Map<String,String> params = new HashMap<>();
        params.put("gridNumber",String.valueOf(userGridNumber));
        this.viewFactory.setScene("VueTP6",params);
    }

    @FXML
    void quitAction(ActionEvent event) {
        System.out.println("QUIT");
        System.exit(0);
    }




}
