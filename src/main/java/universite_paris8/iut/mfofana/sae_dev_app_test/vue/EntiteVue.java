package universite_paris8.iut.mfofana.sae_dev_app_test.vue;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.*;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.*;


import java.util.HashMap;

public class EntiteVue {

    private Pane pane;
    private static final int TILE = 32;
    private static final String BASE =
            "/universite_paris8/iut/mfofana/sae_dev_app_test/Personnages/";

    // HashMap → associe chaque entité à son Node visuel
    // "Pour chaque Personnage, je connais son cercle"
    private HashMap<Personnage, ImageView> affichageEnnemis = new HashMap<>();
    private HashMap<Tour, Node> affichageTours = new HashMap<>();
    private HashMap<Personnage, Rectangle> barresVie = new HashMap<>();
    private HashMap<Personnage, Rectangle> fondBarre = new HashMap<>();


    public EntiteVue(Pane pane) {
        this.pane = pane;
    }

    // -----------------------------------------------------------
    // BINDINGS SUR LES LISTES OBSERVABLES
    // Appelée UNE SEULE FOIS dans initialize() du contrôleur
    // -----------------------------------------------------------

    public void creerBindingsEnnemis(ObservableList<Personnage> ennemis) {
        ennemis.addListener((ListChangeListener<Personnage>) changement -> {
            while (changement.next()) {

                // Un ennemi a été ajouté → on crée son sprite
                if (changement.wasAdded()) {
                    for (Personnage p : changement.getAddedSubList()) {
                        creerSpriteEnnemi(p);

                    }
                }

                // Un ennemi a été supprimé → on retire son sprite
                if (changement.wasRemoved()) {
                    for (Personnage p : changement.getRemoved()) {
                        supprimerSpriteEnnemi(p);
                    }
                }
            }
        });
    }

    public void creerBindingsTours(ObservableList<Tour> tours) {
        tours.addListener((ListChangeListener<Tour>) changement -> {
            while (changement.next()) {

                if (changement.wasAdded()) {
                    for (Tour t : changement.getAddedSubList()) {
                        creerSpriteTour(t);
                    }
                }

                if (changement.wasRemoved()) {
                    for (Tour t : changement.getRemoved()) {
                        supprimerSpriteTour(t);
                    }
                }
            }
        });
    }

    // -----------------------------------------------------------
    // CREATION DES SPRITES
    // -----------------------------------------------------------

    private void creerSpriteEnnemi(Personnage p) {
        // Image selon le type d'ennemi
        Image img;
        if (p instanceof Bobomb) {
            img = charger("bobomb.png");
        } else if (p instanceof Tortue) {
            img= charger("tortue.png");
        } else if (p instanceof Skeleton){
            img = charger("skeleton.png");
        } else if (p instanceof Boo) {
            img = charger("boo.png");
        } else {
            img = charger("erreur.png");
        }

        ImageView imageEnnemis = new ImageView(img);
        imageEnnemis.setFitWidth(TILE);
        imageEnnemis.setFitHeight(TILE);
        imageEnnemis.setPreserveRatio(true);

        imageEnnemis.xProperty().bind(p.xProperty().multiply(TILE));
        imageEnnemis.yProperty().bind(p.yProperty().multiply(TILE));

        int pvMax = p.getPvMax(); // snapshot au moment de l'apparition



        Rectangle barre = new Rectangle(TILE, 4);
        barre.setFill(Color.GREEN);
        barre.layoutXProperty().bind(p.xProperty().multiply(TILE));
        barre.layoutYProperty().bind(p.yProperty().multiply(TILE).subtract(6));

        p.pvProperty().addListener((obs, ancien, nouveau) -> {
            double ratio = nouveau.doubleValue() / pvMax;
            barre.setWidth(TILE * ratio);
            if (ratio > 0.5)       barre.setFill(Color.GREEN);
            else if (ratio > 0.25) barre.setFill(Color.ORANGE);
            else                   barre.setFill(Color.RED);
        });

        pane.getChildren().addAll(imageEnnemis, barre);
        affichageEnnemis.put(p, imageEnnemis);
        barresVie.put(p, barre);

    }

    private void creerSpriteTour(Tour t) {
        Image img;
        if (t instanceof TourBouleDeFeu)       img = charger("Feu.png");
        else if (t instanceof TourBombe)       img = charger("Bombe.png");
        else if (t instanceof TourBouleDeGlace) img = charger("Glace.png");
        else if (t instanceof TourObstacle)     img = charger("Obstacle.png");
        else                                    img = charger("Erreur.png");



        ImageView imageTour = new ImageView(img);
        imageTour.setFitWidth(TILE);
        imageTour.setFitHeight(TILE);
        imageTour.setPreserveRatio(true);

        imageTour.xProperty().bind(t.xProperty().multiply(TILE));
        imageTour.yProperty().bind(t.yProperty().multiply(TILE));

        pane.getChildren().add(imageTour);
        affichageTours.put(t, imageTour);
    }

    // -----------------------------------------------------------
    // SUPPRESSION DES SPRITES
    // -----------------------------------------------------------

    private void supprimerSpriteEnnemi(Personnage p) {
        ImageView sprite = affichageEnnemis.remove(p);
        Rectangle barre = barresVie.remove(p);


        if (sprite != null) pane.getChildren().remove(sprite);
        if (barre != null)  pane.getChildren().remove(barre);
    }

    private void supprimerSpriteTour(Tour t) {
        Node sprite = affichageTours.remove(t);
        if (sprite != null) pane.getChildren().remove(sprite);
    }

    private Image charger(String nom) {
        return new Image(getClass().getResourceAsStream(BASE + nom));
    }
}