package testy;

import org.junit.jupiter.api.Test;
import totolotek.kupony.Blankiet;
import totolotek.kupony.Zakład;
import wyjątki.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestBlankietu {
    @Test
    void testKonstruktora() throws NieprawidłoweDaneZakładu, NieprawidłoweDaneLosowania {
        List<Zakład> zakłady = Arrays.asList(
            new Zakład(new int[]{1, 2, 3, 4, 5, 6}),
            new Zakład(new int[]{7, 8, 9, 10, 11, 12})
        );

        Blankiet blankiet = new Blankiet(zakłady, 3);
        assertEquals(3, blankiet.getIleLosowań(), "Blankiet powinien obejmować 3 losowania");
        assertEquals(2, blankiet.getZakłady().size(), "Blankiet powinien zawierać 2 zakłady");

        assertThrows(NieprawidłoweDaneZakładu.class, () -> new Blankiet(null, 3),
                "Powinno rzucić wyjątek przy próbie utworzenia blankietu nullem jako listą zakładów");

        List<Zakład> pusteZakłady = new ArrayList<>();
        assertThrows(NieprawidłoweDaneZakładu.class, () -> new Blankiet(pusteZakłady, 3),
                "Powinno rzucić wyjątek przy próbie utworzenia blankietu z pustą listą zakładów");

        assertThrows(NieprawidłoweDaneLosowania.class, () -> new Blankiet(zakłady, 0),
                "Powinno rzucić wyjątek przy próbie utworzenia blankietu z liczbą losowań mniejszą niż 1");

        assertThrows(NieprawidłoweDaneLosowania.class, () -> new Blankiet(zakłady, -1),
                "Powinno rzucić wyjątek przy próbie utworzenia blankietu z ujemną liczbą losowań");

        assertThrows(NieprawidłoweDaneLosowania.class, () -> new Blankiet(zakłady, 11),
                "Powinno rzucić wyjątek przy próbie utworzenia blankietu z liczbą losowań większą niż 10");
    }

    @Test
    void testWażnościZakładów() throws NieprawidłoweDaneZakładu, NieprawidłoweDaneLosowania {
        Zakład ważny = new Zakład(new int[]{1, 2, 3, 4, 5, 6});
        Zakład nieważny = new Zakład(new int[]{1, 2, 3, 4, 5}); // mniej niż 6 liczb

        List<Zakład> zakłady = Arrays.asList(ważny, nieważny);
        Blankiet blankiet = new Blankiet(zakłady, 2);

        int ważneZakłady = blankiet.ileWażnychZakładów();
        assertEquals(1, ważneZakłady, "Blankiet powinien zawierać 1 ważny zakład");
    }

    @Test
    void testToString() throws NieprawidłoweDaneZakładu, NieprawidłoweDaneLosowania {
        Zakład zakład1 = new Zakład(new int[]{1, 5, 7, 45, 23, 28});
        Zakład zakład2 = new Zakład(new int[]{2, 4, 6, 8, 10, 12});
        zakład2.anuluj();
        List<Zakład> zakłady = Arrays.asList(zakład1, zakład2);

        Blankiet blankiet = new Blankiet(zakłady, 3);
        String wynik = blankiet.toString();

        String oczekiwany = "0\n" +
                " [ -- ] [  2 ] [  3 ] [  4 ] [ -- ] [  6 ] [ -- ] [  8 ] [  9 ] [ 10 ] \n" +
                " [ 11 ] [ 12 ] [ 13 ] [ 14 ] [ 15 ] [ 16 ] [ 17 ] [ 18 ] [ 19 ] [ 20 ] \n" +
                " [ 21 ] [ 22 ] [ -- ] [ 24 ] [ 25 ] [ 26 ] [ 27 ] [ -- ] [ 29 ] [ 30 ] \n" +
                " [ 31 ] [ 32 ] [ 33 ] [ 34 ] [ 35 ] [ 36 ] [ 37 ] [ 38 ] [ 39 ] [ 40 ] \n" +
                " [ 41 ] [ 42 ] [ 43 ] [ 44 ] [ -- ] [ 46 ] [ 47 ] [ 48 ] [ 49 ] \n" +
                " [    ] anuluj\n" +
                "1\n" +
                " [  1 ] [ -- ] [  3 ] [ -- ] [  5 ] [ -- ] [  7 ] [ -- ] [  9 ] [ -- ] \n" +
                " [ 11 ] [ -- ] [ 13 ] [ 14 ] [ 15 ] [ 16 ] [ 17 ] [ 18 ] [ 19 ] [ 20 ] \n" +
                " [ 21 ] [ 22 ] [ 23 ] [ 24 ] [ 25 ] [ 26 ] [ 27 ] [ 28 ] [ 29 ] [ 30 ] \n" +
                " [ 31 ] [ 32 ] [ 33 ] [ 34 ] [ 35 ] [ 36 ] [ 37 ] [ 38 ] [ 39 ] [ 40 ] \n" +
                " [ 41 ] [ 42 ] [ 43 ] [ 44 ] [ 45 ] [ 46 ] [ 47 ] [ 48 ] [ 49 ] \n" +
                " [ -- ] anuluj\n" +
                "Liczba losowań:  [  1 ]  [  2 ]  [ -- ]  [  4 ]  [  5 ]  [  6 ]  [  7 ]  [  8 ]  [  9 ]  [ 10 ] ";

        assertEquals(oczekiwany, wynik);
    }
}
