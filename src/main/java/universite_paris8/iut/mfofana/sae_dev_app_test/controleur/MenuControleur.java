package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.fxml.FXML;


public class MenuControleur {
    @FXML
    public void clicJouer() {
        NavigationManager.allerVersJeu();
    }

    @FXML
    public void clicQuitter() {
        NavigationManager.quitter();
    }
}
