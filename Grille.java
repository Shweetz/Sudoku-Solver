public class Grille {

	private int [][] tab;

	/**
	 * constructeur par d�faut
	 */
	public Grille(){
		tab = new int[9][9];
		initialisationGrille();
	}


	/**
	 * getter of tab
	 * @return tab la grille de sudoku
	 */
	public int[][] getTab() {
		return tab;
	}


	/**
	 * setter of tab
	 * @param tab la grille de sudoku
	 */
	public void setTab(int[][] tab) {
		this.tab = tab;
	}



	public void initialisationGrille ()	{
		for (int i=0; i<9; i++) 
		{
			for (int j=0; j<9; j++)
			{
				tab[i][j]=0;
			}
		}
	}

	public  void entrerValeur(int i, int j, int valeur) /** throws Exception */ 
	{
		if (0<i && i<10 && 0<j && j<10 && 0<valeur && valeur<10)
		{
			tab[i-1][j-1]=valeur+10; // Le +10 est pour diff�rencier les valeurs fixes
		}							 // des valeurs que l'on va tester
		else
		{
			afficheImpossible();
			/**
			 * pense � System.err.print quand tu veux vraiment imprimer en console d erreur
			 * 
			 * mais de toute facon c est pas trop java de faire un message puis un exit... 
			 * si tu veux faire plus propre il faut soulever une exception 
			 * a la place de toutes les lignes que tu as ecrites tu mets :
			 * throw new Exception("parametres d entreeValeur incorrects, ligne : " +  + "  col : " + j  + " val : " + valeur );
			 * (tu peux changer le texte en parametre ) 
			 * en decommentant le throws Exception dans la declaration de la fonction 
			 * et les try/catch dans la fonction main
			 */

			System.err.print("Erreur : Il y a une valeur non comprise entre 1 et 9 ou ");
			System.err.println("non situ�e dans la grille 9x9.");
			System.err.print("Elle a �t� entr�e apr�s les valeurs renseign�es ");
			System.err.println("dans la grille pr�c�dente.");
			System.exit(0);

		}

	}

	public  void testLigneEntree () 
	{
		// On v�rifie qu'il n'y a pas deux fois une valeur entr�e sur une m�me ligne,
		// la partie calcul du code pouvant traiter tous les autres cas

		for (int i=0; i<9; i++) 
		{
			for (int j=0; j<9; j++)
			{
				for (int k=8; k>=0; k--) 
				{
					if (tab[i][j]==tab[i][k] && k!=j && tab[i][j]!=0) 
					{
						/**
						 * penser aux exceptions ici au lieu de message puis exit
						 */
						afficheImpossible();
						System.err.print("Sudoku impossible, une valeur au moins est deux ");
						System.err.println("fois sur une m�me ligne !");
						System.exit(0);
					}
				}
			}
		}
	}

	public  void resolutionSudoku() // Le coeur du programme
	{
		boolean impossible=false;
		int valeurAvantTests;

		for (int i=0; i<9; i++) 
		{
			for (int j=0; j<9; j++) // Double boucle pour rep�rer la case dans le tableau
			{
				if (impossible==false) 	   // Si la valeur de la case pr�c�dente �tait possible
				{				       
					while (tab[i][j] > 10 && i*j!=64) // Tant que la case actuelle est fixe,
						// on passe � la case suivante
					{					
						if (j==8)
						{
							j=0;
							i++;
						}
						else
						{
							j++;
						}
					}	

					if (tab[i][j] < 10)
					{
						tab[i][j]=1; 			// et on initialise la case non fixe suivante
					}
				}

				else // Si la valeur de la case pr�c�dente ne convient pas
				{
					while (tab[i][j] > 8 && i+j!=0) // Si aucune valeur ne convient ou si elle 
						// est fixe, on va changer la case pr�c�dente � celle-ci
					{
						if (tab[i][j]==9)
						{
							tab[i][j]=0; // tout en r�initialisant la valeur si non fixe
						}

						if (j==0)
						{
							j=8;
							i--;
						}
						else
						{
							j--;		
						}
					}

					if (tab[i][j] < 9)
					{
						tab[i][j]++; // Si la valeur ne convenant pas de la case test�e se trouve 
					}			 	 // entre 1 et 8, on teste avec la valeur suivante 

					if (tab[i][j] > 8 && i==0 && j==0)
					{
						/**
						 * penser aux exceptions ici au lieu de message puis exit
						 */
						afficheImpossible();
						System.err.print("Sudoku impossible : valeurs entr�es fausses ");
						System.err.println("ou pas de combinaison r�alisable.");
						System.exit(0);
					}
				}

				impossible=false;
				valeurAvantTests = 0;

				while (valeurAvantTests!=tab[i][j]) 
					// Si la valeur a chang� apr�s un des tests, il faut tous les refaire
				{
					valeurAvantTests=tab[i][j];

					for (int k=8; k>=0; k--) 
					{
						if (tab[i][j]==tab[i][k]%10 && k!=j) 
							// On v�rifie que la valeur n'est pas dans la ligne
						{
							if (tab[i][j]<9) // On �vite d'inscrire 10 dans une case
							{
								tab[i][j]++;
								k=j;
							}
							else if (j==0) // Pour ce faire, on essaie de changer la valeur de
								// la case pr�c�dente
							{
								tab[i][j]=0;
								j=7;
								i--;
								k=-1;
								impossible=true;
							}
							else if (j==1) 
							{
								tab[i][j]=0;
								j=8;
								i--;
								k=-1;
								impossible=true;
							}
							else
							{
								tab[i][j]=0;
								j=j-2;
								k=-1;
								impossible=true;
							}
						}
					}

					if (impossible == false)
					{	
						for (int k=8; k>=0; k--) 
						{
							if (tab[i][j]==tab[k][j]%10 && k!=i) 
								// Si la valeur n'est pas dans la ligne, on v�rifie ensuite 
								// qu'elle n'est pas non plus dans la colonne
							{
								if (tab[i][j]<9)
								{
									tab[i][j]++;
									k=i;
								}
								else if (j==0)
								{
									tab[i][j]=0;
									j=7;
									i--;
									k=-1;
									impossible=true;
								}
								else if (j==1)
								{
									tab[i][j]=0;
									j=8;
									i--;
									k=-1;
									impossible=true;
								}
								else
								{
									tab[i][j]=0;
									j=j-2;
									k=-1;
									impossible=true;
								}
							}
						}
					}

					if (impossible == false) 
						// Si la valeur n'est ni dans la ligne ni dans la colonne, on v�rifie 
						// qu'elle n'est pas non plus dans la r�gion	 						   
					{
						if (i%3==0 && impossible==false) // Si on est dans la 1e ligne de la r�gion
						{
							if (j%3==0) // Si on est dans la 1e colonne de la r�gion
								if (tab[i][j]==tab[i+1][j+1]%10 || tab[i][j]==tab[i+1][j+2]%10
										|| tab[i][j]==tab[i+2][j+1]%10 || tab[i][j]==tab[i+2][j+2]%10)
								{
									if (tab[i][j]!=9)
										tab[i][j]++;

									else if (j==0)
									{
										tab[i][j]=0;
										j=7;
										i--;
										impossible=true;
									}
									else if (j==1)
									{
										tab[i][j]=0;
										j=8;
										i--;
										impossible=true;
									}
									else
									{
										tab[i][j]=0;
										j=j-2;
										impossible=true;
									}
								}						

							if (j%3==1 && impossible==false)
							{
								if (tab[i][j]==tab[i+1][j-1]%10 || tab[i][j]==tab[i+1][j+1]%10
										|| tab[i][j]==tab[i+2][j-1]%10 || tab[i][j]==tab[i+2][j+1]%10)
								{
									if (tab[i][j]!=9)
										tab[i][j]++;

									else if (j==0)
									{
										tab[i][j]=0;
										j=7;
										i--;
										impossible=true;
									}
									else if (j==1)
									{
										tab[i][j]=0;
										j=8;
										i--;
										impossible=true;
									}
									else
									{
										tab[i][j]=0;
										j=j-2;
										impossible=true;
									}
								}
							}

							if (j%3==2 && impossible==false)
							{
								if (tab[i][j]==tab[i+1][j-2]%10 || tab[i][j]==tab[i+1][j-1]%10
										|| tab[i][j]==tab[i+2][j-2]%10 || tab[i][j]==tab[i+2][j-1]%10)
								{
									if (tab[i][j]!=9)
										tab[i][j]++;

									else if (j==0)
									{
										tab[i][j]=0;
										j=7;
										i--;
										impossible=true;
									}
									else if (j==1)
									{
										tab[i][j]=0;
										j=8;
										i--;
										impossible=true;
									}
									else
									{
										tab[i][j]=0;
										j=j-2;
										impossible=true;
									}
								}
							}
						}

						if (i%3==1) // Si on est dans la 2e ligne de la r�gion
						{
							if (j%3==0)
							{
								if (tab[i][j]==tab[i-1][j+1]%10 || tab[i][j]==tab[i-1][j+2]%10
										|| tab[i][j]==tab[i+1][j+1]%10 || tab[i][j]==tab[i+1][j+2]%10)
								{
									if (tab[i][j]!=9)
										tab[i][j]++;

									else if (j==0)
									{
										tab[i][j]=0;
										j=7;
										i--;
										impossible=true;
									}
									else if (j==1)
									{
										tab[i][j]=0;
										j=8;
										i--;
										impossible=true;
									}
									else
									{
										tab[i][j]=0;
										j=j-2;
										impossible=true;
									}
								}
							}

							if (j%3==1 && impossible==false)
							{
								if (tab[i][j]==tab[i-1][j-1]%10 || tab[i][j]==tab[i-1][j+1]%10
										|| tab[i][j]==tab[i+1][j-1]%10 || tab[i][j]==tab[i+1][j+1]%10)
								{
									if (tab[i][j]!=9)
										tab[i][j]++;

									else if (j==0)
									{
										tab[i][j]=0;
										j=7;
										i--;
										impossible=true;
									}
									else if (j==1)
									{
										tab[i][j]=0;
										j=8;
										i--;
										impossible=true;
									}
									else
									{
										tab[i][j]=0;
										j=j-2;
										impossible=true;
									}
								}
							}

							if (j%3==2 && impossible==false)
							{
								if (tab[i][j]==tab[i-1][j-2]%10 || tab[i][j]==tab[i-1][j-1]%10
										|| tab[i][j]==tab[i+1][j-2]%10 || tab[i][j]==tab[i+1][j-1]%10)
								{
									if (tab[i][j]!=9)
										tab[i][j]++;

									else if (j==0)
									{
										tab[i][j]=0;
										j=7;
										i--;
										impossible=true;
									}
									else if (j==1)
									{
										tab[i][j]=0;
										j=8;
										i--;
										impossible=true;
									}
									else
									{
										tab[i][j]=0;
										j=j-2;
										impossible=true;
									}
								}
							}							
						}			

						if (i%3==2 && impossible==false) // Si on est dans la 3e ligne de la r�gion
						{
							if (j%3==0)
								if (tab[i][j]==tab[i-2][j+1]%10 || tab[i][j]==tab[i-2][j+2]%10
										|| tab[i][j]==tab[i-1][j+1]%10 || tab[i][j]==tab[i-1][j+2]%10)
								{
									if (tab[i][j]!=9)
										tab[i][j]++;

									else if (j==0)
									{
										tab[i][j]=0;
										j=7;
										i--;
										impossible=true;
									}
									else if (j==1)
									{
										tab[i][j]=0;
										j=8;
										i--;
										impossible=true;
									}
									else
									{
										tab[i][j]=0;
										j=j-2;
										impossible=true;
									}
								}						

							if (j%3==1 && impossible==false)
							{
								if (tab[i][j]==tab[i-2][j-1]%10 || tab[i][j]==tab[i-2][j+1]%10
										|| tab[i][j]==tab[i-1][j-1]%10 || tab[i][j]==tab[i-1][j+1]%10)
								{
									if (tab[i][j]!=9)
										tab[i][j]++;

									else if (j==0)
									{
										tab[i][j]=0;
										j=7;
										i--;
										impossible=true;
									}
									else if (j==1)
									{
										tab[i][j]=0;
										j=8;
										i--;
										impossible=true;
									}
									else
									{
										tab[i][j]=0;
										j=j-2;
										impossible=true;
									}
								}
							}

							if (j%3==2 && impossible==false)
							{
								if (tab[i][j]==tab[i-2][j-2]%10 || tab[i][j]==tab[i-2][j-1]%10
										|| tab[i][j]==tab[i-1][j-2]%10 || tab[i][j]==tab[i-1][j-1]%10)
								{
									if (tab[i][j]!=9)
										tab[i][j]++;

									else if (j==0)
									{
										tab[i][j]=0;
										j=7;
										i--;
										impossible=true;
									}
									else if (j==1)
									{
										tab[i][j]=0;
										j=8;
										i--;
										impossible=true;
									}
									else
									{
										tab[i][j]=0;
										j=j-2;
										impossible=true;
									}
								}
							}							
						}
					} // Fin de les tests sur la r�gion
				}					
			}			
		} // On referme la double boucle sur la grille, le soduku est ici r�solu
	} // Fin du coeur du programme

	public  void afficheImpossible()
	{
		System.out.println("Grille originale :");

		for (int i=0; i<9; i++)
		{
			for (int j=0; j<9; j++)
			{
				if (tab[i][j] != 0)
					System.out.print(tab[i][j]%10);
				else
					System.out.print(".");

				if (j==2 || j==5)
					System.out.print(" | ");
				else
					System.out.print("  ");

			}

			System.out.println();

			if (i==2 || i==5)
				System.out.println("-  -  - + -  -  - + -  -  -");
		}
	}

	public void afficheResolu()
	{
		System.out.println("Grille originale :              Grille r�solue :");
		for (int i=0; i<9; i++)
		{
			for (int j=0; j<9; j++)
			{
				if (tab[i][j] > 10)
					System.out.print(tab[i][j]%10);
				else
					System.out.print(".");

				if (j==2 || j==5)
					System.out.print(" | ");
				else
					System.out.print("  ");		
			}

			System.out.print("|| ");

			for (int j=0; j<9; j++)
			{
				System.out.print(tab[i][j]%10);

				if (j==2 || j==5)
					System.out.print(" | ");
				else
					System.out.print("  ");		
			}

			System.out.println();

			if (i==2 || i==5)
			{
				System.out.print("-  -  - + -  -  - + -  -  -  ||");
				System.out.println(" -  -  - + -  -  - + -  -  -");
			}
		}
	}

}
