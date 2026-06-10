package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;
import javafx.collections.ObservableList;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Ennemis;

public class TourBouleDeGlace extends Tour {
    private double vitesseBalle;
    private int ralentissement;

    public TourBouleDeGlace(double x, double y, int ralentissement, double vitesseBalle){
        // Coût: 15, Dégâts: 15, Portée: 4, Cadence: 4, Résistance: 50
        super(x, y, 15, 15, 4, 1, 50);
        this.vitesseBalle = vitesseBalle;
        this.ralentissement = ralentissement;
    }

    @Override
    public void appliquerEffet(Ennemis cible, ObservableList<Ennemis> ennemis) {
        cible.setRalenti(10);
    }
}