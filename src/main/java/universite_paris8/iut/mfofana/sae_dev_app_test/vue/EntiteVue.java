package universite_paris8.iut.mfofana.sae_dev_app_test.vue;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
        // Couleur selon le type d'ennemi
        Image img;
        if (p instanceof Bobomb) {
            img = charger("bobomb.png");
        } else if (p instanceof Tortue) {
            img= charger("tortue_1.png");
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

        pane.getChildren().add(imageEnnemis);
        affichageEnnemis.put(p, imageEnnemis);
    }

    private void creerSpriteTour(Tour t) {
        Color couleur;
        if (t instanceof TourBouleDeFeu)        couleur = Color.ORANGE;
        else if (t instanceof TourBombe)        couleur = Color.GRAY;
        else if (t instanceof TourBouleDeGlace) couleur = Color.CYAN;
        else if (t instanceof TourObstacle)     couleur = Color.BROWN;
        else                                    couleur = Color.BLACK;

        Circle cercle = new Circle(TILE / 2.0, couleur);

        // Les tours ne bougent pas → position fixe
        cercle.setCenterX(t.getX() * TILE + TILE / 2.0);
        cercle.setCenterY(t.getY() * TILE + TILE / 2.0);

        pane.getChildren().add(cercle);
        affichageTours.put(t, cercle);
    }

    // -----------------------------------------------------------
    // SUPPRESSION DES SPRITES
    // -----------------------------------------------------------

    private void supprimerSpriteEnnemi(Personnage p) {
        ImageView sprite = affichageEnnemis.remove(p);
        if (sprite != null) pane.getChildren().remove(sprite);
    }

    private void supprimerSpriteTour(Tour t) {
        Node sprite = affichageTours.remove(t);
        if (sprite != null) pane.getChildren().remove(sprite);
    }

    private Image charger(String nom) {
        return new Image(getClass().getResourceAsStream(BASE + nom));
    }
}