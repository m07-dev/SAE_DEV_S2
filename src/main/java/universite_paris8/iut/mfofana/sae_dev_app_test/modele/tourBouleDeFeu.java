package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

public class tourBouleDeFeu extends Tour{
        public tourBouleDeFeu(double x, double y){
            super(x,y,25,15,4,4);
    }

    @Override
    public void tirer() {
        System.out.println("La tour boule de feu tire, dégats :"+ this.getDegat());
    }
}
