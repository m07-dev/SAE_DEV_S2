package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.*;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Bobomb;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Soldat;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Tour;

import java.util.ArrayList;
import java.util.List;

public class GestionJeu {

    // Données du jeu
    private List<Personnage> ennemis = new ArrayList<>();
    private List<Circle> ennemisVue = new ArrayList<>();
    private List<Integer> indexChemins = new ArrayList<>();
    private List<List<int[]>> chemins = new ArrayList<>();
    private ObservableList<Tour> toursPlacees;

    // Références vers le modèle
    private Chateau chateau;
    private Terrain terrain;
    private Pane paneId;

    // Game loop
    private Timeline gameLoop;
    private int tickCount = 0;

    // Référence vers le controleur pour mettre à jour les pièces
    private IntegerProperty pieces;

    private static final int TILE = 30;

    public GestionJeu(Chateau chateau, Terrain terrain, Pane paneId,
                      ObservableList<Tour> toursPlacees, IntegerProperty pieces) {
        this.chateau = chateau;
        this.terrain = terrain;
        this.paneId = paneId;
        this.toursPlacees = toursPlacees;
        this.pieces = pieces;
    }

    // --- Spawn ---
    public void spawnEnnemi(String typeE, int[] coin) {
        List<int[]> cheminEnnemi = terrain.extraireChemin(coin[0], coin[1]);

        if (cheminEnnemi == null || cheminEnnemi.isEmpty()) {
            System.out.println("Pas de chemin trouvé depuis ce coin !");
            return;
        }

        Personnage modele = switch (typeE) {
            case "BOBOMB" -> new Bobomb(coin[1], coin[0], terrain);
            case "SOLDAT" -> new Soldat(coin[1], coin[0], terrain);
            default       -> new Soldat(coin[1], coin[0], terrain);
        };

        Circle cercle = new Circle(TILE / 2.0, modele.getCouleur());
        cercle.setCenterX(coin[1] * TILE + TILE / 2.0);
        cercle.setCenterY(coin[0] * TILE + TILE / 2.0);

        paneId.getChildren().add(cercle);

        ennemis.add(modele);
        ennemisVue.add(cercle);
        chemins.add(cheminEnnemi);
        indexChemins.add(0);
    }

    // --- Game Loop ---
    public void demarrer() {
        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(Duration.seconds(0.1), ev -> {
            tickCount++;

            // 1. Mise à jour des effets
            for (Personnage p : ennemis) {
                p.mettreAJourEffets();
            }

            // 2. Vérification des morts
            for (int i = ennemis.size() - 1; i >= 0; i--) {
                if (ennemis.get(i).estMort()) {
                    pieces.set(pieces.get() + ennemis.get(i).getRecompense());
                    supprimerEnnemi(i);
                }
            }

            // 3. Déplacement
            for (int i = 0; i < ennemis.size(); i++) {
                int index = indexChemins.get(i);
                List<int[]> ch = chemins.get(i);
                Personnage ennemi = ennemis.get(i);

                if (index < ch.size() - 1) {
                    int intervalle = Math.max(1, 4 / Math.max(1, ennemi.getVitesse()));
                    if (tickCount % intervalle == 0) {
                        int nouvelIndex = index + 1;
                        indexChemins.set(i, nouvelIndex);

                        int ligne   = ch.get(nouvelIndex)[0];
                        int colonne = ch.get(nouvelIndex)[1];

                        ennemi.setX(colonne);
                        ennemi.setY(ligne);

                        ennemisVue.get(i).setCenterX(colonne * TILE + TILE / 2.0);
                        ennemisVue.get(i).setCenterY(ligne   * TILE + TILE / 2.0);
                    }
                } else {
                    chateau.subirDegat(10);
                    if (chateau.estDetruit()) {
                        gameLoop.stop();
                        System.out.println("GAME OVER !");
                    }
                    supprimerEnnemi(i);
                    i--; // on décrémente car on a supprimé un élément
                }
            }

            ObservableList<Personnage> ennemisObs =
                    FXCollections.observableArrayList(ennemis);
            for (Tour t : toursPlacees) {
                t.tirer(ennemisObs);
            }
        });

        gameLoop.getKeyFrames().add(kf);
        gameLoop.play();
    }

    public void arreter() {
        if (gameLoop != null) gameLoop.stop();
    }

    // --- Méthode utilitaire privée ---
    private void supprimerEnnemi(int i) {
        paneId.getChildren().remove(ennemisVue.get(i));
        ennemis.remove(i);
        ennemisVue.remove(i);
        chemins.remove(i);
        indexChemins.remove(i);
    }
}