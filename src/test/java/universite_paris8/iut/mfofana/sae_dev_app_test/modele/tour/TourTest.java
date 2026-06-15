package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Ennemis;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis.Goomba;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de la logique des tours (classe abstraite Tour) testee a travers
 * ses sous-classes : statistiques, amelioration, resistance, paralysie,
 * cadence de tir, choix de cible et application des effets.
 */
public class TourTest {

    private Terrain terrain;

    @BeforeEach
    void setUp() {
        terrain = new Terrain();
    }

    private Goomba ennemiEn(double x, double y, int pv) {
        Point2D p = new Point2D(x, y);
        return new Goomba(x, y, terrain, pv, 2, List.of(p), p);
    }


    //  Statistiques de base
    @Test
    @DisplayName("La tour de feu est creee avec les bonnes statistiques")
    void infosTourFeu() {
        TourBouleDeFeu t = new TourBouleDeFeu(5, 5, 4, 3);

        assertEquals(15, t.getCout());
        assertEquals(15, t.getDegat());
        assertEquals(4,  t.getPortee());
        assertEquals(50, t.getResistance());
        assertEquals(1,  t.getNiveau());
    }

    @Test
    @DisplayName("vendre rend la moitie du cout")
    void venteRendLaMoitie() {
        TourBouleDeFeu feu = new TourBouleDeFeu(5, 5, 4, 3); // cout 15
        TourBombe bombe = new TourBombe(5, 5, 2);            // cout 50

        assertEquals(7,  feu.vendre());   // 15 / 2 (division entiere)
        assertEquals(25, bombe.vendre());
    }


    //  Resistance et destruction
    // Au vu du fait que les ennemis ne tire pas sur les tours, ce n'est pas pertinent mais ca fonctionne
    @Test
    @DisplayName("subirDegat reduit la resistance et la tour est detruite a 0")
    void destructionDeLaTour() {
        TourBouleDeFeu t = new TourBouleDeFeu(5, 5, 4, 3); // resistance 50

        t.subirDegat(30);
        assertEquals(20, t.getResistance());
        assertFalse(t.estDetruite());

        t.subirDegat(20);
        assertTrue(t.estDetruite());
    }

    @Test
    @DisplayName("La resistance ne devient jamais negative")
    void resistanceBorneeAZero() {
        TourBouleDeFeu t = new TourBouleDeFeu(5, 5, 4, 3);
        t.subirDegat(999);
        assertEquals(0, t.getResistance());
        assertTrue(t.estDetruite());
    }


    //  Amelioration (Niveau max 3)
    @Test
    @DisplayName("ameliorer augmente niveau, degats et portee")
    void ameliorationSimple() {
        TourBouleDeFeu t = new TourBouleDeFeu(5, 5, 4, 3);

        t.ameliorer();
        assertEquals(2,  t.getNiveau());
        assertEquals(20, t.getDegat());  // 15 + 5
        assertEquals(5,  t.getPortee()); // 4 + 1
    }

    @Test
    @DisplayName("L'amelioration est stopé au niveau 3")
    void ameliorationAuMax() {
        TourBouleDeFeu t = new TourBouleDeFeu(5, 5, 4, 3);

        for (int i = 0; i < 5; i++) t.ameliorer();

        assertEquals(3,  t.getNiveau(), "Le niveau ne depasse pas 3");
        assertEquals(25, t.getDegat(),  "15 + 2 ameliorations * 5");
        assertEquals(6,  t.getPortee(), "4 + 2 ameliorations * 1");
    }


    //  Paralysie
    @Test
    @DisplayName("paralyser rend la tour paralysee, mettreAJourStatut la libere apres la duree")
    void paralysiePuisLiberation() {
        TourBouleDeFeu t = new TourBouleDeFeu(5, 5, 4, 3);

        t.paralyser(3);
        assertTrue(t.estParalysee());

        t.mettreAJourStatut();
        t.mettreAJourStatut();
        assertTrue(t.estParalysee(), "Encore paralysee apres 2 ticks sur 3");

        t.mettreAJourStatut();
        assertFalse(t.estParalysee(), "Liberee apres 3 ticks");
    }

