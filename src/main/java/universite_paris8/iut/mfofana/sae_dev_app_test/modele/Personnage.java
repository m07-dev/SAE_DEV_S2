package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

public class Personnage {
    private int x,y;
    private Terrain e;
    public Personnage(int x, int y){
        this.x = x;
        this.y = x;
        this.e = new Terrain();
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

    public boolean horsDesLimites(){
        return this.getX() < 0 ||this.getX() >=  32*30 || this.getY() < 0 || this.getY() >= 30*32 ;
    }
}
