package main.java.totolotek.kupony;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.java.totolotek.finanse.Kwota;
import main.java.totolotek.kolektura.Kolektura;
import main.java.wyjątki.*;

public class Kupon {
    private static final int MIN_LOSOWAŃ = 1;
    private static final int MAX_LOSOWAŃ = 10;
    private static final int CENA_ZAKŁADU_ZŁ = 3;
    private static final int CENA_ZAKŁADU_GR = 0;
    private static final int DŁUGOŚĆ_ZNACZNIKA = 9;

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
        sprawdźParametry(wystawiająca, obstawione, losowania, najbliższeLosowanie);

        this.kolekturaWystawiająca = wystawiająca;
        this.zakłady = obstawione;
        this.naIleLosowań = losowania;
        this.nrKuponu = następnyNrKuponu++;
        this.zrealizowany = false;
        this.nrLosowań = generujNumeryLosowań(losowania, najbliższeLosowanie);
        this.cena = obliczCenę(zakłady, naIleLosowań);
        this.identyfikator = stwórzIdentyfikator(nrKuponu, kolekturaWystawiająca.getNrKolektury());
    }

    private void sprawdźParametry(Kolektura wystawiająca, List<Zakład> obstawione, int losowania, int najbliższeLosowanie) throws NieprawidłoweDane {
        if (wystawiająca == null) throw new NieprawidłoweDane("Kolektura wystawiająca nie może być null");
        if (obstawione == null || obstawione.isEmpty()) throw new NieprawidłoweDane("Lista zakładów nie może być pusta");
        if (losowania < MIN_LOSOWAŃ || losowania > MAX_LOSOWAŃ) throw new NieprawidłoweDane("Liczba losowań musi być w zakresie od 1 do 10");
        if (najbliższeLosowanie < 1) throw new NieprawidłoweDane("Numer najbliższego losowania musi być większy lub równy 1");
    }

    private List<Integer> generujNumeryLosowań(int ile, int start) {
        List<Integer> lista = new ArrayList<>(ile);

        for (int i = 0; i < ile; i++) {
            lista.add(start + i);
        }

        return lista;
    }

    private Kwota obliczCenę(List<Zakład> zakłady, int losowania) throws NieprawidłoweDane {
        int ważneZakłady = 0;

        for (Zakład z : zakłady) {
            if (z.jestWażny() && !z.czyAnulowany()) ważneZakłady++;
        }

        Kwota cena = new Kwota(CENA_ZAKŁADU_ZŁ, CENA_ZAKŁADU_GR);
        cena.pomnóż(ważneZakłady);
        cena.pomnóż(losowania);

        return cena;
    }

    private String stwórzIdentyfikator(int nrPorządkowy, int nrKolektury) {
        StringBuilder znacznik = new StringBuilder();

        for (int i = 0; i < DŁUGOŚĆ_ZNACZNIKA; i++) {
            znacznik.append(random.nextInt(10));
        }

        int sumaKontrolna = obliczSumęKontrolną(nrPorządkowy, nrKolektury, znacznik.toString());

        return nrPorządkowy + "-" + nrKolektury + "-" + znacznik + "-" + String.format("%02d", sumaKontrolna);
    }

    private int obliczSumęKontrolną(int nrPorządkowy, int nrKolektury, String znacznik) {
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
        return "Identyfikator kuponu: " + identyfikator + "\n" +
                "Cena kuponu: " + cena + "\n" +
                "W tym podatek: " + cena.getPodatek() + "\n";
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

        // 5. cena brutto kuponu
        Kwota cena = this.cena;
        sb.append("CENA: ").append(cena).append('\n');

        return sb.toString();
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

    public boolean czyWziąłUdziałWeWszystkichLosowaniach() {
        int obecneLosowanie = kolekturaWystawiająca.getAktualneLosowanie();
        int nrOstatniegoLosowaniaWKtórymKuponMaWziąćUdział = nrLosowań.getLast();

        return obecneLosowanie >= nrOstatniegoLosowaniaWKtórymKuponMaWziąćUdział;
    }

    public boolean czyZrealizowany() {
        return zrealizowany;
    }

    public List<Integer> getNrLosowań() {
        return new ArrayList<>(nrLosowań);
    }

    public void zrealizuj() {
        this.zrealizowany = true;
    }

    public Kwota getCena() throws NieprawidłoweDane {
        return new Kwota(cena);
    }
}