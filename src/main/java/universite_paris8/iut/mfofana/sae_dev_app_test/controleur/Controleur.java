package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Jeu;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Personnage;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.*;
import universite_paris8.iut.mfofana.sae_dev_app_test.vue.*;

import java.util.HashMap;
import java.util.List;

public class Controleur {

    public Button boutonVague;
    // --- Labels UI ---
    @FXML private Label Solde;
    @FXML private Label labelPvChateau;
    @FXML private Label labelVague;
    @FXML private Label labelCountdown;


    // --- Composants UI ---
    @FXML private Pane paneId;
    @FXML private TilePane panneTerrain;

    // --- Modèle et Vue ---
    private Jeu jeu;
    private Timeline gameLoop;
    private HashMap<Personnage, EnnemiVue> affichageEnnemis = new HashMap<>();
    private HashMap<Tour, TourVue> affichageTour = new HashMap<>();
    // --- Placement de tours ---
    private String tourSelectionnee = null;
    private static final int TILE = 32;

    @FXML
    public void initialize() {

        // 1. Créer le modèle
        jeu = new Jeu();
        ListenerListeEnnemis listeerennemi = new ListenerListeEnnemis(paneId, affichageEnnemis);
        ListernerListeTour listeTour = new ListernerListeTour(paneId, affichageTour);
        jeu.getEnnemis().addListener(listeerennemi);
        jeu.getTours().addListener(listeTour);
        Solde.textProperty().bind(
                jeu.piecesProperty().asString("Solde : %d$"));
        labelPvChateau.textProperty().bind(
                jeu.getChateau().pvProperty().asString("Château : %d PV"));
        labelVague.textProperty().bind(
                jeu.numeroVagueProperty().asString("Vague : %d"));

        new TerrainVue(panneTerrain, paneId ,jeu.getTerrain()).dessiner();

        GestionAnimation gestionAnimation = new GestionAnimation(paneId);

        // 5. Game loop → juste jeu.tick() !
         gameLoop = new Timeline(
                new KeyFrame(Duration.seconds(0.1), ev -> {
                    List<Jeu.GestionJeu.AlerteTir> evenements = jeu.tick();

                    for (Jeu.GestionJeu.AlerteTir e : evenements) {
                        gestionAnimation.animationTirBouleFeu(e.tour, e.cible);
                    }
                    // Afficher le countdown entre vagues
                    labelCountdown.setText(jeu.getCountdownText());
                    // Game Over
                    if (jeu.estTermine()) {
                        gameLoop.stop();
                        System.out.println("GAME OVER !");
                    }
                })
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();

        // 6. Placement de tours
        placerTourTerrain();
    }

    @FXML
    public void clicLancerVague() {
        jeu.forcerLancement();
    }

    // --- Boutons tours ---
    @FXML
    public void clicBoutonTourFeu() {
        if (jeu.getPieces() >= 15) tourSelectionnee = "FEU";
        else System.out.println("Fonds insuffisants !");
    }

    @FXML
    public void clicBoutonTourBombe() {
        if (jeu.getPieces() >= 50) tourSelectionnee = "BOMBE";
        else System.out.println("Fonds insuffisants !");
    }

    @FXML
    public void clicBoutonTourGlace() {
        if (jeu.getPieces() >= 15) tourSelectionnee = "GLACE";
        else System.out.println("Fonds insuffisants !");
    }

    @FXML
    public void clicBoutonTourObstacle() {
        if (jeu.getPieces() >= 15) tourSelectionnee = "OBSTACLE";
        else System.out.println("Fonds insuffisants !");
    }

    // --- Placement de tours ---
    public void placerTourTerrain() {
        panneTerrain.setOnMouseClicked(e -> {
            if (tourSelectionnee == null) return;

            int col   = (int)(e.getX() / TILE);
            int ligne = (int)(e.getY() / TILE);
            if (col < 0 || col >= jeu.getTerrain().getNbColonnes()
                    || ligne < 0 || ligne >= jeu.getTerrain().getNbLignes()) return;
            if (jeu.getTerrain().getTileTerrain(ligne,col) != 0) return;

            Tour nouvelleTour = null;
            int cout = 0;

            switch (tourSelectionnee) {
                case "FEU"      -> { nouvelleTour = new TourBouleDeFeu(col, ligne, 4, 3);     cout = 15; }
                case "BOMBE"    -> { nouvelleTour = new TourBombe(col, ligne, 2);             cout = 50; }
                case "GLACE"    -> { nouvelleTour = new TourBouleDeGlace(col, ligne, 2, 4);  cout = 15; }
                case "OBSTACLE" -> { nouvelleTour = new TourObstacle(col, ligne, "OBSTACLE"); cout = 15; }
            }

            if (nouvelleTour != null && jeu.getPieces() >= cout) {
                jeu.poserTour(nouvelleTour, cout); // ← modèle gère tout
                tourSelectionnee = null;
            }
        });
    }


}