package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.piege.Piege;
import universite_paris8.iut.mfofana.sae_dev_app_test.vue.PiegeVue;

import java.util.HashMap;

/**
 * Ecoute la liste observable des pieges du Jeu.
 * Ajout d'un piege  -> on cree son sprite.
 * Retrait d'un piege -> on efface son sprite.
 * C'est ce qui fait disparaitre le Whomp tout seul quand il s'est declenche.
 */
public class ListenerListePiege implements ListChangeListener<Piege> {

    private final HashMap<Piege, PiegeVue> affichagePiege;
    private final Pane pane;

    public ListenerListePiege(Pane pane, HashMap<Piege, PiegeVue> affichagePiege) {
        this.pane = pane;
        this.affichagePiege = affichagePiege;
    }

    @Override
    public void onChanged(Change<? extends Piege> changement) {
        while (changement.next()) {
            if (changement.wasAdded()) {
                for (Piege p : changement.getAddedSubList()) {
                    PiegeVue vue = new PiegeVue(pane, p);
                    affichagePiege.put(p, vue);
                }
            }
            if (changement.wasRemoved()) {
                for (Piege p : changement.getRemoved()) {
                    PiegeVue vue = affichagePiege.remove(p);
                    if (vue != null) pane.getChildren().remove(vue);
                }
            }
        }
    }
}
