package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import javafx.collections.ObservableList;

import java.util.List;

public class TourBouleDeGlace extends Tour{
    private double vitesseBalle;
    private int ralentissement;
    public TourBouleDeGlace(double x, double y, int ralentissement, double vitesseBalle){
        super(x,y,10,25,4,4);
        this.vitesseBalle = vitesseBalle;
        this.ralentissement = ralentissement;
    }
    public int getRalentissement() { return ralentissement; }
    public double getVitesseBalle() { return vitesseBalle; }


    @Override
    public void appliquerEffet(Personnage cible, ObservableList<Personnage> ennemis) {

    }
}