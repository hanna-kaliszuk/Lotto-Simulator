package gracze;

import finanse.Kwota;
import kolektura.Kolektura;

import java.util.ArrayList;

public class Minimalista extends Gracz {
    public Minimalista(String imię, String nazwisko, int pesel, Kwota środki, Kolektura ulubiona) {
        super(imię, nazwisko, pesel, środki);
        this.ulubioneKolektury = new ArrayList<>(1);
        ulubioneKolektury.add(ulubiona);
    }

    @Override
    public void kupKupon() {
        Kolektura kolektura = ulubioneKolektury.get(0); // pobieramy ulubioną kolekturę gracza
        kolektura.sprzedajKuponChybiłTrafił(1, 1, this);
    }


}