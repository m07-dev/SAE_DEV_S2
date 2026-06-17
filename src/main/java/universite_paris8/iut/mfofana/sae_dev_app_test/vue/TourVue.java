package universite_paris8.iut.mfofana.sae_dev_app_test.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.*;

import java.util.function.Consumer;

public class TourVue extends VBox {
    private Tour tour;
    private Pane pane;
    private Consumer<Tour> onClic;
    private static final int TILE = 32;
    private static final String BASE =
            "/universite_paris8/iut/mfofana/sae_dev_app_test/Personnages/";

    public TourVue(Pane p, Tour t, Consumer<Tour> onClic){
        super();
        this.pane = p;
        this.tour = t;
        creerSpriteTour(t);
        this.setTranslateX(t.getX() * TILE);
        this.setTranslateY(t.getY() * TILE);
        pane.getChildren().add(this);
        this.setOnMouseClicked(e -> {
            onClic.accept(this.tour);
            e.consume(); // empêche la propagation vers panneTerrain
        });
    }

    public void creerSpriteTour(Tour t) {
        // Choisit les tours grace a la fonction
        Image img = charger(t.nomTour() + ".png");

        ImageView imageTour = new ImageView(img);
        imageTour.setFitWidth(TILE);
        imageTour.setFitHeight(TILE);
        imageTour.setPreserveRatio(true);
        this.getChildren().add(imageTour);

    }
    public Image getImage() {
        ImageView iv = (ImageView) this.getChildren().get(0);
        return iv.getImage();
    }

    public Image charger(String nom) {
        return new Image(getClass().getResourceAsStream(BASE + nom));
    }
}
