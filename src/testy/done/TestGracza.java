package testy.done;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import totolotek.centrala.Centrala;
import totolotek.finanse.Kwota;
import totolotek.gracze.*;
import totolotek.kolektura.Kolektura;
import totolotek.kupony.Blankiet;
import totolotek.kupony.Kupon;
import totolotek.kupony.Zakład;
import wyjątki.BrakŚrodków;
import wyjątki.NieprawidłoweDane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestGracza {
    @BeforeAll
    static void setUp() throws NieprawidłoweDane {
        Centrala.resetInstancji();
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
        Kolektura.resetujNumerację();
    }

    @Test
    void testKonstruktoraMinimalista() throws NieprawidłoweDane {
        Kwota środki = new Kwota(500, 50);
        Kolektura kolektura = new Kolektura();
        Minimalista gracz = new Minimalista("Jan", "Kowalski", 1234567890, środki, kolektura);

        assertEquals("Jan", gracz.getImię(), "Imię powinno być poprawnie ustawione");
        assertEquals("Kowalski", gracz.getNazwisko(), "Nazwisko powinno być poprawnie ustawione");
        assertEquals(1234567890, gracz.getPesel(), "PESEL powinien być poprawnie ustawiony");
        assertEquals(środki, gracz.getŚrodkiFinansowe(), "Środki finansowe powinny być poprawnie ustawione");
    }

    @Test
    void testKonstruktoraStałoliczbowy() throws NieprawidłoweDane {
        Kwota środki = new Kwota(1000, 0);
        int[] ulubioneLiczby = {7, 14, 21, 28, 35, 42};
        ArrayList<Kolektura> kolektury = new ArrayList<>(Arrays.asList(new Kolektura(), new Kolektura()));

        Stałoliczbowy gracz = new Stałoliczbowy("Anna", "Nowak", 98765432, środki, ulubioneLiczby, kolektury);

        assertEquals("Anna", gracz.getImię(), "Imię powinno być poprawnie ustawione");
        assertEquals(2, gracz.ileUlubionych(), "Powinien mieć 2 ulubione kolektury");
        assertArrayEquals(ulubioneLiczby, gracz.getUlubioneLiczby(), "Ulubione liczby powinny być poprawnie ustawione");
    }

    @Test
    void testKonstruktoraLosowy() throws NieprawidłoweDane {
        ArrayList<Kolektura> kolektury = new ArrayList<>(Arrays.asList(new Kolektura(), new Kolektura(), new Kolektura()));

        Losowy gracz = new Losowy("Piotr", "Wiśniewski", 11223344, kolektury);

        assertEquals("Piotr", gracz.getImię(), "Imię powinno być poprawnie ustawione");
        assertEquals(3, gracz.ileUlubionych(), "Powinien mieć 3 kolektury do wyboru");
        assertTrue(gracz.getŚrodkiFinansowe().porównaj(new Kwota(1000000, 0)) < 0,
                "Środki początkowe powinny być mniejsze niż 1 000 000 zł");
    }

    @Test
    void testKonstruktoraStałoblankietowy() throws NieprawidłoweDane {
        Kwota środki = new Kwota(2000, 0);
        List<Zakład> zakłady = Arrays.asList(
                new Zakład(new int[]{1, 5, 10, 15, 20, 25}),
                new Zakład(new int[]{2, 7, 12, 17, 22, 27})
        );
        Blankiet blankiet = new Blankiet(zakłady, 3);
        ArrayList<Kolektura> kolektury = new ArrayList<>(List.of(new Kolektura()));

        Stałoblankietowy gracz = new Stałoblankietowy("Maria", "Kowalczyk", 556677889,
                środki, blankiet, 5, kolektury);

        assertEquals("Maria", gracz.getImię(), "Imię powinno być poprawnie ustawione");
        assertEquals(5, gracz.getCoIleLosowań(), "Częstotliwość losowań powinna być ustawiona na 5");
    }

    @Test
    void testWypłacalności() throws NieprawidłoweDane {
        Kolektura kolektura = new Kolektura();

        // Minimalista z małymi środkami
        Minimalista minimalista = new Minimalista("Adam", "Małysz", 1234567890, new Kwota(50, 0), kolektura);
        assertTrue(minimalista.jestWypłacalny(new Kwota(30, 0)), "Minimalista powinien być wypłacalny");
        assertFalse(minimalista.jestWypłacalny(new Kwota(100, 0)), "Minimalista nie powinien być wypłacalny");

        // Stałoliczbowy z większymi środkami
        int[] liczby = {1, 2, 3, 4, 5, 6};
        ArrayList<Kolektura> kolektury = new ArrayList<>(List.of(kolektura));
        Stałoliczbowy stałoliczbowy = new Stałoliczbowy("Ewa", "Bogdańska", 987654321,
                new Kwota(500, 0), liczby, kolektury);

        assertTrue(stałoliczbowy.jestWypłacalny(new Kwota(300, 0)), "Stałoliczbowy powinien być wypłacalny");
        assertTrue(stałoliczbowy.jestWypłacalny(new Kwota(500, 0)), "Stałoliczbowy powinien być wypłacalny dla równej kwoty");
    }

    @Test
    void testZapłać() throws NieprawidłoweDane {
        Kolektura kolektura = new Kolektura();

        // Test dla Minimalisty
        Minimalista minimalista = new Minimalista("Tomasz", "Lewandowski", 9988776, new Kwota(200, 0), kolektura);
        Kwota płatnośćMinimalista = new Kwota(75, 0);
        minimalista.zapłać(płatnośćMinimalista);
        assertEquals(new Kwota(125, 0), minimalista.getŚrodkiFinansowe(), "Środki Minimalisty powinny być pomniejszone");
    }

    @Test
    void testOdbierzŚrodkiNagrody() throws NieprawidłoweDane {
        Kolektura kolektura = new Kolektura();
        int[] liczby = {7, 14, 21, 28, 35, 42};
        ArrayList<Kolektura> kolektury = new ArrayList<>(List.of(kolektura));

        Stałoliczbowy gracz = new Stałoliczbowy("Paweł", "Szymański", 3344556,
                new Kwota(100, 0), liczby, kolektury);

        Kwota nagroda = new Kwota(5000, 0);
        gracz.odbierzŚrodki(nagroda);

        assertEquals(new Kwota(5100, 0), gracz.getŚrodkiFinansowe(), "Środki powinny zostać zwiększone o nagrodę");
    }

    @Test
    void testKupKuponMinimalista() throws NieprawidłoweDane, BrakŚrodków {
        Kolektura kolektura = new Kolektura();
        Minimalista minimalista = new Minimalista("Adam", "Mazur", 121212121, new Kwota(100, 0), kolektura);

        int początkowaLiczbaKuponów = minimalista.ileKuponów();
        minimalista.kupKupon();

        assertEquals(początkowaLiczbaKuponów + 1, minimalista.ileKuponów(), "Minimalista powinien kupić jeden kupon");

        Kupon kupon = minimalista.getKupony(0);
        assertEquals(1, kupon.getZakłady().size(), "Kupon powinien mieć jeden zakład");
        assertEquals(1, kupon.getNaIleLosowań(), "Kupon powinien być na jedno losowanie");
    }

    @Test
    void testKupKuponStałoliczbowy() throws NieprawidłoweDane, BrakŚrodków {
        Kolektura kolektura = new Kolektura();
        int[] ulubioneLiczby = {7, 14, 21, 28, 35, 42};
        ArrayList<Kolektura> kolektury = new ArrayList<>(Arrays.asList(kolektura, new Kolektura()));

        Stałoliczbowy stałoliczbowy = new Stałoliczbowy("Barbara", "Krawczyk", 345678901,
                new Kwota(1000, 0), ulubioneLiczby, kolektury);

        stałoliczbowy.kupKupon();

        assertEquals(1, stałoliczbowy.ileKuponów(), "Stałoliczbowy powinien kupić jeden kupon");

        Kupon kupon = stałoliczbowy.getKupony(0);
        assertEquals(10, kupon.getNaIleLosowań(), "Kupon powinien być na 10 losowań");
        assertEquals(1, kupon.getZakłady().size(), "Kupon powinien mieć jeden zakład z ulubionymi liczbami");
        assertArrayEquals(ulubioneLiczby, kupon.getZakłady().getFirst().getLiczby(),
                "Kupon powinien zawierać ulubione liczby");
    }

    @Test
    void testKupKuponLosowy() throws NieprawidłoweDane {
        ArrayList<Kolektura> kolektury = new ArrayList<>(Arrays.asList(new Kolektura(), new Kolektura(), new Kolektura()));
        Losowy losowy = new Losowy("Michał", "Wójcik", 66778899, kolektury);

        int początkowaLiczbaKuponów = losowy.ileKuponów();
        losowy.kupKupon();

        assertTrue(losowy.ileKuponów() > początkowaLiczbaKuponów, "Losowy powinien kupić przynajmniej jeden kupon");
        assertTrue(losowy.ileKuponów() <= początkowaLiczbaKuponów + 100, "Losowy nie powinien kupić więcej niż 100 kuponów");

        // Sprawdź, czy kupiony kupon ma losowe parametry w dozwolonych zakresach
        if (losowy.ileKuponów() > 0) {
            Kupon kupon = losowy.getKupony(0);
            assertFalse(kupon.getZakłady().isEmpty(), "Kupon powinien mieć przynajmniej jeden zakład");
            assertTrue(kupon.getNaIleLosowań() >= 1 && kupon.getNaIleLosowań() <= 10,
                    "Kupon powinien być na 1-10 losowań");
        }
    }

    @Test
    void testOddajKupon() throws NieprawidłoweDane, BrakŚrodków {
        Kolektura kolektura = new Kolektura();
        Minimalista gracz = new Minimalista("Agnieszka", "Dąbrowska", 77889900, new Kwota(300, 0), kolektura);

        gracz.kupKupon();
        assertEquals(1, gracz.ileKuponów(), "Gracz powinien mieć jeden kupon przed oddaniem");

        Kupon kupon = gracz.getKupony(0);
        gracz.oddajKupon(kupon);
        assertEquals(0, gracz.ileKuponów(), "Gracz nie powinien mieć kuponów po oddaniu");
    }

    @Test
    void testKupKuponStałoblankietowy() throws NieprawidłoweDane, BrakŚrodków {
        List<Zakład> zakłady = Arrays.asList(
                new Zakład(new int[]{1, 5, 10, 15, 20, 25}),
                new Zakład(new int[]{3, 8, 13, 18, 23, 28})
        );
        Blankiet blankiet = new Blankiet(zakłady, 4);
        ArrayList<Kolektura> kolektury = new ArrayList<>(List.of(new Kolektura()));

        Stałoblankietowy stałoblankietowy = new Stałoblankietowy("Ewa", "Kamińska", 445566778,
                new Kwota(1000, 0), blankiet, 3, kolektury);

        stałoblankietowy.kupKupon();

        assertEquals(1, stałoblankietowy.ileKuponów(), "Stałoblankietowy powinien kupić jeden kupon");

        Kupon kupon = stałoblankietowy.getKupony(0);
        assertEquals(4, kupon.getNaIleLosowań(), "Kupon powinien być na tyle losowań ile w blankiecie");
        assertEquals(2, kupon.getZakłady().size(), "Kupon powinien mieć taką samą liczbę zakładów jak blankiet");
    }


    @Test
    void testOgraniczeniaStałoliczbowy() throws NieprawidłoweDane, BrakŚrodków {
        Kolektura kolektura = new Kolektura();
        int[] liczby = {1, 2, 3, 4, 5, 6};
        ArrayList<Kolektura> kolektury = new ArrayList<>(List.of(kolektura));

        Stałoliczbowy gracz = new Stałoliczbowy("Andrzej", "Duda", 778899001,
                new Kwota(50, 0), liczby, kolektury); // Mało pieniędzy

        gracz.kupKupon(); // Pierwszy kupon - powinien się udać
        assertEquals(1, gracz.ileKuponów(), "Pierwszy kupon powinien być kupiony");

        // Próba kupna drugiego kuponu przed zakończeniem losowań pierwszego
        assertDoesNotThrow(gracz::kupKupon, "Nie powinien kupować drugiego kuponu przed zakończeniem pierwszego");

        assertEquals(1, gracz.ileKuponów(), "Nadal powinien mieć tylko jeden kupon");
    }



}
