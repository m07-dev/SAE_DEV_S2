package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;
import javafx.collections.ObservableList;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Personnage;

public class TourBouleDeFeu extends Tour {
    private double vitesseBalle;
    private int effetBrulure;

    public TourBouleDeFeu(double x, double y, double vitesseBalle, int effetBrulure) {
        // Coût: 15, Dégâts: 15, Portée: 4, Cadence: 4, Résistance: 50
        super(x, y, 15, 6, 4, 4, 50);
        this.vitesseBalle = vitesseBalle;
        this.effetBrulure = effetBrulure;
    }

    @Override
    public void appliquerEffet(Personnage cible, ObservableList<Personnage> ennemis) {
        cible.setTicksBrulure(5);
    }
}