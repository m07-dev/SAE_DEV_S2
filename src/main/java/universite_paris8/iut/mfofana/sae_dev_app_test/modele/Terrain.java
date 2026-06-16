package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import javafx.geometry.Point2D;

import java.util.*;

public class Terrain {

    // NOTRE PLATEAU DE JEU (la feuille Ã  carreaux)
    // 0 = herbe (on peut construire)
    // 1 = chemin (les tuyaux pour les ennemis)
    // 2 = chateau (la base au milieu)
    public static final int HERBE = 0;
    public static final int CHEMIN = 1;
    public static final int CHATEAU = 2;
    public static final int DECOR_VERT = 3;
    public static final int centreChateau = 4;

    public static final int TAILLE_TUILE = 32;

    // La grille contenant tous les numÃ©ros
    private final int[][] terrain;

    // Les coordonnÃ©es de notre chÃ¢teau
    private int chateauLigne, chateauColonne, chateauTaille;

    public Terrain() {
        this.terrain = new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0},
                {0,1,1,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,1,1,1,1,1,1},
                {1,1,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,0,0,0,1,0,0,1,0,0},
                {0,0,0,0,1,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,1,0,0},
                {0,0,1,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0},
                {0,0,1,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,1,1,1,1,0,1,0,0},
                {0,0,1,0,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,0,0,0,0,1,1,1,0,0},
                {0,0,1,0,0,0,0,0,0,0,0,0,1,1,1,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0},
                {0,0,1,1,0,0,0,0,0,0,0,0,0,0,2,2,1,1,0,0,0,0,0,0,0,0,1,0,0,0},
                {0,0,0,1,1,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,1,1,1,1,1,1,1,1,1,0},
                {0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,1,0,0,0,0,0,0,0,1,0},
                {1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,0,0,1,1,1,1,1,1,0,1,1,1,0,1,0},
                {0,0,1,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,0,1,0,1,0},
                {0,0,1,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,0,1,0,1,0},
                {0,0,1,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,0,1,0,1,0,1,1},
                {0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,1,1,0,1,0,1,0,1,0,1,0},
                {0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,1,0,1,0,1,1,1,0},
                {0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,1,0,1,0,1,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0},
                {0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0}
        };
        this.chateauLigne = 9;
        this.chateauColonne = 13;
        this.chateauTaille = 4;
    }

    public List<Point2D> algoBFS(Point2D source, Point2D cible) {
        if (source == null || cible == null) {
            return new ArrayList<>();
        }

        int srcLigne = (int) source.getY();
        int srcCol = (int) source.getX();

        // VÃ©rifier si la case de dÃ©part est valide
        int valeurDepart = getTileTerrain(srcLigne, srcCol);
        if (valeurDepart != 1 && valeurDepart != 2) {
            System.out.println("Erreur : La case de dÃ©part (" + srcLigne + "," + srcCol + ") n'est pas un chemin !");
            return new ArrayList<>();
        }

        ArrayList<Point2D> parcours = new ArrayList<>();
        Map<Point2D, Point2D> predecesseurs = new HashMap<>();
        Queue<Point2D> fifo = new LinkedList<>();

        parcours.add(source);
        fifo.add(source);
        predecesseurs.put(source, null);
        boolean cibleTrouvee = false;
        while (!fifo.isEmpty()) {
            Point2D actuelVisite = fifo.poll();
            if (actuelVisite.equals(cible)) {
                cibleTrouvee = true;
            }else {
                for (Point2D voisin : estAdjacent(actuelVisite)) {
                    if (!parcours.contains(voisin)) {
                        parcours.add(voisin);
                        fifo.add(voisin);
                        predecesseurs.put(voisin, actuelVisite);
                    }
                }
            }
        }

        if (!predecesseurs.containsKey(cible)) {

            System.out.println("Aucun chemin trouvé entre la source et la cible.");

            return new ArrayList<>();
        }

        ArrayList<Point2D> chemin = new ArrayList<>();
        Point2D actuel = cible;
        while (actuel != null) {
            chemin.add(actuel);
            actuel = predecesseurs.get(actuel);
        }
        Collections.reverse(chemin);
        return chemin;
    }

    public List<Point2D> estAdjacent(Point2D actuel) {
        List<Point2D> adj = new ArrayList<>();
        if (actuel == null) {
            return adj;
        }
        // Rappel : Point2D(X, Y) -> X = Colonne, Y = Ligne
        int col = (int) actuel.getX();
        int ligne = (int) actuel.getY();

        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };
        for (int[] dir : directions) {
            int voisinLigne = ligne + dir[0];
            int voisinCol = col + dir[1];
            if (!enDehorsDuTerrain(voisinLigne, voisinCol)) {
                int valeurCase = terrain[voisinLigne][voisinCol];
                if (valeurCase == 1 || valeurCase == 2) {
                    adj.add(new Point2D(voisinCol, voisinLigne));
                }
            }
        }
        return adj;
    }
    public void bloquerCase(int ligne, int col){
        terrain[ligne][col] = 0;
    }
    public void debloquerCase(int ligne, int col){
        terrain[ligne][col] = 1;
    }
    // Les Getters classiques
    public int[][] getTerrain() { return terrain; }
    public int getNbLignes() { return terrain.length; }
    public int getNbColonnes() { return terrain[0].length; }
    public int getTileTerrain(int ligne, int col) {
        if(ligne < 0 || ligne >= terrain.length ||col < 0 || col >= terrain[ligne].length)
        {return -1;}
        return terrain[ligne][col]; }
    public boolean enDehorsDuTerrain(int ligne, int col) {
        if (ligne < 0 || ligne >= getNbLignes() || col < 0 || col >= terrain[ligne].length) {
            return true;
        }
        return false;
    }

    public void setTileTerrain(int ligne, int col, int nouvelleValeur) {
        if (!enDehorsDuTerrain(ligne, col)) {
            this.terrain[ligne][col] = nouvelleValeur;
        }
    }
    public int getChateauLigne() { return chateauLigne; }
    public int getChateauColonne() { return chateauColonne; }
    public int getChateauTaille() { return chateauTaille; }
}