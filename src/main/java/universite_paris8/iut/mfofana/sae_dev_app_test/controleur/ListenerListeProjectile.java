package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Projectile;
import universite_paris8.iut.mfofana.sae_dev_app_test.vue.ProjectileVue;

import java.util.HashMap;
import java.util.List;

public class ListenerListeProjectile implements ListChangeListener<Projectile> {
    private HashMap<Projectile, ProjectileVue> affichageProjectile;
    private Pane pane;

    public ListenerListeProjectile(Pane pane, HashMap affichageProjectile){
        this.pane = pane;
        this.affichageProjectile = affichageProjectile;
    }

    @Override
    public void onChanged(Change<? extends Projectile> changement) {
        while(changement.next()) {
            if(changement.wasAdded()) {
                for (Projectile p : changement.getAddedSubList()) {
                    ProjectileVue pVue = new ProjectileVue(pane, p);
                    affichageProjectile.put(p, pVue);
                }
            }

            if (changement.wasRemoved()) {
                for (Projectile p : changement.getRemoved()) {
                    ProjectileVue pVue = new ProjectileVue(pane, p);
                    affichageProjectile.remove(p, pVue);
                }
            }

        }
    }

    public void actualiserTous() {
        for (ProjectileVue vue : affichageProjectile.values()) {
            vue.actualiserPosition();
        }
    }
}
