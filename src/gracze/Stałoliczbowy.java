package gracze;

import finanse.Kwota;
import kolektura.Kolektura;
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

        // przygotuj kupon z 1 zakładem z ulubionymi liczbami
        List<Zakład> zakłady = new ArrayList<>();
        zakłady.add(zakład);
        Kupon kupon = new Kupon(kolektura, zakłady, centrala.getNrNastępnegoLosowania(), 10);

        // sprawdź, czy stać go na kupienie kuponu
        if (!możeKupićKupon(kupon.getCena())) return;

        // jeżeli tak, to wydajemy kupon
        kolektura.sprzedajKupon(kupon);
        dodajKupon(kupon);
        this.odejmijŚrodki(kupon.getCena());
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
