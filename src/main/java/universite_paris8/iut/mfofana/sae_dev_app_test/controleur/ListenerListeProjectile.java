package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Projectile;
import universite_paris8.iut.mfofana.sae_dev_app_test.vue.ProjectileVue;

import java.util.HashMap;

public class ListenerListeProjectile implements ListChangeListener<Projectile> {
    private HashMap<Projectile, ProjectileVue> affichageProjectile = new HashMap<>();
    private Pane pane;

    public ListenerListeProjectile(Pane pane, HashMap<Projectile,ProjectileVue > affichageProjectile){
        this.pane = pane;
        this.affichageProjectile = affichageProjectile;
    }
    @Override
    public void onChanged(Change<? extends Projectile> changement) {
        while (changement.next()){
            if(changement.wasAdded()){
                for (Projectile projectile : changement.getAddedSubList()){
                    ProjectileVue pVue = new ProjectileVue(pane,projectile);
                    affichageProjectile.put(projectile,pVue);
                }
            }
            if(changement.wasRemoved()){
                for (Projectile projectile : changement.getRemoved()){
                    ProjectileVue p = affichageProjectile.remove(projectile);
                    if(p != null) {
                        pane.getChildren().remove(p);
                    }
                }
            }
        }
    }
}