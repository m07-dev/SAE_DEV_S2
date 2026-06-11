package universite_paris8.iut.mfofana.sae_dev_app_test;

import javafx.stage.Stage;
import universite_paris8.iut.mfofana.sae_dev_app_test.controleur.NavigationManager;

public class Application extends javafx.application.Application {
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