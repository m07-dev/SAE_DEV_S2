package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.fxml.FXML;

public class VictoireControleur {

    @FXML
    public void clicRejouer() {
        NavigationManager.allerVersJeu();
    }

    @FXML
    public void clicMenu() {
        NavigationManager.allerVersMenu();
    }

    @FXML
    public void clicQuitter() {
        NavigationManager.quitter();
    }
}