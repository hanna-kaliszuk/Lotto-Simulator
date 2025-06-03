package testy;

import centrala.Centrala;
import finanse.Kwota;
import kolektura.Kolektura;
import kupony.Kupon;
import kupony.Zakład;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class KuponTest {

    @BeforeEach
    void setUp() {
        Centrala.resetInstancji();
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
    }

    @Test
    void testKonstruktor() {
        Kolektura kolektura = new Kolektura();
        List<Zakład> zakłady = Arrays.asList(
                new Zakład(new int[]{1, 2, 3, 4, 5, 6}),
                new Zakład(new int[]{7, 8, 9, 10, 11, 12})
        );

        Kupon kupon = new Kupon(kolektura, zakłady, 3, 1);

        assertNotNull(kupon.getIdentyfikator(), "Should have an identifier");
        assertEquals(2, kupon.getZakłady().size(), "Should have 2 bets");
        assertEquals(3, kupon.getNaIleLosowań(), "Should be for 3 drawings");
    }

    @Test
    void testUniqueNumbers() {
        Kolektura kolektura = new Kolektura();
        List<Zakład> zakłady = List.of(new Zakład(new int[]{1, 2, 3, 4, 5, 6}));

        Kupon kupon1 = new Kupon(kolektura, zakłady, 1, 1);
        Kupon kupon2 = new Kupon(kolektura, zakłady, 1, 1);
        Kupon kupon3 = new Kupon(kolektura, zakłady, 1, 1);

        assertNotEquals(kupon1.getNumerKuponu(), kupon2.getNumerKuponu(),
                "Each coupon should have a unique number");
        assertNotEquals(kupon1.getNumerKuponu(), kupon3.getNumerKuponu(),
                "Each coupon should have a unique number");
    }

    @Test
    void testGetZakładyDeepCopy() {
        Kolektura kolektura = new Kolektura();
        List<Zakład> originalZakłady = List.of(
                new Zakład(new int[]{1, 2, 3, 4, 5, 6})
        );

        Kupon kupon = new Kupon(kolektura, originalZakłady, 1, 1);
        List<Zakład> returnedZakłady = kupon.getZakłady();

        assertNotSame(originalZakłady, returnedZakłady,
                "Should return a different list instance");
        assertEquals(originalZakłady.size(), returnedZakłady.size(),
                "Should have the same number of bets");

        // Modify returned list - should not affect original
        returnedZakłady.clear();
        assertEquals(1, kupon.getZakłady().size(),
                "Original should not be affected by modifications to returned copy");
    }

    @Test
    void testIdentyfikatorFormat() {
        Kolektura kolektura = new Kolektura();
        List<Zakład> zakłady = List.of(new Zakład(new int[]{1, 2, 3, 4, 5, 6}));

        Kupon kupon = new Kupon(kolektura, zakłady, 1, 1);
        String identyfikator = kupon.getIdentyfikator();

        assertNotNull(identyfikator, "Identifier should not be null");
        assertTrue(identyfikator.contains("-"), "Identifier should contain hyphens");

        String[] parts = identyfikator.split("-");
        assertEquals(4, parts.length, "Identifier should have 4 parts separated by hyphens");
    }

    @Test
    void testToString() {
        Kolektura kolektura = new Kolektura();
        List<Zakład> zakłady = List.of(new Zakład(new int[]{1, 2, 3, 4, 5, 6}));

        Kupon kupon = new Kupon(kolektura, zakłady, 2, 1);
        String kuponString = kupon.toString();

        assertTrue(kuponString.contains("KUPON NR"), "Should contain coupon header");
        assertTrue(kuponString.contains("LICZBA LOSOWAŃ: 2"), "Should show number of drawings");
        assertTrue(kuponString.contains("NUMERY LOSOWAŃ:"), "Should show drawing numbers");
        assertTrue(kuponString.contains("CENA:"), "Should show price");
    }

    @Test
    void testGetSprawozdzanieKuponu() {
        Kolektura kolektura = new Kolektura();
        List<Zakład> zakłady = List.of(new Zakład(new int[]{1, 2, 3, 4, 5, 6}));

        Kupon kupon = new Kupon(kolektura, zakłady, 1, 1);
        String sprawozdanie = kupon.getSprawozdzanieKuponu();

        assertTrue(sprawozdanie.contains("Identyfikator kuponu:"), "Should contain identifier");
        assertTrue(sprawozdanie.contains("Cena kuponu:"), "Should contain price");
    }
}