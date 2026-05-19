package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

public class tourBouleDeFeu extends Tour{
        private double vitesseBalle;
        private int effetBrulure;
        public tourBouleDeFeu(double x, double y, double vitesseBalle, int effetBrulure){
            super(x,y,25,15,4,4);
            this.vitesseBalle =vitesseBalle;
            this.effetBrulure = effetBrulure;
    }
    public double getVitesseBalle() { return vitesseBalle; }
    public int getEffetBrulure() { return effetBrulure; }
    @Override
    public void tirer() {
        System.out.println("La tour boule de feu tire, dégats :"+ this.getDegat());
    }
}
