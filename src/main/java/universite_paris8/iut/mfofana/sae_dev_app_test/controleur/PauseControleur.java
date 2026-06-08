package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.fxml.FXML;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Jeu;

public class PauseControleur {
    @FXML
    public void clicContinuer() {
        NavigationManager.allerVersJeu();
    }

    @FXML
    public void clicQuitter() {
        NavigationManager.quitter();
    }
}
