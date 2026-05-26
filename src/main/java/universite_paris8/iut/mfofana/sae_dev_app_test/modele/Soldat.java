package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import javafx.scene.paint.Color;

import java.util.Random;

public class Soldat extends Personnage {

    public Soldat(int x, int y , Terrain terrain) {
        super(x,y,terrain, 50, 4);
    }

    public javafx.scene.paint.Color getCouleur() {
        return Color.YELLOW;
    }



}
