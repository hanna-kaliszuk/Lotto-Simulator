package testy;

import finanse.Kwota;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class testyTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Kwota - podstawowe operacje")
    void testKwotaPodstawoweOperacje() {
        Kwota kwota = new Kwota(100, 50);

        assertEquals(100, kwota.getZłote());
        assertEquals(50, kwota.getGrosze());
        assertFalse(kwota.saldoUjemne());
    }

    @Test
    @DisplayName("Kwota - dodawanie")
    void testKwotaDodawanie() {
        Kwota kwota1 = new Kwota(10, 50);
        Kwota kwota2 = new Kwota(5, 75);

        kwota1.dodaj(kwota2);

        assertEquals(16, kwota1.getZłote());
        assertEquals(25, kwota1.getGrosze());
    }

    @Test
    @DisplayName("Kwota - odejmowanie")
    void testKwotaOdejmowanie() {
        Kwota kwota1 = new Kwota(100, 25);
        Kwota kwota2 = new Kwota(50, 50);

        kwota1.odejmij(kwota2);

        assertEquals(49, kwota1.getZłote());
        assertEquals(75, kwota1.getGrosze());
    }

    @Test
    @DisplayName("Kwota - mnożenie przez int")
    void testKwotaMnożenieInt() {
        Kwota kwota = new Kwota(10, 50);

        kwota.pomnóż(3);

        assertEquals(31, kwota.getZłote());
        assertEquals(50, kwota.getGrosze());
    }

    @Test
    @DisplayName("Kwota - mnożenie przez double")
    void testKwotaMnożenieDouble() {
        Kwota kwota = new Kwota(10, 0);
        kwota.pomnóż(1.5);
        assertEquals(15, kwota.getZłote());
        assertEquals(0, kwota.getGrosze());

        Kwota kwota2 = new Kwota(10, 0);
        kwota2.pomnóż(0.75);
        assertEquals(7, kwota2.getZłote());
        assertEquals(50, kwota2.getGrosze());
    }

    @Test
    @DisplayName("Kwota - saldo ujemne")
    void testKwotaSaldoUjemne() {
        Kwota kwota1 = new Kwota(10, 0);
        Kwota kwota2 = new Kwota(20, 0);

        kwota1.odejmij(kwota2);

        assertTrue(kwota1.saldoUjemne());
        assertEquals(-10, kwota1.getZłote());
    }

    @Test
    @DisplayName("Kwota - porównywanie")
    void testKwotaPorównywanie() {
        Kwota kwota1 = new Kwota(10, 50);
        Kwota kwota2 = new Kwota(20, 0);
        Kwota kwota3 = new Kwota(10, 50);

        assertTrue(kwota1.porównaj(kwota2) < 0); // kwota1 < kwota2
        assertTrue(kwota2.porównaj(kwota1) > 0); // kwota2 > kwota1
        assertEquals(0, kwota1.porównaj(kwota3)); // kwota1 == kwota3
    }

    @Test
    @DisplayName("Kwota - nieprawidłowe grosze")
    void testKwotaNieprawidłoweGrosze() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Kwota(10, 100);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Kwota(10, -1);
        });
    }

    @Test
    @DisplayName("Kwota - equals i toString")
    void testKwotaEqualsToString() {
        Kwota kwota1 = new Kwota(10, 50);
        Kwota kwota2 = new Kwota(10, 50);
        Kwota kwota3 = new Kwota(20, 0);

        assertEquals(kwota1, kwota2);
        assertNotEquals(kwota1, kwota3);
        assertEquals("10 zł 50 gr", kwota1.toString());
    }
}