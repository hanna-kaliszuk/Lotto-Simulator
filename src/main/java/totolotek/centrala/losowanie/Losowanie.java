package main.java.totolotek.centrala.losowanie;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import main.java.totolotek.kolektura.Kolektura;
import main.java.totolotek.kupony.Kupon;
import main.java.totolotek.kupony.Zakład;
import main.java.wyjątki.NieprawidłoweDane;

public class Losowanie {
    private final int nrLosowania;
    private final int[] wylosowaneLiczby;
    private List<Kupon> kuponyBiorąceUdział;
    private List<Zakład> zakładyBiorąceUdział;

    public Losowanie (int nr) throws NieprawidłoweDane {
        if (nr <= 0) throw new NieprawidłoweDane("Numer losowania musi być większy niż 0");

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
                .flatMap(kupon -> {
                    try {
                        return kupon.getZakłady().stream();
                    } catch (NieprawidłoweDane e) {
                        throw new RuntimeException(e);
                    }
                })
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