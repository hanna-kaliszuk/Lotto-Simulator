package testy;

import centrala.losowanie.Losowanie;
import finanse.BudżetPaństwa;
import finanse.Kwota;
import kupony.Blankiet;
import kupony.Zakład;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class testyTest {

    @BeforeEach
    void setUp() {
        BudżetPaństwa.getInstancja();
    }

    // KWOTA TESTS
    @Nested
    @DisplayName("Kwota Tests")
    class KwotaTests {

        @Test
        @DisplayName("Kwota - konstruktor podstawowy")
        void testKwotaConstructor() {
            Kwota kwota = new Kwota(100, 50);
            assertEquals(100, kwota.getZłote());
            assertEquals(50, kwota.getGrosze());
        }

        @Test
        @DisplayName("Kwota - konstruktor z błędnymi groszami")
        void testKwotaInvalidGrosze() {
            assertThrows(IllegalArgumentException.class, () -> new Kwota(100, -1));
            assertThrows(IllegalArgumentException.class, () -> new Kwota(100, 100));
        }

        @Test
        @DisplayName("Kwota - dodawanie")
        void testKwotaDodawanie() {
            Kwota kwota1 = new Kwota(100, 75);
            Kwota kwota2 = new Kwota(50, 50);

            kwota1.dodaj(kwota2);

            assertEquals(151, kwota1.getZłote());
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
        @DisplayName("Kwota - mnożenie przez double")
        void testKwotaMnożenieDouble() {
            Kwota kwota = new Kwota(100, 5);
            kwota.pomnóż(0.5);

            assertEquals(50, kwota.getZłote());
            assertEquals(2, kwota.getGrosze());
        }

        @Test
        @DisplayName("Kwota - mnożenie przez int")
        void testKwotaMnożenieInt() {
            Kwota kwota = new Kwota(50, 25);
            kwota.pomnóż(5);

            assertEquals(251, kwota.getZłote());
            assertEquals(25, kwota.getGrosze());
        }

        @Test
        @DisplayName("Kwota - porównywanie")
        void testKwotaPorównywanie() {
            Kwota kwota1 = new Kwota(100, 50);
            Kwota kwota2 = new Kwota(100, 50);
            Kwota kwota3 = new Kwota(150, 0);

            assertEquals(0, kwota1.porównaj(kwota2));
            assertTrue(kwota3.porównaj(kwota1) > 0);
            assertTrue(kwota1.porównaj(kwota3) < 0);
        }

        @Test
        @DisplayName("Kwota - saldo ujemne")
        void testKwotaSaldoUjemne() {
            Kwota kwota1 = new Kwota(100, 0);
            Kwota kwota2 = new Kwota(150, 0);

            kwota1.odejmij(kwota2);
            assertTrue(kwota1.saldoUjemne());
        }

        @Test
        @DisplayName("Kwota - equals and hashCode")
        void testKwotaEqualsHashCode() {
            Kwota kwota1 = new Kwota(100, 50);
            Kwota kwota2 = new Kwota(100, 50);
            Kwota kwota3 = new Kwota(200, 0);

            assertEquals(kwota1, kwota2);
            assertNotEquals(kwota1, kwota3);
            assertEquals(kwota1.hashCode(), kwota2.hashCode());
        }
    }

    // ZAKŁAD TESTS
    @Nested
    @DisplayName("Zakład Tests")
    class ZakładTests {

        @Test
        @DisplayName("Zakład - konstruktor poprawny")
        void testZakładPoprawnyKonstruktor() {
            int[] liczby = {1, 15, 23, 34, 42, 49};
            Zakład zakład = new Zakład(liczby);

            assertTrue(zakład.czyWażny());
            assertFalse(zakład.czyAnulowany());
        }

        @Test
        @DisplayName("Zakład - konstruktor z błędną liczbą liczb")
        void testZakładBłędnyKonstruktor() {
            int[] liczby = {1, 2, 3, 4, 5}; // tylko 5 liczb
            Zakład zakład = new Zakład(liczby);

            assertFalse(zakład.czyWażny());
        }

        @Test
        @DisplayName("Zakład - obliczanie trafień")
        void testZakładTrafienia() {
            int[] liczby = {1, 15, 23, 34, 42, 49};
            int[] wylosowane = {1, 10, 23, 30, 42, 48};

            Zakład zakład = new Zakład(liczby);
            int trafienia = zakład.ileTrafień(wylosowane);

            assertEquals(3, trafienia); // 1, 23, 42
        }

        @Test
        @DisplayName("Zakład - anulowanie")
        void testZakładAnulowanie() {
            int[] liczby = {1, 15, 23, 34, 42, 49};
            Zakład zakład = new Zakład(liczby);

            assertFalse(zakład.czyAnulowany());
            zakład.anulujZakład();
            assertTrue(zakład.czyAnulowany());
        }

        @Test
        @DisplayName("Zakład - kopia")
        void testZakładKopia() {
            int[] liczby = {1, 15, 23, 34, 42, 49};
            Zakład original = new Zakład(liczby);
            original.anulujZakład();

            Zakład kopia = original.kopia();

            assertTrue(kopia.czyWażny());
            assertTrue(kopia.czyAnulowany());

            // Test that modifying copy doesn't affect original
            assertNotSame(original, kopia);
        }
    }

    // BLANKIET TESTS
    @Nested
    @DisplayName("Blankiet Tests")
    class BlankietTests {

        @Test
        @DisplayName("Blankiet - podstawowy konstruktor")
        void testBlankietKonstruktor() {
            List<Zakład> zakłady = List.of(
                    new Zakład(new int[]{1, 2, 3, 4, 5, 6}),
                    new Zakład(new int[]{7, 8, 9, 10, 11, 12})
            );

            Blankiet blankiet = new Blankiet(zakłady, 5);

            assertEquals(5, blankiet.naIleLosowań());
            assertEquals(2, blankiet.ileZakładów());
        }

        @Test
        @DisplayName("Blankiet - getListaZakładów zwraca kopię")
        void testBlankietKopiaZakładów() {
            List<Zakład> original = new ArrayList<>();
            original.add(new Zakład(new int[]{1, 2, 3, 4, 5, 6}));

            Blankiet blankiet = new Blankiet(original, 3);
            List<Zakład> kopia = blankiet.getListaZakładów();

            assertEquals(original.size(), kopia.size());
            assertNotSame(original.get(0), kopia.get(0)); // Deep copy
        }
    }

    // LOSOWANIE TESTS
    @Nested
    @DisplayName("Losowanie Tests")
    class LosowanieTests {

        @Test
        @DisplayName("Losowanie - konstruktor")
        void testLosowanieKonstruktor() {
            Losowanie losowanie = new Losowanie(42);

            assertEquals(42, losowanie.getNrLosowania());
            assertNotNull(losowanie.getWylosowaneLiczby());
            assertEquals(6, losowanie.getWylosowaneLiczby().length);
        }

        @Test
        @DisplayName("Losowanie - wylosowane liczby w zakresie 1-49")
        void testLosowanieZakresLiczb() {
            Losowanie losowanie = new Losowanie(1);
            int[] liczby = losowanie.getWylosowaneLiczby();

            for (int liczba : liczby) {
                assertTrue(liczba >= 1 && liczba <= 49);
            }
        }

        @Test
        @DisplayName("Losowanie - wszystkie liczby różne")
        void testLosowanieRóżneLiczby() {
            Losowanie losowanie = new Losowanie(1);
            int[] liczby = losowanie.getWylosowaneLiczby();

            for (int i = 0; i < liczby.length; i++) {
                for (int j = i + 1; j < liczby.length; j++) {
                    assertNotEquals(liczby[i], liczby[j]);
                }
            }
        }
    }
}