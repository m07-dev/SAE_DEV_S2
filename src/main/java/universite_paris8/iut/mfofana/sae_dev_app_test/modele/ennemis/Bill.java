package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.geometry.Point2D;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

import java.util.List;

public class Bill extends Ennemis {
    public Bill(double x, double y, Terrain terrain, int pv, int vitesse, List<Point2D> chemin) {
        super(x, y, terrain, 1, 10, chemin);
    }


}
