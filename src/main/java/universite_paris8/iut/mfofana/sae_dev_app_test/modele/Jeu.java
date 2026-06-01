package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Bobomb;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Personnage;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Soldat;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Chateau;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Tour;

import java.util.List;

public class Jeu {

    // --- Listes observables → la vue écoute ces listes ---
    private ObservableList<Personnage> ennemis = FXCollections.observableArrayList();
    private ObservableList<Tour> tours = FXCollections.observableArrayList();
    private ObservableList<List<int[]>> chemins = FXCollections.observableArrayList();
    private ObservableList<Integer> indexChemins = FXCollections.observableArrayList();

    // --- Modèle ---
    private Chateau chateau;
    private Terrain terrain;

    // --- Properties → bindings dans le contrôleur ---
    private IntegerProperty pieces = new SimpleIntegerProperty(100);
    private IntegerProperty numeroVague = new SimpleIntegerProperty(0);

    // --- Gestion des vagues ---
    private int tickCount = 0;
    private int ticksAvantProchainVague = 0; // compte à rebours en ticks
    private boolean vagueEnCours = false;
    private int ennemisSpawnCeTick = 0;      // index de l'ennemi en cours de spawn

    // Constantes
    private static final int TICKS_PAR_SECONDE = 10; // 1 tick = 0.1s donc 10 ticks = 1s
    private static final int DELAI_ENTRE_VAGUES = 5 * TICKS_PAR_SECONDE; // 5 secondes
    private static final int DELAI_ENTRE_SPAWNS = (int)(1.5 * TICKS_PAR_SECONDE); // 1.5s entre chaque spawn

    // Points d'entrée
    private static final int[] HAUT_GAUCHE1 = {0, 4};
    private static final int[] HAUT_GAUCHE2 = {5, 0};
    private static final int[] HAUT_DROIT  = {0, 27};
    private static final int[] BAS_GAUCHE  = {21, 2};
    private static final int[] BAS_DROIT   = {21, 28};

    public Jeu() {
        this.chateau = new Chateau();
        this.terrain = new Terrain();
        this.ticksAvantProchainVague = DELAI_ENTRE_VAGUES;
    }

    // -----------------------------------------------------------
    // TICK → appelé à chaque frame par le contrôleur
    // -----------------------------------------------------------
    public void tick() {
        if (chateau.estDetruit()) return; // jeu terminé → on ne fait rien

        tickCount++;

        // 1. Gérer les vagues
        gererVagues();

        // 2. Effets de statut sur les ennemis
        for (Personnage p : ennemis) {
            p.mettreAJourEffets();
        }

        // 3. Vérifier les morts
        for (int i = ennemis.size() - 1; i >= 0; i--) {
            if (ennemis.get(i).estMort()) {
                pieces.set(pieces.get() + ennemis.get(i).getRecompense());
                supprimerEnnemi(i);
            }
        }

        // 4. Déplacer les ennemis
        for (int i = 0; i < ennemis.size(); i++) {
            int index = indexChemins.get(i);
            List<int[]> ch = chemins.get(i);
            Personnage ennemi = ennemis.get(i);

            if (index < ch.size() - 1) {
                int intervalle = Math.max(1, 4 / Math.max(1, ennemi.getVitesse()));
                if (tickCount % intervalle == 0) {
                    int nouvelIndex = index + 1;
                    indexChemins.set(i, nouvelIndex);

                    int ligne   = ch.get(nouvelIndex)[0];
                    int colonne = ch.get(nouvelIndex)[1];

                    // setX/setY → DoubleProperty → le sprite se déplace automatiquement !
                    ennemi.setX(colonne);
                    ennemi.setY(ligne);
                }
            } else {
                // L'ennemi a atteint le château
                chateau.subirDegat(10);
                supprimerEnnemi(i);
                i--;
            }
        }

        // 5. Tirs des tours
        for (Tour t : tours) {
            t.tirer(ennemis, tickCount);
        }
    }

