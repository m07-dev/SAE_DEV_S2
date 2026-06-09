package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

public class BouleFeu extends Projectile {

    private int effetBrulure;

    public BouleFeu(double depX, double depY, double cibleX, double cibleY, int degat, int effetBrulure ) {
        super(depX, depY, 1, degat,cibleX, cibleY);
        this.effetBrulure = effetBrulure;
    }

    @Override
    public void appliquerEffet(Personnage cible) {
        cible.subirDegat(effetBrulure);
    }

    @Override
    public String getTypeSprite() {
        return "BOULE-FEU";
    }
}
