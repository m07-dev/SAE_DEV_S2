package universite_paris8.iut.mfofana.sae_dev_app_test.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.TourBouleDeFeu;
import universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour.TourObstacle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de la classe Jeu : economie (pose / cout des tours), gestion des
 * vagues et pose d'obstacles (qui modifie le terrain).
 */
class JeuTest {

    private Jeu jeu;

    @BeforeEach
    void setUp() {
        jeu = new Jeu();
    }


    //  Etat initial
    @Test
    @DisplayName("Une partie demarre vague 0 avec aucune tour ni ennemi")
    void etatInitial() {
        assertEquals(0, jeu.getNumeroVague());
        assertTrue(jeu.getTours().isEmpty());
        assertTrue(jeu.getEnnemis().isEmpty());
    }


    //  Economie : pose de tours
    @Test
    @DisplayName("Poser une tour quand on a assez d'argent l'ajoute et debite le cout")
    void poseAvecAssezDArgent() {
        jeu.piecesProperty().set(100);

        jeu.poserTour(new TourBouleDeFeu(5, 5, 4, 3), 15);

        assertEquals(1, jeu.getTours().size());
        assertEquals(85, jeu.getPieces(), "100 - 15");
    }

    @Test
    @DisplayName("Poser une tour sans assez d'argent ne l'ajoute pas et ne debite rien")
    void poseSansAssezDArgent() {
        jeu.piecesProperty().set(10);

        jeu.poserTour(new TourBouleDeFeu(5, 5, 4, 3), 15);

        assertTrue(jeu.getTours().isEmpty(), "La tour ne doit pas etre posee");
        assertEquals(10, jeu.getPieces(), "Le solde ne doit pas changer");
    }


    //  Vagues
    @Test
    @DisplayName("forcerLancement demarre la vague suivante")
    void lancementDeVague() {
        jeu.forcerLancement();
        assertEquals(1, jeu.getNumeroVague());
    }

    @Test
    @DisplayName("forcerLancement n'enchaine pas deux vagues si une est deja en cours")
    void pasDeDoubleLancement() {
        jeu.forcerLancement();
        jeu.forcerLancement(); // ignore : une vague est deja en cours

        assertEquals(1, jeu.getNumeroVague());
    }


    //  Obstacles : la pose bloque la case sur le terrain
    @Test
    @DisplayName("Poser un obstacle bloque la case de chemin correspondante sur le terrain")
    void poseObstacleBloqueLeTerrain() {

        assertEquals(Terrain.CHEMIN, jeu.getTerrain().getTileTerrain(0, 12));

        jeu.poserTour(new TourObstacle(12, 0, "OBSTACLE"), 50);

        assertEquals(1, jeu.getNombreObstacles());
        assertEquals(Terrain.HERBE, jeu.getTerrain().getTileTerrain(0, 12),
                "La case doit etre bloquee (d apres la pose de l'obstacle");
    }
}
