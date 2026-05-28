package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

public class Chateau {
    private int pv;
    private int pvMax;

    public Chateau() {
        this.pvMax = 100;
        this.pv = 100;
    }

    public void subirDegat(int degat) {
        this.pv = Math.max(0, this.pv - degat);
        System.out.println("Château touché ! PV restants : " + pv + "/" + pvMax);
    }

    public boolean estDetruit() {
        return this.pv == 0;
    }

    public int getPv() { return pv; }
    public int getPvMax() { return pvMax; }
}
