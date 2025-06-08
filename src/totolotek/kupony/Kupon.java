package totolotek.kupony;

import totolotek.finanse.Kwota;
import totolotek.kolektura.Kolektura;
import wyjątki.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Kupon {
    private static int następnyNrKuponu = 1;
    private final int nrKuponu;
    private final Kolektura kolekturaWystawiająca;
    private final String identyfikator;
    private static final Random random = new Random();
    private final List<Zakład> zakłady;
    private final int naIleLosowań;
    private final Kwota cena;
    private boolean zrealizowany; // gracz odebrał wygrane za ten kupon
    private final List<Integer> nrLosowań;

    public Kupon(Kolektura wystawiająca, List<Zakład> obstawione, int losowania, int najbliższeLosowanie) throws NieprawidłoweDane {
        if (wystawiająca == null) {
            throw new NieprawidłoweDane("Kolektura wystawiająca nie może być null");
        }

        if (obstawione == null || obstawione.isEmpty()) {
            throw new NieprawidłoweDane("Lista zakładów nie może być pusta");
        }

        if (losowania < 1 || losowania > 10) {
            throw new NieprawidłoweDane("Liczba losowań musi być w zakresie od 1 do 10");
        }

        if (najbliższeLosowanie < 1) {
            throw new NieprawidłoweDane("Numer najbliższego losowania musi być większy lub równy 1");
        }

        this.kolekturaWystawiająca = wystawiająca;
        this.zakłady = obstawione;
        this.naIleLosowań = losowania;
        this.nrKuponu = następnyNrKuponu++;
        this.zrealizowany = false;

        this.nrLosowań = new ArrayList<>(losowania);
        for (int i = 0; i < losowania; i++) {
            this.nrLosowań.add(najbliższeLosowanie + i);
        }

        this.cena = obliczCenę(zakłady, naIleLosowań);
        this.identyfikator = stwórzIdentyfikator(nrKuponu, kolekturaWystawiająca.getNrKolektury());
    }

    private Kwota obliczCenę(List<Zakład> zakłady, int losowania) throws NieprawidłoweDane {
        int ważneZakłady = 0;

        for (Zakład z : zakłady) {
            if (z.jestWażny() && !z.czyAnulowany()) {
                ważneZakłady++;
            }
        }

        Kwota cena = new Kwota(3, 0); // cena bazowa za zakład
        cena.pomnóż(ważneZakłady);
        cena.pomnóż(losowania);

        return cena;
    }

    private String stwórzIdentyfikator(int nrPorządkowy, int nrKolektury) {
        StringBuilder znacznik = new StringBuilder();

        // generowanie losowego fragmentu
        for (int i = 0; i < 9; i++) {
            znacznik.append(random.nextInt(10));
        }

        // obliczanie sumy kontrolnej
        int sumaKontrolna = ObliczSumęKontrolną(nrPorządkowy, nrKolektury, znacznik.toString());

        return nrPorządkowy + "-" + nrKolektury + "-" + znacznik + "-" + String.format("%02d", sumaKontrolna);
    }

    private int ObliczSumęKontrolną(int nrPorządkowy, int nrKolektury, String znacznik) {
        int suma = 0;
        suma += sumaCyfr(nrPorządkowy);
        suma += sumaCyfr(nrKolektury);

        for (char c : znacznik.toCharArray()) {
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

    public String getSprawozdzanieKuponu() throws NieprawidłoweDane {

        String sb = "Identyfikator kuponu: " + identyfikator + "\n" +
                "Cena kuponu: " + cena + "\n" +
                "W tym podatek: " + cena.getPodatek() + "\n";

        return sb;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // 1. identyfikator kuponu
        sb.append("KUPON NR ");
        sb.append(this.getIdentyfikator()).append("\n");

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

    public void wydrukKuponu() {
        System.out.println(this);
    }

    public String getIdentyfikator() {
        return identyfikator;
    }

    public boolean czyNaToLosowanie(int nrLosowania) {
        return nrLosowań.contains(nrLosowania);
    }

    public List<Zakład> getZakłady() throws NieprawidłoweDane {

            List<Zakład> kopia = new ArrayList<>(zakłady.size());
            for (Zakład z : zakłady) {
                kopia.add(new Zakład(z.getLiczby()));
            }
            return kopia;

    }

    public int getNaIleLosowań() {
        return naIleLosowań;
    }

    public int getNumerKuponu() {
        return nrKuponu;
    }

    public boolean czyWziąłUdziałWeWszystkichLosowaniach() {
        int obecneLosowanie = kolekturaWystawiająca.getAktualneLosowanie();
        int nrOstatniegoLosowaniaWKtórymKuponMaWziąćUdział = nrLosowań.getLast();

        return obecneLosowanie >= nrOstatniegoLosowaniaWKtórymKuponMaWziąćUdział;
    }

    public boolean czyZrealizowany() {
        return zrealizowany;
    }

    public List<Integer> getNrLosowań() {
        List<Integer> kopia = new ArrayList<>(nrLosowań.size());
        kopia.addAll(nrLosowań);
        return kopia;
    }

    public void zrealizuj() {
        this.zrealizowany = true;
    }

    public Kwota getCena() throws NieprawidłoweDane {
        return new Kwota(cena);
    }
}