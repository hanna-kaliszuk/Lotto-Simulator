package kolektura;

import centrala.Centrala;
import finanse.BudżetPaństwa;
import finanse.Kwota;
import gracze.Gracz;
import kupony.Blankiet;
import kupony.Kupon;
import kupony.Zakład;

import java.util.*;

public class Kolektura {
    private List<Kupon> sprzedaneKupony;
    private static int następnyNumer = 1;
    private final int nrKolektury;
    private static final Random random = new Random();

    public Kolektura() {
        this.nrKolektury = następnyNumer++;
        Centrala.getInstancja().zarejestrujKolekturę(this);
        this.sprzedaneKupony = new LinkedList<>();
    }

    public static void resetujNumerację() {
        następnyNumer = 1;
    }

    public void sprzedajKuponZBlankietu(Blankiet blankiet, Gracz gracz) {
        int najbliższeLosowanie = Centrala.getInstancja().getNrNastępnegoLosowania();

        Kwota cena = obliczCenęKuponu(blankiet);

        if (gracz.jestWypłacalny(cena)) {
            gracz.zapłać(cena);
        } else {
            throw new IllegalArgumentException("Gracz nie ma wystarczających środków na zakup kuponu.");
        }

        Kupon kupon = new Kupon(this, blankiet.getZakłady(), blankiet.getIleLosowań(), najbliższeLosowanie);
        sprzedaneKupony.add(kupon);

        this.przekażŚrodki(cena);
        gracz.odbierzKupon(kupon);
    }

    private Kwota obliczCenęKuponu(Blankiet blankiet) {
        int zakłady = blankiet.ileWażnychZakładów();
        int losowania = blankiet.getIleLosowań();
        Kwota cena = new Kwota(3, 0);
        cena.pomnóż(zakłady);
        cena.pomnóż(losowania);
        return cena;
    }

    public void sprzedajKuponChybiłTrafił(int liczbaZakładów, int liczbaLosowań, Gracz gracz) {
        Kwota cena = new Kwota(3, 0);
        cena.pomnóż(liczbaZakładów);
        cena.pomnóż(liczbaLosowań);

        if (gracz.jestWypłacalny(cena)) {
            gracz.zapłać(cena);
        } else {
            throw new IllegalArgumentException("Gracz nie ma wystarczających środków na zakup kuponu.");
        }

        List<Zakład> zakłady = new ArrayList<>(liczbaZakładów);

        for (int i = 0; i < liczbaZakładów; i++) {
            zakłady.add(new Zakład(generujLosoweLiczby()));
        }

        int najbliższeLosowanie = Centrala.getInstancja().getNrNastępnegoLosowania();

        Kupon kupon = new Kupon(this, zakłady, liczbaLosowań, najbliższeLosowanie);
        sprzedaneKupony.add(kupon);

        this.przekażŚrodki(cena);
        gracz.odbierzKupon(kupon);
    }

    private int[] generujLosoweLiczby() {
        int[] liczby = new int[6];
        Set<Integer> wylosowane = new HashSet<>();

        int i = 0;
        while (i < 6) {
            int losowa = random.nextInt(49) + 1; // liczby od 1 do 49

            if (wylosowane.add(losowa)) {
                liczby[i] = losowa;
                i++;
            }
        }

        // posortuj liczby dla lepszej czytelności
        Arrays.sort(liczby);
        return liczby;
    }

    private void przekażŚrodki(Kwota kwota) {
        Kwota podatek = new Kwota(kwota);
        podatek.pomnóż(0.20); // 20% podatek
        BudżetPaństwa.getInstancja().dodajPodatek(podatek);

        Kwota doCentali = new Kwota(kwota);
        doCentali.pomnóż(0.80); // 80% do centrali
        Centrala.getInstancja().dodajŚrodki(doCentali);
    }


    public List<Kupon> getSprzedaneKupony() {
        return sprzedaneKupony;
    }

    public int getNrKolektury() {
        return nrKolektury;
    }
}