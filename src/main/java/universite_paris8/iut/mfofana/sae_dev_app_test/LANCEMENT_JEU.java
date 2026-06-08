package universite_paris8.iut.mfofana.sae_dev_app_test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import universite_paris8.iut.mfofana.sae_dev_app_test.controleur.NavigationManager;

import java.io.IOException;

public class LANCEMENT_JEU extends Application {
    @Override
    public void start(Stage stage)  {
        stage.setTitle("Tower Defense");
        NavigationManager.setStage(stage);
        NavigationManager.allerVersMenu();
    }

    public static void main(String[] args) {
        launch();
    }
}