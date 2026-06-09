package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;
import javafx.collections.ObservableList;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Personnage;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Projectile;

public class TourBombe extends Tour {
    private double vitesseBalle;

    public TourBombe(double x, double y, double vitesseBalle){
        // Coût: 50, Dégâts: 25, Portée: 2, Cadence: 1, Résistance: 100
        super(x, y, 50, 25, 2, 1, 100);
        this.vitesseBalle = vitesseBalle;
    }

    public double getVitesseBalle() { return vitesseBalle; }

    @Override
    public void appliquerEffet(Personnage cible, ObservableList<Personnage> ennemis) {
        // La bombe pourrait faire des dégâts de zone ici plus tard
    }

    @Override
    public Projectile creerProjectile(double cibleX, double cibleY) {
        return null;
    }
}