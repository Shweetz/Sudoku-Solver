
import java.io.*;

public class Main3
{
	int[] nbOccurences = new int[10];
	boolean initialisationTerminee = false;
	int nombreChiffresRemplis = 0;
	String str_ihm_out = "";
	
	int[][] tab = new int[81][11];
	
	/* 	How to understand tab :
	 *  The row of tab is [(row of sudoku*9) + column of sudoku] like this :
	 *  
	 * 	0  1  2 | 3  4  5 | 6  7  8
		9  .  . | .  .  . | .  .  .
		18 .  . | .  .  . | .  .  .
		-  -  - + -  -  - + -  -  -
		27 .  . | .  .  . | .  .  .
		36 .  . | .  .  . | .  .  .
		45 .  . | .  .  50| .  .  .
		-  -  - + -  -  - + -  -  -
		54 .  . | .  .  . | .  .  .
		63 .  . | .  .  . | .  .  .
		72 .  . | .  .  . | .  .  80
		
		For each row, you have 11 columns. Each row is a cell in the sudoku, so each cell has 11 columns
		
		Column 0: Value 0: Don't know what is the value in this cell.
				  Value	1: Value in this cell known (not at the initialization)
				  Value 2: Value in this cell given at the initialization
				  Value 3: Last value found in the sudoku (not at the initialization)
				  
		Column 1 to 9: Value 0: The value [1 to 9] is not possible in this cell
					   Value 1: The value [1 to 9] is a possibility in this cell
					   
		Column 10: Value 0: The value in this cell in not known yet
				   Value 1 to 9: The value in this cell is known, this column holds the final value
	 */
	
	void InitialisationGrille(int[][] tab)
	{
		for (int i=0; i<81; i++) 
		{
			tab[i][0]=0; // 0 means don't know the value at this spot
			tab[i][10]=0; // same
			
			for (int j=1; j<10; j++)
			{
				tab[i][j]=1; // 1 means at the spot i [0 to 80], the j [1 to 9] value is possible
			}
		}
		
		for (int i=0; i<10; i++) 
		{
			nbOccurences[i]=0; // At the start, there are no '1', no '2'... in the sudoku
		}		
	}
	
	void EntrerValeur(int i, int j, int valeur)
	{		
		int l = 0;
		
		if (tab[(i-1)*9 + j-1][0]==0)
		{
			nbOccurences[valeur]++;		
			nombreChiffresRemplis++;
		}
		else
			System.out.println("!!! Vous avez rentre au moins 2 fois une valeur dans la meme case !!!");
		
		while (tab[l][0]!=3 && l < 80) // On cherche la ligne du tableau possédant '3' dans la 1e colonne du tableau
		{
			l++;
		}
		if (tab[l][0]==3) // Et on la repasse à 1 (3 est le signe que c'est le dernière valeur trouvée)
			tab[l][0]=1;
		
		if (initialisationTerminee == false)
			tab[(i-1)*9 + j-1][0]=2;
		else
			tab[(i-1)*9 + j-1][0]=3; // On différencie la dernière valeur trouvée
			
		tab[(i-1)*9 + j-1][10]=valeur;
		
		// On va maintenant déduire des informations de cette nouvelle valeur
		
		for (int k=1; k<10; k++) // Une case est remplie par une unique valeur
		{
			if (k!=valeur)
			{
				tab[(i-1)*9 + j-1][k]=0;
			}
		}
		 
		for (int ligne=0; ligne<9; ligne++) // Une valeur apparaît une fois par ligne, on supprime les autres possibilités
		{
			if (ligne!=i-1)
			{
				tab[ligne*9 + j-1][valeur]=0;
			}
		}
		
		for (int colonne=0; colonne<9; colonne++) // Une valeur apparaît une fois par colonne
		{
			if (colonne!=j-1)
			{
				tab[(i-1)*9 + colonne][valeur]=0;
			}
		}
		
		while (i%3 != 1) // On cherche la 1e case de la région où on se trouve 
			i--;
		while (j%3 != 1)
			j--;
		
		for (int h = (i-1)*9 + j-1; h < (i-1)*9 + j+20; h++) // Une fois par région
		{
			if (tab[h][10]!=valeur)
				tab[h][valeur]=0;
			if (h%3==2)
				h+=6;
		}
		
	}
	
	void Afficher(int[][] tab)
	{
		String str_out = "";
		for (int i=0; i<81; i++)
		{	
			if (i%9!=8)
			{
				if (tab[i][0]>0)
					str_out += tab[i][10];
				else
					str_out += ".";
				
				if (i%3==2 && i%9!=0)
				{
					if (tab[i][0]!=3 && tab[i+1][0]!=3)
						str_out += " | ";
					else if  (tab[i][0]==3)
						str_out += "|| ";
					else
						str_out += " ||";						
				}
				else
				{
					if (tab[i][0]!=3 && tab[i+1][0]!=3)
						str_out += "  ";
					else if  (tab[i][0]==3)
						str_out += "| ";
					else
						str_out += " |";
				}
			}
			else
			{
				if (tab[i][0]>0)
					str_out += tab[i][10] + "\n";
				else
					str_out += ".\n";
				
				if (i%27==26 && i!=80)
					str_out += "-  -  - + -  -  - + -  -  -\n";
			}	
		}
		System.out.println(str_out); 
	}
	
