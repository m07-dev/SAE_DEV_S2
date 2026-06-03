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
        // 1. On prépare nos variables de repérage avec des valeurs extrêmes
        int haut = 99999;
        int gauche = 99999;
        int bas = -1;
        boolean chateauTrouve = false;

        // 2. On fouille chaque case du terrain
        for (int l = 0; l < terrain.length; l++) {
            for (int c = 0; c < terrain[l].length; c++) {

                // Si on tombe sur un bout du château
                if (terrain[l][c] == CHATEAU) {
                    chateauTrouve = true;

                    // On met à jour les bords si on trouve plus extrême
                    if (l < haut) haut = l;
                    if (c < gauche) gauche = c;
                    if (l > bas) bas = l;
                }
            }
        }

        // 3. On range les résultats finaux proprement
        if (chateauTrouve) {
            this.chateauLigne = haut;
            this.chateauColonne = gauche;
            this.chateauTaille = bas - haut + 1;
        } else {
            // Sécurité : si le château n'existe pas sur la carte
            this.chateauLigne = 0;
            this.chateauColonne = 0;
            this.chateauTaille = 0;
        }
    }

    // Les 4 directions pour se déplacer (Haut, Bas, Gauche, Droite)
    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // LE GPS DES ENNEMIS (L'eau qui coule dans les tuyaux)
    public List<int[]> extraireChemin(int startL, int startC) {
        int R = terrain.length, C = terrain[0].length;


        if (startL < 0 || startL >= R || startC < 0 || startC >= C
                || terrain[startL][startC] != CHEMIN) {
            return null;
        }


        boolean[][] vu = new boolean[R][C];

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
            int[] actuelle = file.poll();
            int l = actuelle[0], c = actuelle[1];

            // Si l'eau touche les murs du château, on arrête tout !
            if (estAdjacentChateau(l, c)) {
                but = actuelle;
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
    public int getTileTerrain(int ligne, int col) {
        if(ligne < 0 || ligne > terrain.length ||col < 0 || col >= terrain[ligne].length)
        {return -1;}
        return terrain[ligne][col]; }

    public int getChateauLigne() { return chateauLigne; }
    public int getChateauColonne() { return chateauColonne; }
    public int getChateauTaille() { return chateauTaille; }
}