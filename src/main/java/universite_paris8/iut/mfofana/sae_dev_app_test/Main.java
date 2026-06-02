package universite_paris8.iut.mfofana.sae_dev_app_test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.*;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Bobomb;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Personnage;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Tortue;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Tour;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.TourBouleDeFeu;

public class Main {
    public static void main(String[] args) {
        ObservableList<Personnage> personnages = FXCollections.observableArrayList();
        ObservableList<Tour> tours = FXCollections.observableArrayList();
        Terrain terrain = new Terrain();
        TourBouleDeFeu t1 = new TourBouleDeFeu(100,100,2,2);

        Tortue s1 = new Tortue(105,100,terrain);
        Bobomb b1 = new Bobomb(500,500,terrain);
        Bobomb b2 = new Bobomb(95,100,terrain);
        personnages.addAll(s1,b1);
        tours.addAll(t1);

        System.out.println(b1.getPv());
        System.out.println(s1.getPv());

        /*t1.tirer(personnages);*/

        System.out.println(b1.getPv());
        System.out.println(s1.getPv());

        b1.setPv(10);
        System.out.println(b1.getVitesse());
        b1.vitesseAugmente();
        System.out.println(b1.getVitesse());

        b2.exploser(tours);
        System.out.println("La tour est-elle paralysée ? " + t1.estParalysee());
        System.out.println();
        System.out.println(b2.estMort());

    }
}
