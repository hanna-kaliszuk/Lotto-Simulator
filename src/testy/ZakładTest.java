package testy;

import centrala.Centrala;
import finanse.Kwota;
import kupony.Zakład;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ZakładTest {
    @BeforeEach
    void setUp() {
        Centrala.resetInstancji();
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
    }

    @Test
    void testKonstruktor() {
        int[] liczby = {1, 15, 23, 34, 42, 49};
        Zakład zakład = new Zakład(liczby);

        assertFalse(zakład.czyAnulowany(), "New bet should not be cancelled");
        assertTrue(zakład.jestWażny(), "Bet with 6 numbers should be valid");
    }

    @Test
    void testJestWażnyWithValidNumbers() {
        int[] liczby = {1, 2, 3, 4, 5, 6};
        Zakład zakład = new Zakład(liczby);

        assertTrue(zakład.jestWażny(), "Bet with exactly 6 numbers should be valid");
    }

    @Test
    void testJestWażnyWithInvalidNumbers() {
        int[] liczby = {1, 2, 3, 4, 5}; // only 5 numbers
        Zakład zakład = new Zakład(liczby);

        assertFalse(zakład.jestWażny(), "Bet with less than 6 numbers should be invalid");
    }

    @Test
    void testGetLiczby() {
        int[] liczby = {1, 15, 23, 34, 42, 49};
        Zakład zakład = new Zakład(liczby);

        int[] returned = zakład.getLiczby();

        assertArrayEquals(liczby, returned, "Should return the same numbers");
        assertNotSame(liczby, returned, "Should return a copy, not the original array");
    }

    @Test
    void testSprawdźTrafienia() {
        int[] liczby = {1, 15, 23, 34, 42, 49};
        int[] wylosowane = {1, 15, 23, 10, 20, 30};

        Zakład zakład = new Zakład(liczby);
        int trafienia = zakład.sprawdźTrafienia(wylosowane);

        assertEquals(3, trafienia, "Should have 3 matches");
    }

    @Test
    void testSprawdźTrafieniaNoMatches() {
        int[] liczby = {1, 2, 3, 4, 5, 6};
        int[] wylosowane = {10, 20, 30, 40, 45, 49};

        Zakład zakład = new Zakład(liczby);
        int trafienia = zakład.sprawdźTrafienia(wylosowane);

        assertEquals(0, trafienia, "Should have no matches");
    }

    @Test
    void testSprawdźTrafieniaAllMatches() {
        int[] liczby = {1, 2, 3, 4, 5, 6};
        int[] wylosowane = {1, 2, 3, 4, 5, 6};

        Zakład zakład = new Zakład(liczby);
        int trafienia = zakład.sprawdźTrafienia(wylosowane);

        assertEquals(6, trafienia, "Should have all 6 matches");
    }
}