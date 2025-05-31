package centrala.losowanie;

import finanse.Kwota;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Losowanie {
    private final int nrLosowania;
    private final int[] wylosowaneLiczby;
    private final Map<Integer, Integer> wygrane = new HashMap<>(); // Stopień -> liczba wygranych
    private final Map<Integer, Kwota> nagrody = new HashMap<>();   // Stopień -> kwota nagrody

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
        return wynik.stream()
                .mapToInt(Integer::intValue)
                .toArray();
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

    public Kwota getNagroda(int stopień) {
        Kwota nagroda = nagrody.get(stopień);
        if (nagroda == null) {
            return new Kwota(0, 0);
        }

        return new Kwota(nagroda);
    }
}