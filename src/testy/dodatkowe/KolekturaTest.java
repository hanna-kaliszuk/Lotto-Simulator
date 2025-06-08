package testy.dodatkowe;

import totolotek.centrala.Centrala;
import totolotek.finanse.Kwota;
import totolotek.kolektura.Kolektura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KolekturaTest {

    private Centrala centrala;

    @BeforeEach
    void setUp() {
        Centrala.resetInstancji();
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
        centrala = Centrala.getInstancja();
    }


    @Test
    void testKonstruktor() {
        Kolektura kolektura1 = new Kolektura();
        Kolektura kolektura2 = new Kolektura();

        assertNotEquals(kolektura1.getNrKolektury(), kolektura2.getNrKolektury(),
                "Each totolotek.kolektura should have a unique number");

        assertTrue(kolektura1.getSprzedaneKupony().isEmpty(),
                "New totolotek.kolektura should have no sold coupons");
    }

    @Test
    void testUniqueNumbers() {
        Kolektura k1 = new Kolektura();
        Kolektura k2 = new Kolektura();
        Kolektura k3 = new Kolektura();

        assertEquals(1, k1.getNrKolektury(), "First totolotek.kolektura should have number 1");
        assertEquals(2, k2.getNrKolektury(), "Second totolotek.kolektura should have number 2");
        assertEquals(3, k3.getNrKolektury(), "Third totolotek.kolektura should have number 3");
    }

    @Test
    void testGetSprzedaneKupony() {
        Kolektura kolektura = new Kolektura();

        assertNotNull(kolektura.getSprzedaneKupony(), "Should return a list");
        assertEquals(0, kolektura.getSprzedaneKupony().size(),
                "New totolotek.kolektura should have empty list");
    }
}