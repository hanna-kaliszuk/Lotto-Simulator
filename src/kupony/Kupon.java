package kupony;

import finanse.Kwota;
import kolektura.Kolektura;

import java.util.List;
import java.util.Random;

public class Kupon {
    private static int nrKuponu = 1;
    private Kolektura kolekturaWystawiająca;
    private String identyfikator;
    private static final Random random = new Random();
    private List<Zakład> zakłady;
    private int naIleLosowań;
    private Kwota cena;
    private boolean zrealizowany; // gracz odebrał wygrane za ten kupon

    public Kupon(Kolektura wystawiająca, List<Zakład> obstawione, int losowania) {
        this.kolekturaWystawiająca = wystawiająca;
        this.zakłady = obstawione;
        this.naIleLosowań = losowania;
        this.nrKuponu = nrKuponu++;


        this.cena = obliczCenę(zakłady, naIleLosowań);
        this.identyfikator = stwórzIdentyfikator(nrKuponu, kolekturaWystawiająca.getNrKolektury());
    }

    private Kwota obliczCenę(List<Zakład> zakłady, int losowania) {
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

        String identyfikator = nrPorządkowy + "-" + nrKolektury + "-" + znacznik + "-" +
                String.format("%02d", sumaKontrolna);

        return identyfikator;
    }

    private int ObliczSumęKontrolną(int nrPorządkowy, int nrKolektury, String znacznik) {
        int suma = 0;
        suma += sumaCyfr(nrPorządkowy);
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


}