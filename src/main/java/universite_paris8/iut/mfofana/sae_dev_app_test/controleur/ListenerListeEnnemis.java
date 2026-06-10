package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Ennemis;
import universite_paris8.iut.mfofana.sae_dev_app_test.vue.EnnemiVue;

import java.util.HashMap;

public class ListenerListeEnnemis implements ListChangeListener<Ennemis> {
    private HashMap<Ennemis, EnnemiVue> affichageEnnemis = new HashMap<>();
    private Pane pane;

    public ListenerListeEnnemis(Pane pane, HashMap affichageEnnemis){
        this.pane = pane;
        this.affichageEnnemis = affichageEnnemis;
    }

    @Override
    public void onChanged(Change<? extends Ennemis> changement) {
        while (changement.next()) {
            if (changement.wasAdded()) {
                System.out.println("nouvelle ennemi");
                for (Ennemis p : changement.getAddedSubList()) {
                    EnnemiVue e = new EnnemiVue(pane,p);
                    affichageEnnemis.put(p,e);

                }
            }

            // Un ennemi a été supprimé → on retire son sprite
            if (changement.wasRemoved()) {
                for (Ennemis p : changement.getRemoved()) {
                    System.out.println("ennemiSupprimer");
                   EnnemiVue e = affichageEnnemis.get(p);
                   pane.getChildren().remove(e);
                   affichageEnnemis.remove(p);
                }
            }
        }
    }
}
