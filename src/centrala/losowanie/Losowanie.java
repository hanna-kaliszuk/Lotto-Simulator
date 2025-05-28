package centrala.losowanie;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Losowanie {
    private int nrLosowania;
    private int[] wylosowaneLiczby;

    public Losowanie(int numer) {
        this.nrLosowania = numer;
        this.wylosowaneLiczby = wylosujLiczby();
        Arrays.sort(this.wylosowaneLiczby);
    }

    private int[] wylosujLiczby() {
        Set<Integer> wynik = new HashSet<>();
        while (wynik.size() < 6) {
            int liczba = ThreadLocalRandom.current().nextInt(1, 50);
            wynik.add(liczba);
        }
        return wynik.stream().mapToInt(Integer::intValue).toArray();
    }

    public int[] getWylosowaneLiczby() {
        return Arrays.copyOf(wylosowaneLiczby, wylosowaneLiczby.length);
    }

    public int getNrLosowania() {
        return nrLosowania;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Losowanie nr ").append(nrLosowania).append("\n");
        sb.append("Wyniki: ");
        for (int liczba : wylosowaneLiczby) {
            sb.append(String.format("%2d", liczba)).append(" ");
        }
        return sb.toString();
    }
}
