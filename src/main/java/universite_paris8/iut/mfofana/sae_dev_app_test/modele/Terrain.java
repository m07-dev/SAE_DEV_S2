package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class Terrain {

    // NOTRE PLATEAU DE JEU (la feuille à carreaux)
    // 0 = herbe (on peut construire)
    // 1 = chemin (les tuyaux pour les ennemis)
    // 2 = chateau (la base au milieu)
    public static final int HERBE = 0;
    public static final int CHEMIN = 1;
    public static final int CHATEAU = 2;
    public static final int DECOR_VERT = 3;

    public static final int TAILLE_TUILE = 32;

    // La grille contenant tous les numéros
    private final int[][] terrain;

    // Les coordonnées de notre château
    private int chateauLigne, chateauColonne, chateauTaille;

    public Terrain() {
        this.terrain = new int[][]{
                {0,0,0,0,1,0,0,0,0,0,0,1,0,0,3,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0},
                {0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0},
                {0,0,0,0,1,0,0,0,3,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0},
                {0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},
                {0,3,0,0,1,0,0,0,3,0,0,0,3,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},
                {0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},
                {0,0,0,0,1,1,1,1,1,1,1,1,1,0,1,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,1,2,2,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,1,0,0,1,0,0,0,1,0,0,0,1,0},
                {0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,1,0,0,1,0,0,0,1,0,0,0,1,0},
                {0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,1,0,0,1,0,0,0,1,0,0,0,1,0},
                {0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,1,0},
                {0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0},
                {0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0},
                {0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0},
                {0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0}
        };
        calculerChateau();
    }

    // LE RADAR À CHÂTEAU
    // On passe le doigt sur chaque case pour trouver où sont les "2"
    private void calculerChateau() {
        // On lit la carte de haut en bas, gauche à droite
        for (int l = 0; l < terrain.length; l++) {
            for (int c = 0; c < terrain[l].length; c++) {

                // BINGO ! On a touché la toute première case du château.
                // C'est forcément le point le plus en haut à gauche.
                if (terrain[l][c] == CHATEAU) {
                    this.chateauLigne = l;
                    this.chateauColonne = c;

                    // On compte juste combien de cases de château il y a en dessous
                    int taille = 0;
                    while (l + taille < terrain.length && terrain[l + taille][c] == CHATEAU) {
                        taille++;
                    }

                    this.chateauTaille = taille;

                    return; // Mission accomplie, on coupe tout et on sort de la méthode !
                }
            }
        }

        // Si on arrive ici, c'est qu'on a scanné toute la carte sans rien trouver
        this.chateauLigne = 0;
        this.chateauColonne = 0;
        this.chateauTaille = 0;
    }

    // Les 4 directions pour se déplacer (Haut, Bas, Gauche, Droite)
    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // LE GPS DES ENNEMIS (L'eau qui coule dans les tuyaux)
    public List<int[]> extraireChemin(int startL, int startC) {
        int R = terrain.length, C = terrain[0].length;

        // 1. Contrôle de sécurité
        if (startL < 0 || startL >= R || startC < 0 || startC >= C || terrain[startL][startC] != CHEMIN) {
            return null;
        }

        // 2. Préparation (Le tableau "vu" a disparu !)
        int[][] prevL = new int[R][C];
        int[][] prevC = new int[R][C];
        for (int[] row : prevL) {
            Arrays.fill(row, -2); // -2 veut dire "case jamais visitée"
        }

        Deque<int[]> file = new ArrayDeque<>();
        file.add(new int[]{startL, startC});
        prevL[startL][startC] = -1; // Marque le départ ET signale que la case est "visitée" (!= -2)

        int[] but = null;

        // 3. L'inondation
        while (!file.isEmpty()) {
            int[] actuelle = file.poll();
            int l = actuelle[0], c = actuelle[1];

            if (estAdjacentChateau(l, c)) {
                but = actuelle;
                break;
            }

            for (int[] d : DIRS) {
                int nl = l + d[0], nc = c + d[1];

                // Si sur la carte + est un chemin + NON VISITÉE (prevL vaut toujours -2)
                if (nl >= 0 && nl < R && nc >= 0 && nc < C
                        && terrain[nl][nc] == CHEMIN && prevL[nl][nc] == -2) {

                    // On dessine la flèche, ce qui valide automatiquement la case comme "visitée"
                    prevL[nl][nc] = l;
                    prevC[nl][nc] = c;

                    file.add(new int[]{nl, nc});
                }
            }
        }

        if (but == null) return null;


        List<int[]> chemin = new ArrayList<>();
        int l = but[0], c = but[1];

        while (l != -1) {
            chemin.add(0, new int[]{l, c});

            // On récupère les flèches avant d'écraser l et c
            int lPrecedent = prevL[l][c];
            int cPrecedent = prevC[l][c];

            l = lPrecedent;
            c = cPrecedent;
        }

        return chemin;
    }

    // LE CAPTEUR DE PROXIMITÉ
    // Regarde les 4 cases collées pour voir s'il y a un mur du château (le chiffre 2)
    private boolean estAdjacentChateau(int l, int c) {
        for (int[] d : DIRS) {
            int nl = l + d[0], nc = c + d[1];
            if (nl >= 0 && nl < terrain.length && nc >= 0 && nc < terrain[0].length
                    && terrain[nl][nc] == CHATEAU) {
                return true; // Bip bip, château détecté !
            }
        }
        return false;
    }

    // Les Getters classiques
    public int[][] getTerrain() { return terrain; }
    public int getNbLignes() { return terrain.length; }
    public int getNbColonnes() { return terrain[0].length; }
    public int getTileTerrain(int ligne, int col) {
        if(ligne < 0 || ligne >= terrain.length ||col < 0 || col >= terrain[ligne].length)
        {return -1;}
        return terrain[ligne][col]; }
    public int getChateauLigne() { return chateauLigne; }
    public int getChateauColonne() { return chateauColonne; }
    public int getChateauTaille() { return chateauTaille; }
}