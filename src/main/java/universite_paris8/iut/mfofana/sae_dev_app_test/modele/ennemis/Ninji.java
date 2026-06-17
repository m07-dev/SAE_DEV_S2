package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.geometry.Point2D;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

import java.util.List;

public class Ninji extends Ennemis {
    public Ninji(double x, double y, Terrain terrain, List<Point2D> chemin,Point2D cible) {
        super(x, y, terrain, 5, 8.0, chemin, cible);
    }

    @Override
    public void seDeplacer() {
        if (this.chemin == null || this.indexCible >= this.chemin.size()) return;

        Point2D cibleActuelle = this.chemin.get(this.indexCible);


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
        return "ninji";
    }
}