    @Test
    @DisplayName("paralyser ne fait que prolonger : une duree plus courte ne raccourcit pas la paralysie")
    void paralysiePrendLeMax() {
        TourBouleDeFeu t = new TourBouleDeFeu(5, 5, 4, 3);

        t.paralyser(5);
        t.paralyser(2); // plus court -> ignore

        for (int i = 0; i < 3; i++) t.mettreAJourStatut();
        assertTrue(t.estParalysee(), "Toujours paralysee : la duree 5 a ete conservee");
    }


    //  Cadence de tir (peutTirer)
    @Test
    @DisplayName(" un seul tir par intervalle")
    void respecteCadence() {
        TourBouleDeFeu t = new TourBouleDeFeu(5, 5, 4, 3); // cadence 1 -> 1 tir / 60 ticks

        assertFalse(t.peutTirer(0),  "Pas encore le moment de tirer");
        assertTrue(t.peutTirer(60),  "Apres 60 ticks, la tour peut tirer");
        assertFalse(t.peutTirer(61), "Trop tot pour le tir suivant");
        assertTrue(t.peutTirer(120), "Intervalle a nouveau ecoule");
    }

    // Une tour ne sera pas ammené à être paralysée (à voir) mais ça fonctionne
    @Test
    @DisplayName("Une tour paralysee ne peut pas tirer meme si la cadence le permet")
    void paralyseeNePeutPasTirer() {
        TourBouleDeFeu t = new TourBouleDeFeu(5, 5, 4, 3);
        t.paralyser(10);

        assertFalse(t.peutTirer(60));
    }


    //  Choix de cible
    @Test
    @DisplayName("Doit choisir l'ennemi le plus proche")
    void cibleLaPlusProche() {
        TourBouleDeFeu t = new TourBouleDeFeu(10, 10, 4, 3); // portee 4

        Ennemis proche = ennemiEn(11, 10, 50); // distance 1
        Ennemis loin   = ennemiEn(13, 10, 50); // distance 3
        ObservableList<Ennemis> ennemis = FXCollections.observableArrayList(loin, proche);

        assertSame(proche, t.choisirCible(ennemis));
    }

    @Test
    @DisplayName("choisirCible renvoie null si aucun ennemi n'est a portee")
    void aucuneCibleAPortee() {
        TourBouleDeFeu t = new TourBouleDeFeu(10, 10, 4, 3); // portee 4

        Ennemis horsPortee = ennemiEn(10, 20, 50); // plus bas de dix cases
        ObservableList<Ennemis> ennemis = FXCollections.observableArrayList(horsPortee);

        assertNull(t.choisirCible(ennemis));
    }


    //  Tir complet (degats + effet + cadence)
    @Test
    @DisplayName("tirer inflige les degats a la cible et applique la brulure (tour de feu)")
    void tirInfligeDegatsEtBrulure() {
        TourBouleDeFeu t = new TourBouleDeFeu(10, 10, 4, 3);
        Goomba cible = ennemiEn(11, 10, 50);
        ObservableList<Ennemis> ennemis = FXCollections.observableArrayList(cible);

        Ennemis touche = t.tirer(ennemis, 60);

        assertSame(cible, touche);
        assertEquals(35, cible.getPv(), "50 - 15 degats");

        // Brulure donc moins 2 pv
        cible.mettreAJourEffets();
        assertEquals(33, cible.getPv());
    }

    @Test
    @DisplayName("tirer ne tire pas deux fois dans le meme intervalle de cadence")
    void tirRespecteLaCadence() {
        TourBouleDeFeu t = new TourBouleDeFeu(10, 10, 4, 3);
        Goomba cible = ennemiEn(11, 10, 50);
        ObservableList<Ennemis> ennemis = FXCollections.observableArrayList(cible);

        t.tirer(ennemis, 60);                 // premier tir : pv -> 35
        Ennemis second = t.tirer(ennemis, 61); // trop tot

        assertNull(second);
        assertEquals(35, cible.getPv(), "Pas de second tir dans l'intervalle");
    }


    @Test
    @DisplayName("La tour de glace ralentit sa cible quand elle tire")
    void tirDeGlaceRalentit() {
        TourBouleDeGlace t = new TourBouleDeGlace(10, 10, 2, 4);
        Goomba cible = ennemiEn(11, 10, 50); // vitesse de base 2
        ObservableList<Ennemis> ennemis = FXCollections.observableArrayList(cible);

        t.tirer(ennemis, 60);

        assertEquals(1, cible.getVitesse(), "Vitesse divisee par 2 (2 -> 1) par l'effet de glace");
    }
}
