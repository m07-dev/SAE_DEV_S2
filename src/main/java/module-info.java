module universite_paris8.iut.mfofana.sae_dev_app_test {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens universite_paris8.iut.mfofana.sae_dev_app_test to javafx.fxml;
    exports universite_paris8.iut.mfofana.sae_dev_app_test;
}
