package universite_paris8.iut.mfofana.sae_dev_app_test.vue;

import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

public class TerrainVue {

    private TilePane pane;
    private Terrain e;

    public TerrainVue(TilePane pane, Terrain e) {
        this.pane = pane;
        this.e = e;
    }

    public void dessiner() {

        double tailleTuile = 30;

        int[][] map = e.getTerrain();


        for (int ligne = 0; ligne < map.length; ligne++) {
            for (int col = 0; col < map[ligne].length; col++) {

                int type = map[ligne][col];
                Rectangle carre = new Rectangle(tailleTuile, tailleTuile);

                if(e.getTileTerrain(ligne,col) == 0){
                    carre.setFill(Color.GREEN);
                }
                if(e.getTileTerrain(ligne,col) == 1){
                    carre.setFill(Color.RED);
                }
                if(e.getTileTerrain(ligne,col) == 2){
                    carre.setFill(Color.GRAY);
                }


                pane.getChildren().add(carre);
            }
        }
    }
}