package centrala.losowanie;

import centrala.Centrala;
import kolektura.Kolektura;
import kupony.Kupon;
import kupony.Zakład;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Losowanie {
    private static final Centrala centrala = Centrala.getInstancja();
    private final int nrLosowania;
    private final int[] wylosowaneLiczby;
    private List<Kupon> kuponyBiorąceUdział;
    private List<Zakład> zakładyBiorąceUdział;

    public Losowanie (int nr) {
        this.nrLosowania = nr;
        this.wylosowaneLiczby = this.wylosujLiczby();
        Arrays.sort(wylosowaneLiczby);
        this.kuponyBiorąceUdział = new LinkedList<>();
        this.zakładyBiorąceUdział = new LinkedList<>();
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

    public void zbierzKupony(List<Kolektura> listaKolektur) {
        kuponyBiorąceUdział = listaKolektur.stream()
                .flatMap(kolektura -> kolektura.getSprzedaneKupony().stream())
                .filter(kupon -> kupon.czyNaToLosowanie(this.nrLosowania))
                .collect(Collectors.toList());
    }

    public int ileZakładów() {
        zakładyBiorąceUdział = kuponyBiorąceUdział.stream()
                .flatMap(kupon -> kupon.getZakłady().stream())
                .collect(Collectors.toList());

        return zakładyBiorąceUdział.size();
    }

    public int getTrafienia(int stopień) {
        int licznik = 0;
        int wymaganeTrafienia = 0;

        switch (stopień) {
            case 1: wymaganeTrafienia = 6; break; // I stopień - 6 trafionych
            case 2: wymaganeTrafienia = 5; break; // II stopień - 5 trafionych
            case 3: wymaganeTrafienia = 4; break; // III stopień - 4 trafione
            case 4: wymaganeTrafienia = 3; break; // IV stopień - 3 trafione
            default: return 0;
        }

        for (Zakład z : zakładyBiorąceUdział) {
            if (z.sprawdźTrafienia(this.wylosowaneLiczby) == wymaganeTrafienia) {
                licznik++;
            }
        }

        return licznik;
    }

    public int getNrLosowania() {
        return nrLosowania;
    }

    public int[] getWylosowaneLiczby() {
        return Arrays.copyOf(wylosowaneLiczby, wylosowaneLiczby.length);
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