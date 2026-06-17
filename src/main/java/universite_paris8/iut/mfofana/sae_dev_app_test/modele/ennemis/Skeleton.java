package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.geometry.Point2D;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

import java.util.List;

public class Skeleton extends Ennemis {
    private int degat = 25;

    public Skeleton(double x, double y, Terrain e, List<Point2D> chemin,Point2D cible) {
        super(x, y, e, 40, 2.0, chemin,cible);
        this.degat = 25;
    }

    public Skeleton(double x, double y, Terrain terrain, int pv, int vitesse, List<Point2D> chemin,Point2D cible) {
        super(x, y, terrain, pv, vitesse,  chemin, cible);
    }

    @Override
    public String nomImage() {
        return "skeleton.png";
    }
}
