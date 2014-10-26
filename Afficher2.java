import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.RandomAccessFile;

@SuppressWarnings("serial")
public class Afficher2 extends JFrame implements ActionListener, KeyListener {
	
	Container c;
	JPanel panel, panel2, panel3, panel4, panelGeneral, panelHaut;
	JPanel[][] jp = new JPanel[3][3];
	JPanel[][] jp2 = new JPanel[1][3];
	JPanel[][] jp3 = new JPanel[1][3];
	JButton cases[][] = new JButton [12][9];
	JTextArea text;
	JButton options[][] = new JButton [1][2];
	GridLayout grille, grille2;
	JMenu menuFichier;
	JMenuBar menu;
	String lastNumberClicked = "";
	boolean gridClicked = false;
	int curI = -1, curJ = -1;
	
	public Afficher2()	{
	//on cree la fenetre
		super ("Sudoku");
		
		setSize(540,1000);
		
		c = getContentPane();
		panel = new JPanel();
		grille = new GridLayout(0,3);
		panel.setLayout(grille);
		//panel.setSize(500, 500);
		
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(0,1));
		
		panel3 = new JPanel();
		
		panel4 = new JPanel();
		panel4.setLayout(grille);
		//panel3.setSize(500, 250);
		
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
		jp2[0][0] = new JPanel();
		jp2[0][0].setBorder(BorderFactory.createEtchedBorder());
		
		// html pour les retours ligne
		text = new JTextArea("Appuyer sur R pour reset la grille ou cliquer "
				+ "sur une case puis T pour reset la case");
		text.setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 20));
		/** On souhaite un retour à ligne automatique : */
		text.setLineWrap(true);		 
		/** On souhaite que les mots ne soient pas coupés : */
		text.setWrapStyleWord(true);
		text.setSize(500, 800);

		jp2[0][0].setSize(540, 800);
		jp2[0][0].add(text);
		panel3.add(jp2[0][0].add(text));
		
		text.addKeyListener(this);

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
		//
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
		
		/*for(int i=0;i<2;i++)
		{
			options[0][i] = new JButton("");
			options[0][i].setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 25));
			options[0][i].setSize(10,10);
			
			jp2[0][i].add(options[0][i]);
			
			panel2.add(jp2[0][i]);
			
			options[0][i].addActionListener(this);
			
		}*/
		c.add(panelGeneral);
		
		setVisible(true);		//show();
	}
	
	public void actionPerformed (ActionEvent e) {
		gridClicked = false;
		for(int i=0;i<9;i++) // grille cliquée
		{
			for(int j=0;j<9;j++)
			{
				cases[i][j].setBackground(Color.white);	
				if(e.getSource() == cases[i][j])
				{
					cases[i][j].setText(lastNumberClicked);		
					cases[i][j].setBackground(Color.red);	
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
					cases[i][j].setBackground(Color.white);	
				
				if(e.getSource() == cases[i][j])
				{
					cases[i][j].setBackground(Color.cyan);
					lastNumberClicked = cases[i][j].getText();
				}
			}
		}
		if(e.getSource() == cases[9][0]) // Afficher la solution
		{
			String fileName = reporterGrilleDansFichier();
			Main3.mainFromAfficher(fileName);
		}
		if(e.getSource() == cases[9][6]) // Trouver le prochain chiffre
		{
			String fileName = reporterGrilleDansFichier();
			Main3.mainFromAfficher(fileName);
		}
	}
	
	public String reporterGrilleDansFichier() { // Options clavier

		String fileName = "C:\\Users\\Romain\\workspace\\Sudoku Solver\\customGame.txt";
		
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

		if (evt.getKeyCode() == KeyEvent.VK_R) // R reset la grille
		{
			for(int i=0;i<9;i++)
			{
				for(int j=0;j<9;j++)
				{
					cases[i][j].setText("");						
				}
			}
		}
		if (evt.getKeyCode() == KeyEvent.VK_T) // T reset la case
		{
			if (curI > -1 && curI < 9 && curJ > -1 && curJ < 9)
				cases[curI][curJ].setText("");						
		}

		//repaint();

	}
	
	public void keyReleased(KeyEvent evt) { // méthode non implémentée mais
		// necessaire pour compiler

	}
	
	public void keyTyped(KeyEvent evt) { // méthode non implémentée mais
		// necessaire pour compiler

	}
	
	public void init() {
		
		//requestFocus();
		//addKeyListener(this);
		
		/*for(int i=10;i<13;i++)
		{
			for(int j=3;j<6;j++)
			{	
				cases[i][j].addActionListener(this);
			}
		}
		cases[10][6].addActionListener(new ActionListener() { // écouteur du bouton "niveau1"
			public void actionPerformed(ActionEvent e) {
			//init();
			//timer.restart(); // on remet le timer à 0 pour avoir l même temps de jeu à chaque init()
			}
		});*/
	}
	
	/*public void start() {
		addKeyListener(this);
	}*/
	
	public static void main(String[] args)
	{
		new Afficher2();
	}
}
