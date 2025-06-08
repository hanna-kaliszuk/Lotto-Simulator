package testy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import totolotek.finanse.BudżetPaństwa;
import totolotek.finanse.Kwota;
import wyjątki.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestBudżetPaństwa {
    @BeforeEach
    void setUp() {
        BudżetPaństwa.resetInstancji();
    }

    @Test
    void testSingleton() throws NieprawidłoweDane {
        BudżetPaństwa budżet1 = BudżetPaństwa.getInstancja();
        BudżetPaństwa budżet2 = BudżetPaństwa.getInstancja();

        assertSame(budżet1, budżet2, "Powinien zwracać tę samą instancję (singleton)");
    }

    @Test
    void testStanPoczątkowy() throws NieprawidłoweDane {
        BudżetPaństwa budżet = BudżetPaństwa.getInstancja();
        String raport = budżet.raport();

        assertTrue(raport.contains("Pobrane podatki: 0 zł 00 gr"),
                "Pobrane podatki powinny być początkowo zero");
        assertTrue(raport.contains("Wydane subwencje: 0 zł 00 gr"),
                "Wydane subwencje powinny być początkowo zero");
    }

    @Test
    void testDodajPodatek() throws NieprawidłoweDane {
        BudżetPaństwa budżet = BudżetPaństwa.getInstancja();
        Kwota podatek = new Kwota(1000, 50);

        budżet.dodajPodatek(podatek);
        String raport = budżet.raport();

        assertTrue(raport.contains("Pobrane podatki: 1000 zł 50 gr"),
                "Raport powinien pokazać dodany podatek");
    }

    @Test
    void testDodajWielePodatków() throws NieprawidłoweDane {
        BudżetPaństwa budżet = BudżetPaństwa.getInstancja();

        budżet.dodajPodatek(new Kwota(500, 25));
        budżet.dodajPodatek(new Kwota(750, 75));
        budżet.dodajPodatek(new Kwota(250, 0));

        String raport = budżet.raport();

        // 500.25 + 750.75 + 250.00 = 1501.00
        assertTrue(raport.contains("Pobrane podatki: 1501 zł 00 gr"),
                "Raport powinien pokazać sumę wszystkich podatków");
    }

    @Test
    void testWydajSubwencję() throws NieprawidłoweDane {
        BudżetPaństwa budżet = BudżetPaństwa.getInstancja();
        Kwota subwencja = new Kwota(2000, 0);

        budżet.wydajSubwencję(subwencja);
        String raport = budżet.raport();

        assertTrue(raport.contains("Wydane subwencje: 2000 zł 00 gr"),
                "Raport powinien pokazać wydaną subwencję");
    }

    @Test
    void testWydajWieleSubwencji() throws NieprawidłoweDane {
        BudżetPaństwa budżet = BudżetPaństwa.getInstancja();

        budżet.wydajSubwencję(new Kwota(1000, 0));
        budżet.wydajSubwencję(new Kwota(500, 50));
        budżet.wydajSubwencję(new Kwota(300, 25));

        String raport = budżet.raport();

        // 1000.00 + 500.50 + 300.25 = 1800.75
        assertTrue(raport.contains("Wydane subwencje: 1800 zł 75 gr"),
                "Raport powinien pokazać sumę wszystkich subwencji");
    }

    @Test
    void testMieszanePodatkiISubwencje() throws NieprawidłoweDane {
        BudżetPaństwa budżet = BudżetPaństwa.getInstancja();

        budżet.dodajPodatek(new Kwota(1000, 0));
        budżet.wydajSubwencję(new Kwota(300, 50));
        budżet.dodajPodatek(new Kwota(500, 25));
        budżet.wydajSubwencję(new Kwota(200, 0));

        String raport = budżet.raport();

        assertTrue(raport.contains("Pobrane podatki: 1500 zł 25 gr"),
                "Raport powinien pokazać sumę podatków");
        assertTrue(raport.contains("Wydane subwencje: 500 zł 50 gr"),
                "Raport powinien pokazać sumę subwencji");
    }

    @Test
    void testResetujBudżet() throws NieprawidłoweDane {
        BudżetPaństwa budżet = BudżetPaństwa.getInstancja();

        // Dodaj niektóre wartości
        budżet.dodajPodatek(new Kwota(1000, 0));
        budżet.wydajSubwencję(new Kwota(500, 0));

        // Sprawdź, że wartości zostały dodane
        String raportPrzed = budżet.raport();
        assertTrue(raportPrzed.contains("Pobrane podatki: 1000 zł 00 gr"));
        assertTrue(raportPrzed.contains("Wydane subwencje: 500 zł 00 gr"));

        // Reset budżetu
        budżet.resetujBudżet();

        // Sprawdź, że wartości zostały zresetowane
        String raportPo = budżet.raport();
        assertTrue(raportPo.contains("Pobrane podatki: 0 zł 00 gr"),
                "Po resecie podatki powinny być zero");
        assertTrue(raportPo.contains("Wydane subwencje: 0 zł 00 gr"),
                "Po resecie subwencje powinny być zero");
    }

    @Test
    void testDodajPodatekZero() throws NieprawidłoweDane {
        BudżetPaństwa budżet = BudżetPaństwa.getInstancja();
        Kwota zero = new Kwota(0, 0);

        budżet.dodajPodatek(zero);
        String raport = budżet.raport();

        assertTrue(raport.contains("Pobrane podatki: 0 zł 00 gr"),
                "Dodanie zera nie powinno zmienić stanu");
    }

    @Test
    void testWydajSubwencjęZero() throws NieprawidłoweDane {
        BudżetPaństwa budżet = BudżetPaństwa.getInstancja();
        Kwota zero = new Kwota(0, 0);

        budżet.wydajSubwencję(zero);
        String raport = budżet.raport();

        assertTrue(raport.contains("Wydane subwencje: 0 zł 00 gr"),
                "Wydanie zera nie powinno zmienić stanu");
    }

    @Test
    void testWielokrotnościSingletona() throws NieprawidłoweDane {
        BudżetPaństwa budżet1 = BudżetPaństwa.getInstancja();
        budżet1.dodajPodatek(new Kwota(100, 0));

        BudżetPaństwa budżet2 = BudżetPaństwa.getInstancja();
        budżet2.dodajPodatek(new Kwota(200, 0));

        // Oba powinny mieć tę samą sumę podatków
        String raport1 = budżet1.raport();
        String raport2 = budżet2.raport();

        assertEquals(raport1, raport2, "Wszystkie instancje powinny mieć te same dane");
        assertTrue(raport1.contains("Pobrane podatki: 300 zł 00 gr"),
                "Suma podatków powinna być akumulowana");
    }

    @Test
    void testDużeKwoty() throws NieprawidłoweDane {
        BudżetPaństwa budżet = BudżetPaństwa.getInstancja();

        Kwota dużyPodatek = new Kwota(1_000_000, 99);
        Kwota dużaSubwencja = new Kwota(2_000_000, 1);

        budżet.dodajPodatek(dużyPodatek);
        budżet.wydajSubwencję(dużaSubwencja);

        String raport = budżet.raport();

        assertTrue(raport.contains("Pobrane podatki: 1000000 zł 99 gr"),
                "Powinien obsługiwać duże kwoty podatków");
        assertTrue(raport.contains("Wydane subwencje: 2000000 zł 01 gr"),
                "Powinien obsługiwać duże kwoty subwencji");
    }

}
