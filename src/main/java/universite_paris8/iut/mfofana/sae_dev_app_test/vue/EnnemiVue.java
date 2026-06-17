package universite_paris8.iut.mfofana.sae_dev_app_test.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.*;

public class EnnemiVue extends VBox {
    private Ennemis p;
    private Pane pane;

    private static final int TILE = 32;
    private static final String BASE =
            "/universite_paris8/iut/mfofana/sae_dev_app_test/Personnages/";


    public EnnemiVue(Pane pane, Ennemis p){
        super();
        this.pane = pane;
        this.p = p;
        creebarredevie();
        creerSpriteEnnemi(p);
        this.translateXProperty().bind(p.xProperty().multiply(TILE));
        this.translateYProperty().bind(p.yProperty().multiply(TILE));
        pane.getChildren().add(this);
    }

    public void creerSpriteEnnemi(Ennemis p) {
        // Image selon le type d'ennemi
        Image img;
        if (p instanceof Goomba) {
            img = charger("goomba.png");
        } else if (p instanceof Tortue) {
            img = charger("tortue.png");
        } else if (p instanceof Skeleton) {
            img = charger("skeleton.png");
        } else if (p instanceof Boo) {
            img = charger("boo.png");
        } else if (p instanceof Bobomb) {
            img = charger("bobomb.png");
        } else if (p instanceof Bill) {
            img = charger("bill.png");
        } else if (p instanceof Ninji) {
            img = charger("ninji.png");}
        else if (p instanceof BrowserJr) {
            img = charger("browserJr.png");
        } else if (p instanceof Browser) {
            img = charger("browser.png");
        }else {
            img = charger("Feu.png");
        }

        ImageView imageEnnemis = new ImageView(img);
        imageEnnemis.setFitWidth(TILE);
        imageEnnemis.setFitHeight(TILE);
        imageEnnemis.setPreserveRatio(true);

        //imageEnnemis.layoutXProperty().bind(p.xProperty().multiply(TILE));
       // imageEnnemis.layoutYProperty().bind(p.yProperty().multiply(TILE));

        int pvMax = p.getPvMax();
        this.getChildren().add(imageEnnemis);


    }
        public void creebarredevie(){
        Rectangle barre = new Rectangle(TILE, 4);
        barre.setFill(Color.GREEN);
       // barre.layoutXProperty().bind(p.xProperty().multiply(TILE));
       // barre.layoutYProperty().bind(p.yProperty().multiply(TILE).subtract(6));

        p.pvProperty().addListener((obs, ancien, nouveau) -> {
            double ratio = nouveau.doubleValue() / p.getPvMax();
            barre.setWidth(TILE * ratio);
            if (ratio > 0.5)       barre.setFill(Color.GREEN);
            else if (ratio > 0.25) barre.setFill(Color.ORANGE);
            else                   barre.setFill(Color.RED);
        });

        this.getChildren().add(barre);
        // affichageEnnemis.put(p, imageEnnemis);
        //barresVie.put(p, barre);

    }

    /*public void supprimerSpriteEnnemi(Personnage p) {
        ImageView sprite = affichageEnnemis.remove(p);
        Rectangle barre = barresVie.remove(p);
        if (sprite != null);
        if (barre != null)  pane.getChildren().remove(barre);
    }*/

    private Image charger(String nom) {
        return new Image(getClass().getResourceAsStream(BASE + nom));
    }
}
