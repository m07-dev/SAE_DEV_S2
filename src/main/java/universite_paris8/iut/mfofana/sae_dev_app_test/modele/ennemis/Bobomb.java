package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Tour;

import java.util.List;

public class Bobomb extends Ennemis {
    private int degat;

    public Bobomb(double x, double y, Terrain e, List<Point2D> chemin,Point2D cible) {
        super(x, y, e, 80, (int)1.5, chemin, cible);
        this.degat = 25;
    }

    public void vitesseAugmente() {
        if (this.getPv() <= 50) {
            int nouvelleVitesse = (int) (this.getVitesse() * 1.2);
            this.setVitesse(nouvelleVitesse);
        }
    }

    public void exploser(ObservableList<Tour> tours) {
        double xB = this.getX();
        double yB = this.getY();
        boolean aParalyserTour = false;

        for (int i = 0; i < tours.size(); i++) {
            Tour tourActuelle = tours.get(i);
            double xT = tourActuelle.getX();
            double yT = tourActuelle.getY();

            int distance = (int)(Math.abs(xB - xT) + Math.abs(yB - yT));
            if (distance <= 45) {
                tourActuelle.paralyser(30);
                tourActuelle.setDegat(tourActuelle.getDegat() - this.degat);
                aParalyserTour = true;
            }
        }

        if (aParalyserTour) {
            this.setPv(0);
        }
    }

    @Override
    public int getRecompense() { return 10; }
}