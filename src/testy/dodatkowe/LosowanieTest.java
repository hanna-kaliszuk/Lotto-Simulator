package testy.dodatkowe;

import totolotek.centrala.Centrala;
import totolotek.centrala.losowanie.Losowanie;
import totolotek.finanse.Kwota;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LosowanieTest {
    @BeforeEach
    void setUp() {
        Centrala.resetInstancji();
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
    }

    @Test
    void testKonstruktor() {
        Losowanie losowanie = new Losowanie(1);

        assertEquals(1, losowanie.getNrLosowania(), "Should have correct drawing number");

        int[] wylosowane = losowanie.getWylosowaneLiczby();
        assertEquals(6, wylosowane.length, "Should have 6 drawn numbers");

        // Check if numbers are in range 1-49
        for (int liczba : wylosowane) {
            assertTrue(liczba >= 1 && liczba <= 49, "Numbers should be between 1 and 49");
        }

        // Check if numbers are sorted
        for (int i = 1; i < wylosowane.length; i++) {
            assertTrue(wylosowane[i-1] < wylosowane[i], "Numbers should be sorted and unique");
        }
    }

    @Test
    void testGetWylosowaneLiczby() {
        Losowanie losowanie = new Losowanie(1);

        int[] pierwszePobranie = losowanie.getWylosowaneLiczby();
        int[] drugiePobranie = losowanie.getWylosowaneLiczby();

        assertArrayEquals(pierwszePobranie, drugiePobranie, "Should return the same numbers");
        assertNotSame(pierwszePobranie, drugiePobranie, "Should return a copy, not the original");
    }

    @Test
    void testGetTrafieniaWithInvalidLevel() {
        Losowanie losowanie = new Losowanie(1);

        int trafienia = losowanie.getTrafienia(5); // invalid level

        assertEquals(0, trafienia, "Should return 0 for invalid level");
    }

    @Test
    void testToString() {
        Losowanie losowanie = new Losowanie(5);

        String result = losowanie.toString();

        assertTrue(result.contains("Losowanie nr 5"), "Should contain drawing number");
        assertTrue(result.contains("Wyniki:"), "Should contain results label");
    }
}