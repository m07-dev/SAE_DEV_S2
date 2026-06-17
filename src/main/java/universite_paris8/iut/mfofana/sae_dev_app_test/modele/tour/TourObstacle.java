package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;
import javafx.collections.ObservableList;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Ennemis;

public class TourObstacle extends Tour {
    private String tyObstacle;
    private int pv;

    public TourObstacle(double x, double y, String tyObstacle){
        // Coût: 50, Dégâts: 0, Portée: 6, Cadence: 1, Résistance: 100
        super(x, y, 50, 0, 6, 1, 100);
        this.tyObstacle = tyObstacle;
        this.pv = 100;
    }

    public void detruite() {

    }

    public void subirDegat(int degat) {
        this.pv -= degat;
    }

    public boolean estDetruite() {
        return this.pv <= 0;
    }

    @Override
    public void appliquerEffet(Ennemis cible, ObservableList<Ennemis> ennemis) {
        // Logique spécifique à l'obstacle (ex: repousser l'ennemi, l'étourdir)
    }

    @Override
    public String nomTour() {
        return "Obstacle";
    }
}