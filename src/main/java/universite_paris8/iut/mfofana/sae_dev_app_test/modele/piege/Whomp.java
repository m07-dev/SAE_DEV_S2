package universite_paris8.iut.mfofana.sae_dev_app_test.modele.piege;

import javafx.collections.ObservableList;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Ennemis;

/**
 * Piege Whomp : un bloc de pierre qui tombe sur une case.
 * Il gele tous les ennemis presents dans son rayon, puis disparait (usage unique).
 * S'il n'y a aucun ennemi a portee, il disparait quand meme.
 */
public class Whomp extends Piege {

    // 1 tick = ~1/60 s. 180 ticks = 3 secondes de gel.
    private static final int DUREE_GEL = 180;

    public Whomp(double x, double y) {
        // rayon = 4 cases, cout = 30 pieces
        super(x, y, 4, 30);
    }

    @Override
    public void declencher(ObservableList<Ennemis> ennemis) {
        for (Ennemis e : ennemis) {
            if (dansRayon(e)) {
                e.setGele(DUREE_GEL); // fige l'ennemi 3 secondes
            }
        }
        marquerUtilise();
    }
}
