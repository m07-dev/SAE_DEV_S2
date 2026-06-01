package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;
import javafx.collections.ObservableList;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Personnage;

public class TourObstacle extends Tour {
    private String tyObstacle;

    public TourObstacle(double x, double y, String tyObstacle){
        // Coût: 50, Dégâts: 0, Portée: 6, Cadence: 1, Résistance: 100
        super(x, y, 50, 0, 6, 1, 100);
        this.tyObstacle = tyObstacle;
    }

    @Override
    public void appliquerEffet(Personnage cible, ObservableList<Personnage> ennemis) {
        // Logique spécifique à l'obstacle (ex: repousser l'ennemi, l'étourdir)
    }
}