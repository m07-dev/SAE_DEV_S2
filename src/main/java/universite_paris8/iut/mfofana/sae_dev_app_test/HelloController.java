package universite_paris8.iut.mfofana.sae_dev_app_test;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;

import java.util.Map;

public class HelloController {
    private Environnement e;
    @FXML
    private TilePane panneTerrain;
    @FXML
    private Label welcomeText;

    @FXML
    public void initialize(){
        this.e = new Environnement();



        TerrainVue terrainVue = new TerrainVue(panneTerrain, e);

        terrainVue.dessiner();
    }

}