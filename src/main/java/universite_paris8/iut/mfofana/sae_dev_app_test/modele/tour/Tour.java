package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;

import javafx.collections.ObservableList;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Personnage;

public abstract class Tour {
    private double x, y;
    private int cout, niveau;
    private int degat, portee, cadence;
    private int resistance;
    private long finParalysie = 0;
    private long dernierTir = 0;


    public Tour(double x, double y, int cout,
                int degat, int portee, int cadence, int resistance) {
        this.x = x;
        this.y = y;
        this.cout = cout;
        this.niveau = 1;
        this.degat = degat;
        this.portee = portee;
        this.cadence = cadence;
        this.resistance = resistance;
    }

    public int getCadence() {
        return cadence;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getDegat() {
        return degat;
    }

    public void setDegat(int degat){
        this.degat = degat;
    }

    public int getCout() {
        return cout;
    }

    public int getNiveau() {
        return niveau;
    }

    public int getPortee() {
        return portee;
    }

    public int getResistance() { return resistance; }
    public void subirDegat(int degat) { this.resistance = Math.max(0, this.resistance - degat); }
    public boolean estDetruite() { return this.resistance == 0; }
    public int vendre(){
        return cout / 2;
    }
    public void ameliorer(){
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
    public boolean peutTirer() {
        if (estParalysee()) return false; // ← tour paralysée = ne tire plus
        long maintenant = System.currentTimeMillis();
        if (maintenant - dernierTir >= 1000 / cadence) {
            dernierTir = maintenant;
            return true;
        }
        return false;
    }

    public Personnage choisirCible(ObservableList<Personnage> ennemis) {
        Personnage ciblePlusProche = null;
        double distanceMin = Double.MAX_VALUE;
        int TAILLE_TUILE = 30;

        for (Personnage e : ennemis) {
            double distance = Math.sqrt(
                    Math.pow(e.getX() - this.x, 2) +
                            Math.pow(e.getY() - this.y, 2)
            );
            if (distance <= (this.getPortee())) {
                if (distance < distanceMin) {
                    distanceMin = distance;
                    ciblePlusProche = e;
                }
            }
        }
        return ciblePlusProche;
    }

    public void tirer(ObservableList<Personnage> ennemis) {
        if (!estParalysee() && peutTirer() && !ennemis.isEmpty()) {
            Personnage cible = choisirCible(ennemis);
            if (cible != null) {
                cible.subirDegat(this.degat);
                appliquerEffet(cible, ennemis);
            }
        }
    }



    public abstract void appliquerEffet(Personnage cible, ObservableList<Personnage> ennemis);
}

