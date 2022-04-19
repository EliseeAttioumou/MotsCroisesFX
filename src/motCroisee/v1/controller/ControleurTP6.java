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
                int col = ((int) n.getProperties().get("gridpane-column")) + 1;

                //Bind des cases de la vue avec celles du modele
                tf.textProperty().bindBidirectional(motsCroisesTP6.propositionProperty(lig,col));

                //Ajout des toolTip
				tf.setTooltip(new Tooltip(infoBulles(lig, col)));

                //Ajout des cliques molette pour devoiler la solution
                tf.setOnMouseClicked( (e)-> {this.clicLettre(e);} );
            }
        }
    }

	// affichage infobulles : soit une chaîne donnant la définition horizontale,
	// la définition verticale, ou les deux avec un « / » de séparation
	private String infoBulles(int lig, int col) {
		String defHoriz = motsCroisesTP6.getDefinition(lig, col, true);
		String defVert = motsCroisesTP6.getDefinition(lig, col, false);
		return defHoriz != null && defVert != null ? defHoriz + " / " + defVert : defHoriz != null ? defHoriz : defVert;
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