package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Personnage;

public abstract class Tour {

    // DoubleProperty → la vue peut binder la position de la tour
    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();

    private int cout, niveau;
    private int degat, portee, cadence;
    private int resistance;
    private long finParalysie = 0;
    private long dernierTir = 0;

    public Tour(double x, double y, int cout,
                int degat, int portee, int cadence, int resistance) {
        this.x.set(x);
        this.y.set(y);
        this.cout = cout;
        this.niveau = 1;
        this.degat = degat;
        this.portee = portee;
        this.cadence = cadence;
        this.resistance = resistance;
    }

    // --- Getters valeurs simples ---
    public double getX() { return x.get(); }
    public double getY() { return y.get(); }
    public int getCout() { return cout; }
    public int getNiveau() { return niveau; }
    public int getDegat() { return degat; }
    public int getPortee() { return portee; }
    public int getCadence() { return cadence; }
    public int getResistance() { return resistance; }

    // --- Getters Property → pour les bindings dans la vue ---
    public DoubleProperty xProperty() { return x; }
    public DoubleProperty yProperty() { return y; }

    // --- Setters ---
    public void setDegat(int degat) { this.degat = degat; }

    // --- Logique ---
    public void subirDegat(int degat) {
        this.resistance = Math.max(0, this.resistance - degat);
    }
    public boolean estDetruite() { return this.resistance == 0; }

    public int vendre() { return cout / 2; }

    public void ameliorer() {
        if (niveau < 3) {
            niveau++;
            degat += 5;
            portee += 1;
        }
    }

    public boolean estParalysee() {
        return System.currentTimeMillis() < finParalysie;
    }

    public void paralyser(int dureeMs) {
        finParalysie = System.currentTimeMillis() + dureeMs;
    }

    public boolean peutTirer(int tickCount) {
        if (estParalysee()) return false;
        int intervalleEnTicks = Math.max(1, 10 / cadence);
        if(tickCount % intervalleEnTicks == 0){
            return true;
        }
        return false;
    }

    public Personnage choisirCible(ObservableList<Personnage> ennemis) {
        Personnage ciblePlusProche = null;
        double distanceMin = Double.MAX_VALUE;

        for (Personnage e : ennemis) {
            double distance = Math.sqrt(
                    Math.pow(e.getX() - this.getX(), 2) +
                            Math.pow(e.getY() - this.getY(), 2)
            );
            if (distance <= this.getPortee()) {
                if (distance < distanceMin) {
                    distanceMin = distance;
                    ciblePlusProche = e;
                }
            }
        }
        return ciblePlusProche;
    }

    public Personnage tirer(ObservableList<Personnage> ennemis, int tickCount) {
        if (!estParalysee() && peutTirer(tickCount) && !ennemis.isEmpty()) {
            Personnage cible = choisirCible(ennemis);
            if (cible != null) {
                cible.subirDegat(this.degat);
                appliquerEffet(cible, ennemis);
                return cible;
            }
        }
        return null;
    }

    public abstract void appliquerEffet(Personnage cible, ObservableList<Personnage> ennemis);
}