package universite_paris8.iut.mfofana.sae_dev_app_test.modele.tour;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests du Chateau : points de vie, encaissement des degats et destruction. */
class ChateauTest {

    @Test
    @DisplayName("Un chateau neuf a 100 PV sur 100 et n'est pas detruit")
    void chateauNeuf() {
        Chateau c = new Chateau();
        assertEquals(100, c.getPv());
        assertEquals(100, c.getPvMax());
        assertFalse(c.estDetruit());
    }

    @Test
    @DisplayName("subirDegat retire les PV correspondants")
    void encaisseLesDegats() {
        Chateau c = new Chateau();
        c.subirDegat(30);
        assertEquals(70, c.getPv());
        assertFalse(c.estDetruit());
    }

    @Test
    @DisplayName("Les PV du chateau ne deviennent pas negatifs et il est detruit a 0")
    void destructionDuChateau() {
        Chateau c = new Chateau();
        c.subirDegat(200); // plus que ses PV
        assertEquals(0, c.getPv());
        assertTrue(c.estDetruit());
    }
}
