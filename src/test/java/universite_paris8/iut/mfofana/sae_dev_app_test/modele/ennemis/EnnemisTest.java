package universite_paris8.iut.mfofana.sae_dev_app_test.modele.ennemis;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.Terrain;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de la logique commune des ennemis (classe abstraite Ennemis),
 * testee a travers des sous-classes concretes (Goomba, Tortue).
 * On verifie : degats/mort, effets de statut (brulure, ralentissement)
 * et le deplacement le long d'un chemin.
 */
public class EnnemisTest {

    private Terrain terrain;

    @BeforeEach
    void setUp() {
        terrain = new Terrain();
    }


    private Goomba goomba(double x, double y, int pv, int vitesse, List<Point2D> chemin) {
        Point2D cible = chemin.isEmpty() ? null : chemin.get(chemin.size() - 1);
        return new Goomba(x, y, terrain, pv, vitesse, chemin, cible);
    }


    //  Degats et mort
    @Test
    void degatSimple() {
        Goomba g = goomba(12, 0, 30, 2, List.of(new Point2D(12, 0)));

        g.subirDegat(10);
        assertEquals(20, g.getPv());
        assertFalse(g.estMort());
    }

    @Test
    void pvBornesAZero() {
        Goomba g = goomba(12, 0, 30, 2, List.of(new Point2D(12, 0)));

        g.subirDegat(100); // plus que ses PV
        assertEquals(0, g.getPv(), "Les PV doivent etre bornes a 0, pas negatifs");
        assertTrue(g.estMort());
    }

    @Test
    @DisplayName("getRecompense est specifique a chaque type d'ennemi (Tortue = 5)")
    void recompenseParType() {
        Tortue t = new Tortue(12, 0, terrain, List.of(new Point2D(12, 0)), new Point2D(12, 0));
        Goomba g = goomba(12, 0, 30, 2, List.of(new Point2D(12, 0)));

        assertEquals(5, t.getRecompense());
        assertEquals(10, g.getRecompense());
    }


    //  Effet de brulure
    @Test
    @DisplayName("La brulure inflige 2 degats par tick pendant exactement le nombre de ticks prevu")
    void brulureDureeExacte() {
        Goomba g = goomba(12, 0, 100, 2, List.of(new Point2D(12, 0)));
        g.setTicksBrulure(5);

        for (int i = 0; i < 5; i++) {
            g.mettreAJourEffets();
        }
        assertEquals(90, g.getPv(), "5 ticks * 2 degats = 10 PV perdus");

        // Un tick de plus ne doit plus rien faire : la brulure est terminee
        g.mettreAJourEffets();
        assertEquals(90, g.getPv());
    }

    @Test
    @DisplayName("La brulure ne se recharge pas tant qu'elle est deja active")
    void brulureNonCumulable() {
        Goomba g = goomba(12, 0, 100, 2, List.of(new Point2D(12, 0)));

        g.setTicksBrulure(5);
        g.mettreAJourEffets();
        g.setTicksBrulure(5);

        for (int i = 0; i < 4; i++) g.mettreAJourEffets();
        assertEquals(90, g.getPv());

        // Même verification qu'au desssus (ligne 79)
        g.mettreAJourEffets();
        assertEquals(90, g.getPv());
    }

    //  Effet de ralentissement
    @Test
    @DisplayName("Le ralentissement divise la vitesse par deux puis la restaure a la fin")
    void ralentissementPuisRestauration() {
        Goomba g = goomba(12, 0, 100, 4, List.of(new Point2D(12, 0)));

        g.setRalenti(3);
        assertEquals(2, g.getVitesse(), "Vitesse divisee par 2 (4 -> 2)");

        g.mettreAJourEffets();
        g.mettreAJourEffets();
        g.mettreAJourEffets(); // fin du ralentissement

        assertEquals(4, g.getVitesse(), "Vitesse d'origine restauree");
    }

    @Test
    @DisplayName("Deux ralentissements successifs ne s'empilent pas, pas de x2")
    void ralentissementNonCumulable() {
        Goomba g = goomba(12, 0, 100, 4, List.of(new Point2D(12, 0)));

        g.setRalenti(5);
        g.setRalenti(5); // deja ralenti : ne doit pas re-diviser

        assertEquals(2, g.getVitesse(), "Doit rester a 2, surtout pas tomber a 1");
    }

    @Test
    @DisplayName("La vitesse ralentie ne descend jamais sous 1 ")
    void ralentissementPlancherAUn() {
        Goomba g = goomba(12, 0, 100, 1, List.of(new Point2D(12, 0)));

        g.setRalenti(3);
        assertEquals(1, g.getVitesse(), "max(1, 1/2) = 1");
    }


    //  Deplacement le long du chemin
    @Test
    @DisplayName("Au depart, l'ennemi n'a pas encore atteint le chateau")
    void pasAtteintAuDepart() {
        Goomba g = goomba(12, 0, 30, 60,
                List.of(new Point2D(12, 0), new Point2D(12, 1)));
        assertFalse(g.aAtteintLeChateau());
    }

    @Test
    @DisplayName("seDeplacer avance progressivement vers la case suivante quand la vitesse est faible")
    void deplacementProgressif() {
        Goomba g = goomba(12, 0, 30, 30, List.of(new Point2D(12, 0), new Point2D(12, 1)));
        // Vitesse de 30 = avancement 0.5 par ticks
        g.seDeplacer();

        assertEquals(0.5, g.getY(), "Avance d'un demi-pas vers (12,1)");
        assertFalse(g.aAtteintLeChateau(), "Pas encore arrive sur la case suivante");
    }

    @Test
    @DisplayName("Quand l'ennemi atteint la derniere case du chemin, aAtteintLeChateau devient vrai")
    void arriveeEnBoutDeChemin() {
        // vitesse 60 => pas de 1.0 par tick
        Goomba g = goomba(12, 0, 30, 60,
                List.of(new Point2D(12, 0), new Point2D(12, 1)));

        g.seDeplacer();

        assertEquals(1.0, g.getY());
        assertTrue(g.aAtteintLeChateau());
    }

    @Test
    @DisplayName("horsDesLimites detecte une position negative")
    void detectionHorsLimites() {
        Goomba g = goomba(12, 0, 30, 2, List.of(new Point2D(12, 0)));
        assertFalse(g.horsDesLimites());

        g.setX(-1);
        assertTrue(g.horsDesLimites());
    }
}
