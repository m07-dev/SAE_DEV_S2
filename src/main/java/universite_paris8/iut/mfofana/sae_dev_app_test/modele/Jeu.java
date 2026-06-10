package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.*;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Chateau;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Tour;

import java.util.ArrayList;
import java.util.List;

public class Jeu {

    // --- Listes observables → la vue écoute ces listes ---
    private ObservableList<Ennemis> ennemis = FXCollections.observableArrayList();
    private ObservableList<Tour> tours = FXCollections.observableArrayList();
    private ObservableList<List<int[]>> chemins = FXCollections.observableArrayList();
    private ObservableList<Integer> indexChemins = FXCollections.observableArrayList();

    // --- Modèle ---
    private Chateau chateau;
    private Terrain terrain;

    // --- Properties → bindings dans le contrôleur ---
    private IntegerProperty pieces = new SimpleIntegerProperty(50);
    private IntegerProperty numeroVague = new SimpleIntegerProperty(0);

    // --- Gestion des vagues ---
    private int tickCount = 0;
    private int ticksAvantProchainVague = 0; // compte à rebours en ticks
    private boolean vagueEnCours = false;
    private int ennemisSpawnCeTick = 0;      // index de l'ennemi en cours de spawn

    // Constantes
    private static final int TICKS_PAR_SECONDE = 60; // 1 tick = 0.1s donc 10 ticks = 1s
    private static final int DELAI_ENTRE_VAGUES = 10 * TICKS_PAR_SECONDE; // 10 secondes
    private static final int DELAI_ENTRE_SPAWNS = (int)(1.5 * TICKS_PAR_SECONDE); // 1.5s entre chaque spawn

    // Points d'entrée
    private static final int[] HAUT_GAUCHE = {0, 12};
    private static final int[] HAUT_DROITE = {0, 20};
    private static final int[] BAS_GAUCHE  = {21, 7};
    private static final int[] BAS_DROIT   = {21, 24};
    private static final int[] GAUCHE_HAUT  = {0, 4};
    private static final int[] GAUCHE_BAS  = {0, 13};
    private static final int[] DROITE_HAUT  = {29, 3};
    private static final int[] DROITE_BAS   = {29, 16};


    public Jeu() {
        this.chateau = new Chateau();
        this.terrain = new Terrain();
        this.ticksAvantProchainVague = DELAI_ENTRE_VAGUES;
    }

    // -----------------------------------------------------------
    // TICK → appelé à chaque frame par le contrôleur
    // -----------------------------------------------------------
    public List<GestionJeu.AlerteTir> tick() {
        List<GestionJeu.AlerteTir> evenements = new ArrayList<>();
        if (chateau.estDetruit()) return evenements; // jeu terminé → on ne fait rien

        tickCount++;

        // 1. Gérer les vagues
        gererVagues();

        // 2. Effets de statut sur les ennemis
        for (Ennemis p : ennemis) {
            p.mettreAJourEffets();
            p.seDeplacer();
        }

        // 3. Vérifier les morts
        for (int i = ennemis.size() - 1; i >= 0; i--) {
            if (ennemis.get(i).estMort()) {
                pieces.set(pieces.get() + ennemis.get(i).getRecompense());
                supprimerEnnemi(i);
            } else if (ennemis.get(i).aAtteintLeChateau()) {
            chateau.subirDegat(10);
            supprimerEnnemi(i);
        }
        }

        /**/

        // 5. Tirs des tours
        for (Tour t : tours) {
            t.mettreAJourStatut();
            Ennemis cibleTouche = t.tirer(ennemis, tickCount);

            if (cibleTouche != null ) {
                evenements.add(new GestionJeu.AlerteTir(t, cibleTouche));
            }
        }
        return evenements;
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
                //int[] coin = (ennemisSpawnCeTick % 2 == 0) ? HAUT_GAUCHE1;
                // A faire : Adapter les spawn des ennemis selon l'environnement
                spawnEnnemi("BOO", BAS_DROIT);
                spawnEnnemi("TORTUE", GAUCHE_HAUT);
                spawnEnnemi("SKELETON", GAUCHE_BAS);
                spawnEnnemi("GOOMBA", BAS_GAUCHE);


                ennemisSpawnCeTick++;
            }

            // Bobombs à partir de la vague 3
            if (numeroVague.get() >= 3 && ennemisSpawnCeTick == nbEnnemisVague()) {
                /*spawnEnnemi("BOBOMB", BAS_GAUCHE);*/
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
        Point2D source = new Point2D(coin[1], coin[0]);

        Point2D cible;
        if (typeE.equals("SKELETON") || typeE.equals("BOO")) {
            cible = (coin[0] <= 5) ? new Point2D(17, 9) : new Point2D(17, 12);
        } else {
            cible = (coin[0] <= 5) ? new Point2D(12, 9) : new Point2D(12, 12);
        }
        List<Point2D> cheminEnnemi = terrain.algoBFS(source, cible);

        Ennemis modele = switch (typeE) {
            case "TORTUE"   -> new Tortue(coin[1], coin[0], terrain, cheminEnnemi);
            case "SKELETON" -> new Skeleton(coin[1], coin[0], terrain, cheminEnnemi);
            case "BOO" -> new Boo(coin[1], coin[0], terrain, cheminEnnemi);
            case "GOOMBA" -> new Goomba(coin[1], coin[0], terrain, cheminEnnemi);
            case "BOBOMB" -> new Bobomb(coin[1], coin[0], terrain, cheminEnnemi);
            case "BOSS" -> new Boss(coin[1], coin[0], terrain, cheminEnnemi);
            default         -> new Tortue(coin[1], coin[0], terrain, cheminEnnemi);
        };

        if (typeE.equals("SKELETON") || typeE.equals("BOO")) {
            cible = (coin[0] <= 5) ? new Point2D(14, 9) : new Point2D(14, 12);
        } else {
            cible = (coin[0] <= 5) ? new Point2D(13, 9) : new Point2D(13, 12);
        }
        ennemis.add(modele);
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
    public ObservableList<Ennemis> getEnnemis() { return ennemis; }
    public ObservableList<Tour> getTours() { return tours; }
    public Chateau getChateau() { return chateau; }
    public Terrain getTerrain() { return terrain; }
    public boolean estTermine() { return chateau.estDetruit(); }

    public IntegerProperty piecesProperty() { return pieces; }
    public IntegerProperty numeroVagueProperty() { return numeroVague; }
    public int getPieces() { return pieces.get(); }
    public int getNumeroVague() { return numeroVague.get(); }

    public class GestionJeu {

        public static class AlerteTir {
            public final Tour tour;
            public final Ennemis cible;

            public AlerteTir(Tour tour, Ennemis cible) {
                this.tour = tour;
                this.cible = cible;
            }
        }
    }
}