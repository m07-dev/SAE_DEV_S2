package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Jeu;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Ennemis;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.*;
import universite_paris8.iut.mfofana.sae_dev_app_test.vue.*;

import java.util.HashMap;
import java.util.List;

public class Controleur {

    public Button boutonVague;
    @FXML public Button boutonObstacle;
    @FXML private VBox panneauTour;
    @FXML private ImageView imageTourSelectionnee;
    @FXML private Label labelNiveau, labelDegat, labelPortee, labelCadence, labelResistance;
    @FXML private Button boutonAmeliorer, boutonVendre;
    private Tour tourCliquee = null;
    // --- Labels UI ---
    @FXML private Label Solde;
    @FXML private Label labelPvChateau;
    @FXML private Label labelVague;
    @FXML private Label labelCountdown;
    @FXML private VBox zoneInfoTour;


    // --- Composants UI ---
    @FXML private Pane paneId;
    @FXML private TilePane panneTerrain;

    // --- Modèle et Vue ---
    private Jeu jeu;
    private Timeline gameLoop;
    private HashMap<Ennemis, EnnemiVue> affichageEnnemis = new HashMap<>();
    private HashMap<Tour, TourVue> affichageTour = new HashMap<>();
    private HashMap<Projectile, ProjectileVue> affichageProjectile = new HashMap<>();
    // --- Placement de tours ---
    private String tourSelectionnee = null;
    private static final int TILE = 32;

    @FXML
    public void initialize() {

        // 1. Créer le modèle
        jeu = new Jeu();
        ListenerListeEnnemis listeerennemi = new ListenerListeEnnemis(paneId, affichageEnnemis);
        ListernerListeTour listeTour = new ListernerListeTour(paneId, affichageTour,t -> afficherPanneauTour(t));
        ListenerListeProjectile listenerProjectile = new ListenerListeProjectile(paneId, affichageProjectile);
        jeu.getProjectiles().addListener(listenerProjectile);
        jeu.getEnnemis().addListener(listeerennemi);
        jeu.getTours().addListener(listeTour);
        Solde.textProperty().bind(
                jeu.piecesProperty().asString("Solde : %d$"));
        labelPvChateau.textProperty().bind(
                jeu.getChateau().pvProperty().asString("Château : %d PV"));
        labelVague.textProperty().bind(
                jeu.numeroVagueProperty().asString("Vague : %d"));

        new TerrainVue(panneTerrain, paneId ,jeu.getTerrain()).dessiner();

        // 6. Placement de tours
        placerTourTerrain();


        // 5. Game loop → juste jeu.tick() !
        gameLoop = new Timeline(
                new KeyFrame(Duration.seconds(0.017), ev -> {
                    jeu.tick();
                    labelCountdown.setText(jeu.getCountdownText());
                    int vague = jeu.getNumeroVague();
                    boolean obstacleInterdit = (vague % 4 == 0 || vague % 4 == 1);
                    boutonObstacle.setDisable(obstacleInterdit);
                    // Game Over
                    if (jeu.estTermine()) {
                        gameLoop.stop();
                        NavigationManager.allerVersGameOver();
                    }
                })
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();


    }


    //Fonction FXML
    @FXML
    public void clicContinuer(){
        gameLoop.play();
    }

    @FXML
    public void clicLancerVague() {
        jeu.forcerLancement();
    }

    @FXML
    public void clicPause() {
        System.out.println("DEBUG : clicPause déclenché !");
        gameLoop.pause();
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
    @FXML
    public void afficherPanneauTour(Tour t) {
        tourCliquee = t;
        labelNiveau.setText("Niveau : " + t.getNiveau());
        labelDegat.setText("Dégâts : " + t.getDegat());
        labelPortee.setText("Portée : " + t.getPortee());
        labelCadence.setText("Cadence : " + t.getCadence());
        labelResistance.setText("Résistance : " + t.getResistance());

        TourVue tv = affichageTour.get(t);
        if (tv != null) imageTourSelectionnee.setImage(tv.getImage());
        
        int coutProchain = t.getNiveau() * 25;
        boutonAmeliorer.setText("Améliorer (" + coutProchain + "$)");
        boutonAmeliorer.setDisable(t.getNiveau() >= 3);
        panneauTour.setVisible(true);
        panneauTour.setManaged(true);
    }

    @FXML
    public void clicAmeliorer() {
        if (tourCliquee == null) return;
        if (jeu.getNumeroVague() < 4) {
            System.out.println("Amélioration disponible à partir de la vague 4 !");
            return;
        }
        int cout = tourCliquee.getNiveau() * 25;
        boolean succes = jeu.ameliorerTour(tourCliquee, cout);
        if (succes) {
            afficherPanneauTour(tourCliquee);
        }
    }
    @FXML
    public void clicFermerPanneau() {
        tourCliquee = null;
        panneauTour.setVisible(false);
        panneauTour.setManaged(false);
    }
    @FXML
    public void clicVendre() {
        if (tourCliquee == null) return;
        jeu.vendreTour(tourCliquee);
        tourCliquee = null;
        clicFermerPanneau();
    }




    // --- Placement de tours ---
    public void placerTourTerrain() {
        panneTerrain.setOnMouseClicked(e -> {
            System.err.println("Clic reçu sur le terrain !");
            if (tourSelectionnee == null) return;

            int col   = (int)(e.getX() / TILE);
            int ligne = (int)(e.getY() / TILE);

            // 1. Vérifier les limites de la grille
            if (col < 0 || col >= jeu.getTerrain().getNbColonnes()
                    || ligne < 0 || ligne >= jeu.getTerrain().getNbLignes()) return;

            int typeCase = jeu.getTerrain().getTileTerrain(ligne, col);

            if (tourSelectionnee.equals("OBSTACLE")) {
                if (typeCase != 1) return; // Obstacle sur chemin
            } else {
                if (typeCase != 0) return; // Tours sur herbe
            }

            // 3. Pose de la tour (sans condition supplémentaire sur la vague)
            Tour nouvelleTour = null;
            int cout = 0;

            switch (tourSelectionnee) {
                case "FEU"      -> { nouvelleTour = new TourBouleDeFeu(col, ligne, 4, 3);     cout = 15; }
                case "BOMBE"    -> { nouvelleTour = new TourBombe(col, ligne, 2);             cout = 50; }
                case "GLACE"    -> { nouvelleTour = new TourBouleDeGlace(col, ligne, 2, 4);  cout = 15; }
                case "OBSTACLE" -> { nouvelleTour = new TourObstacle(col, ligne, "OBSTACLE"); cout = 15; }
            }

            if (nouvelleTour != null && jeu.getPieces() >= cout) {
                if(nouvelleTour instanceof TourObstacle){
                    if(jeu.getNombreObstacles() >=3) {
                        System.out.printf("Limites d'obstacle atteinte !");
                        return;
                    }
                }
                jeu.poserTour(nouvelleTour, cout);
                tourSelectionnee = null;
            }
        });
    }


}