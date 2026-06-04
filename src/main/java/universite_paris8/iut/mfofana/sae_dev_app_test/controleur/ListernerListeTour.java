package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Tour;
import universite_paris8.iut.mfofana.sae_dev_app_test.vue.TourVue;

import java.util.HashMap;

public class ListernerListeTour implements ListChangeListener<Tour> {
    private HashMap<Tour, TourVue> affichageTour = new HashMap<>();
    private Pane pane;

    public ListernerListeTour(Pane pane, HashMap affichageTour){
        this.pane  = pane;
        this.affichageTour = affichageTour;
    }

    @Override
    public void onChanged(Change<? extends Tour> changement) {
        while (changement.next()) {

            if (changement.wasAdded()) {
                System.out.println("tour ajouté");
                for (Tour t : changement.getAddedSubList()) {
                    /*creerSpriteTour(t);*/
                    TourVue tv = new TourVue(pane, t);
                    affichageTour.put(t,tv);
                }
            }

            if (changement.wasRemoved()) {
                for (Tour t : changement.getRemoved()) {
                    System.out.println("tour supprimé");
                    /*supprimerSpriteTour(t);*/
                    TourVue tv = affichageTour.get(t);
                    pane.getChildren().remove(tv);
                    affichageTour.remove(t);
                }
            }
        }
    }
}
