# Nouvelle version de `extraireChemin()` dans `Terrain.java`

## Principe

C'est un **BFS standard** (tableau `visite` + tableau de parents pour reconstituer le chemin, plus de truc `-2` bizarre). La nouveauté : **l'ordre dans lequel on explore les 4 voisins dépend du quart du terrain d'où on part**.

Concrètement :
- Si l'ennemi part **en HAUT** du terrain → on essaie d'abord d'aller **vers le BAS** (vers le château).
- Si l'ennemi part **en BAS** → on essaie d'abord d'aller **vers le HAUT**.
- Si l'ennemi part **à GAUCHE** → on poursuit avec **DROITE**.
- Si l'ennemi part **à DROITE** → on poursuit avec **GAUCHE**.

Comme le BFS garantit toujours le plus court chemin, ce changement ne fait que **départager les ex æquo** : entre deux chemins de même longueur, on préfère celui qui pointe vers le château dès le départ.

---

## 1. Constantes à AJOUTER en haut de la classe `Terrain`

Place-les près du champ `DIRS` existant :

```java
// Les 4 directions, chacune avec un nom (pour la lisibilité)
private static final int[] HAUT   = {-1,  0};
private static final int[] BAS    = { 1,  0};
private static final int[] GAUCHE = { 0, -1};
private static final int[] DROITE = { 0,  1};
```

> Tu peux garder le `DIRS` existant tel quel, il est encore utilisé par `estAdjacentChateau()`.

---

## 2. REMPLACER la méthode `extraireChemin()` par celle-ci

```java
/**
 * Calcule un chemin (BFS) depuis (startL, startC) jusqu'à une case adjacente au château.
 * L'ordre d'exploration des voisins dépend du quadrant de départ :
 * on privilégie toujours la direction qui rapproche du centre du terrain.
 * En cas d'égalité de longueur, le BFS retourne donc le chemin le plus orienté
 * vers le château.
 *
 * @param startL ligne de départ (doit être une case CHEMIN)
 * @param startC colonne de départ (doit être une case CHEMIN)
 * @return liste de coordonnées [ligne, colonne] formant le chemin, ou null si aucun
 */
public List<int[]> extraireChemin(int startL, int startC) {
    int nbLignes = terrain.length;
    int nbColonnes = terrain[0].length;

    // Validation : hors grille ou case non-chemin → pas de trajet possible
    if (startL < 0 || startL >= nbLignes || startC < 0 || startC >= nbColonnes
            || terrain[startL][startC] != CHEMIN) {
        return null;
    }

    // Ordre des 4 directions à explorer, adapté au coin de départ
    int[][] ordreDirs = ordreSelonDepart(startL, startC, nbLignes, nbColonnes);

    // Mémoire du BFS : qui est venu d'où, et qui a déjà été visité
    int[][] parentL = new int[nbLignes][nbColonnes];
    int[][] parentC = new int[nbLignes][nbColonnes];
    boolean[][] visite = new boolean[nbLignes][nbColonnes];

    // File classique du BFS
    Deque<int[]> file = new ArrayDeque<>();
    file.add(new int[]{startL, startC});
    visite[startL][startC] = true;
    parentL[startL][startC] = -1; // marqueur "case de départ, pas de parent"

    int[] but = null;

    while (!file.isEmpty()) {
        int[] actuelle = file.poll();
        int l = actuelle[0];
        int c = actuelle[1];

        // On s'arrête dès qu'on touche une case collée au château
        if (estAdjacentChateau(l, c)) {
            but = actuelle;
            break;
        }

        // Exploration des voisins dans l'ordre adapté au point de départ
        for (int[] d : ordreDirs) {
            int nl = l + d[0];
            int nc = c + d[1];

            if (nl >= 0 && nl < nbLignes && nc >= 0 && nc < nbColonnes
                    && terrain[nl][nc] == CHEMIN && !visite[nl][nc]) {
                visite[nl][nc] = true;
                parentL[nl][nc] = l;
                parentC[nl][nc] = c;
                file.add(new int[]{nl, nc});
            }
        }
    }

    // Aucun chemin trouvé jusqu'au château
    if (but == null) return null;

    // Reconstruction : on remonte de but vers le départ, puis on inverse
    List<int[]> chemin = new ArrayList<>();
    int l = but[0];
    int c = but[1];
    while (l != -1) {
        chemin.add(new int[]{l, c});
        int ligneAvant = parentL[l][c];
        int colonneAvant = parentC[l][c];
        l = ligneAvant;
        c = colonneAvant;
    }
    java.util.Collections.reverse(chemin); // ordre départ → château
    return chemin;
}
```

---

## 3. AJOUTER cette méthode privée juste en dessous

```java
/**
 * Renvoie l'ordre dans lequel le BFS doit explorer les 4 directions,
 * en privilégiant la verticale puis l'horizontale orientées vers le centre.
 *
 * @param startL     ligne de départ
 * @param startC     colonne de départ
 * @param nbLignes   nombre total de lignes du terrain
 * @param nbColonnes nombre total de colonnes du terrain
 * @return tableau de 4 directions ordonnées par priorité
 */
private int[][] ordreSelonDepart(int startL, int startC, int nbLignes, int nbColonnes) {
    boolean partDuHaut   = startL < nbLignes / 2;
    boolean partDeGauche = startC < nbColonnes / 2;

    // Direction verticale prioritaire = celle qui rapproche du milieu
    int[] verticale            = partDuHaut   ? BAS    : HAUT;
    int[] verticaleInverse     = partDuHaut   ? HAUT   : BAS;

    // Direction horizontale prioritaire = celle qui rapproche du milieu
    int[] horizontale          = partDeGauche ? DROITE : GAUCHE;
    int[] horizontaleInverse   = partDeGauche ? GAUCHE : DROITE;

    return new int[][]{ verticale, horizontale, verticaleInverse, horizontaleInverse };
}
```

---

## Import à AJOUTER (si pas déjà présent) en haut de `Terrain.java`

```java
import java.util.Collections;
```

> Tu peux aussi enlever le `java.util.` devant `Collections.reverse(chemin)` dans la méthode si tu ajoutes l'import.

---

## Résumé concret de l'effet attendu

Pour les 4 points d'entrée actuels :

| Départ          | Position    | Ordre exploré           |
|-----------------|-------------|-------------------------|
| HAUT_GAUCHE1 (0,4)  | haut-gauche | BAS, DROITE, HAUT, GAUCHE |
| HAUT_DROIT (0,27)   | haut-droite | BAS, GAUCHE, HAUT, DROITE |
| BAS_GAUCHE (21,2)   | bas-gauche  | HAUT, DROITE, BAS, GAUCHE |
| BAS_DROIT (21,28)   | bas-droite  | HAUT, GAUCHE, BAS, DROITE |

Donc visuellement les chemins iront "tout droit vers le centre" plutôt que de zigzaguer.
