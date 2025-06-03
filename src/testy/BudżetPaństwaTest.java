package testy;

import finanse.BudżetPaństwa;
import finanse.Kwota;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BudżetPaństwaTest {

    private BudżetPaństwa budżet;

    @BeforeEach
    void setUp() {
        BudżetPaństwa.resetInstancji();
        budżet = BudżetPaństwa.getInstancja();
        budżet.resetujBudżet();
    }

    @Test
    void testSingletonInstance() {
        BudżetPaństwa budżet1 = BudżetPaństwa.getInstancja();
        BudżetPaństwa budżet2 = BudżetPaństwa.getInstancja();

        assertSame(budżet1, budżet2, "Should return the same singleton instance");
    }

    @Test
    void testDodajPodatek() {
        Kwota podatek = new Kwota(100, 50);
        budżet.dodajPodatek(podatek);

        String raport = budżet.raport();
        assertTrue(raport.contains("100 zł 50 gr"), "Should contain the added tax amount");
    }

    @Test
    void testWydajSubwencję() {
        Kwota subwencja = new Kwota(500, 0);
        budżet.wydajSubwencję(subwencja);

        String raport = budżet.raport();
        assertTrue(raport.contains("500 zł 00 gr"), "Should contain the subsidy amount");
    }

    @Test
    void testRaport() {
        String raport = budżet.raport();

        assertNotNull(raport, "Report should not be null");
        assertTrue(raport.contains("Pobrane podatki:"), "Report should contain tax information");
        assertTrue(raport.contains("Wydane subwencje:"), "Report should contain subsidy information");
    }

    @Test
    void testToString() {
        String toString = budżet.toString();

        assertNotNull(toString, "toString should not be null");
        assertTrue(toString.contains("BudżetPaństwa{"), "Should contain class name");
        assertTrue(toString.contains("pobranePodatki="), "Should contain tax field");
        assertTrue(toString.contains("wydaneSubwencje="), "Should contain subsidy field");
    }

    @Test
    void testMultipleTaxAdditions() {
        Kwota podatek1 = new Kwota(100, 0);
        Kwota podatek2 = new Kwota(200, 50);

        budżet.dodajPodatek(podatek1);
        budżet.dodajPodatek(podatek2);
        String raport = budżet.raport();
        assertTrue(raport.contains("300 zł 50 gr"), "Should contain the sum of all taxes");
    }

    @Test
    void testMultipleSubsidies() {
        Kwota subwencja1 = new Kwota(150, 25);
        Kwota subwencja2 = new Kwota(350, 75);

        budżet.wydajSubwencję(subwencja1);
        budżet.wydajSubwencję(subwencja2);

        String raport = budżet.raport();
        assertTrue(raport.contains("501 zł 00 gr"), "Should contain the sum of all subsidies");
    }
}