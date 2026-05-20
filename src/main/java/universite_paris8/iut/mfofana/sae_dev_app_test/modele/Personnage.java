package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

public abstract class Personnage {
    private int x,y;
    private Terrain e;
    private int pv;
    private int vitesse;



    public Personnage(int x, int y){
        this.x = x;
        this.y = y;
        this.e = new Terrain();
    }

   public Personnage(int x, int y, Terrain e, int pv, int v) {
        this.x = x;
        this.y = y;
        this.e = new Terrain();
        this.pv = pv;
        this.vitesse = v;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public int getPv() {
        return pv;
    }
    public void setPv(int nvPv) {
        this.pv = Math.max(0,pv);
    }


    public boolean horsDesLimites(){
        return this.getX() < 0 ||this.getX() >=  32*30 || this.getY() < 0 || this.getY() >= 30*32 ;
    }

    public boolean estMort() {
        return this.pv == 0;
    }

    public void subirDegat(int degat) {
        this.setPv(this.pv - degat);
    }




}
