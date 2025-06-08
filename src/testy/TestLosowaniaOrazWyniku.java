package testy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import totolotek.centrala.Centrala;
import totolotek.centrala.losowanie.Losowanie;
import totolotek.centrala.losowanie.Wynik;
import totolotek.finanse.Kwota;
import totolotek.finanse.BudżetPaństwa;
import totolotek.gracze.Minimalista;
import totolotek.kolektura.Kolektura;
import totolotek.kupony.Blankiet;
import totolotek.kupony.Zakład;
import wyjątki.*;

import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class TestLosowaniaOrazWyniku {
    @BeforeEach
    void setUp() throws NieprawidłoweDane {
        Centrala.resetInstancji();
        BudżetPaństwa.resetInstancji();
        Kolektura.resetujNumerację();
        Centrala.inicjalizujCentralę(new Kwota(10_000_000, 0));
    }

    @Test
    void testKonstruktora() throws NieprawidłoweDane {
        Losowanie losowanie = new Losowanie(1);

        assertEquals(1, losowanie.getNrLosowania(), "Numer losowania powinien być ustawiony poprawnie");
        assertNotNull(losowanie.getWylosowaneLiczby(), "Wylosowane liczby nie mogą być null");
    }

    @Test
    void testWylosowaneLiczby() throws NieprawidłoweDane {
        Losowanie losowanie = new Losowanie(1);
        int[] liczby = losowanie.getWylosowaneLiczby();

        assertNotNull(liczby, "Wylosowane liczby nie mogą być null");
        assertEquals(6, liczby.length, "Powinno być wylosowanych 6 liczb");

        // czy liczby są w zakresie 1-49
        for (int liczba : liczby) {
            assertTrue(liczba >= 1 && liczba <= 49,
                    "Każda wylosowana liczba powinna być w zakresie 1-49, było: " + liczba);
        }

        // czy liczby są unikalne
        TreeSet<Integer> unikalneLiczby = new TreeSet<>();
        for (int liczba : liczby) {
            unikalneLiczby.add(liczba);
        }
        assertEquals(6, unikalneLiczby.size(), "Wylosowane liczby powinny być unikalne");


        // czy liczby są posortowane
        for (int i = 0; i < liczby.length - 1; i++) {
            assertTrue(liczby[i] < liczby[i + 1],
                    "Wylosowane liczby powinny być posortowane rosnąco");
        }
    }

    @Test
    void testZbieranieKuponów() throws NieprawidłoweDane, BrakŚrodków {
        Centrala centrala = Centrala.getInstancja();
        Kolektura kolektura = new Kolektura();

        // Utwórz gracza i sprzedaj kupon
        Minimalista gracz = new Minimalista("Jan", "Kowalski", 123456789, new Kwota(100, 0), kolektura);
        Blankiet blankiet = new Blankiet(List.of(
                new Zakład(new int[]{1, 2, 3, 4, 5, 6})
        ), 1);
        kolektura.sprzedajKuponZBlankietu(blankiet, gracz);

        Losowanie losowanie = new Losowanie(1);
        losowanie.zbierzKupony(List.of(kolektura));

        // Po zebraniu kuponów powinno być możliwe zliczenie zakładów
        int ileZakładów = losowanie.ileZakładów();
        assertEquals(1, ileZakładów, "Powinien być jeden zakład biorący udział w losowaniu");
    }

    @Test
    void testGetTrafienia() throws NieprawidłoweDane, BrakŚrodków {
        Kolektura kolektura = new Kolektura();

        // Utwórz gracza i sprzedaj kupon
        Minimalista gracz = new Minimalista("Jan", "Kowalski", 123456789, new Kwota(100, 0), kolektura);
        Blankiet blankiet = new Blankiet(List.of(
                new Zakład(new int[]{1, 2, 3, 4, 5, 6})
        ), 1);
        kolektura.sprzedajKuponZBlankietu(blankiet, gracz);

        Losowanie losowanie = new Losowanie(1);
        losowanie.zbierzKupony(List.of(kolektura));
        losowanie.ileZakładów(); // Potrzebne do zebrania zakładów

        // Sprawdź czy getTrafienia zwraca sensowne wartości
        int trafienia1 = losowanie.getTrafienia(1); // 6 trafionych
        int trafienia2 = losowanie.getTrafienia(2); // 5 trafionych
        int trafienia3 = losowanie.getTrafienia(3); // 4 trafione
        int trafienia4 = losowanie.getTrafienia(4); // 3 trafione

        assertTrue(trafienia1 >= 0, "Liczba trafień I stopnia powinna być nieujemna");
        assertTrue(trafienia2 >= 0, "Liczba trafień II stopnia powinna być nieujemna");
        assertTrue(trafienia3 >= 0, "Liczba trafień III stopnia powinna być nieujemna");
        assertTrue(trafienia4 >= 0, "Liczba trafień IV stopnia powinna być nieujemna");

        // Sprawdź nieprawidłowy stopień
        assertEquals(0, losowanie.getTrafienia(0), "Nieprawidłowy stopień powinien zwrócić 0");
        assertEquals(0, losowanie.getTrafienia(5), "Nieprawidłowy stopień powinien zwrócić 0");
    }

    @Test
    void testWynikKonstruktor() throws NieprawidłoweDane {
        Losowanie losowanie = new Losowanie(1);
        Kwota[] wygraneKwoty = {new Kwota(1000, 0), new Kwota(500, 0), new Kwota(100, 0), new Kwota(50, 0)};
        Kwota[] puleNagród = {new Kwota(10000, 0), new Kwota(5000, 0), new Kwota(1000, 0), new Kwota(500, 0)};
        int[] trafioneZakłady = {1, 2, 5, 10};

        Wynik wynik = new Wynik(losowanie, wygraneKwoty, puleNagród, trafioneZakłady);

        assertNotNull(wynik, "Wynik nie może być null");
        assertEquals(1, wynik.getNrLosowania(), "Numer losowania w wyniku powinien być poprawny");
        assertArrayEquals(losowanie.getWylosowaneLiczby(), wynik.getWylosowaneLiczby(),
                "Wylosowane liczby powinny być identyczne");
    }

    @Test
    void testWynikNieprawidłowaLiczbaTrafień() throws NieprawidłoweDane {
        Losowanie losowanie = new Losowanie(1);
        Kwota[] wygraneKwoty = {new Kwota(0, 0), new Kwota(0, 0), new Kwota(0, 0), new Kwota(0, 0)};
        Kwota[] puleNagród = {new Kwota(0, 0), new Kwota(0, 0), new Kwota(0, 0), new Kwota(0, 0)};
        int[] trafioneZakłady = {1, 2, 5, 10};
        // wyżej wprowadzone wartości nie mają znaczenia, bo testujemy jedynie wyjątki
        Wynik wynik = new Wynik(losowanie, wygraneKwoty, puleNagród, trafioneZakłady);

        assertThrows(NieprawidłoweDane.class, () -> {
            wynik.getNagrodaZaTrafienia(-1);
        }, "Powinna być rzucona wyjątek dla ujemnej liczby trafień");

        assertThrows(NieprawidłoweDane.class, () -> {
            wynik.getNagrodaZaTrafienia(7);
        }, "Powinna być rzucona wyjątek dla liczby trafień większej niż 6");

        assertThrows(NieprawidłoweDane.class, () -> {
            wynik.getNagrodaZaTrafienia(0);
        }, "Powinna być rzucona wyjątek dla 0 trafień");

        assertThrows(NieprawidłoweDane.class, () -> {
            wynik.getNagrodaZaTrafienia(1);
        }, "Powinna być rzucona wyjątek dla 1 trafienia");

        assertThrows(NieprawidłoweDane.class, () -> {
            wynik.getNagrodaZaTrafienia(2);
        }, "Powinna być rzucona wyjątek dla 2 trafień");
    }

    @Test
    void testLosowaniaWCentrali() throws NieprawidłoweDane, BrakŚrodków {
        Centrala centrala = Centrala.getInstancja();

        // Sprzedaj kupon przed losowaniem
        Kolektura kolektura = new Kolektura();
        Minimalista gracz = new Minimalista("Jan", "Kowalski", 123456789, new Kwota(100, 0), kolektura);
        Blankiet blankiet = new Blankiet(List.of(
                new Zakład(new int[]{1, 2, 3, 4, 5, 6})
        ), 1);
        kolektura.sprzedajKuponZBlankietu(blankiet, gracz);

        int nrPrzed = centrala.getNrNastępnegoLosowania();
        assertEquals(1, nrPrzed, "Pierwsze losowanie powinno mieć numer 1");

        centrala.przeprowadźLosowanie();

        int nrPo = centrala.getNrNastępnegoLosowania();
        assertEquals(2, nrPo, "Po pierwszym losowaniu następne powinno mieć numer 2");

        // Sprawdź czy można pobrać wynik pierwszego losowania
        Wynik wynik1 = centrala.getWynikLosowania(1);
        assertNotNull(wynik1, "Wynik pierwszego losowania powinien być dostępny");
        assertNotNull(wynik1.getWylosowaneLiczby(), "Wylosowane liczby powinny być dostępne");
        assertEquals(1, wynik1.getNrLosowania(), "Numer losowania w wyniku powinien być 1");
    }

    @Test
    void testPobieranieNieistniejącegoWyniku() {
        Centrala centrala = Centrala.getInstancja();

        assertThrows(NieprawidłoweDane.class, () -> {
            centrala.getWynikLosowania(99);
        }, "Pobranie wyniku nieistniejącego losowania powinno rzucić wyjątek");

        assertThrows(NieprawidłoweDane.class, () -> {
            centrala.getWynikLosowania(0);
        }, "Pobranie wyniku dla numeru 0 powinno rzucić wyjątek");

        assertThrows(NieprawidłoweDane.class, () -> {
            centrala.getWynikLosowania(-1);
        }, "Pobranie wyniku dla ujemnego numeru powinno rzucić wyjątek");
    }

    @Test
    void testUnikalnośćWylosowanychLiczb() throws NieprawidłoweDane {
        // Przeprowadź kilka losowań i sprawdź czy wyniki się różnią
        Losowanie losowanie1 = new Losowanie(1);
        Losowanie losowanie2 = new Losowanie(2);
        Losowanie losowanie3 = new Losowanie(3);

        int[] liczby1 = losowanie1.getWylosowaneLiczby();
        int[] liczby2 = losowanie2.getWylosowaneLiczby();
        int[] liczby3 = losowanie3.getWylosowaneLiczby();

        // Sprawdź czy przynajmniej niektóre wyniki się różnią
        // (teoretycznie mogą być identyczne, ale prawdopodobieństwo jest bardzo małe)
        boolean różne = !java.util.Arrays.equals(liczby1, liczby2) ||
                !java.util.Arrays.equals(liczby2, liczby3) ||
                !java.util.Arrays.equals(liczby1, liczby3);

        assertTrue(różne, "Losowania powinny generować różne wyniki (bardzo wysokie prawdopodobieństwo)");
    }





}