	boolean TrouverChiffreSuivant()
	{
		int compteur;
		
		for (int valeur=1; valeur<10; valeur++) // On va faire tous les tests valeur par valeur
		{
			for (int ligne=0; ligne<9; ligne+=3) // On complète une région si possible
			{
				for (int colonne=0; colonne<9; colonne+=3)
				{
					compteur = 0;
					for (int ligneRegion=0; ligneRegion<3; ligneRegion++)
					{
						for (int colonneRegion=0; colonneRegion<3; colonneRegion++)
						{
							if (tab[(ligne+ligneRegion)*9 + (colonne+colonneRegion)][valeur]==1)
								compteur++;
							
							if (tab[(ligne+ligneRegion)*9 + (colonne+colonneRegion)][10]==valeur) // condition éliminatoire
							{
								colonneRegion=3;
								ligneRegion=3;
							}
						
							if (compteur==1 && ligneRegion==2 && colonneRegion==2) // 1 possibilité dans la région
							{
								CompleterRegion(ligne*9 + colonne, valeur);
								return true;
							}
						}
					}
				}
			}
			
			for (int ligne=0; ligne<9; ligne++) // Sinon on complète une ligne si possible
			{
				compteur = 0;
				for (int colonne=0; colonne<9; colonne++)
				{
					if (tab[(ligne*9) + colonne][valeur]==1)
						compteur++;

					if (tab[(ligne*9) + colonne][10]==valeur)
						colonne=9;
					
					if (compteur==1 && colonne==8)
					{
						CompleterLigne(ligne*9, valeur);
						return true;
					}
				}
			}
			
			for (int colonne=0; colonne<9; colonne++) // Si aucun des 2, on complète une colonne si possible
			{
				compteur = 0;
				for (int ligne=0; ligne<9; ligne++)
				{
					if (tab[(ligne*9) + colonne][valeur]==1)
						compteur++;
					
					if (tab[(ligne*9) + colonne][10]==valeur)
						ligne=9;
					
					if (compteur==1 && ligne==8)
					{
						CompleterColonne(colonne, valeur);
						return true;
					}
				}
			}
			
			for (int ligne=0; ligne<9; ligne++) // Si rien d'autre ne marche, on remplit une case si possible
			{
				for (int colonne=0; colonne<9; colonne++)
				{
					compteur = 0;
					if (tab[(ligne*9) + colonne][0]==0)
					{
						
						if (tab[(ligne*9) + colonne][valeur]==1)
						{
							compteur++;
							for (int v=1; v<10; v++)
							{
								if (tab[(ligne*9) + colonne][v]==1 && v!=valeur)
								{
									compteur++;
									v=10;
								}
							}
						}
						
						if (compteur==1)
						{
							CompleterCase(ligne, colonne, valeur);
							return true;
						}
					}
				}
			}
		}
		return false;		
	}
	
	void CompleterRegion(int i, int valeur)
	{
		String str_out;
		int j = i; // i est la position de la valeur en haut à gauche dans la région à remplir
			// donc i est divisible par 3 et ne se trouve qu'entre 0 et 9, 27 et 36, 54 et 63.
		
		while (tab[j][valeur]==0) // On trouve la position de la valeur à remplir 
		{
			if (j%3==2)
				j+=6;
			j++;
		}

		str_out = "Le chiffre " + valeur + " de la région ";
		if (i/9 < 3)
			str_out += "en haut ";
		if (i/9 > 2 && i/9 < 6 && (i%9 < 3 || i%9 > 5))
			str_out += "au milieu ";
		if (i/9 > 5)
			str_out += "en bas ";
		if (i%9 < 3)
			str_out += "à gauche ";
		if (i%9 > 2 && i%9 < 6)
			str_out += "au milieu ";
		if (i%9 > 5)
			str_out += "à droite ";
		str_out += "n'a qu'une possibilité.";
		System.out.println(str_out);
		str_ihm_out += str_out;
		EntrerValeur(j/9 + 1, j%9 + 1, valeur);
	}
	
	void CompleterLigne(int i, int valeur) 
	{
		String str_out;
		int j = i; // i est le numéro de la ligne à compléter
		
		while (tab[j][valeur]==0) // On trouve la position (ligne, colonne) de la valeur
			j++;
		
		str_out = "Le chiffre " + valeur + " de la " + (i/9 + 1);
		str_out += "e ligne n'a qu'une possibilité.";
		System.out.println(str_out);
		str_ihm_out += str_out;
		EntrerValeur(i/9 + 1, j%9 + 1, valeur);
	}

	void CompleterColonne(int i, int valeur)
	{
		String str_out;
		int j = i; // i est le numéro de la colonne à compléter
		
		while (tab[j][valeur]==0) // On trouve la position (ligne, colonne) de la valeur
			j+=9;

		str_out = "Le chiffre " + valeur + " de la " + (i + 1) ;
		str_out += "e colonne n'a qu'une possibilité.";
		System.out.println(str_out);
		str_ihm_out += str_out;
		EntrerValeur(j/9 + 1, i + 1, valeur);
	}

	void CompleterCase(int ligne, int colonne, int valeur)
	{
		String str_out;
		str_out = "La valeur " + valeur;
		str_out += " est la seule possible ligne " + (ligne+1);
		str_out += ", colonne " + (colonne+1) + ".";
		System.out.println(str_out);
		str_ihm_out += str_out;
		EntrerValeur(ligne+1, colonne+1, valeur);
	}

