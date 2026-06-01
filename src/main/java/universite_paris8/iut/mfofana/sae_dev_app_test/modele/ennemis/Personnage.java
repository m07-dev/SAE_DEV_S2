package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

public abstract class Personnage {

    // DoubleProperty → la vue peut binder sa position dessus
    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();

    // IntegerProperty → la vue peut binder la barre de vie dessus
    private IntegerProperty pv = new SimpleIntegerProperty();

    private Terrain terrain;
    private int vitesse;

    // -- Effets de statut --
    private int ticksBrulure = 0;
    private boolean ralenti = false;
    private int vitesseOriginale = 0;
    private int ticksRalentissement = 0;

    public Personnage(double x, double y, Terrain terrain, int pv, int vitesse) {
        this.x.set(x);      // on initialise via .set()
        this.y.set(y);      // car c'est une Property, pas un double simple
        this.pv.set(pv);
        this.terrain = terrain;
        this.vitesse = vitesse;
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


    // --- Getters valeurs simples ---
    public double getX() { return x.get(); }
    public double getY() { return y.get(); }
    public int getPv()   { return pv.get(); }
    public int getVitesse() { return vitesse; }
    public int getRecompense() { return 10; }

    // --- Getters Property → pour les bindings dans la vue ---
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