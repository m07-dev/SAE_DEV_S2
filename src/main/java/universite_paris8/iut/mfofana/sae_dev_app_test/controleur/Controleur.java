package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.*;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.*;
import universite_paris8.iut.mfofana.sae_dev_app_test.vue.TerrainVue;

public class Controleur {


    @FXML private Label Solde;
    @FXML private Label labelPvChateau;
    @FXML private Label labelVague;
    @FXML private Label labelCountdown;


    @FXML private Pane paneId;
    @FXML private TilePane panneTerrain;


    private Terrain terrain;
    private Chateau chateau;


    private IntegerProperty pieces = new SimpleIntegerProperty(100);
    private IntegerProperty numeroVague = new SimpleIntegerProperty(0);
    private ObservableList<Tour> toursPlacees = FXCollections.observableArrayList();

    private GestionJeu gestionJeu;
    private GestionVagues gestionVagues;

    private String tourSelectionnee = null;
    private static final int TILE = 30;

    @FXML
    public void initialize() {
        terrain = new Terrain();
        chateau = new Chateau();

        Solde.textProperty().bind(pieces.asString("Solde : %d$"));
        labelPvChateau.textProperty().bind(chateau.pvProperty().asString("Château : %d PV"));
        labelVague.textProperty().bind(numeroVague.asString("Vague : %d"));

        new TerrainVue(panneTerrain, terrain).dessiner();

        gestionJeu = new GestionJeu(chateau, terrain, paneId, toursPlacees, pieces);
        gestionVagues = new GestionVagues(numeroVague, labelCountdown, chateau, gestionJeu);

        gestionJeu.demarrer();
        gestionVagues.demarrerProchainerVague();
        placerTourTerrain();
    }

    @FXML
    public void clicBoutonTourFeu() {
        if (pieces.get() >= 15) tourSelectionnee = "FEU";
        else System.out.println("Fonds insuffisants !");
    }

    @FXML
    public void clicBoutonTourBombe() {
        if (pieces.get() >= 50) tourSelectionnee = "BOMBE";
        else System.out.println("Fonds insuffisants !");
    }

    @FXML
    public void clicBoutonTourGlace() {
        if (pieces.get() >= 15) tourSelectionnee = "GLACE";
        else System.out.println("Fonds insuffisants !");
    }

    @FXML
    public void clicBoutonTourObstacle() {
        if (pieces.get() >= 15) tourSelectionnee = "OBSTACLE";
        else System.out.println("Fonds insuffisants !");
    }

    private void placerTourTerrain() {
        panneTerrain.setOnMouseClicked(e -> {
            if (tourSelectionnee == null) return;

            int col   = (int)(e.getX() / TILE);
            int ligne = (int)(e.getY() / TILE);

            if (terrain.getTileTerrain(col, ligne) != 0) return;

            Tour nouvelleTour = null;
            int cout = 0;

            switch (tourSelectionnee) {
                case "FEU"      -> { nouvelleTour = new TourBouleDeFeu(col, ligne, 4, 3);     cout = 15; }
                case "BOMBE"    -> { nouvelleTour = new TourBombe(col, ligne, 2);             cout = 50; }
                case "GLACE"    -> { nouvelleTour = new TourBouleDeGlace(col, ligne, 2, 4);  cout = 15; }
                case "OBSTACLE" -> { nouvelleTour = new TourObstacle(col, ligne, "OBSTACLE"); cout = 15; }
            }

            if (nouvelleTour != null && pieces.get() >= cout) {
                pieces.set(pieces.get() - cout);
                toursPlacees.add(nouvelleTour);
                afficherTour(nouvelleTour);
                tourSelectionnee = null;
            } else {
                System.out.println("Fonds insuffisants !");
            }
        });
    }

    private void afficherTour(Tour t) {
        Circle cercle = new Circle(TILE / 2.0);
        cercle.setCenterX(t.getX() * TILE + TILE / 2.0);
        cercle.setCenterY(t.getY() * TILE + TILE / 2.0);

        if (t instanceof TourBouleDeFeu)        cercle.setFill(Color.ORANGE);
        else if (t instanceof TourBombe)        cercle.setFill(Color.GRAY);
        else if (t instanceof TourBouleDeGlace) cercle.setFill(Color.CYAN);
        else if (t instanceof TourObstacle)     cercle.setFill(Color.BROWN);

        paneId.getChildren().add(cercle);
    }
}