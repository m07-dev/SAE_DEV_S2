package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Chateau {
    private IntegerProperty pv = new SimpleIntegerProperty(100);
    private IntegerProperty pvMax = new SimpleIntegerProperty(100);
    private int pvEnPlus = 10;


    public void subirDegat(int degat) {
        pv.set( Math.max(0, pv.get() - degat));
        System.out.println("Château touché ! PV restants : " + pv.get() + "/" + pvMax.get());
    }

    public boolean estDetruit() {
        return pv.get() == 0;
    }

    public int getPv() { return pv.get(); }
    public int getPvMax() { return pvMax.get(); }

    public void setPv(int pv) {
        this.pv.set(pv + (pvEnPlus++));
    }

    public IntegerProperty pvProperty() { return pv; }
    public IntegerProperty pvMaxProperty() { return pvMax; }
}
