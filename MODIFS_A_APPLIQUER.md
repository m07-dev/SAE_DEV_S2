# Modifications à appliquer dans le code des collègues

Pour que les écrans Menu et Game Over s'intègrent au jeu, **2 fichiers existants** doivent être modifiés. Toutes les modifs sont minimales (quelques lignes).

---

## 1. `LANCEMENT_JEU.java`

**Pourquoi :** au démarrage, charger le menu au lieu de la scène de jeu, et donner le `Stage` au `NavigationManager`.

**Remplacer la méthode `start()` actuelle par :**

```java
@Override
public void start(Stage stage) {
    stage.setTitle("Tower Defense");
    // On donne le Stage au NavigationManager pour qu'il puisse changer de scène
    NavigationManager.setStage(stage);
    // On affiche le menu d'accueil au démarrage
    NavigationManager.allerVersMenu();
}
```

**Imports à ajouter en haut du fichier :**

```java
import universite_paris8.iut.mfofana.sae_dev_app_test.controleur.NavigationManager;
```

**Imports à supprimer (devenus inutiles) :**

```java
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;
```

---

## 2. `controleur/Controleur.java`

**Pourquoi :** quand le château est détruit, basculer vers l'écran Game Over au lieu de juste afficher un message console.

**Trouver ce bloc (vers la fin de `initialize()`) :**

```java
// Game Over
if (jeu.estTermine()) {
    gameLoop.stop();
    System.out.println("GAME OVER !");
}
```

**Le remplacer par :**

```java
// Game Over → on bascule vers l'écran de fin de partie
if (jeu.estTermine()) {
    gameLoop.stop();
    NavigationManager.allerVersGameOver();
}
```

**Import à ajouter en haut du fichier :**

```java
import universite_paris8.iut.mfofana.sae_dev_app_test.controleur.NavigationManager;
```

> Note : comme `NavigationManager` est dans le même package que `Controleur` (`controleur`), l'import n'est techniquement pas obligatoire. Mais je le mets pour la clarté.

---

## Récap des fichiers que J'AI créés (toi tu n'as rien à toucher dessus)

- `src/main/java/.../controleur/NavigationManager.java`
- `src/main/java/.../controleur/MenuControleur.java`
- `src/main/java/.../controleur/GameOverControleur.java`
- `src/main/resources/.../Menu.fxml`
- `src/main/resources/.../GameOver.fxml`
- `src/main/resources/.../styles.css`

## Flux complet une fois tout intégré

1. Lancement → **Menu** (Jouer / Quitter)
2. Clic "Jouer" → **Jeu** (Scene.fxml)
3. Château détruit → **Game Over** (Rejouer / Menu / Quitter)
4. Clic "Rejouer" → retour au **Jeu** (nouvelle instance, état remis à zéro automatiquement)
5. Clic "Menu" → retour au **Menu**
6. Clic "Quitter" → ferme l'app
