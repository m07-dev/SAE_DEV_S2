package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.*;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Chateau;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Projectile;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.Tour;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.TourObstacle;

import java.util.ArrayList;
import java.util.List;

public class Jeu {

    // --- Listes observables â†’ la vue Ã©coute ces listes ---
    private ObservableList<Ennemis> ennemis = FXCollections.observableArrayList();
    private ObservableList<Projectile> projectiles = FXCollections.observableArrayList();
    private ObservableList<Tour> tours = FXCollections.observableArrayList();

    // --- ModÃ¨le ---
    private Chateau chateau;
    private Terrain terrain;

    // --- Properties â†’ bindings dans le contrÃ´leur ---
    private IntegerProperty pieces = new SimpleIntegerProperty(50);
    private IntegerProperty numeroVague = new SimpleIntegerProperty(0);

    // --- Gestion des vagues ---
    private int tickCount = 0;
    private int ticksAvantProchainVague = 0; // compte Ã  rebours en ticks
    private boolean vagueEnCours = false;
    private int ennemisSpawnCeTick = 0;      // index de l'ennemi en cours de spawn

    // Constantes
    private static final int TICKS_PAR_SECONDE = 60; // 1 tick = 0.1s donc 10 ticks = 1s
    private static final int DELAI_ENTRE_VAGUES = 10 * TICKS_PAR_SECONDE; // 10 secondes
    private static final int DELAI_ENTRE_SPAWNS = (int)(1.8 * TICKS_PAR_SECONDE); // 1.5s entre chaque spawn

    // Points d'entrÃ©e
    private static final int[] HAUT_GAUCHE = {0, 12};
    private static final int[] HAUT_DROITE = {0, 20};
    private static final int[] BAS_GAUCHE  = {21, 6};
    private static final int[] BAS_DROIT   = {21, 24};
    private static final int[] GAUCHE_HAUT  = {4, 0};
    private static final int[] GAUCHE_BAS  = {13, 0};
    private static final int[] DROITE_HAUT  = {3, 29};
    private static final int[] DROITE_BAS   = {16, 29};

    private List<Point2D> cheminParfaitDroiteBas;

    public Jeu() {
        this.chateau = new Chateau();
        this.terrain = new Terrain();
        this.ticksAvantProchainVague = DELAI_ENTRE_VAGUES;
        initialiserCheminsDeReference();
    }

    // -----------------------------------------------------------
    // TICK â†’ appelÃ© Ã  chaque frame par le contrÃ´leur
    // -----------------------------------------------------------
    public void tick() {
        if (chateau.estDetruit()){return;}

        tickCount++;

        // 1. Gérer les vagues
        gererVagues();

        // 2. Effets de statut sur les ennemis
        for (int i = ennemis.size() - 1; i >= 0; i--) {
            Ennemis p = ennemis.get(i);
            p.mettreAJourEffets();

            if (p instanceof Bobomb) {
                ((Bobomb) p).vitesseAugmente();
                ((Bobomb) p).exploser(tours);
            }

            p.seDeplacer();
        }

        for (int i = tours.size() - 1; i >= 0; i--) {
            Tour t = tours.get(i);

            if (t instanceof TourObstacle && terrain.getTileTerrain((int) t.getY(), (int) t.getX()) == 1) {
                tours.remove(i); // Déclenche le listener pour effacer l'image
            }
        }


        // 3. Vérifier les morts
        for (int i = ennemis.size() - 1; i >= 0; i--) {
            Ennemis ennemiActuel = ennemis.get(i); // <-- Récupérer l'ennemi spécifique

            if (ennemiActuel.estMort()) {
                pieces.set(pieces.get() + ennemis.get(i).getRecompense());
                supprimerEnnemi(i);

            } else if (ennemis.get(i).aAtteintLeChateau()) {


                if (ennemiActuel instanceof Goomba){
                    chateau.subirDegat(5);
                }
                else if (ennemiActuel instanceof Tortue){
                    chateau.subirDegat(8);
                }
                else if (ennemiActuel instanceof Skeleton){
                    chateau.subirDegat(5);
                }
                else if (ennemiActuel instanceof Boo){
                    chateau.subirDegat(5);
                }
                else if (ennemiActuel instanceof Bobomb){
                    chateau.subirDegat(20);
                }
                else if (ennemiActuel instanceof Bill){
                    chateau.subirDegat(25);
                }
                else if (ennemiActuel instanceof Ninji){
                    chateau.subirDegat(15);
                }
                else if (ennemiActuel instanceof Browser){
                    chateau.subirDegat(15);
                }
                else if (ennemiActuel instanceof BrowserJr){
                    chateau.subirDegat(50);
                }
                supprimerEnnemi(i);
            }
        }



        for (Tour t : tours) {
            t.mettreAJourStatut();
            if(!(t instanceof TourObstacle) && !t.estParalysee()){
                Ennemis cibleTouche = t.tirer(ennemis, tickCount, projectiles);
            }

        }
        if (!projectiles.isEmpty()) {
            for (int i = projectiles.size() - 1; i >= 0; i--) {
                Projectile p = projectiles.get(i);
                p.seDeplacer();
                if (p.aAtteintCible()) {
                    projectiles.remove(i);
                }
            }
            projectiles.removeIf(p -> !p.isEstActif());
        }

    }

