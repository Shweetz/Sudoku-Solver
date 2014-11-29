import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Help extends JFrame {

	private JTextArea message;
	
	public Help(String title) {
		super(title);
		
		Container c = getContentPane();
		JPanel panelPopup = new JPanel();
		
	    // Créer message
	    message = new JTextArea();
		
		message.setText("Commandes :\n"
		+ "- Entrer un chiffre : le sélectionner dans le pad en bas puis cliquer sur une case pour y coller le chiffre\n"
		+ "OU cliquer sur une case puis sur un chiffre sur votre clavier\n"
		+ "- Reset une case : la sélectionner et appuyer sur 0\n"
		+ "- Reset la grille : Ctrl + N\n"
		+ "- Charger un fichier : Ctrl + L\n"
		+ "- Sauvegarder le fichier : Ctrl + S\n"
		+ "- Ouvrir l'aide : Ctrl + H\n"
		+ "- Empêcher valeur conflictueuse : Ctrl + E\n"
		+ "- Se déplacer dans la grille : utiliser les flèches du clavier");
	
		message.setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 20));
		message.setEditable(false);
		
	    // L'ajouter
		panelPopup.add(message);  
		c.add(panelPopup);
		
	    pack();
		setLocationRelativeTo(null); // La fenêtre apparaît au centre de l'écran
        setVisible(true);
	}
	
}
