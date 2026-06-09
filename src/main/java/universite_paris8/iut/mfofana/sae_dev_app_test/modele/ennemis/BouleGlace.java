package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

public class BouleGlace extends Projectile {
    private int ralentissement;

    public BouleGlace(double depX, double depY, double cibleX, double cibleY, int degat, int effetBrulure) {
        super(depX, depY, 1, degat,cibleX, cibleY);
        this.ralentissement = ralentissement;
    }
    @Override
    public void appliquerEffet(Personnage cible) {
        cible.setRalenti(ralentissement);
    }

    @Override
    public String getTypeSprite() {
        return "BOULE-GLACE";
    }
}
