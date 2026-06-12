package universite_paris8.iut.mfofana.sae_dev_app_test.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Projectile;

public class ProjectileVue extends VBox{
    private Projectile projectile;
    private Pane p;


    private static final int TILE = 32;
    private static final String BASE =
            "/universite_paris8/iut/mfofana/sae_dev_app_test/Personnages/";

    public ProjectileVue(Pane p, Projectile projectile){
        super();
        this.p = p;
        this.projectile = projectile;
        this.translateXProperty().bind(projectile.getXProperty().multiply(TILE));
        this.translateYProperty().bind(projectile.getYProperty().multiply(TILE));
        creerSpriteProjectile();
        p.getChildren().add(this);
    }


    public void creerSpriteProjectile(){
        ImageView iv = new ImageView(charger("bouleFeu.png"));
        iv.setFitWidth(TILE);
        iv.setFitHeight(TILE);
        this.getChildren().add(iv); // Important !
    }

    private Image charger(String nom) {
        return new Image(getClass().getResourceAsStream(BASE + nom));
    }
}