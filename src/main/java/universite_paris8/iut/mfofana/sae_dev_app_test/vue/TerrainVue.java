package universite_paris8.iut.mfofana.sae_dev_app_test.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

public class TerrainVue {

    private static final String BASE =
            "/universite_paris8/iut/mfofana/sae_dev_app_test/Personnages/";

    private final TilePane pane;     // la grille (tuiles)
    private final Pane paneParent;   // le Pane qui contient la grille (pour poser le chateau)
    private final Terrain terrain;

    public TerrainVue(TilePane pane, Pane paneParent, Terrain terrain) {
        this.pane = pane;
        this.paneParent = paneParent;
        this.terrain = terrain;
    }

    public void dessiner() {
        final int T = Terrain.TAILLE_TUILE;
        final int R = terrain.getNbLignes();
        final int C = terrain.getNbColonnes();

        // Textures de sol (chargees une seule fois, partagees)
        Image solVert      = charger("tuile_herbe.png");
        Image solBleu      = charger("sol_bleu.png");
        Image solBeige     = charger("sol_beige.png");
        Image solBleuFonce = charger("sol_bleu_fonce.png");
        Image tuyau = charger ("Obstacle.png");

        // Configuration de la grille
        pane.getChildren().clear();
        pane.setPrefColumns(C);
        pane.setPrefRows(R);
        pane.setPrefTileWidth(T);
        pane.setPrefTileHeight(T);
        pane.setHgap(0);
        pane.setVgap(0);
        pane.setPrefSize(C * T, R * T);
        pane.setMaxSize(C * T, R * T);

        for (int l = 0; l < R; l++) {
            for (int c = 0; c < C; c++) {
                Image sol = textureZone(l, c, R, C,
                        solVert, solBleu, solBeige, solBleuFonce);

                ImageView fond = new ImageView(sol);
                fond.setFitWidth(T);
                fond.setFitHeight(T);

                StackPane cellule = new StackPane(fond);
                cellule.setPrefSize(T, T);
                cellule.setMinSize(T, T);
                cellule.setMaxSize(T, T);

                // Chemin = rectangle noir epais par-dessus le sol
                if (terrain.getTileTerrain(l, c) == Terrain.CHEMIN) {
                    Rectangle noir = new Rectangle(T, T, Color.rgb(15, 15, 15));
                    cellule.getChildren().add(noir);
                }else if (terrain.getTileTerrain(l,c) == Terrain.DECOR_VERT) {
                    ImageView decor = new ImageView(tuyau);
                    decor.setFitWidth(T);
                    decor.setFitHeight(T);
                    cellule.getChildren().add(decor);
                }

                pane.getChildren().add(cellule);
            }
        }

        dessinerChateau(T);
    }

    private void dessinerChateau(int T) {
        if (paneParent == null || terrain.getChateauTaille() <= 0) return;

        ImageView chateau = new ImageView(charger("Chateau.png"));
        int taille = terrain.getChateauTaille();
        chateau.setFitWidth(taille * T);
        chateau.setFitHeight(taille * T);
        chateau.setLayoutX(terrain.getChateauColonne() * T);
        chateau.setLayoutY(terrain.getChateauLigne() * T);
        chateau.setMouseTransparent(true); // ne bloque pas les clics de placement
        paneParent.getChildren().add(chateau);
    }

    /** Choix de la texture selon le quadrant de la case. */
    private Image textureZone(int l, int c, int R, int C,
                              Image vert, Image bleu, Image beige, Image bleuFonce) {
        boolean haut = l < R / 2;
        boolean gauche = c < C / 2;
        if (haut && gauche) return vert;        // zone verte (haut-gauche)
        if (haut)           return bleu;        // zone cyan (haut-droite)
        if (gauche)         return beige;       // zone jaune (bas-gauche)
        return bleuFonce;                        // zone violette (bas-droite)
    }

    private Image charger(String nom) {
        return new Image(getClass().getResourceAsStream(BASE + nom));
    }
}
