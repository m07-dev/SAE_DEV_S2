package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class Entite {
    // DoubleProperty → la vue peut binder sa position dessus
    private DoubleProperty x = new SimpleDoubleProperty();
    private DoubleProperty y = new SimpleDoubleProperty();
    private double vitesse;

    public Entite(double x, double y, double vitesse) {
        this.x.set(x);      // on initialise via .set()
        this.y.set(y);          // car c'est une Property, pas un double simple
        this.vitesse = vitesse;
    }

    public double getX() { return x.get(); }
    public double getY() { return y.get(); }
    public double getVitesse() { return vitesse; }

    public void setX(double x) { this.x.set(x); }
    public void setY(double y) { this.y.set(y); }
    public void setVitesse(double vitesse) { this.vitesse = vitesse; }

    public DoubleProperty xProperty()  { return x; }
    public DoubleProperty yProperty()  { return y; }
}
