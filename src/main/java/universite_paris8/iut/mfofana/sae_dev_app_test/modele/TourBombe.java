package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import javafx.beans.Observable;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class TourBombe extends Tour{
    private double vitesseBalle;
    private ObservableList<Personnage> personnage;
    public TourBombe(double x, double y, double vitesseBalle){
        super(x,y,50,25,2,2);
        this.vitesseBalle = vitesseBalle;
    }
    public double getVitesseBalle() { return vitesseBalle; }


    public void tirer() {
        if (!this.estParalysee() && this.peutTirer()) {
            //System.out.println("La tour de bombe tire ! dégâts : " + this.getDegat());
        }
    }

    public void tirer(ObservableList<Personnage> ennemis) { // reçoit la liste en paramètre
        if (!this.estParalysee() && this.peutTirer() && !ennemis.isEmpty()) {
            Personnage cible = choisirCible(ennemis);
            if (cible != null) {
                cible.subirDegat(this.getDegat());
            }
        }
    }

    @Override
    public void appliquerEffet(Personnage cible, ObservableList<Personnage> ennemis) {

    }


}