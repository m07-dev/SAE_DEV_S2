package universite_paris8.iut.mfofana.sae_dev_app_test.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.*;

public class ProjectileVue extends VBox {
    private Pane pane;
    private Projectile p;

    private static final int TILE = 32;
    private static final String BASE =
            "/universite_paris8/iut/mfofana/sae_dev_app_test/Personnages/";
    private ImageView imgV;


    public ProjectileVue(Pane pane, Projectile p) {
        super();
        this.pane = pane;
        this.p = p;
        creerSpriteProjectile(p);
        pane.getChildren().add(this);
    }

    public void creerSpriteProjectile(Projectile p) {
        // Image selon le type d'ennemi
        Image img;
        if (p instanceof BouleFeu) {
            img = charger("bouleFeu.png");
        }
        else {
            img = charger("Feu.png");
        }

        /*else if (p instanceof BouleGlace) {
            img = charger("bouleGlace.png");
        } else if (p instanceof Bombe) {
            img = charger("bombe.png");
        } else if (p instanceof Obstacle) {
            img = charger("obstacle.png");
        }
           */

        imgV = new ImageView(img);
        imgV.setFitWidth(16);
        imgV.setFitHeight(16);

        // Position initiale
        actualiserPosition();
        pane.getChildren().add(imgV);
    }


    public void actualiserPosition() {
        imgV.setLayoutX(p.getX() * TILE);
        imgV.setLayoutY(p.getY() * TILE);
    }

    private Image charger(String nom) {
        return new Image(getClass().getResourceAsStream(BASE + nom));
    }
}
