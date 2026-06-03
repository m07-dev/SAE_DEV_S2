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

    private static final String BASE = "/universite_paris8/iut/mfofana/sae_dev_app_test/Personnages/";

    private final TilePane pane;
    private final Pane paneParent;
    private final Terrain terrain;

    public TerrainVue(TilePane pane, Pane paneParent, Terrain terrain) {
        this.pane = pane;
        this.paneParent = paneParent;
        this.terrain = terrain;
    }

    public void dessiner() {
        final int Tuile = Terrain.TAILLE_TUILE;
        final int Lignes = terrain.getNbLignes();
        final int Colonnes = terrain.getNbColonnes();

        // 1. Configuration pure de la grille
        pane.getChildren().clear();
        pane.setPrefColumns(Colonnes);
        pane.setPrefRows(Lignes);
        pane.setPrefTileWidth(Tuile);
        pane.setPrefTileHeight(Tuile);

        // 2. Chargement du matériel (une seule fois)
        Image solVert      = charger("tuile_herbe.png");
        Image solBleu      = charger("sol_bleu.png");
        Image solBeige     = charger("sol_beige.png");
        Image solBleuFonce = charger("sol_bleu_fonce.png");
        Image imageTuyau   = charger("Obstacle.png");

        // 3. Construction de la carte, case par case
        for (int l = 0; l < Lignes; l++) {
            for (int c = 0; c < Colonnes; c++) {

                // -- ÉTAPE A : On pose le sol --
                Image texture = textureZone(l, c, Lignes, Colonnes, solVert, solBleu, solBeige, solBleuFonce);
                ImageView fond = new ImageView(texture);
                fond.setFitWidth(Tuile);
                fond.setFitHeight(Tuile);

                StackPane caseGrille = new StackPane(fond);

                // -- ÉTAPE B : On regarde s'il y a un objet par-dessus --
                int typeCase = terrain.getTileTerrain(l, c); // On ne pose la question qu'une seule fois !

                if (typeCase == Terrain.CHEMIN) {
                    caseGrille.getChildren().add(new Rectangle(Tuile, Tuile, Color.rgb(15, 15, 15)));
                }
                else if (typeCase == Terrain.DECOR_VERT) {
                    ImageView tuyau = new ImageView(imageTuyau);
                    tuyau.setFitWidth(Tuile);
                    tuyau.setFitHeight(Tuile);
                    caseGrille.getChildren().add(tuyau);
                }

                // -- ÉTAPE Colonnes : On range la case terminée dans la grille --
                pane.getChildren().add(caseGrille);
            }
        }

        // 4. On pose le château par-dessus tout le reste
        dessinerChateau(Tuile);
    }

    private void dessinerChateau(int T) {
        if (paneParent == null || terrain.getChateauTaille() <= 0) return;

        ImageView chateau = new ImageView(charger("Chateau.png"));
        int taille = terrain.getChateauTaille();

        chateau.setFitWidth(taille * T);
        chateau.setFitHeight(taille * T);
        chateau.setLayoutX(terrain.getChateauColonne() * T);
        chateau.setLayoutY(terrain.getChateauLigne() * T);
        chateau.setMouseTransparent(true);

        paneParent.getChildren().add(chateau);
    }

    private Image textureZone(int l, int c, int nbLignes, int nbColonnes, Image vert, Image bleu, Image beige, Image bleuFonce) {
        boolean enHaut = (l < nbLignes / 2);
        boolean aGauche = (c < nbColonnes / 2);

        if (enHaut) {
            // On est dans la moitié HAUTE
            if (aGauche) return vert;
            else         return bleu;

        } else {
            // On est dans la moitié BASSE
            if (aGauche) return beige;
            else         return bleuFonce;
        }
    }

    private Image charger(String nom) {
        return new Image(getClass().getResourceAsStream(BASE + nom));
    }
}