package gracze;

import finanse.Kwota;
import kolektura.Kolektura;
import kupony.Blankiet;
import kupony.Kupon;
import kupony.Zakład;

import java.util.List;

public class Stałoblankietowy extends Gracz {
    private Blankiet blankiet;
    private int stała;
    private List<Kolektura> ulubioneKolektury;
    private int indeksKolejnejKolektury;
    private int ostatnieLosowanie = 0;
    private static int globalnyLicznikLosowań = 0;

    public Stałoblankietowy(String imię, String nazwisko, int pesel, Kwota środki, Blankiet blankiet,
                            List<Kolektura> ulubioneKolektury, int stała) {
        super(imię, nazwisko, pesel, środki);
        // dodaj walidację danych wejściowych
        this.blankiet = blankiet;
        this.ulubioneKolektury = ulubioneKolektury;
        this.stała = stała;
    }


    @Override
    public void kupKupon() {
        // jeżeli nie zgadza się odstęp pomiędzy losowaniami, to nie robimy nic
        if (!następneLosowanie()) return;

        // wybierz kolejną kolekturę w kolejce
        Kolektura kolektura = this.wybierzKolekturę();

        // stwórz listę zakładów na podstawie blankietu
        List<Zakład> zakłady = blankiet.stwórzListęZakładów();

        // wygeneruj kupon na podstawie blankietu
        Kupon kupon = new Kupon(kolektura, zakłady, centrala.getNrNastępnegoLosowania(), blankiet.naIleLosowań());

        // sprawdź, czy stać go na kupienie kuponu
        if (!możeKupićKupon(kupon.getCena())) return;

        // jeżeli tak, to wydajemy mu kupon
        kolektura.sprzedajKupon(kupon);
        dodajKupon(kupon);
        this.odejmijŚrodki(kupon.getCena());
    }

    private Kolektura wybierzKolekturę() {
        Kolektura kolektura = ulubioneKolektury.get(indeksKolejnejKolektury);
        indeksKolejnejKolektury = (indeksKolejnejKolektury + 1) % ulubioneKolektury.size();
        return kolektura;
    }

    private boolean następneLosowanie() {
        globalnyLicznikLosowań = centrala.getNrNastępnegoLosowania() - 1;

        if (ostatnieLosowanie == 0) { // pierwsze losowanie dla tego gracza
            ostatnieLosowanie = globalnyLicznikLosowań;
            return true;
        }

        int różnicaLosowań = globalnyLicznikLosowań - ostatnieLosowanie;

        if (różnicaLosowań >= stała) {
            ostatnieLosowanie = globalnyLicznikLosowań;
            return true;
        }

        return false;
    }

}
