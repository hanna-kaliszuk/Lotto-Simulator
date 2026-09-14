package test.java.testy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.totolotek.centrala.Centrala;
import main.java.totolotek.centrala.losowanie.Wynik;
import main.java.totolotek.finanse.BudżetPaństwa;
import main.java.totolotek.finanse.Kwota;
import main.java.totolotek.gracze.Gracz;
import main.java.totolotek.gracze.Losowy;
import main.java.totolotek.gracze.Minimalista;
import main.java.totolotek.kolektura.Kolektura;
import main.java.totolotek.kupony.Blankiet;
import main.java.totolotek.kupony.Zakład;
import main.java.wyjątki.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCentrala {
    @BeforeEach
    void setUp() throws NieprawidłoweDane {
        Centrala.resetInstancji();
        BudżetPaństwa.getInstancja().resetujBudżet();
    }

    @Test
    void testInicjalizacjaCentrali() throws NieprawidłoweDane {
        Kwota kapitałPoczątkowy = new Kwota(1_000_000, 0);
        Centrala.inicjalizujCentralę(kapitałPoczątkowy);

        Centrala centrala = Centrala.getInstancja();
        assertNotNull(centrala, "Centrala powinna być zainicjalizowana");

        String sprawozdanie = centrala.sprawozdanieFinansowe();
        assertTrue(sprawozdanie.contains("1000000 zł 00 gr"),
                "Sprawozdanie powinno pokazać początkowy kapitał");
    }

    @Test
    void testSingleton() throws NieprawidłoweDane {
        Centrala.inicjalizujCentralę(new Kwota(500_000, 0));

        Centrala centrala1 = Centrala.getInstancja();
        Centrala centrala2 = Centrala.getInstancja();

        assertSame(centrala1, centrala2, "Centrala powinna być singletonem");
    }

    @Test
    void testBrakInicjalizacji() {
        assertThrows(IllegalStateException.class, Centrala::getInstancja,
                "Powinien rzucić wyjątek gdy centrala nie jest zainicjalizowana");
    }

    @Test
    void testResetInstancji() throws NieprawidłoweDane {
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
        Centrala centrala1 = Centrala.getInstancja();

        Centrala.resetInstancji();

        assertThrows(IllegalStateException.class, Centrala::getInstancja,
                "Po resecie getInstancja() powinno rzucić wyjątek");

        Centrala.inicjalizujCentralę(new Kwota(2_000_000, 0));
        Centrala centrala2 = Centrala.getInstancja();

        assertNotSame(centrala1, centrala2, "Po resecie powinna być nowa instancja");
    }

    @Test
    void testZarejestrujKolekturę() throws NieprawidłoweDane {
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));

        Kolektura kolektura1 = new Kolektura();
        Kolektura kolektura2 = new Kolektura();

        assertEquals(1, kolektura1.getNrKolektury(), "Pierwsza kolektura powinna mieć numer 1");
        assertEquals(2, kolektura2.getNrKolektury(), "Druga kolektura powinna mieć numer 2");
        assertEquals(2, Centrala.getInstancja().ileKolektur(), "Powinno być zarejestrowanych " +
                "2 kolektury");
    }

    @Test
    void testNrNastępnegoLosowania() throws NieprawidłoweDane {
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
        Centrala centrala = Centrala.getInstancja();

        assertEquals(1, centrala.getNrNastępnegoLosowania(),
                "Pierwszy numer losowania powinien być 1");

        centrala.przeprowadźLosowanie();
        assertEquals(2, centrala.getNrNastępnegoLosowania(),
                "Po losowaniu numer następnego powinien wzrosnąć");
    }

    @Test
    void testDodajŚrodki() throws NieprawidłoweDane {
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
        Centrala centrala = Centrala.getInstancja();

        Kwota dodatkoweŚrodki = new Kwota(500_000, 50);
        centrala.dodajŚrodki(dodatkoweŚrodki);

        String sprawozdanie = centrala.sprawozdanieFinansowe();
        assertTrue(sprawozdanie.contains("1500000 zł 50 gr"),
                "Sprawozdanie powinno pokazać zwiększone środki");
    }

    @Test
    void testDodajUjemneŚrodki() throws NieprawidłoweDane {
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
        Centrala centrala = Centrala.getInstancja();

        Kwota ujemnaKwota = new Kwota(-100, 0);

        assertThrows(NieprawidłoweDane.class, () -> {
            centrala.dodajŚrodki(ujemnaKwota);
        }, "Nie powinno być możliwe dodanie ujemnej kwoty");
    }

    @Test
    void testWydajNagrodę() throws NieprawidłoweDane {
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
        Centrala centrala = Centrala.getInstancja();

        Kwota nagroda = new Kwota(50_000, 0);
        centrala.wydajNagrodę(nagroda);

        String sprawozdanie = centrala.sprawozdanieFinansowe();
        assertTrue(sprawozdanie.contains("950000 zł 00 gr"),
                "Środki powinny zmniejszyć się o wypłaconą nagrodę");
    }

    @Test
    void testWydajNagrodęWiększąNiżŚrodki() throws NieprawidłoweDane {
        Centrala.inicjalizujCentralę(new Kwota(100_000, 0));
        Centrala centrala = Centrala.getInstancja();
        BudżetPaństwa budżet = BudżetPaństwa.getInstancja();

        Kwota dużaNagroda = new Kwota(150_000, 0);

        centrala.wydajNagrodę(dużaNagroda);

        String sprawozdanie = centrala.sprawozdanieFinansowe();
        assertTrue(sprawozdanie.contains("0 zł 00 gr"),
                "Centrala powinna mieć 0 środków po wypłacie większej nagrody");

        String raportBudżetu = budżet.raport();
        assertTrue(raportBudżetu.contains("Wydane subwencje: 50000 zł 00 gr"),
                "Budżet powinien pokryć różnicę jako subwencję");
    }

    @Test
    void testPrzeprowadźLosowanie() throws NieprawidłoweDane, BrakŚrodków {
        Centrala.inicjalizujCentralę(new Kwota(10_000_000, 0));
        Centrala centrala = Centrala.getInstancja();

        Kolektura kolektura = new Kolektura();
        ArrayList<Kolektura> kolektury = new ArrayList<>(1);
        kolektury.add(kolektura);
        Gracz gracz = new Losowy("Jan", "Kowalski", 123456789, kolektury);

        Blankiet blankiet = new Blankiet(List.of(
                new Zakład(new int[]{1, 2, 3, 4, 5, 6})
        ), 1);
        kolektura.sprzedajKuponZBlankietu(blankiet, gracz);

        int nrPrzedLosowaniem = centrala.getNrNastępnegoLosowania();
        centrala.przeprowadźLosowanie();
        int nrPoLosowaniu = centrala.getNrNastępnegoLosowania();

        assertEquals(nrPrzedLosowaniem + 1, nrPoLosowaniu,
                "Numer następnego losowania powinien wzrosnąć o 1");
    }

    @Test
    void testGetWynikLosowania() throws NieprawidłoweDane, BrakŚrodków {
        Centrala.inicjalizujCentralę(new Kwota(10_000_000, 0));
        Centrala centrala = Centrala.getInstancja();

        Kolektura kolektura = new Kolektura();
        Gracz gracz = new Minimalista("Jan", "Kowalski", 123456789,
                new Kwota(100, 0), kolektura);

        Blankiet blankiet = new Blankiet(List.of(
                new Zakład(new int[]{1, 2, 3, 4, 5, 6})
        ), 1);
        kolektura.sprzedajKuponZBlankietu(blankiet, gracz);

        centrala.przeprowadźLosowanie();

        Wynik wynik = centrala.getWynikLosowania(1);
        assertNotNull(wynik, "Wynik losowania powinien być dostępny");
        assertEquals(1, wynik.getNrLosowania(), "Numer losowania powinien być 1");
    }

    @Test
    void testGetWynikNieistniejącegoLosowania() throws NieprawidłoweDane {
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
        Centrala centrala = Centrala.getInstancja();

        assertThrows(NieprawidłoweDane.class, () -> {
            centrala.getWynikLosowania(1);
        }, "Powinien rzucić wyjątek dla nieistniejącego losowania");

        assertThrows(NieprawidłoweDane.class, () -> {
            centrala.getWynikLosowania(0);
        }, "Powinien rzucić wyjątek dla nieprawidłowego numeru losowania");

        assertThrows(NieprawidłoweDane.class, () -> {
            centrala.getWynikLosowania(-1);
        }, "Powinien rzucić wyjątek dla ujemnego numeru losowania");
    }

    @Test
    void testSprawozdzanieFinansowe() throws NieprawidłoweDane {
        Centrala.inicjalizujCentralę(new Kwota(1_234_567, 89));
        Centrala centrala = Centrala.getInstancja();

        String sprawozdanie = centrala.sprawozdanieFinansowe();

        assertTrue(sprawozdanie.startsWith("Centrala posiada obecnie: "),
                "Sprawozdanie powinno zaczynać się od odpowiedniego tekstu");
        assertTrue(sprawozdanie.contains("1234567 zł 89 gr"),
                "Sprawozdanie powinno zawierać prawidłową kwotę");
    }

    @Test
    void testPodwójnaInicjalizacja() throws NieprawidłoweDane {
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
        Centrala centrala1 = Centrala.getInstancja();

        // druga inicjalizacja nie powinna zmienić instancji
        Centrala.inicjalizujCentralę(new Kwota(2_000_000, 0));
        Centrala centrala2 = Centrala.getInstancja();

        assertSame(centrala1, centrala2,
                "Druga inicjalizacja nie powinna utworzyć nowej instancji");

        String sprawozdanie = centrala2.sprawozdanieFinansowe();
        assertTrue(sprawozdanie.contains("1000000 zł 00 gr"),
                "Środki powinny pozostać z pierwszej inicjalizacji");
    }
}
