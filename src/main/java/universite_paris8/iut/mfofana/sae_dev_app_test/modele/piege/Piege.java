package universite_paris8.iut.mfofana.sae_dev_app_test.modele.piege;

import javafx.collections.ObservableList;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Ennemis;

/**
 * Categorie PIEGE : un objet a usage unique, totalement independant des Tours.
 * Un piege est pose sur une case, agit sur les ennemis a portee, puis disparait.
 *
 * Chaque type de piege (Whomp, etc.) herite de cette classe et redefinit
 * declencher() pour decrire SON effet.
 */
public abstract class Piege {

    private final double x;
    private final double y;
    private final int rayon;   // portee de l'effet (en cases)
    private final int cout;    // prix en pieces
    private boolean utilise;   // true une fois que le piege a agi -> a retirer

    public Piege(double x, double y, int rayon, int cout) {
        this.x = x;
        this.y = y;
        this.rayon = rayon;
        this.cout = cout;
        this.utilise = false;
    }

    /**
     * Applique l'effet du piege aux ennemis. Appele a chaque tick par le Jeu.
     * L'implementation doit appeler marquerUtilise() quand le piege a fait son job.
     */
    public abstract void declencher(ObservableList<Ennemis> ennemis);

    /** Vrai si l'ennemi est dans le rayon du piege **/
    public boolean dansRayon(Ennemis e) {
        double dx = e.getX() - x;
        double dy = e.getY() - y;
        return Math.sqrt(dx * dx + dy * dy) <= rayon;
    }

    protected void marquerUtilise() {
        this.utilise = true;
    }

    public boolean estUtilise() { return utilise; }
    public double getX()        { return x; }
    public double getY()        { return y; }
    public int getRayon()       { return rayon; }
    public int getCout()        { return cout; }
}
