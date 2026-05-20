package universite_paris8.iut.mfofana.sae_dev_app_test.modele;
public class TourBouleDeGlace extends Tour{
    private double vitesseBalle;
    private int ralentissement;
    public TourBouleDeGlace(double x, double y, int ralentissement, double vitesseBalle){
        super(x,y,10,25,4,4);
        this.vitesseBalle = vitesseBalle;
        this.ralentissement = ralentissement;
    }
    public int getRalentissement() { return ralentissement; }
    public double getVitesseBalle() { return vitesseBalle; }
    @Override
    public void tirer() {
        System.out.println("La tour boule de glace a lancée de la glace : "+ this.getDegat());
    }
}