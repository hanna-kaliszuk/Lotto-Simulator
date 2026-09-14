package main.java.totolotek.gracze;

import java.util.ArrayList;

import main.java.totolotek.finanse.Kwota;
import main.java.totolotek.kolektura.Kolektura;
import main.java.wyjątki.*;

public class Minimalista extends Gracz {
    public Minimalista(String imię, String nazwisko, int pesel, Kwota środki, Kolektura ulubiona) throws NieprawidłoweDane {
        super(imię, nazwisko, pesel, środki);

        if (ulubiona == null) {
            throw new NieprawidłoweDane("Ulubiona kolektura nie może być null.");
        }

        this.ulubioneKolektury = new ArrayList<>(1);
        ulubioneKolektury.add(ulubiona);
    }

    @Override
    public void kupKupon() throws NieprawidłoweDane, BrakŚrodków {
        Kolektura kolektura = ulubioneKolektury.getFirst(); // pobieramy ulubioną kolekturę gracza
        kolektura.sprzedajKuponChybiłTrafił(1, 1, this);
    }
}