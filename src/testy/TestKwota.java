package testy;

import org.junit.jupiter.api.Test;
import totolotek.finanse.Kwota;
import wyjątki.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestKwota {
    @Test
    void testKonstruktorPodstawowy() throws NieprawidłoweDane {
        Kwota kwota = new Kwota(100, 50);
        assertEquals(100, kwota.getZłote(), "Złote powinny być ustawione na 100");
        assertEquals(50, kwota.getGrosze(), "Grosze powinny być ustawione na 50");
    }

    @Test
    void testKonstruktorKopiujący() throws NieprawidłoweDane {
        Kwota oryginalna = new Kwota(200, 75);
        Kwota kopia = new Kwota(oryginalna);

        assertEquals(oryginalna.getZłote(), kopia.getZłote(), "Złote powinny być skopiowane");
        assertEquals(oryginalna.getGrosze(), kopia.getGrosze(), "Grosze powinny być skopiowane");
        assertNotSame(oryginalna, kopia, "Powinny być różnymi obiektami");
    }

    @Test
    void testKonstruktorNieprawidłoweGrosze() {
        assertThrows(NieprawidłoweDane.class, () -> {
            new Kwota(100, -1);
        }, "Powinien rzucić wyjątek dla ujemnych groszy");

        assertThrows(NieprawidłoweDane.class, () -> {
            new Kwota(100, 100);
        }, "Powinien rzucić wyjątek dla groszy >= 100");

        assertThrows(NieprawidłoweDane.class, () -> {
            new Kwota(100, 150);
        }, "Powinien rzucić wyjątek dla groszy > 100");
    }

    @Test
    void testDodawanie() throws NieprawidłoweDane {
        Kwota kwota1 = new Kwota(100, 50);
        Kwota kwota2 = new Kwota(50, 30);

        kwota1.dodaj(kwota2);

        assertEquals(150, kwota1.getZłote(), "Po dodaniu powinno być 150 złotych");
        assertEquals(80, kwota1.getGrosze(), "Po dodaniu powinno być 80 groszy");
    }

    @Test
    void testOdejmowanie() throws NieprawidłoweDane {
        Kwota kwota1 = new Kwota(150, 80);
        Kwota kwota2 = new Kwota(50, 30);

        kwota1.odejmij(kwota2);

        assertEquals(100, kwota1.getZłote(), "Po odjęciu powinno być 100 złotych");
        assertEquals(50, kwota1.getGrosze(), "Po odjęciu powinno być 50 groszy");

        Kwota kwota12 = new Kwota(100, 20);
        Kwota kwota22 = new Kwota(50, 50);

        kwota12.odejmij(kwota22);

        assertEquals(49, kwota12.getZłote(), "Po odjęciu z pożyczką powinno być 49 złotych");
        assertEquals(70, kwota12.getGrosze(), "Po odjęciu z pożyczką powinno być 70 groszy");
    }

    @Test
    void testMnożenieDouble() throws NieprawidłoweDane {
        Kwota kwota = new Kwota(100, 0);
        kwota.pomnóż(1.5);

        assertEquals(150, kwota.getZłote(), "Po pomnożeniu przez 1.5 powinno być 150 złotych");
        assertEquals(0, kwota.getGrosze(), "Po pomnożeniu przez 1.5 powinno być 0 groszy");
    }

    @Test
    void testMnożenieInt() throws NieprawidłoweDane {
        Kwota kwota = new Kwota(100, 50);
        kwota.pomnóż(3);

        assertEquals(301, kwota.getZłote(), "Po pomnożeniu przez 3 powinno być 301 złotych");
        assertEquals(50, kwota.getGrosze(), "Po pomnożeniu przez 3 powinno być 50 groszy");
    }

    @Test
    void testMnożenieZaokrąglanie() throws NieprawidłoweDane {
        Kwota kwota = new Kwota(10, 33);
        kwota.pomnóż(0.5);

        // 10.33 * 0.5 = 5.165, powinno zaokrąglić do 5.16
        assertEquals(5, kwota.getZłote(), "Po pomnożeniu z zaokrągleniem");
        assertEquals(17, kwota.getGrosze(), "Grosze powinny być zaokrąglone do 16");
    }

    @Test
    void testDzielenie() throws NieprawidłoweDane {
        Kwota kwota = new Kwota(300, 0);
        kwota.podziel(3);

        assertEquals(100, kwota.getZłote(), "Po podzieleniu przez 3 powinno być 100 złotych");
        assertEquals(0, kwota.getGrosze(), "Po podzieleniu przez 3 powinno być 0 groszy");
    }

    @Test
    void testDzieleniePrzezZero() throws NieprawidłoweDane {
        Kwota kwota = new Kwota(100, 0);

        assertThrows(IllegalArgumentException.class, () -> {
            kwota.podziel(0);
        }, "Powinien rzucić wyjątek przy dzieleniu przez zero (int)");
    }

    @Test
    void testPodatek() throws NieprawidłoweDane {
        Kwota kwota = new Kwota(1000, 0);
        Kwota podatek = kwota.getPodatek();

        assertEquals(200, podatek.getZłote(), "Podatek powinien wynosić 200 złotych (20%)");
        assertEquals(0, podatek.getGrosze(), "Podatek powinien mieć 0 groszy");

        // Sprawdź, że oryginalna kwota się nie zmieniła
        assertEquals(1000, kwota.getZłote(), "Oryginalna kwota nie powinna się zmienić");
    }

    @Test
    void testKwotaNieujemna() throws NieprawidłoweDane {
        Kwota dodatnia = new Kwota(100, 0);
        Kwota ujemna = new Kwota(-50, 0);
        Kwota zero = new Kwota(0, 0);

        assertTrue(dodatnia.kwotaNieujemna(), "Dodatnia kwota powinna być nieujemna");
        assertFalse(ujemna.kwotaNieujemna(), "Ujemna kwota nie powinna być nieujemna");
        assertFalse(zero.kwotaNieujemna(), "Zero nie powinno być większe od zera");
    }

    @Test
    void testPorównywanie() throws NieprawidłoweDane {
        Kwota kwota1 = new Kwota(100, 50);
        Kwota kwota2 = new Kwota(100, 50);
        Kwota kwota3 = new Kwota(200, 0);
        Kwota kwota4 = new Kwota(50, 0);

        assertEquals(0, kwota1.porównaj(kwota2), "Równe kwoty powinny zwrócić 0");
        assertTrue(kwota1.porównaj(kwota3) < 0, "Mniejsza kwota powinna zwrócić wartość ujemną");
        assertTrue(kwota1.porównaj(kwota4) > 0, "Większa kwota powinna zwrócić wartość dodatnią");
    }

    @Test
    void testEquals() throws NieprawidłoweDane {
        Kwota kwota1 = new Kwota(100, 50);
        Kwota kwota2 = new Kwota(100, 50);
        Kwota kwota3 = new Kwota(200, 0);

        assertEquals(kwota1, kwota2, "Równe kwoty powinny być równe według equals");
        assertNotEquals(kwota1, kwota3, "Różne kwoty nie powinny być równe");
        assertNotEquals(kwota1, null, "Kwota nie powinna być równa null");
        assertNotEquals(kwota1, "string", "Kwota nie powinna być równa innemu typowi");
    }

    @Test
    void testHashCode() throws NieprawidłoweDane {
        Kwota kwota1 = new Kwota(100, 50);
        Kwota kwota2 = new Kwota(100, 50);

        assertEquals(kwota1.hashCode(), kwota2.hashCode(),
                "Równe obiekty powinny mieć równe hashCode");
    }

    @Test
    void testToString() throws NieprawidłoweDane {
        Kwota kwota1 = new Kwota(123, 45);
        Kwota kwota2 = new Kwota(0, 5);
        Kwota kwota3 = new Kwota(-50, 0);

        assertEquals("123 zł 45 gr", kwota1.toString(), "Format toString powinien być poprawny");
        assertEquals("0 zł 05 gr", kwota2.toString(), "Grosze powinny być z zerem wiodącym");
        assertEquals("-50 zł 00 gr", kwota3.toString(), "Ujemne kwoty powinny być poprawnie formatowane");
    }

    @Test
    void testGetGroszeDlaUjemnychKwot() throws NieprawidłoweDane {
        Kwota kwota = new Kwota(-50, 0);
        kwota.odejmij(new Kwota(0, 25)); // -50.25

        assertEquals(25, kwota.getGrosze(), "Grosze dla ujemnych kwot powinny być dodatnie");
        assertEquals(-50, kwota.getZłote(), "Złote powinny uwzględniać grosze");
    }
}
