package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.geometry.Point2D;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

import java.util.List;

public class Goomba extends Ennemis {
    private int degat;
    public Goomba(double x, double y, Terrain terrain, int pv, int vitesse, List<Point2D> chemin, Point2D cible) {
        super(x, y, terrain, pv, vitesse, chemin, cible);
    }
    public Goomba(double x, double y, Terrain e, List<Point2D> chemin,Point2D cible) {
        super(x, y, e, 30, 2.0, chemin, cible);
        this.degat = 25;
    }

    @Override
    public String nomImage() {
        return "goomba.png";
    }
}
