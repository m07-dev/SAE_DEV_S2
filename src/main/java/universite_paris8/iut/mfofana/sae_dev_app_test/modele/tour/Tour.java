package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;

import javafx.collections.ObservableList;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Ennemis;

public abstract class Tour {

    private double x;
    private double y ;

    private int cout, niveau;
    private int degat, portee;
    private double  cadence;
    private int resistance;
    private int ticksParalysie = 0;
    private int dernierTickTir = 0;

    public Tour(double x, double y, int cout,
                int degat, int portee, double cadence, int resistance) {
        this.x = x;
        this.y = y;
        this.cout = cout;
        this.niveau = 1;
        this.degat = degat;
        this.portee = portee;
        this.cadence = cadence;
        this.resistance = resistance;
    }

    // --- Getters valeurs simples ---
    public double getX() { return x;}
    public double getY() { return y;}
    public int getCout() { return cout; }
    public int getNiveau() { return niveau; }
    public int getDegat() { return degat; }
    public int getPortee() { return portee; }
    public double getCadence() { return cadence; }
    public int getResistance() { return resistance; }

    // --- Getters Property â†’ pour les bindings dans la vue ---
    /*public DoubleProperty xProperty() { return x; }
    public DoubleProperty yProperty() { return y; }*/

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
        return ticksParalysie > 0;
    }

    public void paralyser(int dureeEnTicks) {
        if (dureeEnTicks > this.ticksParalysie) {
            this.ticksParalysie = dureeEnTicks;
        }
    }

    public boolean peutTirer(int tickCount) {
        if (estParalysee()) return false;
        double intervalleEnTicks = Math.max(1, 60.0 / cadence);
        if(tickCount - dernierTickTir >= intervalleEnTicks){
            dernierTickTir = tickCount;
            return true;
        }
        return false;
    }

    public Ennemis choisirCible(ObservableList<Ennemis> ennemis) {
        Ennemis ciblePlusProche = null;
        double distanceMin = Double.MAX_VALUE;

        for (Ennemis e : ennemis) {
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

    public Ennemis tirer(ObservableList<Ennemis> ennemis, int tickCount) {
        if (!estParalysee() && peutTirer(tickCount) && !ennemis.isEmpty()) {
            Ennemis cible = choisirCible(ennemis);
            if (cible != null) {
                cible.subirDegat(this.degat);
                appliquerEffet(cible, ennemis);
                System.out.println(cible.getPv());
                return cible;
            }
        }
        return null;
    }

    public void mettreAJourStatut() {
        if (ticksParalysie > 0) {
            ticksParalysie--;
        }
    }

    public abstract void appliquerEffet(Ennemis cible, ObservableList<Ennemis> ennemis);
}