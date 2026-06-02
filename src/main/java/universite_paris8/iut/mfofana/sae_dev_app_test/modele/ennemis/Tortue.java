package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

public class Tortue extends Personnage {

    public Tortue(int x, int y, Terrain terrain) {
        super(x, y, terrain, 50, 1);
    }

    @Override
    public int getRecompense() { return 5; }


}


