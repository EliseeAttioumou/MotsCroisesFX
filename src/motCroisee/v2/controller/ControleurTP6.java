package motCroisee.v2.controller;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import javafx.util.Duration;
import motCroisee.v2.modele.MotsCroisesTP6;
import motCroisee.v2.view.ViewFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class ControleurTP6 extends BaseController {

	private MotsCroisesTP6 motsCroisesTP6;

	private TextField modeleTF;

	private int selectedGrid;

	@FXML
	private GridPane grilleMC;

	@FXML
	private Label gridNameLabel;

	public ControleurTP6(String viewPath, ViewFactory viewFactory, int selectedGrid) {
		super(viewPath, viewFactory);
		this.selectedGrid = selectedGrid;
	}

	@FXML
	private void initialize() throws SQLException {

		// Copie de la premiere case comme patron
		modeleTF = (TextField) grilleMC.getChildren().get(0);

		// Chargement de la grille
		loadGrid(selectedGrid);

	}

	@Override
	public void afterInit() {

		// Ajout d'évènements lors d'appuis sur les touches suivantes
		scene.setOnKeyReleased(event -> {
			this.appuisTouche(event);
		});
	}

	// PARTIE METHODES utilitaires

	/**
	 * Methode qui va s'occuper de charger la grille en provenance de la base de
	 * données et de l'afficher sur la scene
	 * 
	 * @param number : numero de la grille a charger en base, Si neg alors choix
	 *               aléatoire
	 */
	private void loadGrid(int number) throws SQLException {

		// choix de la grille
		// Si number neg alors choix aléatoire
		if (number < 0) {

			// recup des grilles disponibles
			Map<String, Integer> grillesInfo = this.chargerGrille.recupererNumerosMinMaxDesGrilles();

			number = ((int) (Math.random() * (grillesInfo.get("max") - grillesInfo.get("min"))))
					+ grillesInfo.get("min");
		}

		// Suppression de tous les enfants du gridpane
		grilleMC.getChildren().clear();

		// Recuperation d'une grille de mot croisés
		this.motsCroisesTP6 = chargerGrille.extraireGrille(number);

		// Changement de la valeur du titre pour la nom de la grille
		gridNameLabel.setText(this.motsCroisesTP6.getNomGrille());

		// Ajout des cases dans la grille
		for (int lig = 1; lig <= this.motsCroisesTP6.getHauteur(); lig++) {
			for (int col = 1; col <= this.motsCroisesTP6.getLargeur(); col++) {
				if (!this.motsCroisesTP6.estCaseNoire(lig, col)) {
					TextField tf = new TextField();
					tf.setPrefWidth(modeleTF.getPrefWidth());
					tf.setPrefHeight(modeleTF.getPrefHeight());

					for (Object cle : modeleTF.getProperties().keySet()) {
						tf.getProperties().put(cle, modeleTF.getProperties().get(cle));
					}

					this.grilleMC.add(tf, col - 1, lig - 1);
				}
			}
		}

		// Initialisation des cases dans la grille
		for (Node n : grilleMC.getChildren()) {
			if (n instanceof TextField) {
				TextField tf = (TextField) n;

				int lig = ((int) n.getProperties().get("gridpane-row")) + 1;
				int col = ((int) n.getProperties().get("gridpane-column")) + 1;

				// Bind des cases de la vue avec celles du modele
				tf.textProperty().bindBidirectional(motsCroisesTP6.propositionProperty(lig, col));

				// Ajout des transitions
				ScaleTransition scaleTransition = new ScaleTransition();
				scaleTransition.setDuration(Duration.millis(250));
				scaleTransition.setCycleCount(1);
				scaleTransition.setAutoReverse(false);
				scaleTransition.setByX(0.03);
				scaleTransition.setByY(0.03);
				scaleTransition.setNode(n);

				// Ajout des toolTip
				tf.setTooltip(new Tooltip(infoBulles(lig, col)));

				// Ajout des evenements

				// Ajout des cliques molette pour devoiler la solution
				tf.setOnMouseClicked((e) -> {
					this.clicLettre(e);
				});

				// Limite le nombre de caractères dans le champ a 1
				tf.textProperty().addListener((observableValue, oldString, newString) -> {

					// System.out.println("Changed event : old=(" + oldString + ")new=(" + newString
					// + ")");

					// Remise a zéro de la taille de la case
					tf.setScaleX(1);
					tf.setScaleY(1);

					// Case vidé
					if (newString.length() == 0) {
						cursorLogic("LEFT");
						// Joue la transition
						setDefaultBackgroundBadBox();
						return;
					}

					String oneChar = newString.substring(newString.length() - 1);


					// Verification de l'entrée utilisateur
					if (!oneChar.matches("[a-zA-Z]")) {
						tf.setText(oldString);

						return;
					}

					// Si case deja rempli
					if (tf.getText().length() > 1) {
						tf.setText(oneChar);
					} else {
						// Se déplacer a droite
						cursorLogic("RIGHT");
						// Remet toutes les cases qui sont mauvaises avec un fond blanc
						setDefaultBackgroundBadBox();
					}

					// Jouer l'animation
					scaleTransition.play();

				});

			}
		}

	}

	// Methode qui permet de selectionner la case en fonction de l'action choisie
	// Retourne faux si l'element actuel n'est pas une case ou que la case est sur
	// une bordure
	private boolean cursorLogic(String action) {
		boolean ret = true;

		// Recuperation de la case active

		if (!(scene.focusOwnerProperty().get() instanceof TextField))
			ret = false;

		TextField tf = (TextField) scene.focusOwnerProperty().get();
		int lig = ((int) tf.getProperties().get("gridpane-row")) + 1;
		int col = ((int) tf.getProperties().get("gridpane-column")) + 1;

		if (action.equals("RIGHT")) {

			// Si pas de case a gauche alors essayer de descendre d'une case
			if (!setFocusOnBox(lig, col + 1)) {
				setFocusOnBox(lig + 1, col);
			}

		} else if (action.equals("LEFT")) {
			if (!setFocusOnBox(lig, col - 1)) {
				setFocusOnBox(lig - 1, col);
			}
		} else if (action.equals("UP")) {
			if (!setFocusOnBox(lig - 1, col)) {
				setFocusOnBox(lig, col - 1);
			}
		} else if (action.equals("DOWN")) {
			if (!setFocusOnBox(lig + 1, col)) {
				setFocusOnBox(lig, col + 1);
			}
		} else {
			System.out.println("Cursor action not defined");
			ret = false;
		}

		return ret;
	}

	private boolean setFocusOnBox(int lig, int col) {
		boolean ret = false;
		for (Node n : grilleMC.getChildren()) {
			if (n instanceof TextField) {
				// TextField tf = (TextField) n;
				int tfLig = ((int) n.getProperties().get("gridpane-row")) + 1;
				int tfCol = ((int) n.getProperties().get("gridpane-column")) + 1;

				if (tfLig >= lig && tfCol > col)
					break;

				if (tfLig == lig && tfCol == col) {
					n.requestFocus();
					ret = true;
					break;
				}
			}
		}
		return ret;

	}

	private void highlightGoodBox() {

		for (Node n : grilleMC.getChildren()) {
			if (n instanceof TextField) {
				TextField tf = (TextField) n;

				int tfLig = ((int) n.getProperties().get("gridpane-row")) + 1;
				int tfCol = ((int) n.getProperties().get("gridpane-column")) + 1;

				if (String.valueOf(this.motsCroisesTP6.getSolution(tfLig, tfCol)).equals(tf.getText())) {
					// Bonne proposition de la part de l'utilisateur
					tf.getStyleClass().add("goodTextField");

				}
			}
		}

	}

	private void checkforWin() {

		if (this.motsCroisesTP6.gagne()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Bravo!");
			alert.setHeaderText("Bien joué tu as réussi a finir cette grille");
			alert.showAndWait().ifPresent(rs -> {
				if (rs == ButtonType.OK) {
					try {
						this.viewFactory.setScene("MainMenu");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}

	}

	private void setDefaultBackgroundBadBox() {
		for (Node n : grilleMC.getChildren()) {
			if (n instanceof TextField) {
				TextField tf = (TextField) n;

				int tfLig = ((int) n.getProperties().get("gridpane-row")) + 1;
				int tfCol = ((int) n.getProperties().get("gridpane-column")) + 1;

				String entreUtilisateur = tf.getText();
				if (!String.valueOf(this.motsCroisesTP6.getSolution(tfLig, tfCol)).equals(entreUtilisateur)) {
					// Bonne proposition de la part de l'utilisateur
					tf.getStyleClass().remove("goodTextField");
				}
			}
		}

	}

	// Events

	private void appuisTouche(KeyEvent e) {
		switch (e.getCode()) {
		case UP:
			cursorLogic("UP");
			break;
		case DOWN:
			cursorLogic("DOWN");
			break;
		case LEFT:
			cursorLogic("LEFT");
			break;
		case RIGHT:
			cursorLogic("RIGHT");
			break;
		case ENTER:
			highlightGoodBox();
			checkforWin();
		default:
			break;
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

		if (e.getButton() == MouseButton.MIDDLE) { // C'est un clic "central"
			TextField laCase = (TextField) e.getSource();
			int lig = ((int) laCase.getProperties().get("gridpane-row")) + 1;
			int col = ((int) laCase.getProperties().get("gridpane-column")) + 1;

			this.motsCroisesTP6.montrerSolution(lig, col);
			laCase.getStyleClass().add("goodTextField");
		}
	}

	@FXML
	void backToMainMenuAction(ActionEvent event) throws IOException {
		this.viewFactory.setScene("MainMenu");
	}

}