package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

public abstract class Projectile extends Entite {

    private double dirX;
    private double dirY;
    private int degat;
    private boolean actif = true;

    public Projectile(double depX, double depY, double vitesse, int degat,
                      double cibleX, double cibleY) {
        super(depX, depY, vitesse);
        this.degat = degat;

        // Calcul de la direction du tire
        double dx = cibleX - depX;
        double dy = cibleY - depY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance == 0) {
            this.dirX = dx / distance;
            this.dirY = dy / distance;
        }
    }

    public boolean deplacer (java.util.List<Personnage> ennemis) {
        if (!actif) {
            return false;
        }

        setX(getX() + dirX * getVitesse());
        setY(getY() + dirY * getVitesse());

        for (Personnage p : ennemis) {
            double dx = p.getX() - getX();
            double dy = p.getY() - getY();
            double distance = Math.sqrt(dx * dx + dy + dy);

            if (distance < 0.6) {
                p.subirDegat(degat);
                appliquerEffet(p);
                actif = false;
                return true;
            }
        }
        return false;
    }

    public boolean isActif() {
        return actif;
    }

    public void desactiver() {
        actif = false;
    }

    public int getDegat() {
        return degat;
    }

    public double getDirX() {
        return dirX;
    }

    public double getDirY() {
        return dirY;
    }

    public abstract void appliquerEffet(Personnage cible);

    public abstract String getTypeSprite();
}
