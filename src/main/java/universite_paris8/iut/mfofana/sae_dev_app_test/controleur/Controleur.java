package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.*;
import universite_paris8.iut.mfofana.sae_dev_app_test.vue.TerrainVue;

import java.util.ArrayList;
import java.util.List;

public class Controleur {

    public Label Solde;
    @FXML private Pane paneId;
    @FXML private TilePane panneTerrain;
    private int pieces = 100;
    private String tourSelectionnee = null;
    private ObservableList<Tour> toursPlacees = FXCollections.observableArrayList();
    private Terrain terrain;
    private List<int[]> chemin;    // le chemin ordonné
    private int indexChemin = 0;   // position actuelle de l'ennemi dans le chemin

    private Circle ennemi;
    private Timeline gameLoop;

    // Taille d'une tile en pixels (adapte à ta TerrainVue)
    private static final int TILE = 30;

    public void clicBoutonTourFeu(){
        if (pieces >= 15) {
            tourSelectionnee = "FEU";
            System.out.println("Tour de Feu sélectionnée ! Cliquez sur l'herbe pour la placer.");
            pieces -= 15;
            Solde.setText("Solde : " + pieces+"$");
        } else {
            System.out.println("Fonds insuffisants pour la Tour de Feu !");
        }
    }
    public void clicBoutonTourBombe(){
        if (pieces >= 50) {
            tourSelectionnee = "BOMBE";
            System.out.println("Tour de BOMBE sélectionnée ! Cliquez sur l'herbe pour la placer.");
            pieces -= 50;
            Solde.setText("Solde : " + pieces+"$");
        } else {
            System.out.println("Fonds insuffisants pour la Tour de BOMBE !");
        }
    }
    public void clicBoutonTourGlace(){
        if (pieces >= 15) {
            tourSelectionnee = "GLACE";
            System.out.println("Tour de GLACE sélectionnée ! Cliquez sur l'herbe pour la placer.");
            pieces -= 15;
            Solde.setText("Solde : " + pieces+"$");
        } else {
            System.out.println("Fonds insuffisants pour la Tour de GLACE !");
        }
    }
    public void clicBoutonTourObstacle(){
        if (pieces >= 15) {
            tourSelectionnee = "OBSTACLE";
            System.out.println("Tour de OBSTACLE sélectionnée ! Cliquez sur l'herbe pour la placer.");
            pieces -= 15;
            Solde.setText("Solde : " + pieces+"$");
        } else {
            System.out.println("Fonds insuffisants pour la Tour de OBSTACLE !");
        }
    }

    @FXML
  public void initialize() {

        terrain = new Terrain();

        // ---  On trouve le chemin le plus court ---
        chemin = terrain.extraireChemin(0, 0);
        System.out.println("Chemin trouvé : " + chemin.size() + " cases");


        TerrainVue terrainVue = new TerrainVue(panneTerrain, terrain);
        terrainVue.dessiner();


        ennemi = new Circle(10, Color.BLACK);
        placerSurCase(0);  // on le place sur la première case du chemin
        paneId.getChildren().add(ennemi);


        initAnimation();
        placerTourTerrain();
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
                Duration.seconds(0.08),  // toutes les 80ms → ~12 cases/sec
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
    public void placerTourTerrain() {
        panneTerrain.setOnMouseClicked(e -> {
            if (tourSelectionnee != null) {
                int col   = (int)(e.getX() / TILE);
                int ligne = (int)(e.getY() / TILE);

                if (terrain.getTileTerrain(col, ligne) == 0) {
                    Tour nouvelleTour = null;
                    int cout = 0;

                    switch (tourSelectionnee) {
                        case "FEU"      -> { nouvelleTour = new TourBouleDeFeu(col, ligne, 4, 3);    cout = 25; }
                        case "BOMBE"    -> { nouvelleTour = new TourBombe(col, ligne, 2);            cout = 50; }
                        case "GLACE"    -> { nouvelleTour = new TourBouleDeGlace(col, ligne, 2, 4); cout = 10; }
                        case "OBSTACLE" -> { nouvelleTour = new TourObstacle(col, ligne, "OBSTACLE"); cout = 10; }
                    }

                    if (nouvelleTour != null && pieces >= cout) {
                        pieces -= cout;
                        Solde.setText("Solde : " + pieces + "$");
                        toursPlacees.add(nouvelleTour);
                        afficherTour(nouvelleTour);
                        tourSelectionnee = null;
                    } else {
                        System.out.println("Fonds insuffisants !");
                    }
                }
            }
        });
    }

    public void afficherTour(Tour t) {  // prend la tour en paramètre
        Circle cercle = new Circle(TILE / 2.0);
        cercle.setCenterX(t.getX() * TILE + TILE / 2.0);
        cercle.setCenterY(t.getY() * TILE + TILE / 2.0);

        // Couleur selon le type
        if (t instanceof TourBouleDeFeu)   cercle.setFill(Color.ORANGE);
        else if (t instanceof TourBombe)   cercle.setFill(Color.GRAY);
        else if (t instanceof TourBouleDeGlace) cercle.setFill(Color.CYAN);
        else if (t instanceof TourObstacle)     cercle.setFill(Color.BROWN);

        paneId.getChildren().add(cercle);
    }

}