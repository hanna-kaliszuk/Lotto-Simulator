package test.java.testy;

import org.junit.jupiter.api.Test;

import main.java.totolotek.kupony.Zakład;
import main.java.wyjątki.NieprawidłoweDane;

import static org.junit.jupiter.api.Assertions.*;

public class ZakładTest {
    @Test
    void testKonstruktor() throws NieprawidłoweDane {
        int[] liczby = {1, 15, 23, 34, 42, 49};
        Zakład zakład = new Zakład(liczby);

        assertFalse(zakład.czyAnulowany(), "Zakład nie powinien być anulowany po utworzeniu");
        assertTrue(zakład.jestWażny(), "Zakład z 6 liczbami z poprawnego zakresu powinien być ważny");
    }

    @Test
    void testKonstrunktorZłeLiczby() throws NieprawidłoweDane {
        int[] liczby = {1, 2, 3};
        Zakład z1 = new Zakład(liczby);
        assertFalse(z1.jestWażny(), "Zakład z mniej niż 6 liczbami powinien być nieważny");

        int[] liczby2 = {1, 50, 0, 34, 32, 32};
        assertThrows(NieprawidłoweDane.class, () -> new Zakład(liczby2),
                "Zakład z liczbami spoza zakresu powinien rzucić wyjątek");

        int[] liczby3 = {1, 2, 3, 4, 5, 6, 7, 8};
        Zakład z3 = new Zakład(liczby3);
        assertFalse(z3.jestWażny(), "Zakład z więcej niż 6 liczbami powinien być nieważny");

        int[] liczby4 = {1, 1, 2, 2, 34, 34};
        Zakład z4 = new Zakład(liczby4);
        assertFalse(z4.jestWażny(), "Zakład z duplikatami liczb powinien być nieważny");
    }

    @Test
    void testAnuluj() throws NieprawidłoweDane {
        int[] liczby = {1, 15, 23, 34, 42, 49};
        Zakład zakład = new Zakład(liczby);

        assertFalse(zakład.czyAnulowany(), "Zakład nie powinien być anulowany po utworzeniu");

        zakład.anuluj();

        assertTrue(zakład.czyAnulowany(), "Zakład powinien być anulowany po wywołaniu metody anuluj");
    }


    @Test
    void testGetLiczby() throws NieprawidłoweDane {
        int[] liczby = {1, 15, 23, 34, 42, 49};
        Zakład zakład = new Zakład(liczby);

        int[] returned = zakład.getLiczby();

        assertArrayEquals(liczby, returned, "Powinno zwrócić te same liczby");
        assertNotSame(liczby, returned, "Powinna zostać zwrócona kopia, a nie oryginał");
    }

    @Test
    void testSprawdźTrafienia() throws NieprawidłoweDane {
        int[] liczby = {1, 15, 23, 34, 42, 49};
        int[] wylosowane = {1, 15, 23, 10, 20, 30};

        Zakład zakład = new Zakład(liczby);
        int trafienia = zakład.sprawdźTrafienia(wylosowane);

        assertEquals(3, trafienia, "Powinno zwrócić 3 trafienia");

        int[] liczby2 = {1, 2, 3, 4, 5, 6};
        int[] wylosowane2 = {10, 20, 30, 40, 45, 49};

        Zakład zakład2 = new Zakład(liczby2);
        int trafienia2 = zakład2.sprawdźTrafienia(wylosowane2);


        assertEquals(0, trafienia2, "Nie powinno być trafień");

        int[] liczby3 = {1, 2, 3, 4, 5, 6};
        int[] wylosowane3 = {1, 2, 3, 4, 5, 6};

        Zakład zakład3 = new Zakład(liczby3);
        int trafienia3 = zakład3.sprawdźTrafienia(wylosowane3);

        assertEquals(6, trafienia3, "Powinno być 6 trafień");
    }
}