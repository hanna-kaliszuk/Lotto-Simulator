package main.java.totolotek.gracze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.totolotek.finanse.Kwota;
import main.java.totolotek.kolektura.Kolektura;
import main.java.totolotek.kupony.Blankiet;
import main.java.totolotek.kupony.Kupon;
import main.java.totolotek.kupony.Zakład;
import main.java.wyjątki.*;

public class Stałoliczbowy extends Gracz {
    private final int[] ulubioneLiczby;
    private int indeksNastępnejKolektury;

    public Stałoliczbowy(String imię, String nazwisko, int pesel, Kwota środki, int[] ulubioneLiczby, ArrayList<Kolektura> ulubioneKolektury) throws NieprawidłoweDane {
        super(imię, nazwisko, pesel, środki);
        if (ulubioneLiczby == null || ulubioneLiczby.length == 0) throw new NieprawidłoweDane("Lista ulubionych " +
                "liczb nie może być null lub pusta.");
        if (ulubioneLiczby.length > 6) throw new NieprawidłoweDane("Maksymalnie 6 ulubionych liczb. Podano: "
                + ulubioneLiczby.length);
        if (ulubioneKolektury == null || ulubioneKolektury.isEmpty()) throw new NieprawidłoweDane("Lista ulubionych " +
                "kolektur nie może być null lub pusta.");


        Arrays.sort(ulubioneLiczby);
        this.ulubioneLiczby = ulubioneLiczby;
        this.indeksNastępnejKolektury = 0;
        this.ulubioneKolektury = ulubioneKolektury;
    }

    @Override
    public void kupKupon() throws NieprawidłoweDane, BrakŚrodków {
        // sprawdzamy, czy ostatni zakupiony przez gracza kupon wziął udział we wszystkich losowaniach, w których miał
        if (!this.możeKupićNowy()) return;
        Kolektura kolektura = wybierzKolejnąKolekturę(ulubioneKolektury, indeksNastępnejKolektury);

        int losowania = 10;
        Blankiet blankiet = new Blankiet(wygenerujZakład(), losowania);
        kolektura.sprzedajKuponZBlankietu(blankiet, this);
    }

    private Kolektura wybierzKolejnąKolekturę(List<Kolektura> ulubione, int nr) {
        Kolektura kolejna = ulubione.get(nr);
        this.indeksNastępnejKolektury = (nr + 1) % ulubione.size();

        return kolejna;
    }

    private List<Zakład> wygenerujZakład() throws NieprawidłoweDane {
            List<Zakład> zakłady = new ArrayList<>(1);
            Zakład zakład = new Zakład(ulubioneLiczby);
            zakłady.add(zakład);

            return zakłady;
    }

    private boolean możeKupićNowy() {
        if (zakupioneKupony.isEmpty()) {
            return true; // jeśli nie ma żadnych kuponów, może kupić nowy
        }

        Kupon ostatniKupon = zakupioneKupony.getLast();
        return ostatniKupon.czyWziąłUdziałWeWszystkichLosowaniach();
    }

    public int[] getUlubioneLiczby() {
        return Arrays.copyOf(ulubioneLiczby, ulubioneLiczby.length);
    }
}