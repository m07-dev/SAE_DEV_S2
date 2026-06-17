package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;
import javafx.collections.ObservableList;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Ennemis;

import java.util.List;

public class TourBombe extends Tour {
    private double vitesseBalle;
    private double rayonExplosion = 2.0;

    public TourBombe(double x, double y, double vitesseBalle){
        // Coût: 50, Dégâts: 25, Portée: 2, Cadence: 1, Résistance: 100
        super(x, y, 50, 30, 2, 1, 100);
        this.vitesseBalle = vitesseBalle;

    }

    @Override
    public void ameliorer() {
        super.ameliorer();
        rayonExplosion += 1.5;
    }

    public double getVitesseBalle() { return vitesseBalle; }

    @Override
    public void appliquerEffet(Ennemis cible, List<Ennemis> ennemis) {
        double rayonExplosion = 2.5 + (getNiveau() * 0.5);
        for (Ennemis e : ennemis) {
            double distance = Math.sqrt(
                    Math.pow(e.getX() - cible.getX(), 2) +
                            Math.pow(e.getY() - cible.getY(), 2)
            );
            if (distance <= rayonExplosion) {
                e.subirDegat(getDegat());
            }
        }
    }

    @Override
    public String nomTour() {
        return "Bombe";
    }
}