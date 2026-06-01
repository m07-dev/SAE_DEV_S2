package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

public class Boss extends Personnage {

    public Boss(int x, int y, Terrain e) {
        super(x, y, e, 100, 25);
    }

    public String getImage() {
        return "universite_paris8/iut/mfofana/sae_dev_app_test/images/bobomb.png";
    }

    /*public javafx.scene.paint.Color getCouleur() {
        return Color.PURPLE;
    }

     */
}
