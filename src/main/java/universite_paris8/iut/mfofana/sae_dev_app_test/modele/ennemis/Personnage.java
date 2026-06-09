package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

public abstract class Personnage extends Entite{

    // IntegerProperty → la vue peut binder la barre de vie dessus
    private IntegerProperty pv = new SimpleIntegerProperty();
    private final int pvMax;

    private Terrain terrain;


    // -- Effets de statut --
    private int ticksBrulure = 0;
    private boolean ralenti = false;
    private double vitesseOriginale = 0;
    private int ticksRalentissement = 0;

    public Personnage(double x, double y, Terrain terrain, int pv, int vitesse) {
        super(x,y,vitesse);
        this.pv.set(pv);
        this.terrain = terrain;
        this.pvMax = pv;
    }

    // --- Effets de statut ---

    public void setRalenti(int duree) {
        if (!this.ralenti) {
            this.vitesseOriginale = super.getVitesse();
            super.setVitesse(Math.max(1, super.getVitesse() / 2));
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
                super.setVitesse(this.vitesseOriginale);;
                this.ralenti = false;
            }
        }
    }


    // --- Getters valeurs simples ---
    public int getPv()   { return pv.get(); }
    public int getRecompense() { return 10; }
    public int getPvMax() { return pvMax; }

    // --- Getters Property → pour les bindings dans la vue ---
    public IntegerProperty pvProperty() { return pv; }

    // --- Setters ---
    public void setPv(int valeur)   { pv.set(Math.max(0, valeur)); }

    // --- Logique ---
    public void subirDegat(int degat) { setPv(pv.get() - degat); }
    public boolean estMort() { return pv.get() == 0; }
    public boolean horsDesLimites() {
        return getX() < 0 || getX() >= 32*30 || getY() < 0 || getY() >= 30*32;
    }
}