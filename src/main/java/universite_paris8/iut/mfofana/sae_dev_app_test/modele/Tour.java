package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

public abstract class Tour {
    private double x, y;
    private int cout, niveau;
    private int degat, portee, cadence;

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

    public abstract void tirer();{
        System.out.println();
    }
    public int vendre(){
        return cout / 2;
    }
    public int ameliorer(){
        return niveau++;
    }


}
