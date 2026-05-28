package universite_paris8.iut.mfofana.sae_dev_app_test.controleur;

import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Chateau;

public class GestionVagues {

    private IntegerProperty numeroVague;
    private Label labelCountdown;
    private Chateau chateau;
    private GestionJeu gestionJeu;

    private static final int[] HAUT_GAUCHE = {0, 0};
    private static final int[] HAUT_DROIT  = {6, 20};
    private static final int[] BAS_GAUCHE  = {31, 8};

    public GestionVagues(IntegerProperty numeroVague, Label labelCountdown,
                         Chateau chateau, GestionJeu gestionJeu) {
        this.numeroVague = numeroVague;
        this.labelCountdown = labelCountdown;
        this.chateau = chateau;
        this.gestionJeu = gestionJeu;
    }

    public void demarrerProchainerVague() {
        int delai = 5;
        final int[] secondesRestantes = {delai};

        Timeline countdown = new Timeline();
        countdown.setCycleCount(delai);

        KeyFrame kf = new KeyFrame(Duration.seconds(1), ev -> {
            secondesRestantes[0]--;
            if (secondesRestantes[0] > 0) {
                labelCountdown.setText("Vague dans : " + secondesRestantes[0] + "s");
            } else {
                labelCountdown.setText("Vague en cours !");
                lancerVague();
            }
        });

        countdown.getKeyFrames().add(kf);
        labelCountdown.setText("Vague dans : " + delai + "s");
        countdown.play();
    }

    private void lancerVague() {
        numeroVague.set(numeroVague.get() + 1);
        int vague = numeroVague.get();

        int nbSoldats = 2 + vague;
        int nbBobombs = vague >= 3 ? 1 : 0;

        // Spawn soldats
        for (int i = 0; i < nbSoldats; i++) {
            final int index = i;
            new Timeline(new KeyFrame(Duration.seconds(index * 1.5), ev -> {
                int[] coin = (index % 2 == 0) ? HAUT_GAUCHE : HAUT_DROIT;
                gestionJeu.spawnEnnemi("SOLDAT", coin);
            })).play();
        }

        // Spawn bobombs
        for (int i = 0; i < nbBobombs; i++) {
            final int index = i;
            new Timeline(new KeyFrame(
                    Duration.seconds(nbSoldats * 1.5 + index * 2.0),
                    ev -> gestionJeu.spawnEnnemi("BOBOMB", BAS_GAUCHE)
            )).play();
        }

        // Prochaine vague
        double duree = (nbSoldats + nbBobombs) * 2.0 + 10.0;
        new Timeline(new KeyFrame(Duration.seconds(duree), ev -> {
            if (!chateau.estDetruit()) demarrerProchainerVague();
        })).play();
    }
}