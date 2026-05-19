package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

public abstract class Tour {
    private double x, y;
    private int cout, niveau;
    private int degat, portee, cadence;
    private long finParalysie = 0;
    private long dernierTir = 0;


    public Tour(double x, double y, int cout,
                int degat, int portee, int cadence) {
        this.x = x;
        this.y = y;
        this.cout = cout;
        this.niveau = 1;
        this.degat = degat;
        this.portee = portee;
        this.cadence = cadence;
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

    public int getCout() {
        return cout;
    }

    public int getNiveau() {
        return niveau;
    }

    public int getPortee() {
        return portee;
    }

    public abstract void tirer();
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

}
