package gracze;

import finanse.Kwota;
import kolektura.Kolektura;
import kupony.Blankiet;

import java.util.ArrayList;
import java.util.List;

public class Stałoblankietowy extends Gracz {
    private Blankiet blankiet;
    private int stała;
    int nrNastępnejKolektury;

    public Stałoblankietowy(String imię, String nazwisko, int pesel, Kwota środki, Blankiet blankiet, int stała, ArrayList<Kolektura> ulubioneKolektury) {
        super(imię, nazwisko, pesel, środki);
        this.blankiet = blankiet;
        this.stała = stała;
        this.nrNastępnejKolektury = 0; // zaczynamy od pierwszej kolektury
        this.ulubioneKolektury = ulubioneKolektury;
    }

    @Override
    public void kupKupon() {
        Kolektura kolektura = wybierzKolejnąKolekturę(ulubioneKolektury, nrNastępnejKolektury);
        kolektura.sprzedajKuponZBlankietu(blankiet, this);
    }

    private Kolektura wybierzKolejnąKolekturę(List<Kolektura> ulubione, int nr) {
        Kolektura kolejna = ulubione.get(nr);
        this.nrNastępnejKolektury = (nr + 1) % ulubione.size(); // przechodzimy do następnej kolektury
        return kolejna;
    }
}