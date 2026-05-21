package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import javafx.collections.ObservableList;

import java.util.List;

public abstract class Tour {
    private double x, y;
    private int cout, niveau;
    private int degat, portee, cadence;
    private long finParalysie = 0;
    private long dernierTir = 0;

    public Tour(double x, double y, int cout, int degat, int portee, int cadence) {
        this.x = x;
        this.y = y;
        this.cout = cout;
        this.niveau = 1;
        this.degat = degat;
        this.portee = portee;
        this.cadence = cadence;
    }


    public double getX() { return x; }
    public double getY() { return y; }
    public int getCout() { return cout; }
    public int getNiveau() { return niveau; }
    public int getDegat() { return degat; }
    public int getPortee() { return portee; }
    public int getCadence() { return cadence; }


    public boolean estParalysee() {
        return System.currentTimeMillis() < finParalysie;
    }

    public void paralyser(int dureeMs) {
        finParalysie = System.currentTimeMillis() + dureeMs;
    public void setDegat(int degat){
        this.degat = degat;
    }

    public int getCout() {
        return cout;
    }

    public boolean peutTirer() {
        if (estParalysee()) return false;
        long maintenant = System.currentTimeMillis();
        if (maintenant - dernierTir >= 1000 / cadence) {
            dernierTir = maintenant;
            return true;
        }
        return false;
    }


    public Personnage choisirCible(ObservableList<Personnage> ennemis) {
        for (Personnage e : ennemis) {
            double distance = Math.sqrt(
                    Math.pow(e.getX() - this.x, 2) +
                            Math.pow(e.getY() - this.y, 2)
            );
            if (distance <= this.portee) {
                return e;
            }
        }
        return null;
    }


    public int vendre() {
        return cout / 2;
    }

    public void ameliorer() {
        if (niveau < 3) {
            niveau++;
            degat += 5;
            portee += 1;
        }
    }


    public void tirer(ObservableList<Personnage> ennemis) {
        if (!estParalysee() && peutTirer() && !ennemis.isEmpty()) {
            Personnage cible = choisirCible(ennemis);
            if (cible != null) {
                cible.subirDegat(this.degat);    // dégâts de base
                appliquerEffet(cible, ennemis);  // effet spécial
            }
        }
    }

    public abstract void appliquerEffet(Personnage cible, ObservableList<Personnage> ennemis);
}