package kolektura;

import centrala.Centrala;
import centrala.losowanie.Wynik;
import finanse.BudżetPaństwa;
import finanse.Kwota;
import gracze.Gracz;
import kupony.Blankiet;
import kupony.Kupon;
import kupony.Zakład;

import java.util.*;

public class Kolektura {
    private List<Kupon> sprzedaneKupony;
    private static int następnyNumer = 1;
    private final int nrKolektury;
    private static final Random random = new Random();

    public Kolektura() {
        this.nrKolektury = następnyNumer++;
        Centrala.getInstancja().zarejestrujKolekturę(this);
        this.sprzedaneKupony = new LinkedList<>();
    }

    public static void resetujNumerację() {
        następnyNumer = 1;
    }

    public void sprzedajKuponZBlankietu(Blankiet blankiet, Gracz gracz) {
        int najbliższeLosowanie = Centrala.getInstancja().getNrNastępnegoLosowania();

        Kwota cena = obliczCenęKuponu(blankiet);

        if (gracz.jestWypłacalny(cena)) {
            gracz.zapłać(cena);
        } else {
            throw new IllegalArgumentException("Gracz nie ma wystarczających środków na zakup kuponu.");
        }

        Kupon kupon = new Kupon(this, blankiet.getZakłady(), blankiet.getIleLosowań(), najbliższeLosowanie);
        sprzedaneKupony.add(kupon);

        this.przekażŚrodki(cena);
        gracz.odbierzKupon(kupon);
    }

    private Kwota obliczCenęKuponu(Blankiet blankiet) {
        int zakłady = blankiet.ileWażnychZakładów();
        int losowania = blankiet.getIleLosowań();
        Kwota cena = new Kwota(3, 0);
        cena.pomnóż(zakłady);
        cena.pomnóż(losowania);
        return cena;
    }

    public void sprzedajKuponChybiłTrafił(int liczbaZakładów, int liczbaLosowań, Gracz gracz) {
        Kwota cena = new Kwota(3, 0);
        cena.pomnóż(liczbaZakładów);
        cena.pomnóż(liczbaLosowań);

        if (gracz.jestWypłacalny(cena)) {
            gracz.zapłać(cena);
        } else {
            throw new IllegalArgumentException("Gracz nie ma wystarczających środków na zakup kuponu.");
        }

        List<Zakład> zakłady = new ArrayList<>(liczbaZakładów);

        for (int i = 0; i < liczbaZakładów; i++) {
            zakłady.add(new Zakład(generujLosoweLiczby()));
        }

        int najbliższeLosowanie = Centrala.getInstancja().getNrNastępnegoLosowania();

        Kupon kupon = new Kupon(this, zakłady, liczbaLosowań, najbliższeLosowanie);
        sprzedaneKupony.add(kupon);

        this.przekażŚrodki(cena);
        gracz.odbierzKupon(kupon);
    }

    private int[] generujLosoweLiczby() {
        int[] liczby = new int[6];
        Set<Integer> wylosowane = new HashSet<>();

        int i = 0;
        while (i < 6) {
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

    private void przekażŚrodki(Kwota kwota) {
        Kwota podatek = new Kwota(kwota);
        podatek.pomnóż(0.20); // 20% podatek
        BudżetPaństwa.getInstancja().dodajPodatek(podatek);

        Kwota doCentali = new Kwota(kwota);
        doCentali.pomnóż(0.80); // 80% do centrali
        Centrala.getInstancja().dodajŚrodki(doCentali);
    }


    public List<Kupon> getSprzedaneKupony() {
        return sprzedaneKupony;
    }

    public int getNrKolektury() {
        return nrKolektury;
    }

    public int getAktualneLosowanie() {
        return Centrala.getInstancja().getNrNastępnegoLosowania();
    }

    public void wydajNagrodę(Gracz gracz, Kupon kupon) {
        if (!sprzedaneKupony.contains(kupon)) {
            throw new IllegalArgumentException("Kupon nie został sprzedany przez tę kolekturę.");
        }

        if (kupon.czyZrealizowany()) {
            throw new IllegalStateException("Kupon został już zrealizowany.");
        }

        // gracz oddaje kupon - oznaczamy go jako zrealizowany
        gracz.oddajKupon(kupon);
        kupon.zrealizuj();

        // zmienna do przechowywania łącznej kwoty, którą należy wypłacić graczowi
        Kwota łącznaWygrana = new Kwota(0, 0);
        int ostatnieLosowanie = getAktualneLosowanie() - 1;
        Kwota prógNagrodyWysokiej = new Kwota(2280, 0);

        List<Integer> losowaniaKuponu = kupon.getNrLosowań();

        for (int nrLosowania : losowaniaKuponu) {
            if (nrLosowania <= ostatnieLosowanie) {
                Wynik wynik = Centrala.getInstancja().getWynikLosowania(nrLosowania);

                for (Zakład zakład : kupon.getZakłady()) {
                    if (!zakład.czyAnulowany() && zakład.jestWażny()) {
                        int trafienia = zakład.sprawdźTrafienia(wynik.getWylosowaneLiczby());

                        if (trafienia >= 3) {
                            Kwota nagrodaZaZakład = obliczNagrodę(trafienia, wynik);
                            łącznaWygrana.dodaj(nagrodaZaZakład);

                            // sprawdzamy, czy to najwyższa nagroda do tej pory
                            if (nagrodaZaZakład.porównaj(prógNagrodyWysokiej) >= 0) {
                                Kwota poOpodatkowaniu = this.odprowadźPodatekZaNagrodę(nagrodaZaZakład);
                                łącznaWygrana.dodaj(poOpodatkowaniu);
                            }
                        }
                    }
                }
            }
        }

        if (łącznaWygrana.kwotaNieujemna()) {
            Centrala.getInstancja().wydajNagrodę(łącznaWygrana);
            gracz.odbierzŚrodki(łącznaWygrana);
        }

    }

    private Kwota obliczNagrodę(int trafienia, Wynik wynik) {
        Kwota nagroda = new Kwota(0, 0);

        if (trafienia < 3) {
            return nagroda; // brak nagrody za mniej niż 3 trafienia
        }

        return wynik.getNagrodaZaTrafienia(trafienia);
    }

    private Kwota odprowadźPodatekZaNagrodę(Kwota nagrodaZaZakład) {
        Kwota podatek = new Kwota(nagrodaZaZakład);
        podatek.pomnóż(0.10); // 10% podatek od nagrody
        BudżetPaństwa.getInstancja().dodajPodatek(podatek);
        Kwota poOpodatkowaniu = new Kwota(nagrodaZaZakład);
        poOpodatkowaniu.odejmij(podatek);
        return poOpodatkowaniu;
    }
}