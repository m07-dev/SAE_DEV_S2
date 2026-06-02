package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

public class Boss extends Personnage {
    public Boss(double x, double y, Terrain e) {
        super(x, y, e, 50, 4);
    }
    public Boss(int x, int y, Terrain e, int pv, int v) {
        super(x, y, e, pv, v);
    }
}
