package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;
import universite_paris8.iut.mfofana.sae_dev_app_test.vue.TerrainVue;

import java.util.List;

public class Controleur {

    @FXML private Pane paneId;
    @FXML private TilePane panneTerrain;

    private Terrain terrain;
    private List<int[]> chemin;
    private int indexChemin = 0;

    private Circle ennemi;
    private Timeline gameLoop;


    private static final int TILE = 30;

    @FXML
    public void initialize() {


        terrain = new Terrain();


        chemin = terrain.extraireChemin(0, 0);
        System.out.println("Chemin trouvé : " + chemin.size() + " cases");


        TerrainVue terrainVue = new TerrainVue(panneTerrain, terrain);
        terrainVue.dessiner();


        ennemi = new Circle(10, Color.BLACK);
        placerSurCase(0);
        paneId.getChildren().add(ennemi);

        // --- 5. LANCER LA GAME LOOP ---
        initAnimation();
        gameLoop.play();
    }

    /**
     * Place l'ennemi (cercle) au centre de la case n° index dans le chemin.
     */
    private void placerSurCase(int index) {
        int ligne   = chemin.get(index)[0]; // ligne   = axe Y
        int colonne = chemin.get(index)[1]; // colonne = axe X

        ennemi.setCenterX(colonne * TILE + TILE / 2.0);
        ennemi.setCenterY(ligne   * TILE + TILE / 2.0);
    }

    private void initAnimation() {
        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.07),  // toutes les 80ms → ~12 cases/sec
                ev -> {
                    // S'il reste des cases à parcourir
                    if (indexChemin < chemin.size() - 1) {
                        indexChemin++;
                        placerSurCase(indexChemin);
                    } else {
                        // L'ennemi a atteint la fin du chemin
                        System.out.println("Ennemi arrivé !");
                        paneId.getChildren().remove(ennemi);
                        gameLoop.stop();
                    }
                }
        );
        gameLoop.getKeyFrames().add(kf);
    }
}