	boolean Algo2()
	{		
		String str_out;
		int compteur;
		int i;
		boolean RechercheFructueuse = false;
		
		for (int valeur=1; valeur<10; valeur++) 
		{ 
			for (int ligne=0; ligne<9; ligne++) // Algorithme de niveau 2 horizontal
			{
				compteur=0;
				i=ligne;
				
				for (int colonne=0; colonne<9; colonne+=3) // Horizontal sur la région à partir de la ligne
				{
					if (tab[ligne*9 + colonne][valeur]==0 && tab[ligne*9 + colonne+1][valeur]==0 
														  && tab[ligne*9 + colonne+2][valeur]==0)
						compteur += colonne+1;
					
					if (tab[ligne*9 + colonne][10]==valeur || tab[ligne*9 + colonne+1][10]==valeur 
							  							   || tab[ligne*9 + colonne+2][10]==valeur)
						colonne = 7;
					
					if (compteur%3==2 && colonne==6)
					{
						while (i%3!=0)
							i--;
							
						for (int j=i; j<i+3; j++)
						{
							if (j!=ligne) // 11 - compteur car (c+1)*2
							{
								if (tab[j*9 + 11 - compteur][valeur]!=0
								 || tab[j*9 + 12 - compteur][valeur]!=0
								 || tab[j*9 + 13 - compteur][valeur]!=0)
								{
									RechercheFructueuse = true;
									tab[j*9 + 11 - compteur][valeur]=0;
									tab[j*9 + 12 - compteur][valeur]=0;
									tab[j*9 + 13 - compteur][valeur]=0;	
								}								
							}									
						}
						if (RechercheFructueuse)
						{	
							str_out = "<b>Niveau 2 :</b>";
							str_out += " le chiffre " + valeur;
							str_out += " de la ligne " + (ligne+1);
							str_out += " est dans la région ";
							if (i==0)
								str_out += "en haut ";
							if (i==3 && compteur!=8)
								str_out += "au milieu ";
							if (i==6)
								str_out += "en bas ";
							if (compteur==11)
								str_out += "à gauche, ";
							if (compteur==8)
								str_out += "au milieu, ";
							if (compteur==5)
								str_out += "à droite, ";
							str_out += "donc pas de " + valeur;
							str_out += " dans le reste de la région.\n";
							System.out.print(str_out);
							str_ihm_out += str_out;
							return RechercheFructueuse;
						}
					}
				}
			}
			
			for (int ligne=0; ligne<9; ligne+=3) // Algorithme de niveau 2 horizontal
			{				
				for (int colonne=0; colonne<9; colonne+=3)  // Horizontal sur la ligne à partir de la région
				{
					compteur=0;
					for (int ligneRegion=0; ligneRegion<3; ligneRegion++)
					{						
						if (tab[(ligne+ligneRegion)*9 + colonne][valeur]==0 
								&& tab[(ligne+ligneRegion)*9 + colonne+1][valeur]==0 
								&& tab[(ligne+ligneRegion)*9 + colonne+2][valeur]==0)
							compteur += ligneRegion+5;
						
						if (tab[(ligne+ligneRegion)*9 + colonne][10]==valeur 
								|| tab[(ligne+ligneRegion)*9 + colonne+1][10]==valeur 
								|| tab[(ligne+ligneRegion)*9 + colonne+2][10]==valeur)
							ligneRegion = 3;
						
						if (compteur > 10 && compteur < 14)
						{
							for (int j=0; j<3; j++)
							{
								if (j!=colonne/3)
								{
									if (tab[(ligne + 13 - compteur)*9 + j*3][valeur]!=0
										|| tab[(ligne + 13 - compteur)*9 + j*3 + 1][valeur]!=0
										|| tab[(ligne + 13 - compteur)*9 + j*3 + 2][valeur]!=0)
									{
										RechercheFructueuse = true;
										tab[(ligne + 13 - compteur)*9 + j*3][valeur]=0;
										tab[(ligne + 13 - compteur)*9 + j*3 + 1][valeur]=0;
										tab[(ligne + 13 - compteur)*9 + j*3 + 2][valeur]=0;	
									}	
								}
							}
									
							if (RechercheFructueuse)
							{	
								str_out = "<b>Niveau 2 :</b>";
								str_out += " le chiffre " + valeur;
								str_out += " de la région ";
								if (ligne==0)
									str_out += "en haut ";
								if (ligne==3 && colonne!=3)
									str_out += "au milieu ";
								if (ligne==6)
									str_out += "en bas ";
								if (colonne==0)
									str_out += "à gauche ";
								if (colonne==3)
									str_out += "au milieu ";
								if (colonne==6)
									str_out += "à droite ";
								str_out += "est dans la ligne " + (ligne + 14 - compteur);
								str_out += " donc pas de " + valeur;
								str_out += " dans le reste de la ligne.\n";
								System.out.print(str_out);
								str_ihm_out += str_out;
								return RechercheFructueuse;
							}
						}
					}
				}
			}
			
			for (int colonne=0; colonne<9; colonne++) // Algorithme de niveau 2 vertical
			{
				compteur=0;
				i=colonne;
				
				for (int ligne=0; ligne<9; ligne+=3) // Vertical sur la région à partir de la colonne
				{
					//compteurAffichage=0;
					
					if (tab[ligne*9 + colonne][valeur]==0 && tab[(ligne+1)*9 + colonne][valeur]==0 
														  && tab[(ligne+2)*9 + colonne][valeur]==0)
						compteur += ligne+1;
					
					if (tab[ligne*9 + colonne][10]==valeur || tab[(ligne+1)*9 + colonne][10]==valeur 
							  							   || tab[(ligne+2)*9 + colonne][10]==valeur)
						ligne = 7;
					
					if (compteur%3==2 && ligne==6)
					{
						while (i%3!=0)
							i--;
							
						for (int j=i; j<i+3; j++)
						{							
							if (j!=colonne)
							{
								if (tab[(11-compteur)*9 + j][valeur]!=0
								 || tab[(12-compteur)*9 + j][valeur]!=0
								 || tab[(13-compteur)*9 + j][valeur]!=0)
								{
									RechercheFructueuse = true;
									tab[(11-compteur)*9 + j][valeur]=0;
									tab[(12-compteur)*9 + j][valeur]=0;
									tab[(13-compteur)*9 + j][valeur]=0;
								}
							}									 
						}
						if (RechercheFructueuse)
						{
							str_out = "<b>Niveau 2 :</b>";
							str_out += " le chiffre " + valeur;
							str_out += " de la colonne " + (colonne+1);
							str_out += " est dans la région ";
							if (compteur==11)
								str_out += "en haut ";
							if (compteur==8 && i!=3)
								str_out += "au milieu ";
							if (compteur==5)
								str_out += "en bas ";
							if (i==0)
								str_out += "à gauche, ";
							if (i==3)
								str_out += "au milieu, ";
							if (i==6)
								str_out += "à droite, ";
							str_out += "donc pas de " + valeur;
							str_out += " dans le reste de la région.\n";
							System.out.print(str_out);
							str_ihm_out += str_out;
							return RechercheFructueuse;
						}
					}
				}
			}
			
			for (int ligne=0; ligne<9; ligne+=3) // Algorithme de niveau 2 vertical
			{				
				for (int colonne=0; colonne<9; colonne+=3)  // Vertical sur la colonne à partir de la région
				{
					compteur=0;
					for (int colonneRegion=0; colonneRegion<3; colonneRegion++)
					{						
						if (tab[(ligne)*9 + colonne+colonneRegion][valeur]==0 
								&& tab[(ligne+1)*9 + colonne+colonneRegion][valeur]==0 
								&& tab[(ligne+2)*9 + colonne+colonneRegion][valeur]==0)
							compteur += colonneRegion+5;
						
						if (tab[(ligne)*9 + colonne+colonneRegion][10]==valeur 
								|| tab[(ligne+1)*9 + colonne+colonneRegion][10]==valeur 
								|| tab[(ligne+2)*9 + colonne+colonneRegion][10]==valeur)
							colonneRegion = 3;
						
						if (compteur > 10 && compteur < 14)
						{
							for (int j=0; j<3; j++)
							{
								if (j!=ligne/3)
								{
									if (tab[(j*3)*9 + colonne + 13 - compteur][valeur]!=0
										|| tab[(j*3 + 1)*9 + colonne + 13 - compteur][valeur]!=0
										|| tab[(j*3 + 2)*9 + colonne + 13 - compteur][valeur]!=0)
									{
										RechercheFructueuse = true;
										tab[(j*3)*9 + colonne + 13 - compteur][valeur]=0;
										tab[(j*3 + 1)*9 + colonne + 13 - compteur][valeur]=0;
										tab[(j*3 + 2)*9 + colonne + 13 - compteur][valeur]=0;	
									}	
								}
							}
									
							if (RechercheFructueuse)
							{	
								str_out = "<b>Niveau 2 :</b>";
								str_out += " le chiffre " + valeur;
								str_out += " de la région ";
								if (ligne==0)
									str_out += "en haut ";
								if (ligne==3 && colonne!=3)
									str_out += "au milieu ";
								if (ligne==6)
									str_out += "en bas ";
								if (colonne==0)
									str_out += "à gauche ";
								if (colonne==3)
									str_out += "au milieu ";
								if (colonne==6)
									str_out += "à droite ";
								str_out += "est dans la colonne " + (colonne + 14 - compteur);
								str_out += " donc pas de " + valeur;
								str_out += " dans le reste de la colonne.\n";
								System.out.print(str_out);
								str_ihm_out += str_out;
								return RechercheFructueuse;
							}
						}
					}
				}
			}
		}
		return RechercheFructueuse;
	}
	
