package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Tour;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.TourObstacle;

import java.util.List;

public class Bill extends Ennemis {
    private int degatObstacle;

    public Bill(double x, double y, Terrain terrain, List<Point2D> chemin, Point2D cible) {
        super(x, y, terrain, 20, 5.0, chemin, cible);
        degatObstacle = 100;
    }

    public void detruireObstacle(ObservableList<Tour> tours) {

        for( int i = tours.size() - 1; i >= 0; i --) {
            Tour t = tours.get(i);

            if (t instanceof TourObstacle) {
                // Distance entre Bill et la Tour
                double distance = Math.abs(this.getX() - t.getX()) + Math.abs(this.getY() - t.getY());

                if (distance <= 20) {
                    TourObstacle tObstacle = (TourObstacle) t;
                    tObstacle.subirDegat(this.degatObstacle);

                    if (tObstacle.estDetruite()) {
                        tours.remove(i);
                    }
                }
            }
        }
    }

    @Override
    public void recalculerChemin(Terrain terrain) {
        // Bloque toute tentative de recalcul externe
    }

    @Override
    public void seDeplacer() {
        System.out.println("Bill bouge seulllle - Index actuel: " + getIndexCible());

        // Si Bill n'a pas de chemin ou est arrivé au bout, on s'arrête
        if (this.chemin == null || this.indexCible >= this.chemin.size()) {
            return;
        }

        // On récupère le point du chemin INITIAL (BFS de départ)
        Point2D cibleActuelle = this.chemin.get(this.indexCible);

        // 🎯 L'ÉCRASEMENT D'OBSTACLE : On vérifie la case cible
        int typeCase = this.terrain.getTileTerrain(
                (int) Math.round(cibleActuelle.getY()),
                (int) Math.round(cibleActuelle.getX())
        );

        if (typeCase == 0) {
            System.out.println("💥 [BILL] Obstacle détruit en position : " + cibleActuelle);
            // On force la case à redevenir un chemin (1)
            this.terrain.setTileTerrain((int) Math.round(cibleActuelle.getY()), (int) Math.round(cibleActuelle.getX()), 1);
        }

        // 🎯 LE DÉPLACEMENT FORCÉ : Bill ignore totalement si c'est un obstacle (0).
        // Il avance bêtement vers la cible suivante de son plan initial.
        double disX = cibleActuelle.getX() - this.getX();
        double disY = cibleActuelle.getY() - this.getY();
        double pas = this.getVitesse() / 60.0;

        if (disX > 0) {
            this.setX(this.getX() + pas);
        } else if (disX < 0) {
            this.setX(this.getX() - pas);
        }

        if (disY > 0) {
            this.setY(this.getY() + pas);
        } else if (disY < 0) {
            this.setY(this.getY() - pas);
        }

        // Si on est pile sur la case, on passe à l'index suivant du chemin
        if (Math.abs(disX) <= pas && Math.abs(disY) <= pas) {
            this.setX(cibleActuelle.getX());
            this.setY(cibleActuelle.getY());
            this.indexCible++; // On avance dans la liste d'origine
        }
    }

    public String getNom() {
        return "Bill";
    }


    public int getDegatObstacle() {
        return degatObstacle;
    }




}
