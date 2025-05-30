package kolektura;

import centrala.Centrala;
import finanse.BudżetPaństwa;
import finanse.Kwota;
import kupony.Blankiet;
import kupony.Kupon;
import kupony.Zakład;

import java.util.*;

public class Kolektura {
    private static final int LICZBY_W_ZAKŁADZIE = 6;
    private static int następnyNumer = 1;
    private int nrKolektury;
    private List<Kupon> sprzedaneKupony;
    private Centrala centrala;
    private BudżetPaństwa budżetPaństwa;
    private static final Random random = new Random();

    public Kolektura() {
        this.nrKolektury = następnyNumer++;
        this.sprzedaneKupony = new LinkedList<>();
        this.centrala = Centrala.getInstancja();
        this.budżetPaństwa = BudżetPaństwa.getInstancja();
    }

    // metody do sprzedawania biletu
    public Kupon sprzedajKuponZBlankietu(Blankiet blankiet) {
        int ileLosowań = blankiet.naIleLosowań();
        List<Zakład> zakłady = blankiet.getListaZakładów();
        int najbliższeLosowanie = centrala.getNrNastępnegoLosowania();

        Kupon kupon = new Kupon(this, zakłady, najbliższeLosowanie, ileLosowań);

        this.zarejestrujKupon(kupon);
        this.przekażPieniądze(kupon.getCena(), zakłady.size(), ileLosowań);

        return kupon;
    }

    public Kupon sprzedajKuponChybiłTrafił(int liczbaZakładów, int liczbaLosowań) {
        List<Zakład> zakłady = new ArrayList<>(liczbaZakładów);

        for (int i = 0; i < liczbaZakładów; i++) {
            zakłady.add(new Zakład(generujLosoweLiczby()));
        }

        int najbliższeLosowanie = centrala.getNrNastępnegoLosowania();

        Kupon kupon = new Kupon(this, zakłady, najbliższeLosowanie, liczbaLosowań);

        this.zarejestrujKupon(kupon);
        this.przekażPieniądze(kupon.getCena(), liczbaZakładów, liczbaLosowań);

        return kupon;
    }

    private void zarejestrujKupon(Kupon kupon) {
        this.sprzedaneKupony.add(kupon);
    }

    // obsługa wygranych
    public void wypłaćWygraną() {

    }

    private boolean sprawdźKupon(Kupon kupon) { // sprawdź, czy kupiony w tej kolekturze

    }

    private boolean sprawdźCzyNieZrealizowany(Kupon kupon) { // sprawdź, czy nagroda nie została już wypłacona

    }

    private Kwota obliczWysokośćWygranej() {

    }

    private Kwota odbliczPodatekOdWygranej() {

    }

    // obsługa finansowa
    private void przekażPieniądze(Kwota kwota, int zakłady, int losowania) {
        // podatek przekazujemy do budżetu państwa a zysk do centrali
        Kwota podatek = new Kwota(0, 60);
        podatek.pomnóż(zakłady);
        podatek.pomnóż(losowania);

        budżetPaństwa.dodajPodatek(podatek);

        Kwota zysk = new Kwota(kwota.getZłote(), kwota.getGrosze());
        zysk.odejmij(podatek);
        centrala.dodajZysk(zysk);
    }

    private Kwota pobierzŚrodkiNaWygraną(Kwota kwota) {

    }

    // pomocnicze
    public int getNrKolektury() {
        return nrKolektury;
    }

    private int[] generujLosoweLiczby() {
        int[] liczby = new int[LICZBY_W_ZAKŁADZIE];
        Set<Integer> wylosowane = new HashSet<>();

        int i = 0;
        while (i < LICZBY_W_ZAKŁADZIE) {
            int losowa = random.nextInt(49) + 1; // liczby od 1 do 49

            if (wylosowane.add(losowa)) {
                liczby[i] = losowa;
                i++;
            }
        }

        // posortuj liczby dla lepszej czytelności
        Arrays.sort(liczby);
        return liczby;
    }

}
