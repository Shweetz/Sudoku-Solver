import java.io.RandomAccessFile;

	
public class Utils
{
	int[][] tab = new int[9][9];
	
	String str_ihm_out = "";

	void InitialisationGrille (String fileName)	{
		for (int i=0; i<9; i++) 
		{
			for (int j=0; j<9; j++)
			{
				tab[i][j]=0;
			}
		}
		
		reporterFichierDansGrille(fileName);
	}

	void reporterFichierDansGrille(String fileName) { // Charge le fichier dans la grille IHM
		String str;
		
		try {
			RandomAccessFile raf = new RandomAccessFile(fileName, "r");
			String ligne;
			
			for (int i = 0; (ligne = raf.readLine()) != null; i++) 
			{
				if (i >= 9) break;
				for (int j = 0; j < ligne.length(); ++j)
				{
					if (j >= 9) break;
					char c = ligne.charAt(j);
					// On ne remplit pas si le char n'est pas un chiffre entre 1 et 9
					if (c - '0' > 0 && c - '0' < 10)
						EntrerValeur(i+1, j+1 , c - '0'); // c format char->int
				}
			}
			raf.close();
		}
		catch (Exception e) {
			str = fileName + " not found: " + e.toString() + "\n";
			System.out.print(str);
			str_ihm_out = str;
		}
	}
	
	void EntrerValeur(int i, int j, int valeur)
	{
		String str;
		
		if (0<i && i<10 && 0<j && j<10 && 0<valeur && valeur<10)
		{
			tab[i-1][j-1]=valeur+10; // Le +10 est pour différencier les valeurs fixes
		}							 // des valeurs que l'on va tester
		else
		{
			afficheImpossible();
			str = "Erreur : Il y a une valeur non comprise entre 1 et 9 ou "
					+ "non située dans la grille 9x9.\n"
					+ "Elle a été entrée après les valeurs renseignées "
					+ "dans la grille précédente.\n";
			System.out.print(str);
			str_ihm_out += str;
			return;
		}								 
	}
	
