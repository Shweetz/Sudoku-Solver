	
public class Main2
{

	static int[][] tab = new int[9][9];
	
	public static void main(String[] args) 
	{
		
		InitialisationGrille(tab); // On met 0 dans toutes les cases de la grille
		
		// Déclarer ici les valeurs initiales sous la forme EntrerValeur(ligne, colonne, valeur)
		
		EntrerValeur(1,1,3);
		EntrerValeur(1,2,1);
		EntrerValeur(1,4,4);
		EntrerValeur(1,5,5);
		EntrerValeur(1,6,2);
		EntrerValeur(2,3,2);
		EntrerValeur(2,5,7);
		EntrerValeur(2,8,3);
		EntrerValeur(2,9,4);
		EntrerValeur(3,4,8);
		EntrerValeur(3,8,5);
		EntrerValeur(4,1,7);
		EntrerValeur(4,2,2);
		EntrerValeur(4,4,9);
		EntrerValeur(4,5,6);
		EntrerValeur(4,9,1);
		EntrerValeur(5,1,5);
		EntrerValeur(5,2,9);
		EntrerValeur(5,8,2);
		EntrerValeur(5,9,3);
		EntrerValeur(6,1,4);
		EntrerValeur(6,5,3);
		EntrerValeur(6,6,5);
		EntrerValeur(6,8,6);
		EntrerValeur(6,9,9);
		EntrerValeur(7,2,3);
		EntrerValeur(7,6,8);
		EntrerValeur(8,1,2);
		EntrerValeur(8,2,6);
		EntrerValeur(8,5,1);
		EntrerValeur(8,7,3);
		EntrerValeur(9,4,3);
		EntrerValeur(9,5,2);
		EntrerValeur(9,6,7);
		EntrerValeur(9,8,9);
		EntrerValeur(9,9,8);
	
		TestLigneEntree(tab); // On vérifie qu'une valeur en entrée ne se trouve pas 2 fois
							  // sur la même ligne
		
		// Cette prochaine boucle for contient toute la partie calcul pour résoudre le sudoku, 
		// elle est composée de beaucoup de lignes de code !
		
		ResolutionSudoku(tab); // Cette méthode contient tous les calculs pour résoudre le sudoku 
		
		afficheResolu(tab); // Une fois les calculs faits, on affiche le résultat
			
	} // Fin du main

	public static void InitialisationGrille (int[][] tab)	{
		for (int i=0; i<9; i++) 
		{
			for (int j=0; j<9; j++)
			{
				tab[i][j]=0;
			}
		}
	}
	
	public static void EntrerValeur(int i, int j, int valeur)
	{
		if (0<i && i<10 && 0<j && j<10 && 0<valeur && valeur<10)
		{
			tab[i-1][j-1]=valeur+10; // Le +10 est pour différencier les valeurs fixes
		}							 // des valeurs que l'on va tester
		else
		{
			afficheImpossible(tab);
			System.out.print("Erreur : Il y a une valeur non comprise entre 1 et 9 ou ");
			System.out.println("non située dans la grille 9x9.");
			System.out.print("Elle a été entrée après les valeurs renseignées ");
			System.out.println("dans la grille précédente.");
			System.exit(0);
		}
								 
	}
	
	public static void TestLigneEntree (int[][] tab) 
	{
		// On vérifie qu'il n'y a pas deux fois une valeur entrée sur une même ligne,
		// la partie calcul du code pouvant traiter tous les autres cas
			
		for (int i=0; i<9; i++) 
		{
			for (int j=0; j<9; j++)
			{
				for (int k=8; k>=0; k--) 
				{
					if (tab[i][j]==tab[i][k] && k!=j && tab[i][j]!=0) 
					{
						afficheImpossible(tab);
						System.out.print("Sudoku impossible, une valeur au moins est deux ");
						System.out.println("fois sur une même ligne !");
						System.exit(0);
					}
				}
			}
		}
	}
	
	public static void ResolutionSudoku(int[][] tab) // Le coeur du programme
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
						afficheImpossible(tab);
						System.out.print("Sudoku impossible : valeurs entrées fausses ");
						System.out.println("ou pas de combinaison réalisable.");
						System.exit(0);
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
	
	public static void afficheImpossible(int[][] tab)
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
	
	public static void afficheResolu(int[][] tab)
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
}
