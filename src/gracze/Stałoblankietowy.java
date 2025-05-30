package gracze;

import finanse.Kwota;
import kolektura.Kolektura;
import kupony.Blankiet;
import kupony.Kupon;

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
        if (!bierzeUdziałWLosowaniu()) return;

        // wybierz kolejną kolekturę w kolejce
        Kolektura kolektura = this.wybierzKolekturę();

        int ileLosowań = blankiet.naIleLosowań();
        int ileZakładów = blankiet.ileZakładów();

        // sprawdź, czy stać go na kupienie kuponu
        Kwota cena = new Kwota(3, 0);
        cena.pomnóż(ileLosowań);
        cena.pomnóż(ileZakładów);

        if (!maWystarczająceŚrodki(cena)) return;

        // jeżeli tak, to pobieramy zapłatę i wydajemy kupon
        this.odejmijŚrodki(cena);
        Kupon kupon = kolektura.sprzedajKuponZBlankietu(blankiet);
        dodajKupon(kupon);
    }

    private Kolektura wybierzKolekturę() {
        Kolektura kolektura = ulubioneKolektury.get(indeksKolejnejKolektury);
        indeksKolejnejKolektury = (indeksKolejnejKolektury + 1) % ulubioneKolektury.size();
        return kolektura;
    }

    private boolean bierzeUdziałWLosowaniu() {
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
