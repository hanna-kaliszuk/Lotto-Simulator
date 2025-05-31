package kupony;

import centrala.Centrala;
import centrala.losowanie.Losowanie;
import finanse.Kwota;
import kolektura.Kolektura;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Kupon {
    private static final AtomicInteger globalnyLicznik = new AtomicInteger(0); // aby zapewnić systemową
    // unikalność numeru porządkowego kuponu

    private int nrPorządkowy;
    private Kolektura Kolektura; // w jakiej kolekturze został sprzedany
    private String identyfikator;
    private Kwota cena;
    private boolean zrealizowany; // czy nagroda została odebrana przez kupującego
    private boolean wykorzystany; // czy wziął udział we wszytkich losowaniach
    private List<Zakład> zakłady; // lista obstawionych zakładów na pods
    private List<Integer> nrLosowań;
    private Map<Integer, int[]> wygrane;

    public Kupon(Kolektura kolektura, List<Zakład> zakłady, int najbliższeLosowanie, int ileLosowań) {
        assert !zakłady.isEmpty() && zakłady.size() <= 8: "Kupon musi zawierać od 1 do 8 zakładów";
        assert ileLosowań >= 1 && ileLosowań <= 10: "Kupon może obejmować jedynie od 1 do 10 losowań";

        this.nrPorządkowy = globalnyLicznik.incrementAndGet();
        this.Kolektura = kolektura;
        this.zakłady = zakłady;
        this.zrealizowany = false;

        this.nrLosowań = new ArrayList<>(ileLosowań);
        for (int i = 0; i < ileLosowań; i++) {
            this.nrLosowań.add(najbliższeLosowanie + i);
        }

        this.identyfikator = generujIdentyfikator();

        Kwota cena = new Kwota(3, 0);
        cena.pomnóż(zakłady.size());
        cena.pomnóż(ileLosowań);
        this.cena = cena;
        this.wygrane = new HashMap<>(); // nrLosowania -> [I, II, III, IV]
    }

    public Kupon(Kolektura kolektura, Zakład zakład, int najbliższeLosowanie, int ileLosowań) {
        this(kolektura, Collections.singletonList(zakład), najbliższeLosowanie, ileLosowań);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        // 1. identyfikator kuponu
        sb.append("KUPON NR ");
        sb.append(this.getIdentyfikator() + "\n");

        // 2. ponumerowana lista kolejnych zakładów z wyrównanymi wylosowanymi liczbami do prawej
        for (int i = 0; i < zakłady.size(); i++) {
            sb.append(String.format("%d: ", i + 1));
            sb.append(zakłady.get(i).toString()).append('\n');
        }

        // 3. liczba losowań
        sb.append("LICZBA LOSOWAŃ: ").append(nrLosowań.size()).append('\n');

        // 4. lista numerów losowań
        sb.append("NUMERY LOSOWAŃ:\n ");
        for (Integer integer : nrLosowań) {
            sb.append(integer).append(' ');
        }
        sb.append('\n');

        // cena brutto kuponu
        Kwota cena = this.cena;
        sb.append("CENA: ").append(cena).append('\n');

        return sb.toString();
    }

    private String generujIdentyfikator() {
        Random rand = new Random();
        StringBuilder znacznik = new StringBuilder();

        // generowanie losowego fragmentu
        for (int i = 0; i < 9; i++) {
            znacznik.append(rand.nextInt(10));
        }

        // obliczanie sumy kontrolnej
        int sumaKontrolna = ObliczSumęKontrolną(nrPorządkowy, Kolektura.getNrKolektury(),
                znacznik.toString());

        String identyfikator = nrPorządkowy + "-" + Kolektura + "-" + znacznik + "-" +
                String.format("%02d", sumaKontrolna);

        return identyfikator;
    }

    private int ObliczSumęKontrolną(int nrKuponu, int nrKolektury, String znacznik) {
        int suma = 0;
        suma += sumaCyfr(nrKuponu);
        suma += sumaCyfr(nrKolektury);

        for(char c : znacznik.toCharArray()) {
            suma += Character.getNumericValue(c);
        }

        return suma % 100;
    }

    private int sumaCyfr(int liczba) {
        int suma = 0;

        while (liczba > 0) {
            suma += liczba % 10;
            liczba /= 10;
        }

        return suma;
    }

    public String getIdentyfikator() {
        return identyfikator;
    }

    public void wykorzystajKupon() {
        this.zrealizowany = true;
    }

    public boolean czyZrealizowany() {
        return zrealizowany;
    }

    public boolean czyWykorzystany() {
        return wykorzystany;
    }

    public Kwota getCena() {
        return new Kwota(this.cena.getZłote(), this.cena.getGrosze());
    }

    public boolean zawieraLosowanie(int nrLosowania) {
        return nrLosowań.contains(nrLosowania);
    }

    public List<Zakład> getZakłady() {
        List<Zakład> kopia = new ArrayList<>(zakłady.size());

        for (Zakład z : zakłady) {
            kopia.add(z.kopia());
        }

        return kopia;

    }

    public Kolektura getKolektura() {
        return this.Kolektura;
    }

    public void dodajWygraną(int nrLosowania, int stopień) {
        if (stopień < 1 || stopień > 4) return;

        // Pobierz istniejącą tablicę lub utwórz nową
        int[] wygraneWLosowaniu = wygrane.getOrDefault(nrLosowania, new int[4]);

        // Zwiększ licznik odpowiedniego stopnia (indeks = stopień - 1)
        wygraneWLosowaniu[stopień - 1]++;

        // Aktualizuj mapę
        wygrane.put(nrLosowania, wygraneWLosowaniu);
    }

    public Map<Integer, int[]> getWygrane() {
        // Zwróć głęboką kopię mapy
        Map<Integer, int[]> kopia = new HashMap<>();
        for (Map.Entry<Integer, int[]> entry : wygrane.entrySet()) {
            kopia.put(entry.getKey(), Arrays.copyOf(entry.getValue(), entry.getValue().length));
        }
        return kopia;
    }

    public Kwota obliczWygranąBrutto() {
        Kwota suma = new Kwota(0, 0);
        Centrala centrala = Centrala.getInstancja();

        for (Map.Entry<Integer, int[]> entry : wygrane.entrySet()) {
            int nrLosowania = entry.getKey();
            int[] wygraneStopnie = entry.getValue();
            Losowanie losowanie = centrala.getLosowanie(nrLosowania);

            if (losowanie != null) {
                for (int stopień = 1; stopień <= 4; stopień++) {
                    int ilość = wygraneStopnie[stopień - 1];
                    if (ilość > 0) {
                        Kwota nagroda = losowanie.getNagroda(stopień);
                        Kwota temp = new Kwota(nagroda);
                        temp.pomnóż(ilość);
                        suma.dodaj(temp);
                    }
                }
            }
        }

        return suma;
    }
}

