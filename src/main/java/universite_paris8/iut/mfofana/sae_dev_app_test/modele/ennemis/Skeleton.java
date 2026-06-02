package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

public class Skeleton extends Personnage{
    private int degat = 25;

    public Skeleton(double x, double y, Terrain e) {
        super(x, y, e, 100, 4);
        this.degat = 25;
    }

    public Skeleton(double x, double y, Terrain terrain, int pv, int vitesse) {
        super(x, y, terrain, pv, vitesse);
    }
}
