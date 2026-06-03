package universite_paris8.iut.mfofana.sae_dev_app_test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LANCEMENT_JEU extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LANCEMENT_JEU.class.getResource("Scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 760);
        stage.setTitle("Tower Defense");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}