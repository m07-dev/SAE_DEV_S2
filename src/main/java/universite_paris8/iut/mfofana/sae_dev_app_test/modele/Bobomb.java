package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import javafx.beans.Observable;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Bobomb extends Personnage {
    private int degat;
    private ObservableList<Tour> tours;

    public Bobomb(double x, double y, Terrain e) {
        super(x, y, e,100,8);
        this.degat = 25;
    }

    public int vitesseAugmente() {
        if (this.getPv() <= 50) {
            return (int) (this.getVitesse()*1.2);
        }

        return getVitesse();
    }


    public void exploser(ObservableList<Tour> tours) {
        //Distance entre le Bobomb et la tour
        int distance;

        boolean aParalyserTour = false;

            // Coordonnée de Bobomb = B
            double xB = this.getX();
            double yB = this.getY();

            for(int i = 0; i < tours.size(); i++) {
                Tour tourActuelle = tours.get(i);
                // Coordonnée de la Tour = T
                double xT = tourActuelle.getX();
                double yT= tourActuelle.getY();

                distance = (int) (Math.abs(xB - xT) + Math.abs(yB - yT));
                        if ( distance == 1) {
                            tourActuelle.paralyser(3000);
                            tourActuelle.setDegat(tourActuelle.getDegat() - this.degat);
                            aParalyserTour = true;
                        }
            }

            if (aParalyserTour) {
                this.setPv(0);
            }



    }


}
