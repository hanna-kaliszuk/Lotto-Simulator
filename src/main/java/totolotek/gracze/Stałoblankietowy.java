package main.java.totolotek.gracze;

import java.util.ArrayList;
import java.util.List;

import main.java.totolotek.finanse.Kwota;
import main.java.totolotek.kolektura.Kolektura;
import main.java.totolotek.kupony.Blankiet;
import main.java.wyjątki.*;

public class Stałoblankietowy extends Gracz {
    private final Blankiet blankiet;
    private final int stała;
    int indeksNastępnejKolektury;

    public Stałoblankietowy(String imię, String nazwisko, int pesel, Kwota środki, Blankiet blankiet, int stała, ArrayList<Kolektura> ulubioneKolektury) throws NieprawidłoweDane {
        super(imię, nazwisko, pesel, środki);

        if (blankiet == null) throw new NieprawidłoweDane("Blankiet nie może być null.");
        if (stała <= 0) throw new NieprawidłoweDane("Stała musi być dodatnią liczbą całkowitą. Podano: " + stała);
        if (ulubioneKolektury == null || ulubioneKolektury.isEmpty())
            throw new NieprawidłoweDane("Lista ulubionych kolektur nie może być null lub pusta.");

        this.blankiet = blankiet;
        this.stała = stała;
        this.indeksNastępnejKolektury = 0; // zaczynamy od pierwszej kolektury
        this.ulubioneKolektury = ulubioneKolektury;
    }

    @Override
    public void kupKupon() throws NieprawidłoweDane, BrakŚrodków {
        Kolektura kolektura = wybierzKolejnąKolekturę(ulubioneKolektury, indeksNastępnejKolektury);
        kolektura.sprzedajKuponZBlankietu(blankiet, this);
    }

    private Kolektura wybierzKolejnąKolekturę(List<Kolektura> ulubione, int nr) {
        Kolektura kolejna = ulubione.get(nr);
        this.indeksNastępnejKolektury = (nr + 1) % ulubione.size(); // przechodzimy do następnej kolektury
        return kolejna;
    }

    public int getCoIleLosowań() {
        return stała;
    }
}