package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import javafx.collections.ObservableList;

import java.util.List;

public class TourObstacle extends Tour{
    private String tyObstacle;
    public TourObstacle(double x, double y, String tyObstacle){
        super(x,y,10,0,2,1);
        this.tyObstacle = tyObstacle;
    }


    @Override
    public void appliquerEffet(Personnage cible, ObservableList<Personnage> ennemis) {

    }
}