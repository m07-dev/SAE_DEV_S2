package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import universite_paris8.iut.mfofana.sae_dev_app_test.Application;

import java.io.IOException;

/**
 * Lui cest le patron qui va gerer la nav entre les scene
 * Les transition entre le menu la scene et le game over
 * On l'appellle dans lancement jeu, on lui donne la fenetre
 * puis NavigationManager.allerVersMenu() / allerVersJeu() / allerVersGameOver().
 */
public class NavigationManager {


    private static final int LARGEUR  = 1150;
    private static final int HAUTEUR  = 760;

    private static Stage stageCourant;

    public static void setStage(Stage stage) {
        stageCourant = stage;
    }

    private static void chargerScene(String fichierFxml) {
        if (stageCourant == null) {
            System.err.println("NavigationManager : Stage non initialisé. Appelle setStage() d'abord.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource(fichierFxml));
            Scene scene = new Scene(loader.load(), LARGEUR, HAUTEUR);
            stageCourant.setScene(scene);
            stageCourant.show();
        } catch (IOException e) {
            System.err.println("Erreur de chargement de " + fichierFxml + " : " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void allerVersMenu() {
        chargerScene("Menu.fxml");
    }


    public static void allerVersJeu() {
        chargerScene("Scene.fxml");
    }


    public static void allerVersGameOver() {
        chargerScene("GameOver.fxml");
    }

    public static void allerVersVictoire() {
        chargerScene("Victoire.fxml");
    }


    public static void quitter() {
        if (stageCourant != null) {
            stageCourant.close();
        }
    }
}
