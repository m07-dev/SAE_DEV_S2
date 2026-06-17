package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.geometry.Point2D;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Tour;

import java.util.List;

public class Browser extends Ennemis {
    private int comptToursDetruites = 0;
    private List<Tour> toursJeu;


    public Browser(double x, double y, Terrain terrain, List<Point2D> chemin, Point2D cible ) {
        super(x, y, terrain, 500, 1.0, chemin, cible);
        this.terrain = terrain;
        this.indexCible = 1;

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

        if (comptToursDetruites < 3) {
            detruireToursAutour();
        }
    }

    private void detruireToursAutour() {

        for (int i = toursJeu.size() - 1; i >= 0; i--) {
            Tour t = toursJeu.get(i);

            double distance = calculerDistance(this.getX(), this.getY(), t.getX(), t.getY());

            if (distance <= 2.5) {
                toursJeu.remove(i);
                comptToursDetruites++;

                if (comptToursDetruites >= 3) {
                    break;
                }
            }
        }
    }
  

    private double calculerDistance(double bx, double by, double tx, double ty) {
        return Math.sqrt(Math.pow(tx - bx, 2) + Math.pow(ty - tx, 2));
    }

    @Override
    public String nomImage() {
        return "browser";
    }
}