	boolean GroupesIsolesMelanges()
	{		
		String str_out;
		boolean RechercheFructueuse = false;
		int compteur;
		int i;
		int n;
		int d;
		int temp[] = new int[10];
		int compteurColonnesAffichees = 0; // On peut initialiser ici car on return dès qu'on l'utilise
		int nombreCasesATester;
		int casesPossibles;
		
		for (int ligne=0; ligne<9; ligne++) // Algorithme sur les lignes
		{
			for (int colonne=0; colonne<9; colonne++) // Trouve le nombre de chiffres impossibles par case
			{
				i=ligne*9 + colonne;
				compteur = 0 ; 
				temp[colonne] = 0;
				if (tab[i][0]==0) 
				{
					for (int v=1; v<10; v++)
					{
						if (tab[i][v]==0)
							compteur++; // Compte le nombre de chiffres impossibles par case
					}
					
					if (compteur > 1 && compteur < 8)  // Test si la case a entre 2 et 7 impossibilités
						temp[colonne] = 9-compteur; // temp stocke le nombre de valeurs possibles
													// et donc le nombre de cases à tester
				}
			}
			
			for (int colonne=0; colonne<9; colonne++) // On regarde si les mêmes n valeurs impossibles 
													  // sont dans 9-n cases
			{
				i=ligne*9 + colonne;
				nombreCasesATester = temp[colonne]; // Nombre de valeurs possibles
				casesPossibles = 1;
				
				if (nombreCasesATester > 1 && nombreCasesATester < 8) // Si la case est sujette à tests
				{
					n=1;
					for (int v=1; v<10; v++) // On écrit dans temp[colonne] les valeurs impossibles
					{
						if  (tab[i][v] == 0)
						{
							temp[colonne]+=n*v; 
							n*=10;
						}
					}
					
					temp[colonne]-=nombreCasesATester; // temp est un nombre contenant 
													   // (nombreCasesATester) chiffres
				
					for (int j=0; j<9; j++) // On regarde si les n valeurs impossibles sont dans 
											// 8-n autres cases
					{
						i = ligne*9 + j;
						
						if (j!=colonne && tab[i][0]==0) // On teste case par case les non remplies
						{
							for (int m=0; m < 9-nombreCasesATester; m++) 
								// m sélectionne le chiffre de temp[colonne]
							{
								if (tab[i][(temp[colonne]/((int)Math.pow(10,m))) % 10] != 0)
									m=10; // Si une valeur impossible d'une case est possible dans l'autre
								if (m==8-nombreCasesATester)
								{
									casesPossibles++;
									temp[j]=colonne+10; // On retient les cases vérifiant l'algorithme
										// +10 différencie les temp[j] vérifiant l'algorithme
										// des temp[j] stockant le nombre de valeurs possibles.
										// C'est 2 tableaux en 1, moins de place mémoire
										// mais beaucoup plus de bordel !															
								}
							}
						}
					}
					
					if (casesPossibles > nombreCasesATester)
					{
						str_out = "Sudoku impossible, n valeurs doivent être dans n-1 cases !\n";
						System.out.print(str_out);
						str_ihm_out += str_out;
					}
					
					else if (casesPossibles == nombreCasesATester)
					{
						for (int j=0; j < 9; j++) // On enlève les possibilités
						{
							i = ligne*9 + j;
							
							if (temp[j] != colonne+10 && j!=colonne)
							{
								d=1;
								
								for (int m=1; m < 10; m++) // On enlève les possibilités case par case
								{
									if ((temp[colonne]/((int)Math.pow(10,m-d))) % 10!=m)
									{
										d++;
										if (tab[i][m]!=0)
										{
											RechercheFructueuse = true;	
											tab[i][m]=0;
										}
									}
								}	
							}
						}
						
						if (RechercheFructueuse) // On affiche les infos
						{
							d=1;
							str_out = "Niveau 3 :";
							str_out += " dans la ligne " + (ligne+1) + ", les seules valeurs possibles";
							str_out += " dans les colonnes ";
							
							for (int j=0; j < 9; j++)
							{
								if (temp[j] == colonne+10 || j==colonne)
								{
									if (compteurColonnesAffichees < nombreCasesATester-1)
									{
										str_out += (j+1) + ", ";
										compteurColonnesAffichees++;
									}
									else
										str_out += (j+1);
								}		
							}
							
							str_out += " sont ";
							
							for (int m=1; m < 10; m++) 
							{
								if ((temp[colonne]/((int)Math.pow(10,m-d))) % 10!=m)
								{
									d++;
									if (d!=nombreCasesATester+1)
										str_out += m + ", ";
									else
										str_out += m + ", ";
								}
							}
							str_out += "donc ces valeurs ne sont pas dans le reste de la ligne.\n"; 
							System.out.print(str_out);
							str_ihm_out += str_out;
							return RechercheFructueuse;
						}
					}
				}
			}
		}
		
		for (int j=0; j<10; j++)
			temp[j]=0;

		for (int colonne=0; colonne<9; colonne++) // Algorithme sur les colonnes
		{
			for (int ligne=0; ligne<9; ligne++) // Trouve le nombre de chiffres impossibles par case
			{
				i=ligne*9 + colonne;
				compteur = 0 ; 
				temp[ligne] = 0;
				if (tab[i][0]==0) 
				{
					for (int v=1; v<10; v++)
					{
						if (tab[i][v]==0)
							compteur++; // Compte le nombre de chiffres impossibles par case
					}
					
					if (compteur > 1 && compteur < 8)  // Test si la case a entre 2 et 7 impossibilités
						temp[ligne] = 9-compteur; // temp stocke le nombre de valeurs possibles
													// et donc le nombre de cases à tester
				}
			}
			
			for (int ligne=0; ligne<9; ligne++) // On regarde si les mêmes n valeurs impossibles 
													  // sont dans 9-n cases
			{
				i=ligne*9 + colonne;
				nombreCasesATester = temp[ligne]; // Nombre de valeurs possibles
				casesPossibles = 1;
				
				if (nombreCasesATester > 1 && nombreCasesATester < 8) // Si la case est sujette à tests
				{
					n=1;
					for (int v=1; v<10; v++) // On écrit dans temp[colonne] les valeurs impossibles
					{
						if  (tab[i][v] == 0)
						{
							temp[ligne]+=n*v; 
							n*=10;
						}
					}
					
					temp[ligne]-=nombreCasesATester; // temp est un nombre contenant 
													   // (nombreCasesATester) chiffres
				
					for (int j=0; j<9; j++) // On regarde si les n valeurs impossibles sont dans 
											// 8-n autres cases
					{
						i = j*9 + colonne;
						
						if (j!=ligne && tab[i][0]==0) // On teste case par case les non remplies
						{
							for (int m=0; m < 9-nombreCasesATester; m++) 
								// m sélectionne le chiffre de temp[colonne]
							{
								if (tab[i][(temp[ligne]/((int)Math.pow(10,m))) % 10] != 0)
									m=10; // Si une valeur impossible d'une case est possible dans l'autre
								if (m==8-nombreCasesATester)
								{
									casesPossibles++;
									temp[j]=ligne+10; // On retient les cases vérifiant l'algorithme
										// +10 différencie les temp[j] vérifiant l'algorithme
										// des temp[j] stockant le nombre de valeurs possibles.
										// C'est 2 tableaux en 1, moins de place mémoire
										// mais beaucoup plus de bordel !															
								}
							}
						}
					}
					
					if (casesPossibles > nombreCasesATester)
					{
						str_out = "Sudoku impossible, n valeurs doivent être dans n-1 cases !\n";
						System.out.print(str_out);
						str_ihm_out += str_out;
					}
					
					else if (casesPossibles == nombreCasesATester)
					{
						for (int j=0; j < 9; j++) // On enlève les possibilités
						{
							i = j*9 + colonne;
							
							if (temp[j] != ligne+10 && j!=ligne)
							{
								d=1;
								
								for (int m=1; m < 10; m++) // On enlève les possibilités case par case
								{
									if ((temp[ligne]/((int)Math.pow(10,m-d))) % 10!=m)
									{
										d++;
										if (tab[i][m]!=0)
										{
											RechercheFructueuse = true;	
											tab[i][m]=0;
										}
									}
								}	
							}
						}
						
						if (RechercheFructueuse) // On affiche les infos
						{
							d=1;
							str_out = "Niveau 3 :";
							str_out += " dans la colonne " + (colonne+1) + ", les seules valeurs possibles";
							str_out += " dans les lignes ";
							
							for (int j=0; j < 9; j++)
							{
								if (temp[j] == ligne+10 || j==ligne)
								{
									if (compteurColonnesAffichees < nombreCasesATester-1)
									{
										str_out += (j+1) + ", ";
										compteurColonnesAffichees++;
									}
									else
										str_out += (j+1);

								}		
							}
							
							str_out += " sont ";
							
							for (int m=1; m < 10; m++) 
							{
								if ((temp[ligne]/((int)Math.pow(10,m-d))) % 10!=m)
								{
									d++;
									if (d!=nombreCasesATester+1)
										str_out += m + ", ";
									else
										str_out += m + ", ";
								}
							}
							str_out += "donc ces valeurs ne sont pas dans le reste de la colonne.\n";
							System.out.print(str_out);
							str_ihm_out += str_out;
							return RechercheFructueuse;
						}
					}
				}
			}
		}
		
		for (int j=0; j<10; j++)
			temp[j]=0;
		
		for (int ligne=0; ligne<9; ligne+=3) // Algorithme sur les régions
		{
			for (int colonne=0; colonne<9; colonne+=3) // Trouve le nombre de chiffres impossibles par case
			{
				for (int ligneRegion=0; ligneRegion<3; ligneRegion++) // Algorithme sur les régions
				{
					for (int colonneRegion=0; colonneRegion<3; colonneRegion++) // Trouve le nombre de chiffres impossibles par case
					{
						i=(ligne+ligneRegion)*9 + (colonne+colonneRegion);
						compteur = 0 ; 
						temp[ligneRegion*3 + colonneRegion] = 0;
						if (tab[i][0]==0) 
						{
							for (int v=1; v<10; v++)
							{
								if (tab[i][v]==0)
									compteur++; // Compte le nombre de chiffres impossibles par case
							}
							
							if (compteur > 1 && compteur < 8)  // Test si la case a entre 2 et 7 impossibilités
								temp[ligneRegion*3 + colonneRegion] = 9-compteur; // temp stocke le nombre de valeurs possibles
															// et donc le nombre de cases à tester
						}
					}
					
					for (int colonneRegion=0; colonneRegion<3; colonneRegion++) // On regarde si les mêmes n valeurs impossibles 
															  // sont dans 9-n cases
					{
						i=(ligne+ligneRegion)*9 + (colonne+colonneRegion);
						nombreCasesATester = temp[ligneRegion*3 + colonneRegion]; // Nombre de valeurs possibles
						casesPossibles = 1;
						
						if (nombreCasesATester > 1 && nombreCasesATester < 8) // Si la case est sujette à tests
						{
							n=1;
							for (int v=1; v<10; v++) // On écrit dans temp[colonne] les valeurs impossibles
							{
								if  (tab[i][v] == 0)
								{
									temp[ligneRegion*3 + colonneRegion]+=n*v; 
									n*=10;
								}
							}
							
							temp[ligneRegion*3 + colonneRegion]-=nombreCasesATester; // temp est un nombre contenant 
															   // (nombreCasesATester) chiffres
						
							for (int j=0; j<9; j++) // On regarde si les n valeurs impossibles sont dans 
													// 8-n autres cases
							{
								i = ligne*9 + colonne + (j/3)*9 + j%3; //
								
								if (j!=ligneRegion*3 + colonneRegion && tab[i][0]==0) // On teste case par case les non remplies
								{
									for (int m=0; m < 9-nombreCasesATester; m++) 
										// m sélectionne le chiffre de temp[colonne]
									{
										if (tab[i][(temp[ligneRegion*3 + colonneRegion]/((int)Math.pow(10,m))) % 10] != 0)
											m=10; // Si une valeur impossible d'une case est possible dans l'autre
										if (m==8-nombreCasesATester)
										{
											casesPossibles++;
											temp[j]=ligneRegion*3 + colonneRegion+10; // On retient les cases vérifiant l'algorithme
												// +10 différencie les temp[j] vérifiant l'algorithme
												// des temp[j] stockant le nombre de valeurs possibles.
												// C'est 2 tableaux en 1, moins de place mémoire
												// mais beaucoup plus de bordel !															
										}
									}
								}
							}
							
							if (casesPossibles > nombreCasesATester)
							{
								str_out = "Sudoku impossible, n valeurs doivent être dans n-1 cases !\n";
								System.out.print(str_out);
								str_ihm_out += str_out;
							}
							
							else if (casesPossibles == nombreCasesATester)
							{
								for (int j=0; j < 9; j++) // On enlève les possibilités
								{
									i = ligne*9 + colonne + (j/3)*9 + j%3;
									
									if (temp[j] != ligneRegion*3 + colonneRegion+10 && j!=ligneRegion*3 + colonneRegion)
									{
										d=1;
										
										for (int m=1; m < 10; m++) // Case par case
										{
											if ((temp[ligneRegion*3 + colonneRegion]/((int)Math.pow(10,m-d))) % 10!=m)
											{
												d++;
												if (tab[i][m]!=0)
												{
													RechercheFructueuse = true;	
													tab[i][m]=0;
												}
											}
										}	
									}
								}
								
								if (RechercheFructueuse) // On affiche les infos
								{
									d=1;
									str_out = "Niveau 3 :";
									str_out += " dans la région ";
									if (ligne==0)
										str_out += "en haut ";
									if (ligne==3 && colonne!=3)
										str_out += "au milieu ";
									if (ligne==6)
										str_out += "en bas ";
									if (colonne==0)
										str_out += "à gauche, ";
									if (colonne==3)
										str_out += "au milieu, ";
									if (colonne==6)
										str_out += "à droite, ";
									str_out += "les seules valeurs possibles dans les cases ";
									
									for (int j=0; j < 9; j++)
									{
										if (temp[j] == ligneRegion*3 + colonneRegion+10 || j==ligneRegion*3 + colonneRegion)
										{
											if (compteurColonnesAffichees < nombreCasesATester-1)
											{
												str_out += (j+1) + ", ";
												compteurColonnesAffichees++;
											}
											else
												str_out += (j+1);

										}		
									}
									
									str_out += " sont ";
									
									for (int m=1; m < 10; m++) 
									{
										if ((temp[ligneRegion*3 + colonneRegion]/((int)Math.pow(10,m-d))) % 10!=m)
										{
											d++;
											if (d!=nombreCasesATester+1)
												str_out += m + ", ";
											else
												str_out += m + ", ";
										}
									}
									str_out += "donc ces valeurs ne sont pas dans le reste de la région.\n";
									System.out.print(str_out);
									str_ihm_out += str_out;
									return RechercheFructueuse;
								}
							}
						}
					}
				}
			}
		}
		return RechercheFructueuse;

	}
	
