package universite_paris8.iut.mfofana.sae_dev_app_test.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.piege.Piege;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.piege.Whomp;

/**
 * Sprite d'un piege. Sur le meme modele que TourVue / ProjectileVue :
 * la vue ne fait qu'afficher le piege a sa position, elle ne contient
 * aucune logique de jeu.
 */
public class PiegeVue extends VBox {

    private static final int TILE = 32;
    private static final String BASE =
            "/universite_paris8/iut/mfofana/sae_dev_app_test/Personnages/";

    public PiegeVue(Pane pane, Piege piege) {
        super();
        creerSprite(piege);
        this.setTranslateX(piege.getX() * TILE);
        this.setTranslateY(piege.getY() * TILE);
        pane.getChildren().add(this);
    }

    private void creerSprite(Piege piege) {
        Image img;
        if (piege instanceof Whomp) img = charger("Whomp.png");
        else                        img = charger("Erreur.png");

        ImageView iv = new ImageView(img);
        iv.setFitWidth(TILE);
        iv.setFitHeight(TILE);
        iv.setPreserveRatio(true);
        this.getChildren().add(iv);
    }

    // Charge l'image ; si Whomp.png n'existe pas encore, retombe sur Skull.png.
    private Image charger(String nom) {
        var flux = getClass().getResourceAsStream(BASE + nom);
        if (flux == null) flux = getClass().getResourceAsStream(BASE + "Whomp.png");
        return new Image(flux);
    }
}
