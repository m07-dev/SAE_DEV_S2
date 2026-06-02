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
                // ... (Ta grille de 0, 1 et 2) ...
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
        int minL = Integer.MAX_VALUE, minC = Integer.MAX_VALUE, maxL = -1;
        for (int l = 0; l < terrain.length; l++) {
            for (int c = 0; c < terrain[l].length; c++) {
                if (terrain[l][c] == CHATEAU) {
                    minL = Math.min(minL, l); // On note le point le plus haut/gauche
                    minC = Math.min(minC, c);
                    maxL = Math.max(maxL, l); // On note le point le plus bas pour avoir la taille
                }
            }
        }
        this.chateauLigne = (minL == Integer.MAX_VALUE) ? 0 : minL;
        this.chateauColonne = (minC == Integer.MAX_VALUE) ? 0 : minC;
        this.chateauTaille = (maxL < 0) ? 0 : (maxL - minL + 1);
    }

    // Les 4 directions pour se déplacer (Haut, Bas, Gauche, Droite)
    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // LE GPS DES ENNEMIS (L'eau qui coule dans les tuyaux)
    public List<int[]> extraireChemin(int startL, int startC) {
        int R = terrain.length, C = terrain[0].length;

        // Si on essaie de spawner hors de la carte ou dans l'herbe, on annule
        if (startL < 0 || startL >= R || startC < 0 || startC >= C
                || terrain[startL][startC] != CHEMIN) {
            return null;
        }

        // Les cailloux qu'on pose pour dire "je suis déjà passé par là"
        boolean[][] vu = new boolean[R][C];

        // Les flèches dessinées par terre (pour retenir d'où on vient)
        int[][] prevL = new int[R][C];
        int[][] prevC = new int[R][C];
        for (int[] row : prevL) Arrays.fill(row, -2);

        // Le seau d'eau qu'on vide au point de départ
        Deque<int[]> file = new ArrayDeque<>();
        file.add(new int[]{startL, startC});
        vu[startL][startC] = true; // On pose le premier caillou
        prevL[startL][startC] = -1; // Marque le point de départ exact

        int[] but = null;

        // L'eau s'étale case par case
        while (!file.isEmpty()) {
            int[] cur = file.poll();
            int l = cur[0], c = cur[1];

            // Si l'eau touche les murs du château, on arrête tout !
            if (estAdjacentChateau(l, c)) {
                but = cur;
                break;
            }

            // L'eau essaie d'aller dans les 4 directions
            for (int[] d : DIRS) {
                int nl = l + d[0], nc = c + d[1];

                // Si la case existe, qu'il n'y a pas de caillou, et que c'est un tuyau (chemin)
                if (nl >= 0 && nl < R && nc >= 0 && nc < C
                        && !vu[nl][nc] && terrain[nl][nc] == CHEMIN) {

                    vu[nl][nc] = true; // On pose un caillou

                    // On dessine la flèche par terre pointant vers la case d'avant
                    prevL[nl][nc] = l;
                    prevC[nl][nc] = c;

                    // L'eau continue d'avancer
                    file.add(new int[]{nl, nc});
                }
            }
        }

        if (but == null) return null; // L'eau n'a jamais trouvé le château

        // LE REMBOBINAGE
        // On part de la fin et on suit les flèches à l'envers jusqu'au départ
        List<int[]> chemin = new ArrayList<>();
        int l = but[0], c = but[1];
        while (l != -1) {
            chemin.add(0, new int[]{l, c}); // On ajoute la case au début de la liste
            int pl = prevL[l][c], pc = prevC[l][c]; // On regarde la flèche pour reculer
            l = pl;
            c = pc;
        }
        return chemin; // Notre GPS a trouvé la route parfaite
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
    public int getTileTerrain(int ligne, int col) { return terrain[ligne][col]; }
    public int getChateauLigne() { return chateauLigne; }
    public int getChateauColonne() { return chateauColonne; }
    public int getChateauTaille() { return chateauTaille; }
}