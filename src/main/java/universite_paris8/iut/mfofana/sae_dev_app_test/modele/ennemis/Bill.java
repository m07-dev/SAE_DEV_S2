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
        super(x, y, terrain, 10, 4.5, chemin, cible);
        degatObstacle = 100;
    }

    @Override
    public void recalculerChemin(Terrain terrain) {
        // Vide pour éviter de recalculer le chemin
    }


    @Override
    public void seDeplacer() {
        if (this.chemin == null || this.indexCible >= this.chemin.size()) return;

        Point2D cibleActuelle = this.chemin.get(this.indexCible);

        int typeCase = this.terrain.getTileTerrain((int)cibleActuelle.getY(), (int)cibleActuelle.getX());

        // Changle la tile de l'obstacle pour le faire disparaître
        if (typeCase == 0) {
            this.terrain.setTileTerrain((int)cibleActuelle.getY(), (int)cibleActuelle.getX(), 1); // La case redevient de la route
        }

        // Déplacement dans la map
        double disX = cibleActuelle.getX() - this.getX();
        double disY = cibleActuelle.getY() - this.getY();
        double pas = this.getVitesse() / 60.0;

        if (disX > 0) this.setX(this.getX() + pas);
        else if (disX < 0) this.setX(this.getX() - pas);
        if (disY > 0) this.setY(this.getY() + pas);
        else if (disY < 0) this.setY(this.getY() - pas);

        if (Math.abs(disX) <= pas && Math.abs(disY) <= pas) {
            this.setX(cibleActuelle.getX());
            this.setY(cibleActuelle.getY());
            this.indexCible++;
        }
    }

    @Override
    public String nomImage() {
        return "bill.png";
    }


    public int getDegatObstacle() {
        return degatObstacle;
    }




}
