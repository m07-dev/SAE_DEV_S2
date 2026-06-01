package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import universite_paris8.iut.mfofana.sae_dev_app_test.HelloApplication;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.*;
import universite_paris8.iut.mfofana.sae_dev_app_test.vue.TerrainVue;

import java.util.ArrayList;
import java.util.List;

public class Controleur {
    @FXML private Label labelPvChateau;
    @FXML private Label labelVague;
    @FXML private Label labelCountdown;

    private int numeroVague = 0;
    private boolean entreVagues = false; // true = on est dans le compte à rebours

    @FXML private Label Solde;
    @FXML private Pane paneId;
    @FXML private TilePane panneTerrain;
    private int pieces = 100;
    private String tourSelectionnee = null;
    private ObservableList<Tour> toursPlacees = FXCollections.observableArrayList();
    private Terrain terrain;
    private List<int[]> chemin;    // le chemin ordonné
    private int indexChemin = 0;   // position actuelle de l'ennemi dans le chemin
    private Chateau chateau;
    private List<Personnage> ennemis = new ArrayList<>();
    private List<javafx.scene.Node> ennemisVue = new ArrayList<>();
    private List<Integer> indexChemins = new ArrayList<>();
    private List<List<int[]>> chemins = new ArrayList<>();

    private Timeline gameLoop;

    private static final int[] HAUT_GAUCHE = {0,0};
    private static final int[] HAUT_DROIT = {6,20};
    private static final int[] BAS_GAUCHE = {29,9};
    private static final int[] BAS_DROIT = {31,31};
    private ImageView img;

    // Taille d'une tile en pixels (adapte à ta TerrainVue)
    private static final int TILE = 30;

    public void clicBoutonTourFeu(){
        if (pieces >= 15) {
            tourSelectionnee = "FEU";
            System.out.println("Tour de Feu sélectionnée ! Cliquez sur l'herbe pour la placer.");
            Solde.setText("Solde : " + pieces+"$");
        } else {
            System.out.println("Fonds insuffisants pour la Tour de Feu !");
        }
    }
    public void clicBoutonTourBombe(){
        if (pieces >= 50) {
            tourSelectionnee = "BOMBE";
            System.out.println("Tour de BOMBE sélectionnée ! Cliquez sur l'herbe pour la placer.");
            Solde.setText("Solde : " + pieces+"$");
        } else {
            System.out.println("Fonds insuffisants pour la Tour de BOMBE !");
        }
    }
    public void clicBoutonTourGlace(){
        if (pieces >= 15) {
            tourSelectionnee = "GLACE";
            System.out.println("Tour de GLACE sélectionnée ! Cliquez sur l'herbe pour la placer.");
            Solde.setText("Solde : " + pieces+"$");
        } else {
            System.out.println("Fonds insuffisants pour la Tour de GLACE !");
        }
    }
    public void clicBoutonTourObstacle(){
        if (pieces >= 15) {
            tourSelectionnee = "OBSTACLE";
            System.out.println("Tour de OBSTACLE sélectionnée ! Cliquez sur l'herbe pour la placer.");
            Solde.setText("Solde : " + pieces+"$");
        } else {
            System.out.println("Fonds insuffisants pour la Tour de OBSTACLE !");
        }
    }

    private void spawnEnnemis(String typeE, int[] coin) {

        List<int[]> cheminEnnemi = terrain.extraireChemin(coin[0], coin[1]);// Retourne liste de chemins

        if (cheminEnnemi == null || cheminEnnemi.isEmpty()) {
            System.out.println("Pas de chemin trouvé depuis ce coin !");
            return;
        }


        Personnage modele = switch (typeE) {
            case "BOBOMB" -> new Bobomb(coin[1], coin[0], terrain);
            case "SOLDAT" -> new Soldat(coin[1], coin[0], terrain);
            case "BOSS"   -> new Boss(coin[1],coin[0], terrain);
            default       -> new Soldat(coin[1], coin[0], terrain);
        };

        String cheminImage = String.valueOf(HelloApplication.class.getResource("images/bobomb.png"));



        ImageView iv;
            iv = new ImageView(new Image(cheminImage));
            iv.setFitWidth(TILE);
            iv.setFitHeight(TILE);
            iv.setPreserveRatio(true);
            iv.setX(32 * TILE);
            iv.setY(32   * TILE);

        paneId.getChildren().add(iv);
        ennemis.add(modele);
        ennemisVue.add(iv);
        chemins.add(cheminEnnemi);
        indexChemins.add(0);
    }

    @FXML
  public void initialize() {

        terrain = new Terrain();
        chateau = new Chateau();

        chemin = terrain.extraireChemin(0, 0);
        System.out.println("Chemin trouvé : " + chemin.size() + " cases");


        TerrainVue terrainVue = new TerrainVue(panneTerrain, terrain);
        terrainVue.dessiner();

        spawnEnnemis("SOLDAT", HAUT_GAUCHE);
        spawnEnnemis("SOLDAT", HAUT_DROIT);
        spawnEnnemis("BOSS", BAS_GAUCHE);
        demarrerProchainerVague();




        initAnimation();
        placerTourTerrain();
        gameLoop.play();
    }

    private int tickCount = 0; // ← ajoute ce champ en haut de la classe

