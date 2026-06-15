package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Tour;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.TourObstacle;

import java.util.List;

public class Bill extends Ennemis {
    private int degatObstacle;

    public Bill(double x, double y, Terrain terrain, List<Point2D> chemin, Point2D cible) {
        super(x, y, terrain, 20, 10.0, chemin, cible);
        degatObstacle = 100;
    }

    public void detruireObstacle(ObservableList<Tour> tours) {

        for( int i = tours.size() - 1; i >= 0; i --) {
            Tour t = tours.get(i);

            if (t instanceof TourObstacle) {
                // Distance entre Bill et la Tour
                double distance = Math.abs(this.getX() - t.getX()) + Math.abs(this.getY() - t.getY());

                if (distance <= 20) {
                    TourObstacle tObstacle = (TourObstacle) t;
                    tObstacle.subirDegat(this.degatObstacle);

                    if (tObstacle.estDetruite()) {
                        tours.remove(i);
                    }
                }
            }
        }
    }



    public int getDegatObstacle() {
        return degatObstacle;
    }




}
