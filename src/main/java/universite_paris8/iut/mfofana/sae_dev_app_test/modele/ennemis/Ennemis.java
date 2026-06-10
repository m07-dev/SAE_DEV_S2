package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

import java.util.List;

public abstract class Ennemis {
    private List<Point2D> chemin;
    private int indexCible;
    private Point2D cible;
    // DoubleProperty â†’ la vue peut binder sa position dessus
    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();

    // IntegerProperty â†’ la vue peut binder la barre de vie dessus
    private IntegerProperty pv = new SimpleIntegerProperty();
    private final int pvMax;

    private Terrain terrain;
    private int vitesse;

    // -- Effets de statut --
    private int ticksBrulure = 0;
    private boolean ralenti = false;
    private int vitesseOriginale = 0;
    private int ticksRalentissement = 0;

    public Ennemis(double x, double y, Terrain terrain, int pv, int vitesse, List<Point2D> chemin, Point2D cible) {
        this.x.set(x);
        this.y.set(y);
        this.pv.set(pv);
        this.terrain = terrain;
        this.vitesse = vitesse;
        this.pvMax = pv;
        this.chemin = chemin;
        this.indexCible = 1;
        this.cible = cible;
    }

    // --- Effets de statut ---

    public void setRalenti(int duree) {
        if (!this.ralenti) {
            this.vitesseOriginale = this.vitesse;
            this.vitesse = Math.max(1, this.vitesse / 2);
            this.ralenti = true;
        }
        this.ticksRalentissement = duree;
    }

    public void setTicksBrulure(int ticks) {
        if(this.ticksBrulure <= 0){
            this.ticksBrulure = ticks;
        }
    }

    public void mettreAJourEffets() {
        if (ticksBrulure > 0) {
            subirDegat(2);
            ticksBrulure--;
        }
        if (ralenti) {
            ticksRalentissement--;
            if (ticksRalentissement <= 0) {
                this.vitesse = this.vitesseOriginale;
                this.ralenti = false;
            }
        }
    }
    public void recalculerChemin(Terrain terrain) {
        Point2D posActuelle = new Point2D(Math.round(getX()), Math.round(getY()));
        List<Point2D> nouveauChemin = terrain.algoBFS(posActuelle, this.cible);
        if (!nouveauChemin.isEmpty()) {
            this.chemin = nouveauChemin;
            this.indexCible = 1;
        }
    }

    public void seDeplacer(){
        if (chemin != null && indexCible < chemin.size()) {
            Point2D cibleActuelle = chemin.get(indexCible);
            double disX = cibleActuelle.getX() - this.getX();
            double disY = cibleActuelle.getY() - this.getY();

            if(disX > 0){
                this.setX(this.getX() + vitesse / 60.0);
            } else if (disX < 0) {
                this.setX(this.getX() - vitesse / 60.0);
            }

            if(disY > 0){
                this.setY(this.getY() + vitesse / 60.0);
            } else if (disY < 0) {
                this.setY(this.getY() - vitesse / 60.0);
            }

            if (Math.abs(disX) <= vitesse / 60.0 && Math.abs(disY) <= vitesse/ 60.0) {
                this.setX(cibleActuelle.getX()); // snap exact sur la case
                this.setY(cibleActuelle.getY());
                indexCible++;
            }
        }
    }
    public boolean aAtteintLeChateau() {
        return chemin != null && indexCible >= chemin.size();
    }


    // --- Getters valeurs simples ---
    public double getX() { return x.get(); }
    public double getY() { return y.get(); }
    public int getPv()   { return pv.get(); }
    public int getVitesse() { return vitesse; }
    public int getRecompense() { return 10; }
    public int getPvMax() { return pvMax; }

    // --- Getters Property â†’ pour les bindings dans la vue ---
    public DoubleProperty xProperty()  { return x; }
    public DoubleProperty yProperty()  { return y; }
    public IntegerProperty pvProperty() { return pv; }

    // --- Setters ---
    public void setX(double valeur) { x.set(valeur); }
    public void setY(double valeur) { y.set(valeur); }
    public void setPv(int valeur)   { pv.set(Math.max(0, valeur)); }
    public void setVitesse(int v)   { this.vitesse = v; }

    // --- Logique ---
    public void subirDegat(int degat) { setPv(pv.get() - degat); }
    public boolean estMort() { return pv.get() == 0; }
    public boolean horsDesLimites() {
        return getX() < 0 || getX() >= 32*30 || getY() < 0 || getY() >= 30*32;
    }
}