    private void initAnimation() {
        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.1), // 100ms par tick → vitesse jouable
                ev -> {
                    // Dans le KeyFrame, tout au début
                    labelPvChateau.setText("Château : " + chateau.getPv() + " PV");
                    tickCount++;

                    // 1. Mise à jour des effets (brûlure, ralentissement)
                    for (Personnage p : ennemis) {
                        p.mettreAJourEffets();
                    }

                    // 2. Vérification des morts
                    for (int i = ennemis.size() - 1; i >= 0; i--) {
                        if (ennemis.get(i).estMort()) {
                            System.out.println("Ennemi tué ! Récompense : " + ennemis.get(i).getRecompense());
                            pieces += ennemis.get(i).getRecompense();
                            Solde.setText("Solde : " + pieces + "$");
                            paneId.getChildren().remove(ennemisVue.get(i));
                            ennemis.remove(i);
                            ennemisVue.remove(i);
                            chemins.remove(i);
                            indexChemins.remove(i);
                        }
                    }

                    // 3. Déplacement des ennemis
                    for (int i = 0; i < ennemis.size(); i++) {
                        int index = indexChemins.get(i);
                        List<int[]> ch = chemins.get(i);
                        Personnage ennemi = ennemis.get(i);

                        if (index < ch.size() - 1) {
                            // Vitesse 4 → avance tous les 1 tick
                            // Vitesse 2 → avance tous les 2 ticks
                            // Vitesse 1 → avance tous les 4 ticks
                            int intervalle = Math.max(1, 4 / Math.max(1, ennemi.getVitesse()));
                            if (tickCount % intervalle == 0) {
                                int nouvelIndex = index + 1;
                                indexChemins.set(i, nouvelIndex);

                                int ligne   = ch.get(nouvelIndex)[0];
                                int colonne = ch.get(nouvelIndex)[1];

                                ennemi.setX(colonne);
                                ennemi.setY(ligne);

                                ((ImageView) ennemisVue.get(i)).setX(colonne * TILE);
                                ((ImageView) ennemisVue.get(i)).setY(ligne   * TILE);
                            }
                        } else {
                            chateau.subirDegat(10);
                            System.out.println("Ennemi arrivé ! PV château : " + chateau.getPv());
                            if (chateau.estDetruit()) {
                                gameLoop.stop();
                                System.out.println("GAME OVER !");
                            }
                            paneId.getChildren().remove(ennemisVue.get(i));
                            ennemis.remove(i);
                            ennemisVue.remove(i);
                            chemins.remove(i);
                            indexChemins.remove(i);
                        }
                    }

                    // 4. Tir des tours
                    ObservableList<Personnage> ennemisObs = FXCollections.observableArrayList(ennemis);
                    for (Tour t : toursPlacees) {
                        t.tirer(ennemisObs);
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

    private void demarrerProchainerVague() {
        entreVagues = true;
        int delai = 5; // secondes avant la vague

        // Compte à rebours affiché
        Timeline countdown = new Timeline();
        countdown.setCycleCount(delai);
        final int[] secondesRestantes = {delai};

        KeyFrame kfCountdown = new KeyFrame(Duration.seconds(1), ev -> {
            secondesRestantes[0]--;
            if (secondesRestantes[0] > 0) {
                labelCountdown.setText("Vague dans : " + secondesRestantes[0] + "s");
            } else {
                labelCountdown.setText("Vague en cours !");
                lancerVague();
                entreVagues = false;
            }
        });

        countdown.getKeyFrames().add(kfCountdown);
        labelCountdown.setText("Vague dans : " + delai + "s");
        countdown.play();
    }

    private void lancerVague() {
        numeroVague++;
        labelVague.setText("Vague : " + numeroVague);

        // Nombre d'ennemis augmente avec les vagues
        int nbSoldats = 2 + numeroVague;        // vague 1 = 3, vague 2 = 4...
        int nbBobombs = numeroVague >= 3 ? 1 : 0; // Bobomb à partir de la vague 3

        // Spawn des soldats avec délai entre chaque
        for (int i = 0; i < nbSoldats; i++) {
            final int index = i;
            Timeline delaiSpawn = new Timeline(
                    new KeyFrame(Duration.seconds(index * 1.5), ev -> {
                        // Spawn depuis un coin aléatoire
                        int[] coin = (index % 2 == 0) ? HAUT_GAUCHE : HAUT_DROIT;
                        spawnEnnemis("SOLDAT", coin);
                    })
            );
            delaiSpawn.play();
        }

        // Spawn des Bobombs
        for (int i = 0; i < nbBobombs; i++) {
            final int index = i;
            Timeline delaiSpawn = new Timeline(
                    new KeyFrame(Duration.seconds(nbSoldats * 1.5 + index * 2.0), ev -> {
                        spawnEnnemis("BOBOMB", BAS_GAUCHE);
                    })
            );
            delaiSpawn.play();
        }

        // Déclencher la prochaine vague après que celle-ci soit terminée
        double dureeTotaleVague = (nbSoldats + nbBobombs) * 2.0 + 10.0;
        Timeline finVague = new Timeline(
                new KeyFrame(Duration.seconds(dureeTotaleVague), ev -> {
                    if (!chateau.estDetruit()) {
                        demarrerProchainerVague();
                    }
                })
        );
        finVague.play();
    }
}