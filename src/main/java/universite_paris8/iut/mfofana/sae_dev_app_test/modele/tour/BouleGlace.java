package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;

import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Ennemis;

import java.util.List;

public class BouleGlace extends Projectile{
    public BouleGlace(int degat, Ennemis cible, double x, double y, Tour tour, List<Ennemis> ennemis) {
        super(degat, cible, x, y, tour, ennemis);
    }

    @Override
    public String nomProjectile() {
        return "bouleGlace.png";
    }


}
