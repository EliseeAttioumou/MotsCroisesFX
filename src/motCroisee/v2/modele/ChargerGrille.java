package motCroisee.v2.modele;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChargerGrille {

	private Connection connexion;

	public ChargerGrille() {

		try {
			connexion = connecterBD();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static Connection connecterBD() throws SQLException {
		Connection connect;
		connect = DriverManager.getConnection("jdbc:mysql://phpmyadmin2.istic.univ-rennes1.fr/base_bousse",
				"user_17009059", "root");
		return connect;
	}

	// Retourne la liste des grilles disponibles dans la B.D.
	// Chaque grille est décrite par la concaténation des valeurs
	// respectives des colonnes nom_grille, hauteur et largeur.
	// L’élément de liste ainsi obtenu est indexé par le numéro de
	// la grille (colonne num_grille).
	// Ainsi "Français débutants (7x6)" devrait être associé à la clé 10
	public Map<Integer, String> grillesDisponibles() throws SQLException {

		Map<Integer, String> grilles = new HashMap<>();

		// Recuperation des grilles
		String sql = "SELECT * FROM TP5_GRILLE";
		Statement st = this.connexion.createStatement();
		ResultSet results = st.executeQuery(sql);

		while (results.next()) {
			Integer num_grille = results.getInt("num_grille");
			String nom_grille = results.getString("nom_grille");
			String hauteur = results.getString("hauteur");
			String largeur = results.getString("largeur");

			grilles.put(num_grille, String.format("%s (%sx%s)", nom_grille, hauteur, largeur));

		}

		return grilles;
	}

	public Map<String, Integer> recupererNumerosMinMaxDesGrilles() throws SQLException {
		Map<Integer, String> grillesDispo = this.grillesDisponibles();
		int numgrilleMin = 0;
		int numgrilleMax = 0;

		Iterator<Integer> it = grillesDispo.keySet().iterator();
		// recuperation du numero de la premiere grille
		numgrilleMin = it.next();

		// parcour pour obtenir le numero de la derniere grille
		while (it.hasNext()) {
			numgrilleMax = it.next();
		}

		Map<String, Integer> ret = new HashMap<>();
		ret.put("min", numgrilleMin);
		ret.put("max", numgrilleMax);
		return ret;
	}

	public MotsCroisesTP6 extraireGrille(int numGrille) throws SQLException {

		int hauteur = 0, largeur = 0, ligne = 0, colonne = 0;
		String definition = "", solution = "";
		boolean horizontal = false;
		String nomGrille = "Grille de mots croisés";
		MotsCroisesTP6 motsCroises = null;// motCroise vide pour le cas ou numGrille invalide

		String sql = "SELECT nom_grille, hauteur , largeur, definition, ligne, colonne, solution, horizontal "
				+ " FROM TP5_GRILLE, TP5_MOT " + " WHERE TP5_GRILLE.num_grille = ? "
				+ " AND TP5_MOT.num_grille = TP5_GRILLE.num_grille";

		PreparedStatement sth = connexion.prepareStatement(sql);
		sth.setInt(1, numGrille);
		ResultSet result = sth.executeQuery();

		boolean firstLoop = true;

		// si le resultat est nul alors aucune grille ne correspond a ce num grille
		while (result.next()) {

			// Creer le mot croisé a la premier itération
			if (firstLoop) {
				hauteur = result.getInt("hauteur");
				largeur = result.getInt("largeur");
				nomGrille = result.getString("nom_grille");

				motsCroises = new MotsCroisesTP6(hauteur, largeur);
				motsCroises.setNomGrille(nomGrille);

				firstLoop = false;
			}

			definition = result.getString("definition");
			ligne = result.getInt("ligne");
			colonne = result.getInt("colonne");
			solution = result.getString("solution");
			horizontal = (result.getInt("horizontal") == 1) ? true : false;

			// **** Remplissage des grilles

			// solution
			for (int i = 0; i < solution.length(); i++) {
				// si le mot est horizontal
				if (horizontal) {
					motsCroises.setSolution(ligne, colonne + i, solution.charAt(i));
				} else {
					motsCroises.setSolution(ligne + i, colonne, solution.charAt(i));
				}
			}

			// definition
			if (horizontal) {
				motsCroises.setDefinition(ligne, colonne, true, definition);
			} else {
				motsCroises.setDefinition(ligne, colonne, false, definition);
			}

		}
		return motsCroises;
	}

}