	boolean RegleExclusion()
	{		
		String str_out;
		boolean RechercheFructueuse = false;
		boolean valeurPresente;
		boolean ligneOkay;
		int lignesOkay[] = new int[7];
		int colonnesOkay[] = new int[7];
		int colonnesImpossibles;
		int nombreNecessaire;
		int compteur;
		int n;
		int d;
		int t;
		int u;
		
		for (int valeur=1; valeur<10; valeur++)
		{
			for (int ligne=0; ligne<9; ligne++) // Algorithme sur les lignes
			{
				valeurPresente = false;
				for (int valeurNonDansLigne = 0; valeurNonDansLigne < 9; valeurNonDansLigne++)
				{
					if (tab[ligne*9 + valeurNonDansLigne][10]==valeur)
					{
						valeurPresente = true;
					}
				}
				
				if (valeurPresente == false)
				{
					colonnesImpossibles = 0; 
					nombreNecessaire = 0; 
					lignesOkay[0] = ligne;
					t=1;
					u=0;
					n=1;
					
					for (int colonne=0; colonne<9; colonne++) 
					{
						if (tab[ligne*9 + colonne][valeur] == 0) 
						{
							colonnesImpossibles+=n*(colonne+1); 
							n*=10;
							nombreNecessaire++; 
						}	
					}
					
					int saveColonnesImpossibles = colonnesImpossibles;
					
					if (nombreNecessaire > 1 && nombreNecessaire < 8)
					{
						for (int ligne2=0; ligne2<9; ligne2++)
						{
							if (ligne2 != ligne)
							{
								compteur = 1; 
								colonnesImpossibles = saveColonnesImpossibles;
								
								while (colonnesImpossibles!=0 && tab[ligne2*9 + (colonnesImpossibles%10-1)][valeur]!=1)
								{
									colonnesImpossibles/=10;
									if (colonnesImpossibles%10==0)
									{
										compteur++;
										lignesOkay[t]= ligne2;
										t++;
										if (compteur==9-nombreNecessaire)
										{
											colonnesImpossibles = saveColonnesImpossibles;
											d=1;
											
											for (int m=1; m < 10; m++) 
											{
												if ((colonnesImpossibles/((int)Math.pow(10,m-d))) % 10!=m)
												{
													d++;
													colonnesOkay[u] = m;
													u++;
												}
											}
											
											for (int j=0; j<9; j++) // On enlève les possibilités
											{
												d=1;
												ligneOkay = false;

												for (int l=0; l<9-nombreNecessaire; l++)
												{
													if (j==lignesOkay[l])
													{
														ligneOkay = true;
													}
												}	
												
												if (!ligneOkay)
												{
													u=0;
													while (u<9-nombreNecessaire) // On enlève les possibilités case par case
													{
														d++;
														if (tab[j*9 + colonnesOkay[u]-1][valeur]!=0)
														{
															RechercheFructueuse = true;	
															tab[j*9 + colonnesOkay[u]-1][valeur]=0;														
														}
														u++;
													}
												}
											}

											if (RechercheFructueuse) // On affiche les infos
											{
												str_out = "Niveau 4 :";
												str_out += " dans les lignes ";
												for (int i=0; i<9-nombreNecessaire; i++)
													str_out += lignesOkay[i]+1 + ", ";	
												
												str_out += "la valeur " + valeur;
												str_out += " n'est possible que dans les colonnes ";
												
												for (int i=0; i<9-nombreNecessaire; i++)
													str_out += colonnesOkay[i] + ", ";	
												
												str_out += "donc pas de " + valeur + " dans le reste ";
												str_out += "des " + (9-nombreNecessaire) + " colonnes.\n";
												System.out.print(str_out);
												str_ihm_out += str_out;
												
												return RechercheFructueuse;
											}									
										}
									}
								}
							}		
						}
					}
				}
			}		
			
			for (int colonne=0; colonne<9; colonne++) // Algorithme sur les colonnes
			{
				valeurPresente = false;
				for (int valeurNonDansColonne = 0; valeurNonDansColonne < 9; valeurNonDansColonne++)
				{
					if (tab[valeurNonDansColonne*9 + colonne][10]==valeur)
					{
						valeurPresente = true;
					}
				}
				
				if (valeurPresente == false)
				{
					colonnesImpossibles = 0; 
					nombreNecessaire = 0; 
					lignesOkay[0] = colonne;
					t=1;
					u=0;
					n=1;
					
					for (int ligne=0; ligne<9; ligne++) 
					{
						if (tab[ligne*9 + colonne][valeur] == 0) 
						{
							colonnesImpossibles+=n*(ligne+1); 
							n*=10;
							nombreNecessaire++; 
						}	
					}
					
					int saveColonnesImpossibles = colonnesImpossibles;
					
					if (nombreNecessaire > 1 && nombreNecessaire < 8)
					{
						for (int colonne2=0; colonne2<9; colonne2++)
						{
							if (colonne2 != colonne)
							{
								compteur = 1; 
								colonnesImpossibles = saveColonnesImpossibles;
								
								while (colonnesImpossibles!=0 && tab[(colonnesImpossibles%10-1)*9 + colonne2][valeur]!=1)
								{
									colonnesImpossibles/=10;
									if (colonnesImpossibles%10==0)
									{
										compteur++;
										lignesOkay[t]= colonne2;
										t++;
										if (compteur==9-nombreNecessaire)
										{
											colonnesImpossibles = saveColonnesImpossibles;
											d=1;
											
											for (int m=1; m < 10; m++) // On teste si les lignes 
											// impossibles de la colonne actuelle le sont aussi sur la colonne testée
											{
												if ((colonnesImpossibles/((int)Math.pow(10,m-d))) % 10!=m)
												{
													d++;
													colonnesOkay[u] = m;
													u++;
												}
											}
											
											for (int j=0; j<9; j++) // On enlève les possibilités
											{
												d=1;
												ligneOkay = false;

												for (int l=0; l<9-nombreNecessaire; l++)
												{
													if (j==lignesOkay[l])
													{
														ligneOkay = true;
													}
												}	
												
												if (!ligneOkay)
												{
													u=0;
													while (u<9-nombreNecessaire) // On enlève les possibilités case par case
													{
														d++;
														if (tab[(colonnesOkay[u]-1)*9 + j][valeur]!=0)
														{
															RechercheFructueuse = true;	
															tab[(colonnesOkay[u]-1)*9 + j][valeur]=0;														
														}
														u++;
													}
												}
											}

											if (RechercheFructueuse) // On affiche les infos
											{
												str_out = "Niveau 4 :";
												str_out += " dans les colonnes ";
												for (int i=0; i<9-nombreNecessaire; i++)
													str_out += lignesOkay[i]+1 + ", ";	
												
												str_out += "la valeur " + valeur;
												str_out += " n'est possible que dans les lignes ";
												
												for (int i=0; i<9-nombreNecessaire; i++)
													str_out += colonnesOkay[i] + ", ";	
												
												str_out += "donc pas de " + valeur + " dans le reste ";
												str_out += "des " + (9-nombreNecessaire) + " lignes.\n";
												System.out.print(str_out);
												str_ihm_out += str_out;
												
												return RechercheFructueuse;
											}									
										}
									}
								}
							}		
						}
					}
				}
			}		
		}
		return RechercheFructueuse;
	}
		