	boolean ValeursEntreesCoherentes () 
	{
		// On vérifie qu'il n'y a pas deux fois une même valeur entrée sur une seule ligne			
		for (int i=0; i<9; i++) 
		{
			for (int j=0; j<9; j++)
			{
				for (int k=j+1; k<9; k++) 
				{
					if (tab[i][j]==tab[i][k] && tab[i][j]!=0) 
					{
						afficheImpossible();
						String str = "Sudoku impossible, la valeur " + tab[i][j]%10 + " est "
								+ "au moins est deux fois dans la " + (i+1) + "e ligne !\n";
						System.out.print(str);
						str_ihm_out += str;
						return false;
					}
				}
			}
		}
		
		// On vérifie qu'il n'y a pas deux fois une même valeur entrée sur une seule colonne			
		for (int i=0; i<9; i++) 
		{
			for (int j=0; j<9; j++)
			{
				for (int k=j+1; k<9; k++) 
				{
					if (tab[j][i]==tab[k][i] && tab[j][i]!=0) 
					{
						afficheImpossible();
						String str = "Sudoku impossible, la valeur " + tab[j][i]%10 + " est "
								+ "au moins est deux fois dans la " + (i+1) + "e colonne !\n";
						System.out.print(str);
						str_ihm_out += str;
						return false;
					}
				}
			}
		}
		
		// On vérifie qu'il n'y a pas deux fois une même valeur entrée dans une seule région			
		for (int i=0; i<9; i++) 
		{
			for (int j=0; j<9; j++)
			{
				// La case mère est la case en haut à gauche de la région de la case testée
				int iMere = (i/3)*3;
				int jMere = (j/3)*3;
				
				for (int iRegion = 0; iRegion < 3; iRegion++)
				{
					for (int jRegion = 0; jRegion < 3; jRegion++)
					{
						if (tab[iMere+iRegion][jMere+jRegion] == tab[i][j] && tab[i][j] != 0
								&& (iMere+iRegion != i || jMere+jRegion != j))
						{
							afficheImpossible();
							String str = "Sudoku impossible, la valeur " + tab[i][j]%10 
									+ " (ligne " + (i+1) + ", colonne " + (j+1) 
									+ ") est au moins deux fois dans sa région !\n";
							System.out.print(str);
							str_ihm_out += str;
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	
	void ResolutionSudoku() // Le coeur du programme
	{
		boolean impossible=false;
		int valeurAvantTests;
		
		for (int i=0; i<9; i++) 
		{
			for (int j=0; j<9; j++) // Double boucle pour repérer la case dans le tableau
			{
				if (impossible==false) 	   // Si la valeur de la case précédente était possible
				{				       
					while (tab[i][j] > 10 && i*j!=64) // Tant que la case actuelle est fixe,
													  // on passe à la case suivante
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
					
				else // Si la valeur de la case précédente ne convient pas
				{
					while (tab[i][j] > 8 && i+j!=0) // Si aucune valeur ne convient ou si elle 
									// est fixe, on va changer la case précédente à celle-ci
					{
						if (tab[i][j]==9)
						{
							tab[i][j]=0; // tout en réinitialisant la valeur si non fixe
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
						tab[i][j]++; // Si la valeur ne convenant pas de la case testée se trouve 
					}			 	 // entre 1 et 8, on teste avec la valeur suivante 
					
					if (tab[i][j] > 8 && i==0 && j==0)
					{
						afficheImpossible();
						String str;
						str = "Sudoku impossible (pas de combinaison réalisable) : vérifier les chiffres entrés\n";
						System.out.print(str);
						str_ihm_out += str;
						return;
					}
				}
					 
				impossible=false;
				valeurAvantTests = 0;
				
				while (valeurAvantTests!=tab[i][j]) 
					// Si la valeur a changé après un des tests, il faut tous les refaire
				{
					valeurAvantTests=tab[i][j];
					
					for (int k=8; k>=0; k--) 
					{
						if (tab[i][j]==tab[i][k]%10 && k!=j) 
							// On vérifie que la valeur n'est pas dans la ligne
						{
							if (tab[i][j]<9) // On évite d'inscrire 10 dans une case
							{
								tab[i][j]++;
								k=j;
							}
							else if (j==0) // Pour ce faire, on essaie de changer la valeur de
						   		   		   // la case précédente
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
								// Si la valeur n'est pas dans la ligne, on vérifie ensuite 
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
					    // Si la valeur n'est ni dans la ligne ni dans la colonne, on vérifie 
					    // qu'elle n'est pas non plus dans la région	 						   
					{
						if (i%3==0 && impossible==false) // Si on est dans la 1e ligne de la région
						{
							if (j%3==0) // Si on est dans la 1e colonne de la région
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
						
						if (i%3==1) // Si on est dans la 2e ligne de la région
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
						
						if (i%3==2 && impossible==false) // Si on est dans la 3e ligne de la région
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
					} // Fin de les tests sur la région
				}					
			}			
		} // On referme la double boucle sur la grille, le soduku est ici résolu
	} // Fin du coeur du programme
	
	void afficheImpossible()
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
	
	void afficheResolu()
	{
		System.out.println("Grille originale :              Grille résolue :");
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

	boolean ValeurEntreeCoherente (int i, int j) 
	{
		// On vérifie qu'il n'y a pas deux fois une même valeur entrée sur une seule ligne			
		for (int k=0; k<9; k++) 
		{
			if (tab[i][j]==tab[i][k] && k!=j && tab[i][j]!=0) 
				return false;
		}
		
		// On vérifie qu'il n'y a pas deux fois une même valeur entrée sur une seule colonne			
		for (int k=0; k<9; k++) 
		{
			if (tab[i][j]==tab[k][j] && k!=i && tab[i][j]!=0)
				return false;
		}
		
		// On vérifie qu'il n'y a pas deux fois une même valeur entrée dans une seule région			
		// La case mère est la case en haut à gauche de la région de la case testée
		int iMere = (i/3)*3;
		int jMere = (j/3)*3;
		
		for (int iRegion = 0; iRegion < 3; iRegion++)
		{
			for (int jRegion = 0; jRegion < 3; jRegion++)
			{
				if (tab[iMere+iRegion][jMere+jRegion] == tab[i][j] && tab[i][j] != 0
						&& (iMere+iRegion != i || jMere+jRegion != j))
					return false;
			}
		}
		
		return true;
	}
	
	// Static methods (called from Afficher2)
	
	static String PossedeSolution(String fileName)
	{
		Utils game = new Utils();
		
		game.InitialisationGrille(fileName);
	
		if (game.ValeursEntreesCoherentes() == true) // On vérifie qu'une valeur en entrée ne se trouve pas 2 fois sur la même ligne
			game.ResolutionSudoku();
		
		return game.str_ihm_out;			
	}
	
	static boolean ValeurEntreeCoherente(String fileName, int i, int j)
	{
		Utils game = new Utils();
		
		game.InitialisationGrille(fileName);
	
		return game.ValeurEntreeCoherente(i, j);			
	}
}
