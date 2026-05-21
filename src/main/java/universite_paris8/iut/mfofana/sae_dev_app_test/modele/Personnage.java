package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

public abstract class Personnage {
    private double x,y;
    private Terrain e;
    private int pv;
    private int vitesse;



    public Personnage(double x, double y){
        this.x = x;
        this.y = y;
        this.e = new Terrain();
    }

   public Personnage(double x, double y, Terrain e, int pv, int v) {
        this.x = x;
        this.y = y;
        this.e = new Terrain();
        this.pv = pv;
        this.vitesse = v;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
    public int getPv() {
        return pv;
    }
    public void setPv(int nvPv) {
        this.pv = Math.max(0,pv);
    }
    public void setVitesse(int v) {
        this.vitesse = v;
    }

    public int getVitesse() {
        return this.vitesse;
    }

    public boolean horsDesLimites(){
        return this.getX() < 0 ||this.getX() >=  32*30 || this.getY() < 0 || this.getY() >= 30*32 ;
    }
    public void subirDegat(int degat) {
        this.setPv(this.pv - degat);
    }

    public boolean estMort() {
        return this.pv == 0;
    }




}
