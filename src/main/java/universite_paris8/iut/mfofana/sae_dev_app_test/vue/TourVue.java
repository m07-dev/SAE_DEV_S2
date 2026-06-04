package universite_paris8.iut.mfofana.sae_dev_app_test.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.*;

public class TourVue extends VBox {
    private Tour tour;
    private Pane pane;
    private static final int TILE = 32;
    private static final String BASE =
            "/universite_paris8/iut/mfofana/sae_dev_app_test/Personnages/";

    public TourVue(Pane p, Tour t){
        super();
        this.pane = p;
        this.tour = t;
        creerSpriteTour(t);
        pane.getChildren().add(this);
    }

    public void creerSpriteTour(Tour t) {
        Image img;
        if (t instanceof TourBouleDeFeu)       img = charger("Feu.png");
        else if (t instanceof TourBombe)       img = charger("Bombe.png");
        else if (t instanceof TourBouleDeGlace) img = charger("Glace.png");
        else if (t instanceof TourObstacle)     img = charger("Obstacle.png");
        else                                    img = charger("Erreur.png");



        ImageView imageTour = new ImageView(img);
        imageTour.setFitWidth(TILE);
        imageTour.setFitHeight(TILE);
        imageTour.setPreserveRatio(true);

        /*imageTour.xProperty().bind(t.xProperty().multiply(TILE));*/
        imageTour.setTranslateX(t.getX() * TILE);
        imageTour.setTranslateY(t.getY() * TILE);
        /*imageTour.yProperty().bind(t.yProperty().multiply(TILE));*/

        this.getChildren().add(imageTour);
        /*affichageTours.put(t, imageTour);*/
    }

    public Image charger(String nom) {
        return new Image(getClass().getResourceAsStream(BASE + nom));
    }
}
