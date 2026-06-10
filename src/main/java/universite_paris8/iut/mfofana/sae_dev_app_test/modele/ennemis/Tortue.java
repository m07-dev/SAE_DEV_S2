package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;
import javafx.geometry.Point2D;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

import java.util.List;

public class Tortue extends Ennemis {

    public Tortue(int x, int y, Terrain terrain, List<Point2D> chemin,Point2D cible) {
        super(x, y, terrain, 50, 1, chemin,cible);
    }

    @Override
    public int getRecompense() { return 5; }


}


