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

    // LE GPS DES ENNEMIS
    public List<int[]> extraireChemin(int startL, int startC) {
        int nbLignes = terrain.length;
        int nbColonnes = terrain[0].length;

        if (startL < 0 || startL >= nbLignes || startC < 0 || startC >= nbColonnes || terrain[startL][startC] != CHEMIN) {
            return null;
        }

        // 1. LES DEUX CALQUES DE MÉMOIRE
        int[][] provenanceLigne = new int[nbLignes][nbColonnes];
        int[][] provenanceColonne = new int[nbLignes][nbColonnes];

        for (int[] ligne : provenanceLigne) {
            Arrays.fill(ligne, -2); // -2 = "L'eau n'est pas encore passée par ici"
        }

        Deque<int[]> file = new ArrayDeque<>();
        file.add(new int[]{startL, startC});
        provenanceLigne[startL][startC] = -1; // Le point de départ n'a pas de provenance

        int[] but = null;

        // 2. L'INONDATION
        while (!file.isEmpty()) {
            int[] actuelle = file.poll();
            int l = actuelle[0];
            int c = actuelle[1];

            if (estAdjacentChateau(l, c)) {
                but = actuelle;
                break;
            }

            for (int[] d : DIRS) {
                int nl = l + d[0]; // Nouvelle Ligne
                int nc = c + d[1]; // Nouvelle Colonne

                // Si c'est un chemin et que l'eau n'y est jamais allée (la provenance est toujours à -2)
                if (nl >= 0 && nl < nbLignes && nc >= 0 && nc < nbColonnes
                        && terrain[nl][nc] == CHEMIN && provenanceLigne[nl][nc] == -2) {

                    // ON LAISSE NOTRE MESSAGE SUR LA NOUVELLE CASE :
                    // "Pour arriver sur cette nouvelle case (nl, nc), je proviens de la case (l, c)"
                    provenanceLigne[nl][nc] = l;
                    provenanceColonne[nl][nc] = c;

                    file.add(new int[]{nl, nc});
                }
            }
        }

        if (but == null) return null;

        // 3. LE REMBOBINAGE
        List<int[]> chemin = new ArrayList<>();
        int l = but[0];
        int c = but[1];

        while (l != -1) {
            chemin.add(0, new int[]{l, c});

            // On regarde d'où on provenait pour reculer
            int ligneAvant = provenanceLigne[l][c];
            int colonneAvant = provenanceColonne[l][c];

            l = ligneAvant;
            c = colonneAvant;
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