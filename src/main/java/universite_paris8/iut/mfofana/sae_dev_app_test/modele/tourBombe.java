package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

public class tourBombe extends Tour{
    private double vitesseBalle;
    public tourBombe(double x, double y, double vitesseBalle){
        super(x,y,50,25,2,2);
        this.vitesseBalle = vitesseBalle;
    }
    public double getVitesseBalle() { return vitesseBalle; }

    @Override
    public void tirer() {
        System.out.println("La tour de bombe tire ! dégâts : " + this.getDegat());
    }

}