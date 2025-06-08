package totolotek.kupony;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class Zakład {
    private int[] liczby;
    private boolean anulowany;

    public Zakład(int[] liczby) {
        // dodaj sprawdzenie długości tablicy i czy ma 6 różnych liczb z zakresu 1-49
        this.liczby = Arrays.copyOf(liczby, liczby.length);
        this.anulowany = false;
    }

    public boolean jestWażny() {
        return liczby.length == 6;
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

        TreeSet<Integer> wybraneLiczby = new TreeSet<>();
        for (int number : liczby) {
            wybraneLiczby.add(number);
        }

        for (int wiersz = 0; wiersz < 5; wiersz++) {
            sb.append(" ");

            if (wiersz < 4) {
                for (int kolumna = 0; kolumna < 10; kolumna++) {
                    int liczba = wiersz * 10 + kolumna + 1;

                    sb.append("[ ");
                    if (wybraneLiczby.contains(liczba)) {
                        sb.append("--");
                    } else {
                        sb.append(String.format("%2d", liczba));
                    }
                    sb.append(" ] ");
                }
            } else {
                for (int kolumna = 0; kolumna < 9; kolumna++) {
                    int number = 41 + kolumna;

                    sb.append("[ ");
                    if (wybraneLiczby.contains(number)) {
                        sb.append("--");
                    } else {
                        sb.append(String.format("%2d", number));
                    }
                    sb.append(" ] ");
                }
            }
            sb.append("\n");
        }

        sb.append(" [ ");
        if (anulowany) {
            sb.append("--");
        } else {
            sb.append("  ");
        }
        sb.append(" ] anuluj\n");

        return sb.toString();
    }

    public void anuluj() {
        this.anulowany = true;
    }
}