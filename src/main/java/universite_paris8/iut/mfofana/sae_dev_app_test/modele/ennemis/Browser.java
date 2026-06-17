package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.geometry.Point2D;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

import java.util.List;

public class Browser extends Ennemis {
    public Browser(double x, double y, Terrain terrain, List<Point2D> chemin, Point2D cible) {
        super(x, y, terrain, 500, 1.0, chemin, cible);
    }

    public void detruireTour() {
        
    }

    @Override
    public String nomImage() {
        return "browser";
    }
}
