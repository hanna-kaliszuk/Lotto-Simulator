package testy.done;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import totolotek.centrala.Centrala;
import totolotek.finanse.Kwota;
import totolotek.kolektura.Kolektura;
import totolotek.kupony.Kupon;
import totolotek.kupony.Zakład;
import wyjątki.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestKuponu {
    @BeforeEach
    void setUp() throws NieprawidłoweDane {
        Centrala.resetInstancji();
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
    }

    @Test
    void testKonstruktora() throws NieprawidłoweDane {
        Kolektura kolektura = new Kolektura();
        List<Zakład> zakłady = Arrays.asList(
                new Zakład(new int[]{1, 2, 3, 4, 5, 6}),
                new Zakład(new int[]{7, 8, 9, 10, 11, 12})
        );

        Kupon kupon = new Kupon(kolektura, zakłady, 3, 1);

        assertEquals(3, kupon.getNaIleLosowań(), "Kupon powinien obejmować 3 losowania");
        assertEquals(2, kupon.getZakłady().size(), "Kupon powinien zawierać 2 zakłady");
        assertFalse(kupon.czyZrealizowany(), "Kupon powinien być początkowo niezrealizowany");
    }

    @Test
    void testIdenryfikatorów() throws NieprawidłoweDane {
        Kolektura kolektura = new Kolektura();
        List<Zakład> zakłady = List.of(new Zakład(new int[]{1, 2, 3, 4, 5, 6}));

        Kupon k1 = new Kupon(kolektura, zakłady, 1, 5);
        Kupon k2 = new Kupon(kolektura, zakłady, 1, 5);

        assertNotEquals(k1.getIdentyfikator(), k2.getIdentyfikator(),
                "Identyfikatory kuponów powinny być różne");
    }

    @Test
    void testObliczCenę() throws NieprawidłoweDane {
        Kolektura kolektura = new Kolektura();
        List<Zakład> zakłady = new LinkedList<>();
        Zakład z1 = new Zakład(new int[]{1, 2, 3, 4, 5, 6});
        Zakład z2 = new Zakład(new int[]{7, 8, 9, 10, 11, 12});
        zakłady.add(z1);
        zakłady.add(z2);

        Kupon kupon = new Kupon(kolektura, zakłady, 8, 1);

        // cena = zakłady * losowania * 3 zł
        Kwota oczekiwana = new Kwota(48, 0);
        assertEquals(oczekiwana, kupon.getCena(), "Cena kuponu powinna być poprawnie obliczona");

        Zakład nieważny = new Zakład(new int[]{1});
        zakłady.add(nieważny);
        assertEquals(oczekiwana, kupon.getCena(),
                "Cena kuponu powinna być obliczona z uwzględnieniem tylko ważnych zakładów");

        Zakład anulowany = new Zakład(new int[]{2, 3, 4, 5, 6, 7});
        anulowany.anuluj();
        zakłady.add(anulowany);

        assertEquals(oczekiwana, kupon.getCena(),
                "Cena kuponu powinna być obliczona z uwzględnieniem tylko ważnych i nieanulowanych zakładów");
    }

    @Test
    void testNumerówLosowań() throws NieprawidłoweDane {
        Kolektura kolektura = new Kolektura();
        List<Zakład> zakłady = Arrays.asList(
                new Zakład(new int[]{1, 2, 3, 4, 5, 6}),
                new Zakład(new int[]{7, 8, 9, 10, 11, 12})
        );

        Kupon kupon = new Kupon(kolektura, zakłady, 3, 1);

        assertTrue(kupon.czyNaToLosowanie(1), "Kupon powinien obejmować losowanie nr 1");
        assertTrue(kupon.czyNaToLosowanie(2), "Kupon powinien obejmować losowanie nr 2");
        assertTrue(kupon.czyNaToLosowanie(3), "Kupon powinien obejmować losowanie nr 3");
        assertFalse(kupon.czyNaToLosowanie(4), "Kupon nie powinien obejmować losowania nr 4");
    }

    @Test
    void testZrealizowanyKupon() throws NieprawidłoweDane {
        Kolektura kolektura = new Kolektura();
        List<Zakład> zakłady = Arrays.asList(
                new Zakład(new int[]{1, 2, 3, 4, 5, 6}),
                new Zakład(new int[]{7, 8, 9, 10, 11, 12})
        );

        Kupon kupon = new Kupon(kolektura, zakłady, 3, 1);

        kupon.zrealizuj();
        assertTrue(kupon.czyZrealizowany(), "Kupon powinien być zrealizowany po wywołaniu metody zrealizuj");
    }
}
