package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import java.util.ArrayList;
import java.util.List;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Terrain {
    /*
     * 0 - herbe (zone, constructible)
     * 1 - chemin (les ennemis marchent dessus)
     * 2 - chateau (centre, non constructible)
     *
     * NB : cette grille est generee a partir de "labyrinthe.txt".
     * Pour la modifier, editez labyrinthe.txt puis re-importez.
     */
    public static final int HERBE = 0;
    public static final int CHEMIN = 1;
    public static final int CHATEAU = 2;

    /** Taille d'une tuile en pixels (source unique pour toute l'appli). */
    public static final int TAILLE_TUILE = 32;

    private final int[][] terrain;

    // Bornes du chateau (calculees au demarrage)
    private int chateauLigne, chateauColonne, chateauTaille;

    public Terrain() {
        this.terrain = new int[][]{
                {0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0},
                {0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0},
                {0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0},
                {0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},
                {0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},
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

    private void calculerChateau() {
        int minL = Integer.MAX_VALUE, minC = Integer.MAX_VALUE, maxL = -1;
        for (int l = 0; l < terrain.length; l++) {
            for (int c = 0; c < terrain[l].length; c++) {
                if (terrain[l][c] == CHATEAU) {
                    minL = Math.min(minL, l);
                    minC = Math.min(minC, c);
                    maxL = Math.max(maxL, l);
                }
            }
        }
        this.chateauLigne = (minL == Integer.MAX_VALUE) ? 0 : minL;
        this.chateauColonne = (minC == Integer.MAX_VALUE) ? 0 : minC;
        this.chateauTaille = (maxL < 0) ? 0 : (maxL - minL + 1);
    }

    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * Calcule le plus court chemin (BFS) depuis (startL, startC) jusqu'a une
     * case de chemin adjacente au chateau. Remplace l'ancienne extraction
     * lineaire : fonctionne meme avec des embranchements et plusieurs entrees.
     *
     * @return la liste ordonnee des cases {ligne, colonne}, ou null si aucun chemin.
     */
    public List<int[]> extraireChemin(int startL, int startC) {
        int R = terrain.length, C = terrain[0].length;
        if (startL < 0 || startL >= R || startC < 0 || startC >= C
                || terrain[startL][startC] != CHEMIN) {
            return null;
        }

        boolean[][] vu = new boolean[R][C];
        int[][] prevL = new int[R][C];
        int[][] prevC = new int[R][C];
        for (int[] row : prevL) java.util.Arrays.fill(row, -2);

        Deque<int[]> file = new ArrayDeque<>();
        file.add(new int[]{startL, startC});
        vu[startL][startC] = true;
        prevL[startL][startC] = -1; // marque le depart

        int[] but = null;
        while (!file.isEmpty()) {
            int[] cur = file.poll();
            int l = cur[0], c = cur[1];

            if (estAdjacentChateau(l, c)) {
                but = cur;
                break;
            }
            for (int[] d : DIRS) {
                int nl = l + d[0], nc = c + d[1];
                if (nl >= 0 && nl < R && nc >= 0 && nc < C
                        && !vu[nl][nc] && terrain[nl][nc] == CHEMIN) {
                    vu[nl][nc] = true;
                    prevL[nl][nc] = l;
                    prevC[nl][nc] = c;
                    file.add(new int[]{nl, nc});
                }
            }
        }

        if (but == null) return null;

        // Reconstruction du chemin (du depart vers le chateau)
        List<int[]> chemin = new ArrayList<>();
        int l = but[0], c = but[1];
        while (l != -1) {
            chemin.add(0, new int[]{l, c});
            int pl = prevL[l][c], pc = prevC[l][c];
            l = pl;
            c = pc;
        }
        return chemin;
    }

    private boolean estAdjacentChateau(int l, int c) {
        for (int[] d : DIRS) {
            int nl = l + d[0], nc = c + d[1];
            if (nl >= 0 && nl < terrain.length && nc >= 0 && nc < terrain[0].length
                    && terrain[nl][nc] == CHATEAU) {
                return true;
            }
        }
        return false;
    }

    public int[][] getTerrain() {
        return terrain;
    }

    public int getNbLignes() {
        return terrain.length;
    }

    public int getNbColonnes() {
        return terrain[0].length;
    }

    /** Acces par (ligne, colonne). */
    public int getTileTerrain(int ligne, int col) {
        return terrain[ligne][col];
    }

    public int getChateauLigne() {
        return chateauLigne;
    }

    public int getChateauColonne() {
        return chateauColonne;
    }

    public int getChateauTaille() {
        return chateauTaille;
    }
}
