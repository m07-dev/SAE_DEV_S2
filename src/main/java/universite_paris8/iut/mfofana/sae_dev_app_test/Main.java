package universite_paris8.iut.mfofana.sae_dev_app_test;

import universite_paris8.iut.mfofana.sae_dev_app_test.modele.*;

import java.util.*;
import javafx.geometry.Point2D;

public class Main {
    public static void main(String[] args) {
        Terrain t = new Terrain();
        Point2D depart = new Point2D(27, 0);
        Point2D cible = new Point2D(14,9);
        List<Point2D> chemin = t.algoBFS(depart,cible);
        System.out.println(chemin);
    }
}