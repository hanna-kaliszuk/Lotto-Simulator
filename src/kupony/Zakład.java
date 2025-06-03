package kupony;

import java.util.Arrays;

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
}