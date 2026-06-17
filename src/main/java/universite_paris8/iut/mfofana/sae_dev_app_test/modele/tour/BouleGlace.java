package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;

import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Ennemis;

public class BouleGlace extends Projectile{
    public BouleGlace(int degat, Ennemis cible, double x, double y, Tour tour) {
        super(degat, cible, x, y, tour);
    }

    @Override
    public String nomProjectile() {
        return "bouleGlace.png";
    }


}
