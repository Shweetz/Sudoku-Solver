import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.File;
import java.io.RandomAccessFile;

class ResultToGUI {
	String text;
	int[][] tab;
}

@SuppressWarnings("serial")
public class Afficher2 extends JFrame implements ActionListener, KeyListener {
	
	Container c;
	JPanel panel, panel2, panel3, panel4, panelGeneral, panelHaut;
	JPanel[][] jp = new JPanel[3][3];
	JPanel jp2;
	JPanel[][] jp3 = new JPanel[1][3];
	JButton cases[][] = new JButton [12][9];
	JTextArea ihmMessage;
	JButton options[][] = new JButton [1][2];
	GridLayout grille, grille2;
	JMenu menuFichier;
	JMenuBar menu;
	String lastNumberClicked = "";
	boolean gridClicked = false;
	int curI = -1, curJ = -1;
	
	public Afficher2(int largeur, int hauteur)	{
	//on cree la fenetre
		super ("Sudoku");
		
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
    				}
    			}
            }
        };
        ajouterItem("Nouveau", menuFichier, a1, KeyEvent.VK_R); // Nouveau reset la grille
        
        ActionListener a2 = new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	chargerFichier();
            }
        };
        ajouterItem("Charger", menuFichier, a2, KeyEvent.VK_L);
        
        /*ActionListener a3 = new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	enregistrerFichier();
            }
        };
        ajouterItem("Enregistrer", menuFichier, a3);*/
        
        ActionListener a4 = new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	enregistrerSousFichier();
            }
        };
        ajouterItem("Enregistrer sous", menuFichier, a4, KeyEvent.VK_S);

        // Menu Autre
        JMenu menuAutre = new JMenu("Autre");
        ActionListener a10 = new ActionListener(){
                public void actionPerformed(ActionEvent e){//sauveGarder();
                	//}
                }
        };
        ajouterItem("TODO", menuAutre, a10);
        
		JMenuBar barreDeMenu = new JMenuBar();
		barreDeMenu.add(menuFichier);
		barreDeMenu.add(menuAutre);
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
    	File fichier;
    	
    	if (dialogue.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
    		fichier = dialogue.getSelectedFile();
    		
    		// On remplit l'IHM
    		try {
    			RandomAccessFile raf = new RandomAccessFile(fichier, "r");
    			String ligne;
    			
    			for (int i = 0; (ligne = raf.readLine()) != null; i++) {
    				for (int j = 0; j < ligne.length(); ++j)
    				{
    					char c = ligne.charAt(j);
    					if (c != '.')
    						cases[i][j].setText(Character.toString(c));
    					else
    						cases[i][j].setText("");
    				}
    			}
    			raf.close();
    		}
    		catch (Exception e) {
    			System.out.print(fichier + " not found: " + e.toString() + "\n");
    			ihmMessage.setText(fichier + " not found: " + e.toString() + "\n");
    		}
    	}	
	}		
	
	/*private void enregistrerFichier()
	{
		String fileName = reporterGrilleDansFichier("customGame.txt"); // IHM -> fichier
	}*/
	
	private void enregistrerSousFichier()
	{
		// Utiliser jnlp si possible : FileOpenService fos;
		
		// Dossier d'ouverture de l'explorateur
    	JFileChooser dialogue = new JFileChooser(new File("./Testing Sudokus")); 
    	
    	if (dialogue.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
    		reporterGrilleDansFichier(dialogue.getSelectedFile().toString()); // IHM -> fichier
    	}
	}
	
	private void creerInterface()
	{
		c = getContentPane();
		panel = new JPanel();
		grille = new GridLayout(0,3);
		panel.setLayout(grille);
		//panel.setSize(500, 500);
		
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(0,1));
		
		panel3 = new JPanel();
		panel3.setMaximumSize(new Dimension(500, 100));
		
		panel4 = new JPanel();
		panel4.setLayout(grille);
		
		panelGeneral = new JPanel();
		panelGeneral.setLayout(new GridLayout(0,1));
		//panelGeneral.add(panel, BorderLayout.CENTER);
		panelGeneral.add(panel);
		panelGeneral.add(panel2);
		panel2.add(panel3);
		panel2.add(panel4);
		
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
		ihmMessage.setText("Commandes :\n"
				+ "- Entrer un chiffre : le sélectionner dans le pad en bas puis cliquer sur une case pour y coller le chiffre\n"
				+ "OU cliquer sur une case puis sur un chiffre sur votre clavier\n"
				+ "- Reset une case : la sélectionner et appuyer sur T ou 0\n"
				+ "- Reset la grille : appuyer sur R\n"
				+ "NB : Utiliser les flèches pour se déplacer dans la grille");
		
		ihmMessage.setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 18));
		JScrollPane scrollPane = new JScrollPane(ihmMessage);
		ihmMessage.setEditable(false);
		ihmMessage.setLineWrap(true); // Retour ligne auto 
		ihmMessage.setWrapStyleWord(true); // Mots non coupés par retour ligne
		ihmMessage.setSize(500, 100);
		ihmMessage.setMaximumSize(new Dimension(500, 100));

		jp2.add(scrollPane);
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
		// html pour les retours ligne
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
		
		gridClicked = false;
		
		for(int i=0;i<9;i++) // grille cliquée
		{
			for(int j=0;j<9;j++)
			{
				cases[i][j].setBackground(null);	
				if(e.getSource() == cases[i][j])
				{
					cases[i][j].setText(lastNumberClicked);		
					cases[i][j].setBackground(Color.green);	
					gridClicked = true;
					curI = i;
					curJ = j;
				}
			}
		}
		
		for(int i=9;i<12;i++) // minipad cliqué
		{
			for(int j=3;j<6;j++)
			{	
				if (gridClicked == false)
					cases[i][j].setBackground(null);	
				
				if(e.getSource() == cases[i][j])
				{
					cases[i][j].setBackground(Color.cyan);
					lastNumberClicked = cases[i][j].getText();
				}
			}
		}
		
		if(e.getSource() == cases[9][0]) // Afficher la solution
		{
			String fileName = reporterGrilleDansFichier("customGame.txt"); // IHM -> fichier
			Main3 game = Main3.mainFromAfficher(fileName, 81); // Lancer calculs
			
			// Affcher la grille finie
			for (int i = 0; i < 81; i++)
			{
				cases[i/9][i%9].setBackground(null);
				if (game.tab[i][10] != 0)
					cases[i/9][i%9].setText(Integer.toString(game.tab[i][10]));
			}

			// Affcher le message de fin (difficulté du sudoku)
			ihmMessage.setText(game.str_ihm_out);
		}
		
		if(e.getSource() == cases[9][6]) // Trouver le prochain chiffre
		{
			String fileName = reporterGrilleDansFichier("customGame.txt"); // IHM -> fichier
			Main3 game = Main3.mainFromAfficher(fileName, 1); // Lancer calculs
			
			// Affcher la grille avec le prochain chiffre
			String curNumber = "";
			int curPos = 0;
			
			for (int i = 0; i < 81; i++)
			{
				cases[i/9][i%9].setBackground(null);
				
				if (game.tab[i][10] != 0) // S'il y a un chiffre dans la case
				{
					cases[i/9][i%9].setText(Integer.toString(game.tab[i][10]));
					
					if (game.tab[i][0] == 3) // Si c'est le dernier chiffre trouvé
					{
						cases[i/9][i%9].setBackground(Color.green);
						curNumber = cases[i/9][i%9].getText();
						curPos = i;
					}
				}
			}
			
			ColorIdenticalNumbers(curNumber, curPos);

			// Affcher le message
			ihmMessage.setText(game.str_ihm_out);
		}
	}
	
	public void ColorIdenticalNumbers(String curNumber, int curPos)
	{
		for (int i = 0; i < 81; i++)
		{
			if (i != curPos)
			{
				cases[i/9][i%9].setBackground(null);

				if (curNumber != "" && cases[i/9][i%9].getText().equals(curNumber))
				{
					cases[i/9][i%9].setBackground(Color.yellow);
				}
			}
		}
	}
	
	public String reporterGrilleDansFichier(String fileName) { // Traduire la grille IHM en fichier

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

		// R reset la grille (techniquement inutile après l'accélérateur)
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
		
		// T ou 0 reset la case
		if (evt.getKeyCode() == KeyEvent.VK_T) 
		{
			if (gridClicked == true)
				cases[curI][curJ].setText("");						
		}
		if (evt.getKeyCode() == KeyEvent.VK_0) 
		{
			if (gridClicked == true)
				cases[curI][curJ].setText("");			
			nextCell();										
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD0) 
		{
			if (gridClicked == true)
				cases[curI][curJ].setText("");			
			nextCell();									
		}

		// Les flèches permettent de se déplacer dans la grille
		if (evt.getKeyCode() == KeyEvent.VK_LEFT) 
		{	
			prevCell();
		}
		if (evt.getKeyCode() == KeyEvent.VK_KP_LEFT) 
		{
			prevCell();
		}
		if (evt.getKeyCode() == KeyEvent.VK_RIGHT) 
		{
			nextCell();
		}
		if (evt.getKeyCode() == KeyEvent.VK_KP_RIGHT) 
		{
			nextCell();
		}
		if (evt.getKeyCode() == KeyEvent.VK_UP) 
		{
			if (gridClicked == true && curI > 0)
			{
				cases[curI][curJ].setBackground(null);	
				cases[curI-1][curJ].setBackground(Color.green);
				curI--;				
			}
			
			ColorIdenticalNumbers(cases[curI][curJ].getText(), curI*9 + curJ);
		}
		if (evt.getKeyCode() == KeyEvent.VK_KP_UP) 
		{
			if (gridClicked == true && curI > 0)
			{
				cases[curI][curJ].setBackground(null);	
				cases[curI-1][curJ].setBackground(Color.green);
				curI--;				
			}
			
			ColorIdenticalNumbers(cases[curI][curJ].getText(), curI*9 + curJ);
		}
		if (evt.getKeyCode() == KeyEvent.VK_DOWN) 
		{
			if (gridClicked == true && curI < 8)	
			{
				cases[curI][curJ].setBackground(null);
				cases[curI+1][curJ].setBackground(Color.green);
				curI++;				
			}
			
			ColorIdenticalNumbers(cases[curI][curJ].getText(), curI*9 + curJ);
		}
		if (evt.getKeyCode() == KeyEvent.VK_KP_DOWN) 
		{
			if (gridClicked == true && curI < 8)
			{
				cases[curI][curJ].setBackground(null);	
				cases[curI+1][curJ].setBackground(Color.green);
				curI++;				
			}
			
			ColorIdenticalNumbers(cases[curI][curJ].getText(), curI*9 + curJ);
		}
		
		// Entrer un chiffre dans une case et passer à la suivante
		if (evt.getKeyCode() == KeyEvent.VK_1 && gridClicked == true) 
		{
			cases[curI][curJ].setText("1");		
			nextCell();
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD1 && gridClicked == true) 
		{
			cases[curI][curJ].setText("1");		
			nextCell();			
		}
		if (evt.getKeyCode() == KeyEvent.VK_2 && gridClicked == true) 
		{
			cases[curI][curJ].setText("2");		
			nextCell();					
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD2 && gridClicked == true) 
		{
			cases[curI][curJ].setText("2");		
			nextCell();						
		}
		if (evt.getKeyCode() == KeyEvent.VK_3 && gridClicked == true) 
		{
			cases[curI][curJ].setText("3");		
			nextCell();							
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD3 && gridClicked == true) 
		{
			cases[curI][curJ].setText("3");		
			nextCell();					
		}
		if (evt.getKeyCode() == KeyEvent.VK_4 && gridClicked == true) 
		{
			cases[curI][curJ].setText("4");		
			nextCell();						
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD4 && gridClicked == true) 
		{
			cases[curI][curJ].setText("4");		
			nextCell();						
		}
		if (evt.getKeyCode() == KeyEvent.VK_5 && gridClicked == true) 
		{
			cases[curI][curJ].setText("5");		
			nextCell();						
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD5 && gridClicked == true) 
		{
			cases[curI][curJ].setText("5");		
			nextCell();						
		}
		if (evt.getKeyCode() == KeyEvent.VK_6 && gridClicked == true) 
		{
			cases[curI][curJ].setText("6");		
			nextCell();							
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD6 && gridClicked == true) 
		{
			cases[curI][curJ].setText("6");		
			nextCell();					
		}
		if (evt.getKeyCode() == KeyEvent.VK_7 && gridClicked == true) 
		{
			cases[curI][curJ].setText("7");		
			nextCell();						
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD7 && gridClicked == true) 
		{
			cases[curI][curJ].setText("7");		
			nextCell();					
		}
		if (evt.getKeyCode() == KeyEvent.VK_8 && gridClicked == true) 
		{
			cases[curI][curJ].setText("8");		
			nextCell();						
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD8 && gridClicked == true) 
		{
			cases[curI][curJ].setText("8");		
			nextCell();					
		}
		if (evt.getKeyCode() == KeyEvent.VK_9 && gridClicked == true) 
		{
			cases[curI][curJ].setText("9");		
			nextCell();							
		}
		if (evt.getKeyCode() == KeyEvent.VK_NUMPAD9 && gridClicked == true) 
		{
			cases[curI][curJ].setText("9");		
			nextCell();					
		}
	}
	
	public void nextCell() {
		
		if (gridClicked == true && (curI < 8 || curJ < 8))
		{
			cases[curI][curJ].setBackground(null);	
			if (curJ < 8)
			{
				cases[curI][curJ+1].setBackground(Color.green);
				curJ++;		
			}
			else
			{
				cases[curI+1][0].setBackground(Color.green);
				curI++;		
				curJ = 0;		
			}
		}
		
		ColorIdenticalNumbers(cases[curI][curJ].getText(), curI*9 + curJ);
	}

	public void prevCell() {
		
		if (gridClicked == true && (curI > 0 || curJ > 0))
		{
			cases[curI][curJ].setBackground(null);
			if (curJ > 0)
			{
				cases[curI][curJ-1].setBackground(Color.green);
				curJ--;	
			}
			else
			{
				cases[curI-1][8].setBackground(Color.green);
				curI--;		
				curJ = 8;		
			}
		}
		
		ColorIdenticalNumbers(cases[curI][curJ].getText(), curI*9 + curJ);
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
		new Afficher2(540, 1000);
	}
}