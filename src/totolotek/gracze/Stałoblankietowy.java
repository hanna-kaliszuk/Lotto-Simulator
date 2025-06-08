package totolotek.gracze;

import totolotek.finanse.Kwota;
import totolotek.kolektura.Kolektura;
import totolotek.kupony.Blankiet;
import wyjątki.BrakŚrodków;
import wyjątki.NieprawidłoweDane;

import java.util.ArrayList;
import java.util.List;

public class Stałoblankietowy extends Gracz {
    private final Blankiet blankiet;
    private final int stała;
    int nrNastępnejKolektury;

    public Stałoblankietowy(String imię, String nazwisko, int pesel, Kwota środki, Blankiet blankiet, int stała, ArrayList<Kolektura> ulubioneKolektury) throws NieprawidłoweDane {
        super(imię, nazwisko, pesel, środki);

        if (blankiet == null) {
            throw new NieprawidłoweDane("Blankiet nie może być null.");
        }

        if (stała <= 0) {
            throw new NieprawidłoweDane("Stała musi być dodatnią liczbą całkowitą. Podano: " + stała);
        }

        if (ulubioneKolektury == null || ulubioneKolektury.isEmpty()) {
            throw new NieprawidłoweDane("Lista ulubionych kolektur nie może być null lub pusta.");
        }

        this.blankiet = blankiet;
        this.stała = stała;
        this.nrNastępnejKolektury = 0; // zaczynamy od pierwszej kolektury
        this.ulubioneKolektury = ulubioneKolektury;
    }

    @Override
    public void kupKupon() throws NieprawidłoweDane, BrakŚrodków {
        Kolektura kolektura = wybierzKolejnąKolekturę(ulubioneKolektury, nrNastępnejKolektury);
        kolektura.sprzedajKuponZBlankietu(blankiet, this);
    }

    private Kolektura wybierzKolejnąKolekturę(List<Kolektura> ulubione, int nr) {
        Kolektura kolejna = ulubione.get(nr);
        this.nrNastępnejKolektury = (nr + 1) % ulubione.size(); // przechodzimy do następnej kolektury
        return kolejna;
    }

    // metody do testów
    public int getCoIleLosowań() {
        return stała;
    }
}