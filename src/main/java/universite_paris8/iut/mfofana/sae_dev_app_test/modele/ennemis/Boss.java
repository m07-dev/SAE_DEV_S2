package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.geometry.Point2D;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

import java.util.List;

public abstract class Boss extends Ennemis {
    public Boss(double x, double y, Terrain e, List<Point2D> chemin,Point2D cible) {
        super(x, y, e, 50, 4.0, chemin, cible);
    }
    public Boss(int x, int y, Terrain e, int pv, int v, List<Point2D> chemin,Point2D cible) {
        super(x, y, e, pv, v, chemin, cible);
    }
}