	void solve(String fileName, int nombreChiffresATrouver) // 81 : trouver prochain, 1 : solution
	{
		// nombreChiffresATrouver est le nombre de chiffres suivants à trouver 
		// (écrire au moins 81 pour finir le sudoku)
		String str_out;
		int nombre = 0;
		int difficulté = 1;
		
		InitialisationGrille(tab);
		
		// Fill game table 
		try {
			RandomAccessFile raf = new RandomAccessFile(fileName, "r");
			String ligne;
			
			for (int i = 0; (ligne = raf.readLine()) != null; i++) {
				for (int j = 0; j < ligne.length(); ++j)
				{
					char c = ligne.charAt(j);
					if (c != '.')
						EntrerValeur(i+1, j+1 , c - '0'); // c format char->int
				}
			}
			raf.close();
		}
		catch (Exception e) {
			str_out = fileName + " not found: " + e.toString() + "\n";
			System.out.print(str_out);
			str_ihm_out = str_out;
		}
		
		initialisationTerminee = true;
		
		System.out.println("Grille originale : ");
		
		Afficher(tab);
		
		while (nombre < nombreChiffresATrouver && nombreChiffresRemplis != 81) 
		{
			if (TrouverChiffreSuivant()==false)
			{
				if (difficulté==1)
					difficulté=2;
				
				if (Algo2()==false)
				{
					if (difficulté==2)
						difficulté=3;
					
					if (GroupesIsolesMelanges()==false)
					{
						difficulté=4;
						
						if (RegleExclusion()==false)
						{
							
							str_out = "Chiffre suivant non trouvé : ";
							if (nombreChiffresRemplis < 70)
								str_out += "Sudoku trop difficile pour ce programme (pour l'instant) !";
							else
								str_out += "Sudoku non résolvable, vérifier les chiffres entrés !";
							System.out.println(str_out);
							str_ihm_out += str_out;
							nombre = nombreChiffresATrouver;
						
						}
					}
				}
				
				if (TrouverChiffreSuivant()==true)
				{
					Afficher(tab);
					nombre++;
				}
			}
			else
			{
				Afficher(tab);
				nombre++;
			}
			
			if (nombreChiffresRemplis==81)
			{
				nombre = nombreChiffresATrouver;
				str_out = "\nSudoku résolu ! Difficulté : " + difficulté + "/4 ";
				if (difficulté==1)
					str_out += "(facile).";
				if (difficulté==2)
					str_out += "(moyen).";
				if (difficulté==3)
					str_out += "(assez difficile).";
				if (difficulté==4)
					str_out += "(difficile).";
				System.out.println(str_out);
				str_ihm_out += str_out;
			}
		}
	}
	
	public static Main3 mainFromAfficher(String fichier, int number) 
	{
		//String fichier = "C:\\Users\\Romain\\workspace\\Sudoku Solver\\game.txt";
		
		Main3 game = new Main3();
		
		game.solve(fichier, number); // 1 : trouver prochain, 81 : solution
		
		return game;
	}
}
