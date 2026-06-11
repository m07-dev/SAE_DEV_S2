package universite_paris8.iut.mfofana.sae_dev_app_test.vue;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import universite_paris8.iut.mfofana.sae_dev_app_test.LANCEMENT_JEU;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Ennemis;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Tour;

public class GestionAnimation {
    private Pane paneAnim;
    private static final double TILE = 32;

    public GestionAnimation(Pane paneA) {
        this.paneAnim = paneA;
    }

    public void animationTirBouleFeu(Tour tour, Ennemis cible) {
        String cheminImage = String.valueOf(LANCEMENT_JEU.class.getResource("/universite_paris8/iut/mfofana/sae_dev_app_test/Personnages/bouleFeu.png"));

        ImageView bouleFeu = new ImageView(new Image(cheminImage));
        double tailleBF = 15;
        bouleFeu.setFitWidth(tailleBF);
        bouleFeu.setFitHeight(tailleBF);
        bouleFeu.setPreserveRatio(true);

        double departX = (tour.getX() * TILE + TILE / 2.0) - (tailleBF / 2.0);
        double departY = (tour.getY() * TILE + TILE / 2.0) - (tailleBF / 2.0);

        bouleFeu.setX(departX);
        bouleFeu.setY(departY);

        paneAnim.getChildren().add(bouleFeu);

        double arriveeX = (cible.getX() * TILE + TILE / 2.0) - (tailleBF / 2.0);
        double arriveeY = (cible.getY() * TILE + TILE / 2.0) - (tailleBF / 2.0);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.2),bouleFeu);
        transition.setToX(arriveeX - departX);
        transition.setToY(arriveeY - departY);

        transition.play();
        transition.setOnFinished(ev -> paneAnim.getChildren().remove(bouleFeu));
    }
}
