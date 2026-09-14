package main.java.totolotek.kupony;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import main.java.wyjątki.NieprawidłoweDane;

public class Zakład {
    private static final int MIN_LICZB = 1;
    private static final int MAX_LICZB = 49;
    private static final int WYMAGANE_LICZBY = 6;

    private final int[] liczby;
    private boolean anulowany;

    public Zakład(int[] liczby) throws NieprawidłoweDane {
        sprawdźZakresLiczb(liczby);
        this.liczby = Arrays.copyOf(liczby, liczby.length);
        Arrays.sort(this.liczby);
        this.anulowany = false;
    }

    private void sprawdźZakresLiczb(int[] liczby) throws NieprawidłoweDane {
        for (int liczba : liczby) {
            if (liczba < MIN_LICZB || liczba > MAX_LICZB) {
                throw new NieprawidłoweDane("Liczby muszą być w zakresie od 1 do 49");
            }
        }
    }

    public boolean jestWażny() {
        Set<Integer> unikalneLiczby = new TreeSet<>();

        for (int liczba : liczby) {
            if (!unikalneLiczby.add(liczba)) {
                return false; // duplikat liczby
            }
        }

        return liczby.length == WYMAGANE_LICZBY; // musi być dokładnie 6 liczb
    }

    public boolean czyAnulowany() {
        return anulowany;
    }

    public int[] getLiczby() {
        return Arrays.copyOf(liczby, liczby.length);
    }

    public int sprawdźTrafienia(int[] wylosowaneLiczby) {
        int trafienia = 0;

        for (int liczba : liczby) {
            if (Arrays.binarySearch(wylosowaneLiczby, liczba) >= 0) {
                trafienia++;
            }
        }

        return trafienia;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Set<Integer> wybraneLiczby = new TreeSet<>();
        for (int number : liczby) wybraneLiczby.add(number);

        for (int wiersz = 0; wiersz < 5; wiersz++) {
            sb.append(" ");

            int kolumny = (wiersz < 4) ? 10 : 9;
            for (int kolumna = 0; kolumna < kolumny; kolumna++) {
                int liczba = (wiersz < 4) ? (wiersz * 10 + kolumna + 1) : (41 + kolumna);
                sb.append("[ ");
                sb.append(wybraneLiczby.contains(liczba) ? "--" : String.format("%2d", liczba));
                sb.append(" ] ");
            }
            sb.append("\n");
        }

        sb.append(" [ ").append(anulowany ? "--" : "  ").append(" ] anuluj\n");
        return sb.toString();
    }

    public void anuluj() {
        this.anulowany = true;
    }
}