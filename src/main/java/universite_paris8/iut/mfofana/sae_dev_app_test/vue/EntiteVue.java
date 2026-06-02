package universite_paris8.iut.mfofana.sae_dev_app_test.vue;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Bobomb;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Personnage;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Tortue;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.*;

import java.util.HashMap;

public class EntiteVue {

    private Pane pane;
    private static final int TILE = 32;

    // HashMap → associe chaque entité à son Node visuel
    // "Pour chaque Personnage, je connais son cercle"
    private HashMap<Personnage, Node> affichageEnnemis = new HashMap<>();
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
        Color couleur;
        if (p instanceof Bobomb)     couleur = Color.PINK;
        else if (p instanceof Tortue) couleur = Color.YELLOW;
        else                          couleur = Color.BLACK;

        Circle cercle = new Circle(TILE / 2.0, couleur);

        // BIND automatique sur la position → le cercle suit l'ennemi !
        cercle.centerXProperty().bind(p.xProperty().multiply(TILE).add(TILE / 2.0));
        cercle.centerYProperty().bind(p.yProperty().multiply(TILE).add(TILE / 2.0));

        pane.getChildren().add(cercle);
        affichageEnnemis.put(p, cercle);
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
        Node sprite = affichageEnnemis.remove(p);
        if (sprite != null) pane.getChildren().remove(sprite);
    }

    private void supprimerSpriteTour(Tour t) {
        Node sprite = affichageTours.remove(t);
        if (sprite != null) pane.getChildren().remove(sprite);
    }
}