package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

public class tourBombe extends Tour{
    public tourBombe(double x, double y){
        super(x,y,50,25,2,2);
    }
    @Override
    public void tirer() {
        System.out.println("La tour de bombe tire ! dégâts : " + this.getDegat());
    }

}