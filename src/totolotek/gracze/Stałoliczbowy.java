package totolotek.gracze;

import totolotek.finanse.Kwota;
import totolotek.kolektura.Kolektura;
import totolotek.kupony.Blankiet;
import totolotek.kupony.Kupon;
import totolotek.kupony.Zakład;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Stałoliczbowy extends Gracz {
    private int[] ulubioneLiczby;
    private final int LOSOWANIA = 10;
    private int nrNastępnejKolektury;

    public Stałoliczbowy(String imię, String nazwisko, int pesel, Kwota środki, int[] ulubioneLiczby, ArrayList<Kolektura> ulubioneKolektury) {
        super(imię, nazwisko, pesel, środki);
        Arrays.sort(ulubioneLiczby); // sortowanie ulubionych liczb
        this.ulubioneLiczby = ulubioneLiczby;
        this.nrNastępnejKolektury = 0; // początkowo brak kolektury
        this.ulubioneKolektury = ulubioneKolektury; // ustawienie ulubionych kolektur
    }

    @Override
    public void kupKupon() {
        // sprawdzamy, czy ostatni zakupiony przez gracza kupon wziął udział we wszystkich losowaniach, w których miał
        if (!this.możeKupićNowy()) return;
        Kolektura kolektura = wybierzKolejnąKolekturę(ulubioneKolektury, nrNastępnejKolektury);
        Blankiet blankiet = new Blankiet(wygenerujZakład(), LOSOWANIA);
        kolektura.sprzedajKuponZBlankietu(blankiet, this);
    }

    private Kolektura wybierzKolejnąKolekturę(List<Kolektura> ulubione, int nr) {
        Kolektura kolejna = ulubione.get(nr);
        this.nrNastępnejKolektury = (nr + 1) % ulubione.size(); // przechodzimy do następnej kolektury
        return kolejna;
    }

    private List<Zakład> wygenerujZakład() {
        List<Zakład> zakłady = new ArrayList<>(1);
        Zakład zakład = new Zakład(ulubioneLiczby);
        zakłady.add(zakład);
        return zakłady;
    }

    private boolean możeKupićNowy() {
        if (zakupioneKupony.isEmpty()) {
            return true; // jeśli nie ma żadnych kuponów, można kupić nowy
        }

        Kupon ostatniKupon = zakupioneKupony.getLast();
        return ostatniKupon.czyWziąłUdziałWeWszystkichLosowaniach();
    }
}