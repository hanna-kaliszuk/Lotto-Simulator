package kupony;

import centrala.losowanie.Losowanie;
import finanse.Kwota;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Kupon {
    private static final AtomicInteger globalnyLicznik = new AtomicInteger(0);

    private int nrPorządkowy;
    private int nrKolektury;
    private String identyfikator;
    private boolean wykorzystany;
    private List<Zakład> zakłady;
    private List<Integer> nrLosowań;

    public Kupon(int nrKolektury, List<Zakład> zakłady, int najbliższeLosowanie, int ileLosowań) {
        assert zakłady.size() >= 1 && zakłady.size() <= 8: "Kupon musi zawierać od 1 do 8 zakładów";
        assert ileLosowań >= 1 && ileLosowań <= 10: "Kupon może obejmować jedynie od 1 do 10 losowań";

        this.nrPorządkowy = globalnyLicznik.incrementAndGet();
        this.nrKolektury = nrKolektury;
        this.zakłady = zakłady;
        this.wykorzystany = false;

        this.nrLosowań = new ArrayList<>();
        for (int i = 0; i < ileLosowań; i++) {
            this.nrLosowań.add(najbliższeLosowanie + i);
        }

        this.identyfikator = generujIdentyfikator();

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
        Kwota cena = policzCenę();
        sb.append("CENA: ").append(cena).append('\n');

        return sb.toString();
    }

    private Kwota policzCenę() {
        Kwota cena = new Kwota(0, 0);
        cena.dodaj(new Kwota(3, 0));
        cena.pomnóż(zakłady.size());
        cena.pomnóż(nrLosowań.size());

        return cena;
    }

    private String generujIdentyfikator() {
        Random rand = new Random();
        StringBuilder znacznik = new StringBuilder();

        // generowanie losowego fragmentu
        for (int i = 0; i < 9; i++) {
            znacznik.append(rand.nextInt(10));
        }

        // obliczanie sumy kontrolnej
        int sumaKontrolna = ObliczSumęKontrolną(nrPorządkowy, nrKolektury, znacznik.toString());

        String identyfikator = nrPorządkowy + "-" + nrKolektury + "-" + znacznik + "-" +
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
}
