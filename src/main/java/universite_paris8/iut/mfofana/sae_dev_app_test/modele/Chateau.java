package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

public class Chateau {
    private int limiteEnnemis;
    private int compteurEnnemis;


    public Chateau() {
        this.limiteEnnemis = 10;
        this.compteurEnnemis = 0;
    }

    public void ennemiEstEntre() {
        this.compteurEnnemis++;
        System.out.println("Attention ! Un nouvel ennemi dans le château " + compteurEnnemis +  " / " + limiteEnnemis);

    }

    public boolean partieFinis() {
        return this.compteurEnnemis >= this.limiteEnnemis;
    }
}
