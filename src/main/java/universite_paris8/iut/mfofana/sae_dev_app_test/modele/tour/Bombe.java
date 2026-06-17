package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;

import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Ennemis;

public class Bombe extends Projectile{
    public Bombe(int degat, Ennemis cible, double x, double y, Tour tour) {
        super(degat, cible, x, y, tour);
    }

    @Override
    public String nomProjectile() {
        return "bouleBombe.png";
    }
}
