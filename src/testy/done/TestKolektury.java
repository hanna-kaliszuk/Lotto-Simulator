package testy.done;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import totolotek.centrala.Centrala;
import totolotek.finanse.Kwota;
import totolotek.gracze.Gracz;
import totolotek.gracze.Minimalista;
import totolotek.kolektura.Kolektura;
import totolotek.kupony.Blankiet;
import totolotek.kupony.Kupon;
import totolotek.kupony.Zakład;
import wyjątki.BrakŚrodków;
import wyjątki.MożliwaPróbaOszustwa;
import wyjątki.NieprawidłoweDane;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestKolektury {
    @BeforeEach
    void setUp() {
        Centrala.resetInstancji();
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
        Kolektura.resetujNumerację();
    }

    @Test
    void testKonstruktoraOrazNumeracji() {
        Kolektura k1 = new Kolektura();
        Kolektura k2 = new Kolektura();
        Kolektura k3 = new Kolektura();
        Kolektura k4 = new Kolektura();

        assertEquals(1, k1.getNrKolektury(), "Pierwsza kolektura powinna mieć numer 1");
        assertEquals(2, k2.getNrKolektury(), "Druga kolektura powinna mieć numer 2");
        assertEquals(3, k3.getNrKolektury(), "Trzecia kolektura powinna mieć numer 3");
        assertEquals(4, k4.getNrKolektury(), "Czwarta kolektura powinna mieć numer 4");
    }

    @Test
    void testPoczątkowegoStanu() {
        Kolektura kolektura = new Kolektura();
        assertNotNull(kolektura.getSprzedaneKupony(), "Lista sprzedanych kuponów powinna być zainicjalizowana");
        assertTrue(kolektura.getSprzedaneKupony().isEmpty(), "Lista sprzedanych kuponów powinna być początkowo pusta");
        assertTrue(kolektura.getNrKolektury() > 0, "Numer kolektury powinien być większy niż 0");
    }

    @Test
    void testResetNumeracji() {
        Kolektura k1 = new Kolektura();
        assertEquals(1, k1.getNrKolektury(), "Pierwsza kolektura powinna mieć numer 1");

        Kolektura.resetujNumerację();

        Kolektura k2 = new Kolektura();
        assertEquals(1, k2.getNrKolektury(), "Po resecie numeracji, nowa kolektura powinna mieć numer 1");
    }

    @Test
    void testSprzedajKuponZBlankietu() throws NieprawidłoweDane, BrakŚrodków {
        Kolektura kolektura = new Kolektura();
        Gracz gracz = generujGraczaZPieniędzmi(new Kwota(100, 0), kolektura);

        List<Zakład> zakłady = Arrays.asList(
                new Zakład(new int[]{1, 2, 3, 4, 5, 6}),
                new Zakład(new int[]{7, 8, 9, 10, 11, 12})
        );
        Blankiet blankiet = new Blankiet(zakłady, 2);

        kolektura.sprzedajKuponZBlankietu(blankiet, gracz);

        assertEquals(1, kolektura.getSprzedaneKupony().size(), "Kolektura powinna mieć sprzedany jeden kupon");
        assertEquals(1, gracz.ileKuponów(), "Gracz powinien mieć jeden kupon");
    }

    @Test
    void testSprzedajKuponZBlankietuBrakŚrodków() throws NieprawidłoweDane {
        Kolektura kolektura = new Kolektura();
        Gracz gracz = generujGraczaZPieniędzmi(new Kwota(0, 0), kolektura);

        List<Zakład> zakłady = Arrays.asList(
                new Zakład(new int[]{1, 2, 3, 4, 5, 6}),
                new Zakład(new int[]{7, 8, 9, 10, 11, 12})
        );
        Blankiet blankiet = new Blankiet(zakłady, 2);

        assertThrows(BrakŚrodków.class, () -> {
            kolektura.sprzedajKuponZBlankietu(blankiet, gracz);
        }, "Powinno rzucić wyjątek BrakŚrodków, gdy gracz nie ma wystarczających środków");

        assertEquals(0, kolektura.getSprzedaneKupony().size(), "Kolektura nie powinna mieć sprzedanych kuponów");
    }

    @Test
    void testSprzedajKuponChybiłTrafił() throws NieprawidłoweDane, BrakŚrodków {
        Kolektura kolektura = new Kolektura();
        Gracz gracz = generujGraczaZPieniędzmi(new Kwota(100, 0), kolektura);

        kolektura.sprzedajKuponChybiłTrafił(2, 3, gracz);

        assertEquals(1, kolektura.getSprzedaneKupony().size(), "Kolektura powinna mieć sprzedany jeden kupon");
        assertEquals(1, gracz.ileKuponów(), "Gracz powinien mieć jeden kupon");
    }

    @Test
    void testSprzedajKuponChybiłTrafiłBrakŚrodków() throws NieprawidłoweDane, BrakŚrodków {
        Kolektura kolektura = new Kolektura();
        Gracz gracz = generujGraczaZPieniędzmi(new Kwota(0, 0), kolektura);

        assertThrows(BrakŚrodków.class, () -> {
            kolektura.sprzedajKuponChybiłTrafił(2, 3, gracz);
        }, "Powinno rzucić wyjątek BrakŚrodków, gdy gracz nie ma wystarczających środków");

        assertEquals(0, kolektura.getSprzedaneKupony().size(), "Kolektura nie powinna mieć sprzedanych kuponów");
    }

    @Test
    void testWydajNagrodęZłyKupon() throws NieprawidłoweDane, BrakŚrodków {
        Kolektura k1 = new Kolektura();
        Kolektura k2 = new Kolektura();

        Gracz gracz = generujGraczaZPieniędzmi(new Kwota(100, 0), k2);
        k2.sprzedajKuponChybiłTrafił(2, 3, gracz);
        Kupon kupon = gracz.getKupony(0);

        assertThrows(MożliwaPróbaOszustwa.class, () -> {
            k1.wydajNagrodę(gracz, kupon);
        }, "Powinno rzucić wyjątek MożliwaPróbaOszustwa, gdy kupon nie należy do kolektury");
    }

    @Test
    void testWydajNagrodęPonownie() throws NieprawidłoweDane, BrakŚrodków {
        Kolektura kolektura = new Kolektura();
        Gracz gracz = generujGraczaZPieniędzmi(new Kwota(100, 0), kolektura);

        kolektura.sprzedajKuponChybiłTrafił(2, 3, gracz);
        Kupon kupon = gracz.getKupony(0);
        kupon.zrealizuj();

        assertThrows(MożliwaPróbaOszustwa.class, () -> {
            kolektura.wydajNagrodę(gracz, kupon);
        }, "Powinno rzucić wyjątek MożliwaPróbaOszustwa, gdy kupon jest już zrealizowany");
    }

    private Gracz generujGraczaZPieniędzmi(Kwota środki, Kolektura ulubiona) throws NieprawidłoweDane {
        return new Minimalista("Test", "Gracz", 123456, środki, ulubiona);
    }
}
