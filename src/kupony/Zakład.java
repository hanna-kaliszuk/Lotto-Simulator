package kupony;

import java.util.Arrays;

public class Zakład {
    private final int[] liczby;
    boolean ważny;

    public Zakład(int[] liczby) {
        if (liczby.length == 6) {
            this.liczby = Arrays.copyOf(liczby, liczby.length);
            Arrays.sort(this.liczby);
            this.ważny = true;
        } else {
            this.ważny = false;
            this.liczby = null;
        }
    }

    public int ileTrafień(int[] wylosowane) {
        int trafienia = 0;
        for (int liczba : liczby) {
            if (Arrays.binarySearch(wylosowane, liczba) >= 0) {
                trafienia++;
            }
        }

        return trafienia;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < liczby.length; i++) {
            sb.append(String.format("%2d", liczby[i]));
            if (i < liczby.length - 1) sb.append(" ");
        }
        return sb.toString();
    }
}
