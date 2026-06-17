package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Ennemis;

import java.util.List;


public abstract class Projectile {
    private Ennemis cible;
    private Tour tour;
    private int degat;
    private DoubleProperty x,y;
    private double vitesse = 5.2 / 60.0;
    private boolean estActif= true;
    private List<Ennemis> ennemis;


    public Projectile(int degat, Ennemis cible, double x, double y, Tour tour,List<Ennemis> ennemis){
        this.degat = degat;
        this.cible = cible;
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.tour = tour;
        this.ennemis = ennemis;
    }

    public void seDeplacer(){
        if(cible == null || cible.estMort()){
            return;
        }
        double xCible = cible.getX();
        double yCible = cible.getY();

        double dx = xCible - this.x.get();
        double dy = yCible - this.y.get();

        double distance = Math.hypot(this.x.get() - xCible, this.y.get() - yCible);

        dx = dx / distance;
        dy = dy / distance;

        if(distance > 0){
            this.x.set(this.x.get() + (dx * vitesse));
            this.y.set(this.y.get() + (dy * vitesse));
        }
        if (aAtteintCible()){
            if(estActif){
                cible.subirDegat(degat);
                tour.appliquerEffet(cible,ennemis   );
                estActif = false;
            }
        }
    }

    public boolean aAtteintCible(){
        if(cible == null || cible.estMort()){
            return true;
        }

        double dx = cible.getX() - this.x.get();
        double dy = cible.getY() - this.y.get();

        double distance = Math.hypot(dx,dy);

        return distance < vitesse;
    }

    public DoubleProperty getXProperty(){
        return this.x;
    }

    public DoubleProperty getYProperty(){
        return this.y;
    }

    public boolean isEstActif() {
        return estActif;
    }

    public abstract String nomProjectile();
}