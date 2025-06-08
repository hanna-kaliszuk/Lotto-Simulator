package testy;

import org.junit.jupiter.api.Test;
import totolotek.finanse.Kwota;

import static org.junit.jupiter.api.Assertions.*;

public class TestKwota {
    @Test
    void testKonstruktorPodstawowy() {
        Kwota kwota = new Kwota(100, 50);
        assertEquals(100, kwota.getZłote(), "Złote powinny być ustawione na 100");
        assertEquals(50, kwota.getGrosze(), "Grosze powinny być ustawione na 50");
    }

    @Test
    void testKonstruktorKopiujący() {
        Kwota oryginalna = new Kwota(200, 75);
        Kwota kopia = new Kwota(oryginalna);

        assertEquals(oryginalna.getZłote(), kopia.getZłote(), "Złote powinny być skopiowane");
        assertEquals(oryginalna.getGrosze(), kopia.getGrosze(), "Grosze powinny być skopiowane");
        assertNotSame(oryginalna, kopia, "Powinny być różnymi obiektami");
    }

    @Test
    void testKonstruktorNieprawidłoweGrosze() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Kwota(100, -1);
        }, "Powinien rzucić wyjątek dla ujemnych groszy");

        assertThrows(IllegalArgumentException.class, () -> {
            new Kwota(100, 100);
        }, "Powinien rzucić wyjątek dla groszy >= 100");

        assertThrows(IllegalArgumentException.class, () -> {
            new Kwota(100, 150);
        }, "Powinien rzucić wyjątek dla groszy > 100");
    }

    @Test
    void testDodawanie() {
        Kwota kwota1 = new Kwota(100, 50);
        Kwota kwota2 = new Kwota(50, 30);

        kwota1.dodaj(kwota2);

        assertEquals(150, kwota1.getZłote(), "Po dodaniu powinno być 150 złotych");
        assertEquals(80, kwota1.getGrosze(), "Po dodaniu powinno być 80 groszy");
    }

    @Test
    void testOdejmowanie() {
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
}
