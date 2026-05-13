package universite_paris8.iut.mfofana.sae_dev_app_test;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import java.util.Map;
import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class HelloController {
    @FXML
    private Pane paneId;
    private Timeline gameLoop;
    private int temps;
    private Circle c1;
    private Personnage p1;
    private Environnement e;
    @FXML
    private TilePane panneTerrain;

    @FXML
    public void initialize(){
        c1 = new Circle();
        paneId.getChildren().addAll(c1);
        this.e = new Environnement();
        p1 = new Personnage(0,0);
        TerrainVue terrainVue = new TerrainVue(panneTerrain, e);
        terrainVue.dessiner();
        initAnimation();
        gameLoop.play();

    }

    private void initAnimation() {
        c1.setFill(Color.BLACK);
        c1.setCenterX(10.0);
        c1.setCenterY(10.0);
        c1.setRadius(10.0);
        gameLoop = new Timeline();
        temps=0;
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                // on définit le FPS (nbre de frame par seconde)
                Duration.seconds(0.017),
                // on définit ce qui se passe à chaque frame
                // c'est un eventHandler d'ou le lambda
                (ev ->{
                    if(e.enDehorsTerrain(p1.getX(), p1.getY())){
                        paneId.getChildren().remove(c1);
                        System.out.println("pas de deplacement out ");
                    }else {
                        if (temps == 2500) {
                            System.out.println("fini");
                            gameLoop.stop();
                        } else if (temps % 5 == 0) {
                            System.out.println("un tour");
                            p1.setX(p1.getX() + 5);
                            p1.setY(p1.getY() + 5);
                            c1.setTranslateX(p1.getX());
                            c1.setTranslateY(p1.getY());
                            System.out.println(p1.getX());
                            System.out.println(p1.getY());

                        }
                    }
                    temps++;
                })
        );
        gameLoop.getKeyFrames().add(kf);
    }
}

