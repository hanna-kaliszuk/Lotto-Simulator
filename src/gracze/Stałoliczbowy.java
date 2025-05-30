package gracze;

import finanse.Kwota;
import kolektura.Kolektura;
import kupony.Blankiet;
import kupony.Kupon;
import kupony.Zakład;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Stałoliczbowy extends Gracz {
    private Zakład zakład;
    private List<Kolektura> ulubioneKolektury;
    private int indeksKolejnejKolektury;

    public Stałoliczbowy(String imię, String nazwisko, int pesel, Kwota środki, int[] ulubione,
                         List<Kolektura> ulubioneKolektury) {
        super(imię, nazwisko, pesel, środki);
        // dodaj walidację
        this.ulubioneKolektury = ulubioneKolektury;

        Arrays.sort(ulubione);
        this.zakład = new Zakład(ulubione);
        this.indeksKolejnejKolektury = 0;
    }


    @Override
    public void kupKupon() {
        // sprawdzamy, czy przeprowadzono wszystkie losowania obstawione w poprzednim kuponie
        if(!możeKupićNowyKupon()) return;

        // wybierz kolejną kolekturę w kolejce
        Kolektura kolektura = this.wybierzKolekturę();

        // przygotuj blankiet z ulubionymi liczbami
        List<Zakład> zakłady = new ArrayList<>(1);
        zakłady.add(zakład);

        Blankiet blankiet = new Blankiet(zakłady, 10);

        // sprawdź, czy stać go na kupienie kuponu
        Kwota cena = new Kwota(3, 0);
        cena.pomnóż(10); // 10 losowań
        cena.pomnóż(1); // 1 zakład

        // sprawdź, czy stać go na kupienie kuponu
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

    private boolean możeKupićNowyKupon() {
        if (posiadaneKupony.isEmpty()) return true;

        Kupon ostatniKupon = posiadaneKupony.getLast();
        return ostatniKupon.czyWykorzystany();
    }
}
