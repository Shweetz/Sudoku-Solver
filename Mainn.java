public class Mainn {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Grille sudoku = new Grille();
		//sudoku.initialisationGrille(); // On met 0 dans toutes les cases de la grille
		//0 mis pendant la creation de la grille

		// D�clarer ici les valeurs initiales sous la forme EntrerValeur(ligne, colonne, valeur)

		/**
		 * essaye de lancer la section de code jusqu a ce qu une exception survienne 
		 * si throw Exception est appele, le code est interompu et on tombe dans catch
		try {
		*/
			sudoku.entrerValeur(1,1,3);
			sudoku.entrerValeur(1,2,1);
			sudoku.entrerValeur(1,4,4);
			sudoku.entrerValeur(1,5,5);
			sudoku.entrerValeur(1,6,2);
			sudoku.entrerValeur(2,3,2);
			sudoku.entrerValeur(2,5,7);
			sudoku.entrerValeur(2,8,3);
			sudoku.entrerValeur(2,9,4);
			sudoku.entrerValeur(3,4,8);
			sudoku.entrerValeur(3,8,5);
			sudoku.entrerValeur(4,1,7);
			sudoku.entrerValeur(4,2,2);
			sudoku.entrerValeur(4,4,9);
			sudoku.entrerValeur(4,5,6);
			sudoku.entrerValeur(4,9,1);
			sudoku.entrerValeur(5,1,5);
			sudoku.entrerValeur(5,2,9);
			sudoku.entrerValeur(5,8,2);
			sudoku.entrerValeur(5,9,3);
			sudoku.entrerValeur(6,1,4);
			sudoku.entrerValeur(6,5,3);
			sudoku.entrerValeur(6,6,5);
			sudoku.entrerValeur(6,8,6);
			sudoku.entrerValeur(6,9,9);
			sudoku.entrerValeur(7,2,3);
			sudoku.entrerValeur(7,6,8);
			sudoku.entrerValeur(8,1,2);
			sudoku.entrerValeur(8,2,6);
			sudoku.entrerValeur(8,5,1);
			sudoku.entrerValeur(8,7,3);
			sudoku.entrerValeur(9,4,3);
			sudoku.entrerValeur(9,5,2);
			sudoku.entrerValeur(9,6,7);
			sudoku.entrerValeur(9,8,9);
			sudoku.entrerValeur(9,9,8);

			sudoku.testLigneEntree(); // On v�rifie qu'une valeur en entr�e ne se trouve pas 2 fois
			// sur la m�me ligne

			// Cette prochaine boucle for contient toute la partie calcul pour r�soudre le sudoku, 
			// elle est compos�e de beaucoup de lignes de code !

			sudoku.resolutionSudoku(); // Cette m�thode contient tous les calculs pour r�soudre le sudoku 

			sudoku.afficheResolu(); // Une fois les calculs faits, on affiche le r�sultat

		/**
		 * code traite uniquement si une Exception est interceptee pendant le try du code
		 * printStackTrace affiche le message d erreur associe et d ou il vient
		}catch (Exception e){
			e.printStackTrace();
		}
		*/
	}

}