    // -----------------------------------------------------------
    // GESTION DES VAGUES
    // -----------------------------------------------------------
    private void gererVagues() {
        if (!vagueEnCours) {
            // On est entre deux vagues → décompte
            ticksAvantProchainVague--;

            if (ticksAvantProchainVague <= 0) {
                // Lancer la prochaine vague
                lancerVague();
            }
        } else {
            // Vague en cours → spawner les ennemis progressivement
            if (ennemisSpawnCeTick < nbEnnemisVague() && tickCount % DELAI_ENTRE_SPAWNS == 0) {
                int[] coin = (ennemisSpawnCeTick % 2 == 0) ? HAUT_GAUCHE1 : HAUT_DROIT;
                spawnEnnemi("SOLDAT", coin);
                ennemisSpawnCeTick++;
            }

            // Bobombs à partir de la vague 3
            if (numeroVague.get() >= 3 && ennemisSpawnCeTick == nbEnnemisVague()) {
                spawnEnnemi("BOBOMB", BAS_GAUCHE);
                ennemisSpawnCeTick++; // évite de spawner en boucle
            }

            // La vague est terminée quand tous les ennemis sont morts
            if (ennemis.isEmpty() && ennemisSpawnCeTick >= nbEnnemisVague()) {
                vagueEnCours = false;
                ticksAvantProchainVague = DELAI_ENTRE_VAGUES;
                ennemisSpawnCeTick = 0;
            }
        }
    }

    public void lancerVague() {
        numeroVague.set(numeroVague.get() + 1);
        vagueEnCours = true;
        ennemisSpawnCeTick = 0;
    }


    public void forcerLancement() {
        if (!vagueEnCours) {
            lancerVague();
        }
    }

    private int nbEnnemisVague() {
        return 2 + numeroVague.get(); // vague 1 = 3, vague 2 = 4...
    }

    // -----------------------------------------------------------
    // SPAWN
    // -----------------------------------------------------------
    public void spawnEnnemi(String typeE, int[] coin) {
        List<int[]> cheminEnnemi = terrain.extraireChemin(coin[0], coin[1]);
        if (cheminEnnemi == null || cheminEnnemi.isEmpty()) return;

        Personnage modele = switch (typeE) {
            case "BOBOMB" -> new Bobomb(coin[1], coin[0], terrain);
            case "SOLDAT" -> new Soldat(coin[1], coin[0], terrain);
            default       -> new Soldat(coin[1], coin[0], terrain);
        };

        // On ajoute à la liste → le ListChangeListener crée le sprite automatiquement !
        ennemis.add(modele);
        chemins.add(cheminEnnemi);
        indexChemins.add(0);
    }

    // -----------------------------------------------------------
    // TOURS
    // -----------------------------------------------------------
    public void poserTour(Tour t, int cout) {
        if (pieces.get() >= cout) {
            pieces.set(pieces.get() - cout);
            tours.add(t); // → ListChangeListener crée le sprite automatiquement !
        } else {
            System.out.println("Fonds insuffisants !");
        }
    }

    // -----------------------------------------------------------
    // TEXTE COUNTDOWN → affiché dans le contrôleur
    // -----------------------------------------------------------
    public String getCountdownText() {
        if (vagueEnCours) {
            return "Vague en cours !";
        } else {
            int secondesRestantes = ticksAvantProchainVague / TICKS_PAR_SECONDE;
            return "Vague dans : " + secondesRestantes + "s";
        }
    }

    // -----------------------------------------------------------
    // UTILITAIRES PRIVÉS
    // -----------------------------------------------------------
    private void supprimerEnnemi(int i) {
        // On supprime de la liste → le ListChangeListener supprime le sprite automatiquement !
        ennemis.remove(i);
        chemins.remove(i);
        indexChemins.remove(i);
    }

    // -----------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------
    public ObservableList<Personnage> getEnnemis() { return ennemis; }
    public ObservableList<Tour> getTours() { return tours; }
    public Chateau getChateau() { return chateau; }
    public Terrain getTerrain() { return terrain; }
    public boolean estTermine() { return chateau.estDetruit(); }

    public IntegerProperty piecesProperty() { return pieces; }
    public IntegerProperty numeroVagueProperty() { return numeroVague; }
    public int getPieces() { return pieces.get(); }
    public int getNumeroVague() { return numeroVague.get(); }
}