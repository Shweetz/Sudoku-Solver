import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.File;
import java.io.RandomAccessFile;

@SuppressWarnings("serial")
public class Afficher2 extends JFrame implements ActionListener, KeyListener {
	
	File fileChosen = null;
	Container c;
	JPanel panel, panel2, panel3, panel4, panelGeneral;
	JPanel[][] jp = new JPanel[3][3];
	JPanel jp2;
	JPanel[][] jp3 = new JPanel[1][3];
	JButton cases[][] = new JButton[12][9];
	JTextArea ihmMessage;
	int lastPadNumberClicked;
	String clicked = "other";
	int curI = -1;
	int curJ = -1;
	
	public Afficher2(int largeur, int hauteur)	{
	//on cree la fenetre
		super ("Sudoku");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(largeur, hauteur);
		
		creerMenus();        		
		creerInterface();
		
		setVisible(true);		//show();
	}
	
	private void creerMenus() 
	{
		// Menu Fichier
        JMenu menuFichier = new JMenu("Fichier");
        menuFichier.setMnemonic(KeyEvent.VK_F);
        
        ActionListener a1 = new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	for(int i=0;i<9;i++)
    			{
    				for(int j=0;j<9;j++)
    				{
    					cases[i][j].setText("");
    					cases[i][j].setBackground(null);
    				}
    			}
            	fileChosen = null;
            }
        };
        ajouterItem("Nouveau", menuFichier, a1, KeyEvent.VK_N); // Nouveau reset la grille
        
        ActionListener a2 = new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	chargerFichier();
            }
        };
        ajouterItem("Charger", menuFichier, a2, KeyEvent.VK_L);
        
        ActionListener a3 = new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	enregistrerFichier();
            }
        };
        ajouterItem("Enregistrer", menuFichier, a3, KeyEvent.VK_S);
        
        ActionListener a4 = new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	enregistrerSousFichier();
            }
        };
        ajouterItem("Enregistrer sous", menuFichier, a4);

        // Menu Aide
        JMenu menuAide = new JMenu("Aide");
        menuAide.setMnemonic(KeyEvent.VK_A);
        
        ActionListener a10 = new ActionListener(){
                public void actionPerformed(ActionEvent e){
                	afficherGuide();
                }
        };
        ajouterItem("Guide du programme TODO", menuAide, a10);

        // Menu Personnalisation
        JMenu menuPersonnalisation = new JMenu("Personnalisation");
        menuAide.setMnemonic(KeyEvent.VK_P);
        
        ActionListener a20 = new ActionListener(){
                public void actionPerformed(ActionEvent e){
                	//faireAction();
                }
        };
        ajouterItem("Empêcher entrée valeur aberrante TODO", menuPersonnalisation, a20);
        
        ActionListener a21 = new ActionListener(){
                public void actionPerformed(ActionEvent e){
                	//faireAction();
                }
        };
        ajouterItem("Personnaliser couleurs TODO", menuPersonnalisation, a21);
        
        // Relier les menus à la fenêtre
		JMenuBar barreDeMenu = new JMenuBar();
		barreDeMenu.add(menuFichier);
		barreDeMenu.add(menuAide);
		barreDeMenu.add(menuPersonnalisation);
		this.setJMenuBar(barreDeMenu);
	}
	
	private void ajouterItem(String intitule, JMenu menu, ActionListener a)
	{
        JMenuItem item = new JMenuItem(intitule);
        menu.add(item);
        item.addActionListener(a);
	}
	
	private void ajouterItem(String intitule, JMenu menu, ActionListener a, int key)
	{
        JMenuItem item = new JMenuItem(intitule);
        menu.add(item);
        item.addActionListener(a);
        item.setAccelerator(KeyStroke.getKeyStroke(key, ActionEvent.CTRL_MASK));
	}
	
	private void chargerFichier()
	{
		// Utiliser jnlp si possible : FileOpenService fos;
		
		// Dossier d'ouverture de l'explorateur
    	JFileChooser dialogue = new JFileChooser(new File("./Testing Sudokus")); 
    	
    	if (dialogue.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
    		fileChosen = dialogue.getSelectedFile();
    		ihmMessage.setText(fileChosen.toString());
    		
    		reporterFichierDansGrille(fileChosen); // Charger IHM
    	}	
    	
    	// On enlève les colorations de cases de la grille
    	for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				cases[i][j].setBackground(null);
			}
		}
	}		
	
	private void enregistrerFichier()
	{
		if (fileChosen != null)
		{
			reporterGrilleDansFichier(fileChosen.toString());
			ihmMessage.setText("Fichier " + fileChosen.getName() + " sauvegardé !");
		}
		else
			enregistrerSousFichier();
	}
	
	private void enregistrerSousFichier()
	{
		// Utiliser jnlp si possible : FileOpenService fos;
		
		// Dossier d'ouverture de l'explorateur
    	JFileChooser dialogue = new JFileChooser(new File("./Testing Sudokus")); 
    	dialogue.setSelectedFile(new File("exemple.txt"));
    	
    	if (dialogue.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
    		fileChosen = dialogue.getSelectedFile();
    		reporterGrilleDansFichier(fileChosen.toString()); 		
			ihmMessage.setText("Fichier " + fileChosen.getName() + " sauvegardé !");
    	}
	}
	
	private void afficherGuide()
	{
		// Faire popup et noter toutes les commandes, exemple :
		
		/*ihmMessage.setText("Commandes :\n"
		+ "- Entrer un chiffre : le sélectionner dans le pad en bas puis cliquer sur une case pour y coller le chiffre\n"
		+ "OU cliquer sur une case puis sur un chiffre sur votre clavier\n"
		+ "- Reset une case : la sélectionner et appuyer sur 0\n"
		+ "- Reset la grille : appuyer sur Ctrl + N\n"
		+ "NB : Utiliser les flèches pour se déplacer dans la grille");*/
	}
	
	private void creerInterface()
	{
		c = getContentPane();

		panelGeneral = new JPanel();
		panelGeneral.setLayout(new GridLayout(0,2));
		panel = new JPanel();
		panel.setLayout(new GridLayout(0,3));
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(0,1));
		panel3 = new JPanel();
		panel4 = new JPanel();
		panel4.setLayout(new GridLayout(0,3));		
		
		panel2.add(panel3);
		panel2.add(panel4);
		panelGeneral.add(panel);
		panelGeneral.add(panel2);
		
		// On crée les régions
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				jp[i][j] = new JPanel();
				jp[i][j].setLayout(new GridLayout(3,3));
				jp[i][j].setBorder(BorderFactory.createEtchedBorder());
			}
		}	
		
		// On crée les cases
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				cases[i][j] = new JButton("");
				cases[i][j].setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 25));
				cases[i][j].setSize(10,10);
				
				jp[(int)(i/3)][(int)(j/3)].add(cases[i][j]);
				
				panel.add(jp[(int)(i/3)][(int)(j/3)]);
				
				cases[i][j].addActionListener(this);
				cases[i][j].addKeyListener(this);
			}
		}

		// On crée la zone de texte
		jp2 = new JPanel();
		jp2.setBorder(BorderFactory.createEtchedBorder());
		
		ihmMessage = new JTextArea(9,36);
		
		ihmMessage.setText("Pour commencer :\n"
				+ "- Soit charger un fichier (cliquer sur le menu Fichier)\n"
				+ "- Soit entrer un chiffre : selon préférence, soit cliquer d'abord la case à remplir soit cliquer le chiffre à inscrire (dans le pad à droite)\n"
				+ "- Pour plus d'informations, cliquer sur le menu Aide");
		
		ihmMessage.setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 18));
		ihmMessage.setEditable(false);
		ihmMessage.setLineWrap(true);
		ihmMessage.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(ihmMessage);
		panel3.add(jp2.add(scrollPane));
		
		ihmMessage.addKeyListener(this);

		// On crée les zones en-dessous du texte
		jp3[0][0] = new JPanel();
		jp3[0][0].setBorder(BorderFactory.createEtchedBorder());
		jp3[0][1] = new JPanel();
		jp3[0][1].setLayout(new GridLayout(3,3));
		jp3[0][1].setBorder(BorderFactory.createEtchedBorder());
		jp3[0][2] = new JPanel();
		jp3[0][2].setBorder(BorderFactory.createEtchedBorder());

		// En-dessous du texte, le bouton "afficher la solution"
		cases[9][0] = new JButton("<html><center>Afficher la solution</center></html>");
		cases[9][0].setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 20));
		jp3[0][0].add(cases[9][0]);
		panel4.add(jp3[0][0].add(cases[9][0]));
		cases[9][0].addActionListener(this);
		cases[9][0].addKeyListener(this);
		
		// En-dessous du texte, les chiffres pour remplir la grille
		cases[9][3] = new JButton("1");
		cases[9][3].setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 25));
		cases[9][4] = new JButton("2");
		cases[9][4].setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 25));
		cases[9][5] = new JButton("3");
		cases[9][5].setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 25));
		cases[10][3] = new JButton("4");
		cases[10][3].setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 25));
		cases[10][4] = new JButton("5");
		cases[10][4].setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 25));
		cases[10][5] = new JButton("6");
		cases[10][5].setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 25));
		cases[11][3] = new JButton("7");
		cases[11][3].setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 25));
		cases[11][4] = new JButton("8");
		cases[11][4].setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 25));
		cases[11][5] = new JButton("9");
		cases[11][5].setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 25));
		
		for(int i=9;i<12;i++)
		{
			for(int j=3;j<6;j++)
			{
				jp3[0][(int)(j/3)].add(cases[i][j]);
		
				panel4.add(jp3[0][(int)(j/3)]);
		
				cases[i][j].addActionListener(this);
				cases[i][j].addKeyListener(this);		
			}
		}

		// En-dessous du texte, le bouton "trouver le prochain chiffre"
		cases[9][6] = new JButton("<html><center>Trouver le prochain chiffre<center></html>");
		cases[9][6].setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 20));
		jp3[0][2].add(cases[9][6]);
		panel4.add(jp3[0][2].add(cases[9][6]));
		cases[9][6].addActionListener(this);
		cases[9][6].addKeyListener(this);
		
		c.add(panelGeneral);	
	}
	
	public void actionPerformed (ActionEvent e) {
		
		// On cherche ce qui a été cliqué
		clicked = "other";
		curI = -1;
		curJ = -1;
		
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++) 
			{
				if(e.getSource() == cases[i][j])
				{
					clicked = "grid";
					curI = i;
					curJ = j;
				}
			}
		}
		for(int i=9;i<12;i++)
		{
			for(int j=3;j<6;j++)
			{	
				if(e.getSource() == cases[i][j])
				{
					clicked = "pad";
					curI = i;
					curJ = j;
				}
			}
		}
		
		// Action du clic
		String curNumber = "";
		int curPos = -1;
		
		for(int i=0;i<9;i++) // Action sur la grille
		{
			for(int j=0;j<9;j++)
			{
				cases[i][j].setBackground(null);	
				
				if(i == curI && j == curJ)
				{
					String str_out = "- Appuyer sur un chiffre de votre clavier pour l'inscrire\n"
							 + "- Appuyer sur 0 pour vider la case\n"
							 + "- Menu Fichier pour vider la grille, charger ou sauvegarder\n"
							 + "- Naviguer avec les flèches\n";
					
					if (lastPadNumberClicked != 0) {
						cases[i][j].setText(Integer.toString(lastPadNumberClicked));
						str_out += "- Cliquer sur une case pour y inscrire le chiffre sur fond bleu";
					}
					
					curNumber = cases[i][j].getText();
					curPos = i*9 + j;
					
					ihmMessage.setText(str_out);
				}
			}
		}
		
		for(int i=9;i<12;i++) // Action sur le pad
		{
			for(int j=3;j<6;j++)
			{	
				if (!clicked.equals("grid")) 
					cases[i][j].setBackground(null);
				
				if(i == curI && j == curJ)
				{
					curNumber = cases[i][j].getText();
					curPos = 82;
					
					cases[i][j].setBackground(Color.cyan);
					lastPadNumberClicked = Integer.parseInt(cases[i][j].getText());
					
					ihmMessage.setText("- Cliquer sur une case pour inscrire ce chiffre dedans\n"
									 + "- Menu Fichier pour vider la grille, charger ou sauvegarder\n");
				}
			}
		}
		
		if(e.getSource() == cases[9][0]) // Afficher la solution
		{
			String fileName = reporterGrilleDansFichier("customGame.txt");
			String hasSolution = Utils.PossedeSolution(fileName);
			
			// Vérifier que le sudoku a au moins une solution
			if (hasSolution.equals(""))
			{
				Main3 game = Main3.mainFromAfficher(fileName, 81); // Lancer calculs
				
				// Afficher la grille finie
				for (int i = 0; i < 81; i++)
				{
					cases[i/9][i%9].setBackground(null);
					if (game.tab[i][10] != 0)
						cases[i/9][i%9].setText(Integer.toString(game.tab[i][10]));
				}
	
				// Afficher le message de fin (difficulté du sudoku)
				ihmMessage.setText(game.str_ihm_out);
			}
			else
			{
				// Afficher pourquoi le sudoku est impossible
				ihmMessage.setText(hasSolution);
			}
		}
		
		if(e.getSource() == cases[9][6]) // Trouver le prochain chiffre
		{
			String fileName = reporterGrilleDansFichier("customGame.txt");
			String hasSolution = Utils.PossedeSolution(fileName);
			
			// Vérifier que le sudoku a au moins une solution
			if (hasSolution.equals(""))
			{
				Main3 game = Main3.mainFromAfficher(fileName, 1);
				
				// Afficher la grille avec le prochain chiffre				
				for (int i = 0; i < 81; i++)
				{
					cases[i/9][i%9].setBackground(null);
					
					if (game.tab[i][10] != 0) // Affiche le chiffre dans la case s'il est trouvé
					{
						cases[i/9][i%9].setText(Integer.toString(game.tab[i][10]));
						
						if (game.tab[i][0] == 3) // Si c'est le dernier chiffre trouvé, case verte
						{
							curNumber = cases[i/9][i%9].getText();
							curPos = i;
						}
					}
				}
	
				// Afficher le message
				ihmMessage.setText(game.str_ihm_out);
			}
			else
			{
				// Afficher pourquoi le sudoku est impossible
				ihmMessage.setText(hasSolution);
			}
		}
		
		ColorGrid(curNumber, curPos);
	}
	
	public void ColorGrid(String curNumber, int curPos)
	{	
		for (int i = 0; i < 81; i++)
		{
			// On met le background des cases par défaut, puis on modifie si besoin	
			cases[i/9][i%9].setBackground(null);

			// Jaune pour les emplacements du même chiffre que celui sélectionné
			if (curNumber != "" && cases[i/9][i%9].getText().equals(curNumber))
				cases[i/9][i%9].setBackground(Color.yellow);

			// Rouge pour les valeurs 2x dans une même ligne/colonne/region
			reporterGrilleDansFichier("customGame.txt");
			if (Utils.ValeurEntreeCoherente("customGame.txt", i/9, i%9) == false)
				cases[i/9][i%9].setBackground(Color.red);
		}

		// Vert pour la case courante
		if (curPos >= 0 && curPos <= 81)
			cases[curPos/9][curPos%9].setBackground(Color.green);
	}
	
	public void reporterFichierDansGrille(File fileChosen) { // Charge le fichier dans la grille IHM

		try {
			RandomAccessFile raf = new RandomAccessFile(fileChosen, "r");
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
						cases[i][j].setText(Character.toString(c));
					else
						cases[i][j].setText("");
				}
			}
			raf.close();
			
			ihmMessage.setText("Fichier " + fileChosen.getName() + " chargé !");
		}
		catch (Exception e) {
			System.out.print(fileChosen + " not found: " + e.toString() + "\n");
			ihmMessage.setText(fileChosen + " not found: " + e.toString() + "\n");
		}
	}
	
	public String reporterGrilleDansFichier(String fileName) { // Sauvegarde la grille IHM en fichier

		try {
			
			RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
			
			for(int i=0;i<9;i++)
			{
				for(int j=0;j<9;j++)
				{
					if (cases[i][j].getText() == "")
						raf.writeBytes(".");
					else
						raf.writeBytes(cases[i][j].getText());
				}
				if (i != 8)
					raf.writeBytes("\n");
			}
			raf.close();
		}
		catch (Exception e) {
			System.out.println(fileName + " not found: " + e.toString());
		}
		
		return fileName;
	}
	
	public void keyPressed(KeyEvent evt) { // Options clavier

		/*// R reset la grille (retiré au profit de Ctrl + N)
		if (evt.getKeyCode() == KeyEvent.VK_R) 
		{
			for(int i=0;i<9;i++)
			{
				for(int j=0;j<9;j++)
				{
					cases[i][j].setText("");						
				}
			}
		}
		// T reset la case (retiré au profit de 0)
		if (evt.getKeyCode() == KeyEvent.VK_T) 
		{
			if (clicked.equals("grid"))
				cases[curI][curJ].setText("");						
		}*/

		// Les flèches permettent de se déplacer dans la grille
		if (evt.getKeyCode() == KeyEvent.VK_LEFT) 
		{	
			if (clicked.equals("grid"))
				prevCell();
		}
		if (evt.getKeyCode() == KeyEvent.VK_KP_LEFT) 
		{
			if (clicked.equals("grid"))
				prevCell();
		}
		if (evt.getKeyCode() == KeyEvent.VK_RIGHT) 
		{
			if (clicked.equals("grid"))
				nextCell();
		}
		if (evt.getKeyCode() == KeyEvent.VK_KP_RIGHT) 
		{
			if (clicked.equals("grid"))
				nextCell();
		}
		if (evt.getKeyCode() == KeyEvent.VK_UP) 
		{
			if (clicked.equals("grid") && curI > 0)
				curI--;		
			
			if (curI != 9 || (curJ != 0 && curJ != 6))
				ColorGrid(cases[curI][curJ].getText(), curI*9 + curJ);
		}
		if (evt.getKeyCode() == KeyEvent.VK_KP_UP) 
		{
			if (clicked.equals("grid") && curI > 0)
				curI--;				
			
			if (curI != 9 || (curJ != 0 && curJ != 6))
				ColorGrid(cases[curI][curJ].getText(), curI*9 + curJ);
		}
		if (evt.getKeyCode() == KeyEvent.VK_DOWN) 
		{
			if (clicked.equals("grid") && curI < 8)	
				curI++;				

			if (curI != 9 || (curJ != 0 && curJ != 6))
				ColorGrid(cases[curI][curJ].getText(), curI*9 + curJ);
		}
		if (evt.getKeyCode() == KeyEvent.VK_KP_DOWN) 
		{
			if (clicked.equals("grid") && curI < 8)
				curI++;	

			if (curI != 9 || (curJ != 0 && curJ != 6))
				ColorGrid(cases[curI][curJ].getText(), curI*9 + curJ);
		}
		
		// 0 reset la case
		if (evt.getKeyCode() == KeyEvent.VK_0) 
		{
			if (clicked.equals("grid"))
				cases[curI][curJ].setText("");			
			nextCell();										
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD0) 
		{
			if (clicked.equals("grid"))
				cases[curI][curJ].setText("");			
			nextCell();									
		}
		
		// Entrer un chiffre dans une case et passer à la suivante
		if (evt.getKeyCode() == KeyEvent.VK_1 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("1");		
			nextCell();
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD1 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("1");		
			nextCell();			
		}
		if (evt.getKeyCode() == KeyEvent.VK_2 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("2");		
			nextCell();					
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD2 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("2");		
			nextCell();						
		}
		if (evt.getKeyCode() == KeyEvent.VK_3 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("3");		
			nextCell();							
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD3 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("3");		
			nextCell();					
		}
		if (evt.getKeyCode() == KeyEvent.VK_4 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("4");		
			nextCell();						
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD4 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("4");		
			nextCell();						
		}
		if (evt.getKeyCode() == KeyEvent.VK_5 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("5");		
			nextCell();						
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD5 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("5");		
			nextCell();						
		}
		if (evt.getKeyCode() == KeyEvent.VK_6 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("6");		
			nextCell();							
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD6 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("6");		
			nextCell();					
		}
		if (evt.getKeyCode() == KeyEvent.VK_7 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("7");		
			nextCell();						
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD7 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("7");		
			nextCell();					
		}
		if (evt.getKeyCode() == KeyEvent.VK_8 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("8");		
			nextCell();						
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD8 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("8");		
			nextCell();					
		}
		if (evt.getKeyCode() == KeyEvent.VK_9 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("9");		
			nextCell();							
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD9 && clicked.equals("grid")) 
		{
			cases[curI][curJ].setText("9");		
			nextCell();					
		}
	}
	
	public void nextCell() {
		
		if (clicked.equals("grid") && (curI < 8 || curJ < 8))
		{
			cases[curI][curJ].setBackground(null);	
			if (curJ < 8)
			{
				curJ++;		
			}
			else
			{
				curI++;		
				curJ = 0;		
			}
		}
		
		ColorGrid(cases[curI][curJ].getText(), curI*9 + curJ);
	}

	public void prevCell() {
		
		if (clicked.equals("grid") && (curI > 0 || curJ > 0))
		{
			cases[curI][curJ].setBackground(null);
			if (curJ > 0)
			{
				curJ--;	
			}
			else
			{
				curI--;		
				curJ = 8;		
			}
		}
		
		ColorGrid(cases[curI][curJ].getText(), curI*9 + curJ);
	}
	
	public void keyReleased(KeyEvent evt) { // méthode non implémentée mais
		// necessaire pour compiler

	}
	
	public void keyTyped(KeyEvent evt) { // méthode non implémentée mais
		// necessaire pour compiler

	}
	
	/*public void init() {
	}
	
	public void start() {
	}*/
	
	public static void main(String[] args)
	{
		new Afficher2(1050, 540);
	}
}