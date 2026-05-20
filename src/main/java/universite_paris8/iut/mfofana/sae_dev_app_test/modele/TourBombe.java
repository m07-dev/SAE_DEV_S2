package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import javafx.beans.Observable;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class TourBombe extends Tour{
    private double vitesseBalle;
    public TourBombe(double x, double y, double vitesseBalle){
        super(x,y,50,25,2,2);
        this.vitesseBalle = vitesseBalle;
    }
    public double getVitesseBalle() { return vitesseBalle; }

    @Override
    public void appliquerEffet(Personnage cible, ObservableList<Personnage> ennemis) {

    }


}