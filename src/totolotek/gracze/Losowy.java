package totolotek.gracze;

import totolotek.finanse.Kwota;
import totolotek.kolektura.Kolektura;
import wyjątki.BrakŚrodków;
import wyjątki.NieprawidłoweDane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Losowy extends Gracz {
    private static final Random random = new Random();

    public Losowy(String imię, String nazwisko, int pesel, ArrayList<Kolektura> ulubioneKolektury) throws NieprawidłoweDane {
        super(imię, nazwisko, pesel, wygenerujLosoweŚrodki());

        if (ulubioneKolektury == null || ulubioneKolektury.isEmpty()) {
            throw new NieprawidłoweDane("Lista ulubionych kolektur nie może być pusta.");
        }

        this.ulubioneKolektury = ulubioneKolektury;
    }

    private static Kwota wygenerujLosoweŚrodki() {
        int złote = random.nextInt(1_000_000);
        int grosze = random.nextInt(100);

        return new Kwota(złote, grosze);
    }

    @Override
    public void kupKupon() throws NieprawidłoweDane {
        Kolektura kolektura = wybierzLosowąKolekturę(ulubioneKolektury);
        int ileKuponów = random.nextInt(100) + 1; // losujemy liczbę kuponów od 1 do 100

        for (int i = 0; i < ileKuponów; i++) {
            try {
                int ileZakładów = random.nextInt(8) + 1;
                int ileLosowań = random.nextInt(10) + 1;
                kolektura.sprzedajKuponChybiłTrafił(ileZakładów, ileLosowań, this);
            } catch (BrakŚrodków e) {
                continue;
            }
        }
    }

    private Kolektura wybierzLosowąKolekturę(List<Kolektura> ulubione) {
        int indeks = random.nextInt(ulubione.size());
        return ulubione.get(indeks);
    }
}