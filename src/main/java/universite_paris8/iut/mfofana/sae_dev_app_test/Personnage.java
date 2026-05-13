package universite_paris8.iut.mfofana.sae_dev_app_test;

import org.controlsfx.control.action.Action;

public class Personnage {
    private int x,y;
    private Environnement e;
    public Personnage(int x, int y){
        this.x = x;
        this.y = x;
        this.e = new Environnement();
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

    /*public void deplacement(int x , int y){
        if(e.enDehorsTerrain(x,y)){
            System.out.println("peut pas se déplacer");
        }else{
            
        }
    }*/
}