    // -----------------------------------------------------------
    // GESTION DES VAGUES
    // -----------------------------------------------------------
    private void gererVagues() {
        if (!vagueEnCours) {
            // On est entre deux vagues â†’ dÃ©compte
            ticksAvantProchainVague--;

            if (ticksAvantProchainVague <= 0) {
                // Lancer la prochaine vague
                lancerVague();
            }
        } else {
            // Vague en cours â†’ spawner les ennemis progressivement
            if (ennemisSpawnCeTick < nbEnnemisVague() && tickCount % DELAI_ENTRE_SPAWNS == 0) {
                spawnEnnemi("BOO", BAS_DROIT);
                spawnEnnemi("TORTUE", GAUCHE_HAUT);
                spawnEnnemi("SKELETON", HAUT_DROITE);
                spawnEnnemi("GOOMBA", GAUCHE_BAS);
                spawnEnnemi("BILL", DROITE_BAS);
                ennemisSpawnCeTick++;
            }

            // Bobombs Ã  partir de la vague 3
            if (numeroVague.get() >= 3 && ennemisSpawnCeTick == nbEnnemisVague()) {
                /*spawnEnnemi("BOBOMB", BAS_GAUCHE);*/
                ennemisSpawnCeTick++; // Ã©vite de spawner en boucle
            }

            // La vague est terminÃ©e quand tous les ennemis sont morts
            if (ennemis.isEmpty() && ennemisSpawnCeTick >= nbEnnemisVague()) {
                vagueEnCours = false;
                ticksAvantProchainVague = DELAI_ENTRE_VAGUES;
                ennemisSpawnCeTick = 0;
                chateau.setPv(100);
            }
        }
    }

    public void initialiserCheminsDeReference() {
        // On calcule le chemin DIRECT au tout début du jeu, quand il n'y a AUCUN obstacle
        Point2D sourceBill = new Point2D(DROITE_BAS[1], DROITE_BAS[0]);
        Point2D cibleBill = (DROITE_BAS[0] <= 5) ? new Point2D(14, 10) : new Point2D(15, 11);

        // Ce chemin sera pur et suivra parfaitement la route d'origine !
        this.cheminParfaitDroiteBas = terrain.algoBFS(sourceBill, cibleBill);
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

    public int getNombreObstacles() {
        int count = 0;
        for (Tour t : tours) {
            if (t instanceof TourObstacle) {
                count++;
            }
        }
        return count;
    }

    // -----------------------------------------------------------
    // SPAWN
    // -----------------------------------------------------------
    public void spawnEnnemi(String typeE, int[] coin) {
        Point2D source = new Point2D(coin[1], coin[0]);
        Point2D cible = (coin[0] <= 5) ? new Point2D(14, 10) : new Point2D(15, 11);
        List<Point2D> cheminEnnemi;

        if (typeE.equals("BILL")) {
            // On lui passe une copie pour éviter que les modifications d'index n'altèrent la référence
            cheminEnnemi = new ArrayList<>(this.cheminParfaitDroiteBas);
        } else {
            // Les autres ennemis calculent leur chemin normalement (et contournent si besoin)
            cheminEnnemi = terrain.algoBFS(source, cible);
        }

        Ennemis modele = switch (typeE) {
            case "TORTUE"   -> new Tortue(coin[1], coin[0], terrain, cheminEnnemi, cible);
            case "SKELETON" -> new Skeleton(coin[1], coin[0], terrain, cheminEnnemi, cible);
            case "BOO"      -> new Boo(coin[1], coin[0], terrain, cheminEnnemi, cible);
            case "GOOMBA"   -> new Goomba(coin[1], coin[0], terrain, cheminEnnemi, cible);
            case "BOBOMB"   -> new Bobomb(coin[1], coin[0], terrain, cheminEnnemi, cible);
            case "BILL"     -> new Bill(coin[1], coin[0], terrain, cheminEnnemi, cible);
            case "NINJI"    -> new Ninji(coin[1], coin[0], terrain, cheminEnnemi, cible);
            case "BROWSERJR"-> new BrowserJr(coin[1], coin[0], terrain, cheminEnnemi, cible);
            case "BROWSER"  -> new Browser(coin[1], coin[0], terrain, cheminEnnemi, cible);
            case "BOSS"     -> new Boss(coin[1], coin[0], terrain, cheminEnnemi, cible);
            default         -> new Tortue(coin[1], coin[0], terrain, cheminEnnemi, cible);
        };
        ennemis.add(modele);
    }

    // -----------------------------------------------------------
    // TOURS
    // -----------------------------------------------------------
    public void poserTour(Tour t, int cout) {
        if (pieces.get() >= cout) {
            pieces.set(pieces.get() - cout);
            tours.add(t); // â†’ ListChangeListener crÃ©e le sprite automatiquement !
            if (t instanceof TourObstacle) {
                terrain.bloquerCase((int) t.getY(), (int) t.getX());
                recalculerTousLesChemins();
            }
        } else {
            System.out.println("Fonds insuffisants !");
        }
    }
    public void recalculerTousLesChemins() {
        for (Ennemis e : ennemis) {
            e.recalculerChemin(terrain);
        }
    }

    // -----------------------------------------------------------
    // TEXTE COUNTDOWN â†’ affichÃ© dans le contrÃ´leur
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
    // UTILITAIRES PRIVÃ‰S
    // -----------------------------------------------------------
    private void supprimerEnnemi(int i) {
        // On supprime de la liste â†’ le ListChangeListener supprime le sprite automatiquement !
        ennemis.remove(i);
    }

    // -----------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------
    public ObservableList<Ennemis> getEnnemis() { return ennemis; }
    public ObservableList<Tour> getTours() { return tours; }
    public ObservableList<Projectile> getProjectiles() {return projectiles;}
    public Chateau getChateau() { return chateau; }
    public Terrain getTerrain() { return terrain; }
    public boolean estTermine() { return chateau.estDetruit(); }
    public IntegerProperty piecesProperty() { return pieces; }
    public IntegerProperty numeroVagueProperty() { return numeroVague; }
    public int getPieces() { return pieces.get(); }
    public int getNumeroVague() { return numeroVague.get(); }


}