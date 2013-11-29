import java.util.Scanner;


public class MainUser {

  public static void main(String[] args)
  {
    Grille sudoku = new Grille();
    Scanner sc = new Scanner (System.in);
    
    for (int i=1; i<9; i++)
    {
      for (int j=1; j<9; j++)
      {
        int val = 0;
        try
        {
          System.out.println("valeur ? ");
          val = sc.nextInt(); //demande une valeur numerique à l utilisateur
        }catch(Exception e){} // la valeur reste a 0 si il y a un probleme avec la valeur entree
        
        if (val != 0) sudoku.entrerValeur(i,j,val);
      }
    } //demande a l utilisateur la valeur de chaque case
    
    /* 
    // alternative
    int l =-1, c =-1, val = -1; //valeur abberrantes pour commencer la boucle
    while ((val != 0) && (l != 0)  && (c !=0)) 
    {
      l =0, c =0, val = 0; // valeurs par defaut en cas de probleme
      
      try
      {
        System.out.println("ligne ? ");
        l = sc.nextInt();
        System.out.println("colonne ? ");
        c = sc.nextInt();
        System.out.println("valeur ? ");
        val = sc.nextInt();
      }catch(Exception e) {}
      
       if ((val > 0) && (val  < 10) && (l < 10) && (l > 0) && (c < 10) && (c > 0)) sudoku.entrerValeur(l,c,val);
       else if ((val  < 10) && (l < 10) && (c < 10)) System.err.println("une des valeurs incorrecte (doit être entre 1 et 9) : l ="+l+" c=" +c+" val="+val );
    }//demande a l utilisateur de rentrer ligne colonne et valeur jusqu a ce qu il y  ait une erreur ou que l utitilsateur tape 0
    
    */
    
    sudoku.testLigneEntree(); // On v�rifie qu'une valeur en entr�e ne se trouve pas 2 fois
    // sur la m�me ligne

    sudoku.resolutionSudoku(); // Cette m�thode contient tous les calculs pour r�soudre le sudoku

    sudoku.afficheResolu(); // Une fois les calculs faits, on affiche le r�sultat
  }




}
