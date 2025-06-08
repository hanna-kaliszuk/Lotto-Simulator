package testy.dodatkowe;

import totolotek.finanse.Kwota;
import totolotek.gracze.Gracz;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Test implementation of abstract Gracz class
class TestGracz extends Gracz {
    public TestGracz(String imię, String nazwisko, int pesel, Kwota środki) {
        super(imię, nazwisko, pesel, środki);
    }

    @Override
    public void kupKupon() {

    }
}

public class GraczTest {

    @Test
    void testKonstruktor() {
        Kwota środki = new Kwota(1000, 0);
        TestGracz gracz = new TestGracz("Jan", "Kowalski", 123456789, środki);

        String graczString = gracz.toString();
        assertTrue(graczString.contains("Jan Kowalski"), "Should contain player name");
        assertTrue(graczString.contains("123456789"), "Should contain PESEL");
        assertTrue(graczString.contains("1000,00"), "Should contain funds amount");
    }

    @Test
    void testJestWypłacalnyTrue() {
        Kwota środki = new Kwota(1000, 0);
        TestGracz gracz = new TestGracz("Jan", "Kowalski", 123456789, środki);

        Kwota cena = new Kwota(500, 0);

        assertTrue(gracz.jestWypłacalny(cena), "Player should be able to afford this amount");
    }

    @Test
    void testJestWypłacalnyFalse() {
        Kwota środki = new Kwota(100, 0);
        TestGracz gracz = new TestGracz("Jan", "Kowalski", 123456789, środki);

        Kwota cena = new Kwota(500, 0);

        assertFalse(gracz.jestWypłacalny(cena), "Player should not be able to afford this amount");
    }

    @Test
    void testZapłać() {
        Kwota środki = new Kwota(1000, 0);
        TestGracz gracz = new TestGracz("Jan", "Kowalski", 123456789, środki);

        Kwota cena = new Kwota(200, 50);
        gracz.zapłać(cena);

        String graczString = gracz.toString();
        assertTrue(graczString.contains("799,50"), "Should have reduced funds after payment");
    }

    @Test
    void testOdbierzKuponEmptyInitially() {
        Kwota środki = new Kwota(1000, 0);
        TestGracz gracz = new TestGracz("Jan", "Kowalski", 123456789, środki);

        String graczString = gracz.toString();
        assertTrue(graczString.contains("Nie posiada żadnych kuponów"),
                "Should indicate no coupons initially");
    }
}