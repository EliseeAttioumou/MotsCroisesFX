package motCroisee.v1.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import motCroisee.v1.modele.MotsCroisesFactory;
import motCroisee.v1.modele.MotsCroisesTP6;

public class ControleurTP6 {

    private MotsCroisesTP6 motsCroisesTP6;

    @FXML
    private GridPane grilleMC;

    @FXML
    private void initialize() {
        this.motsCroisesTP6 = MotsCroisesFactory.creerMotsCroises2x3();

        for (Node n : grilleMC.getChildren()){
            if (n instanceof TextField) {
                TextField tf = (TextField) n;

                int lig = ((int) n.getProperties().get("gridpane-row")) + 1;
                int col = ((int) n.getProperties().get("gridpane-column")) + 1;// Initialisation du TextField tf ayant pour coordonnées (lig, col)// (cf. sections 1.3, 1.4 et 1.5)   }}

                //Bind des cases de la vue avec celles du modele
                tf.textProperty().bindBidirectional(motsCroisesTP6.propositionProperty(lig,col));

                //Ajout des toolTip

                //Trois cas
                String defHorizontal = motsCroisesTP6.getDefinition(lig,col,true);
                String defVertical = motsCroisesTP6.getDefinition(lig,col,false);

                //Deux definition horizontal et vertical
                if ( defHorizontal != null && defVertical != null ){
                    tf.setTooltip(new Tooltip("ligne " + lig + " colonne " + col + " (horizontal / vertical) : " + defHorizontal + "/" + defVertical));
                } else if ( defHorizontal != null ){
                    tf.setTooltip(new Tooltip("ligne " + lig + " colonne " + col + " (horizontal) : " + defHorizontal ));
                } else if ( defVertical != null ){
                    tf.setTooltip(new Tooltip("ligne " + lig + " colonne " + col + " (vertical) : " + defVertical));
                }

                //Ajout des cliques molette pour devoiler la solution
                tf.setOnMouseClicked( (e)-> {this.clicLettre(e);} );




            }
        }
    }


    @FXML
    public void clicLettre(MouseEvent e) {
        if (e.getButton() == MouseButton.MIDDLE) {                     // C'est un clic "central"
            TextField laCase = (TextField) e.getSource();
            int lig = ((int) laCase.getProperties().get("gridpane-row")) + 1;
            int col = ((int) laCase.getProperties().get("gridpane-column")) + 1;

            this.motsCroisesTP6.montrerSolution(lig,col);
        }
    }


}