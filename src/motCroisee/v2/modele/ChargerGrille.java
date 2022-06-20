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

		Statement stmt = connexion.createStatement();
		ResultSet res = stmt.executeQuery("select * from TP5_GRILLE");
		while (res.next()) {
			String nomGrille = res.getString(2);
			int numGrille = res.getInt(1), haut = res.getInt(3), larg = res.getInt(4);
			grilles.put(numGrille, nomGrille + " (" + haut + "x" + larg + ")");
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

		// parcours pour obtenir le numero de la derniere grille
		while (it.hasNext()) {
			numgrilleMax = it.next();
		}

		Map<String, Integer> ret = new HashMap<>();
		ret.put("min", numgrilleMin);
		ret.put("max", numgrilleMax);
		return ret;
	}

	public MotsCroisesTP6 extraireGrille(int numGrille) throws SQLException {

		Statement stmt = connexion.createStatement();
		ResultSet res = stmt.executeQuery("select hauteur, largeur, nom_grille from TP5_GRILLE where num_grille = " + numGrille);
		// Extraire la hauteur et la largeur de la BD pour initialiser notre grille
		int haut, larg;
		String nomGrille;
		if (res.next()) {
			haut = res.getInt(1);
			larg = res.getInt(2);
			nomGrille = res.getString(3);
		} else
			throw new IllegalArgumentException("La grille : n°" + numGrille + " n'existe pas");

		MotsCroisesTP6 mc = new MotsCroisesTP6(haut, larg);
		mc.setNomGrille(nomGrille);	

		// Extraire les autres infos pour compléter la grille (ligne, colonne, horiz,..)
		res = stmt.executeQuery("select * from TP5_MOT where num_grille = " + numGrille);
		while (res.next()) {
			int lig = res.getInt(4), col = res.getInt(5);
			boolean horiz = res.getBoolean(3);
			String def = res.getString(2), sol = res.getString(6);
			setCase(mc, lig, col, horiz, def, sol);
		}
		connexion.close();
		return mc;
	}

	private void setCase(MotsCroisesTP6 m, int lig, int col, boolean horiz, String def, String sol) {
		// "denoircir" la case qu'on veut modifier
		m.setCaseNoire(lig, col, false);
		// ajout def
		m.setDefinition(lig, col, horiz, def);
		// ajout solution caractère par caractère
		int i = 0;
		if (horiz) {
			while (i < sol.length()) {
				m.setCaseNoire(lig, col + i, false);
				m.setSolution(lig, col + i, sol.charAt(i));
				i++;
			}
		} else {
			while (i < sol.length()) {
				m.setCaseNoire(lig + i, col, false);
				m.setSolution(lig + i, col, sol.charAt(i));
				i++;
			}
		}
	}

}