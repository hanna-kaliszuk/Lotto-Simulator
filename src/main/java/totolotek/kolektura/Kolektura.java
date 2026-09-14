package main.java.totolotek.kolektura;

import java.util.*;

import main.java.totolotek.centrala.Centrala;
import main.java.totolotek.centrala.losowanie.Wynik;
import main.java.totolotek.finanse.*;
import main.java.totolotek.gracze.Gracz;
import main.java.totolotek.kupony.*;
import main.java.wyjątki.*;

public class Kolektura {
    private static final int MIN_ZAKŁADÓW = 1;
    private static final int MAX_ZAKŁADÓW = 8;
    private static final int MIN_LOSOWAŃ = 1;
    private static final int MAX_LOSOWAŃ = 10;
    private static final int LICZBA_LICZB_W_ZAKŁADZIE = 6;
    private static final int MAKSYMALNA_LICZBA = 49;
    private static final int CENA_ZAKŁADU_ZŁ = 3;
    private static final int CENA_ZAKŁADU_GR = 0;
    private static final double PROCENT_PODATKU = 0.20;
    private static final double PROCENT_DO_CENTRALI = 0.80;
    private static final double PROCENT_PODATKU_OD_NAGRODY = 0.10;
    private static final int PRÓG_NAGRODY_WYSOKIEJ_ZŁ = 2280;
    private static final int PRÓG_NAGRODY_WYSOKIEJ_GR = 0;

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

    public void sprzedajKuponZBlankietu(Blankiet blankiet, Gracz gracz) throws NieprawidłoweDane, BrakŚrodków {
        if (blankiet == null) throw new NieprawidłoweDane("Blankiet nie może być null");
        if (gracz == null) throw new NieprawidłoweDane("Gracz nie może być null");
        if (blankiet.ileWażnychZakładów() == 0) throw new NieprawidłoweDane("Blankiet nie zawiera żadnych " +
                "ważnych zakładów.");

        int najbliższeLosowanie = Centrala.getInstancja().getNrNastępnegoLosowania();
        Kwota cena = obliczCenęKuponu(blankiet);

        if (!gracz.jestWypłacalny(cena)) throw new BrakŚrodków("Gracz nie ma wystarczających środków " +
                "na zakup kuponu.");
        gracz.zapłać(cena);

        Kupon kupon = new Kupon(this, blankiet.getZakłady(), blankiet.getIleLosowań(),
                najbliższeLosowanie);
        sprzedaneKupony.add(kupon);

        przekażŚrodki(cena);
        gracz.odbierzKupon(kupon);
    }

    private Kwota obliczCenęKuponu(Blankiet blankiet) throws NieprawidłoweDane {
        int zakłady = blankiet.ileWażnychZakładów();
        int losowania = blankiet.getIleLosowań();

        Kwota cena = new Kwota(CENA_ZAKŁADU_ZŁ, CENA_ZAKŁADU_GR);
        cena.pomnóż(zakłady);
        cena.pomnóż(losowania);

        return cena;
    }

    public void sprzedajKuponChybiłTrafił(int liczbaZakładów, int liczbaLosowań, Gracz gracz) throws NieprawidłoweDane,
            BrakŚrodków {
        if (liczbaZakładów < MIN_ZAKŁADÓW || liczbaZakładów > MAX_ZAKŁADÓW)
            throw new NieprawidłoweDane("Liczba zakładów musi być w zakresie od 1 do 8. Podano: " + liczbaZakładów);
        if (liczbaLosowań < MIN_LOSOWAŃ || liczbaLosowań > MAX_LOSOWAŃ)
            throw new NieprawidłoweDane("Liczba losowań musi być w zakresie od 1 do 10. Podano: " + liczbaLosowań);
        if (gracz == null) throw new NieprawidłoweDane("Gracz nie może być null");

        Kwota cena = new Kwota(CENA_ZAKŁADU_ZŁ, CENA_ZAKŁADU_GR);
        cena.pomnóż(liczbaZakładów);
        cena.pomnóż(liczbaLosowań);

        if (!gracz.jestWypłacalny(cena)) throw new BrakŚrodków("Gracz nie ma wystarczających środków na " +
                "zakup kuponu.");
        gracz.zapłać(cena);

        List<Zakład> zakłady = new ArrayList<>(liczbaZakładów);
        for (int i = 0; i < liczbaZakładów; i++) {
            zakłady.add(new Zakład(generujLosoweLiczby()));
        }

        int najbliższeLosowanie = Centrala.getInstancja().getNrNastępnegoLosowania();
        Kupon kupon = new Kupon(this, zakłady, liczbaLosowań, najbliższeLosowanie);
        sprzedaneKupony.add(kupon);

        przekażŚrodki(cena);
        gracz.odbierzKupon(kupon);
    }

    private int[] generujLosoweLiczby() {
        int[] liczby = new int[LICZBA_LICZB_W_ZAKŁADZIE];

        Set<Integer> wylosowane = new HashSet<>();
        int i = 0;
        while (i < LICZBA_LICZB_W_ZAKŁADZIE) {
            int losowa = random.nextInt(MAKSYMALNA_LICZBA) + 1;
            if (wylosowane.add(losowa)) {
                liczby[i++] = losowa;
            }
        }
        Arrays.sort(liczby);

        return liczby;
    }

    private void przekażŚrodki(Kwota kwota) throws NieprawidłoweDane {
        Kwota podatek = new Kwota(kwota);
        podatek.pomnóż(PROCENT_PODATKU);
        BudżetPaństwa.getInstancja().dodajPodatek(podatek);

        Kwota doCentrali = new Kwota(kwota);
        doCentrali.pomnóż(PROCENT_DO_CENTRALI);
        Centrala.getInstancja().dodajŚrodki(doCentrali);
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

    public void wydajNagrodę(Gracz gracz, Kupon kupon) throws NieprawidłoweDane, MożliwaPróbaOszustwa {
        if (!sprzedaneKupony.contains(kupon))
            throw new MożliwaPróbaOszustwa("Kupon nie został sprzedany przez tę kolekturę.");
        if (kupon.czyZrealizowany())
            throw new MożliwaPróbaOszustwa("Kupon został już zrealizowany.");

        gracz.oddajKupon(kupon);

        Kwota łącznaWygrana = new Kwota(0, 0);
        int ostatnieLosowanie = getAktualneLosowanie() - 1;
        Kwota prógNagrodyWysokiej = new Kwota(PRÓG_NAGRODY_WYSOKIEJ_ZŁ, PRÓG_NAGRODY_WYSOKIEJ_GR);

        List<Integer> losowaniaKuponu = kupon.getNrLosowań();
        for (int nrLosowania : losowaniaKuponu) {
            if (nrLosowania <= ostatnieLosowanie) {
                Wynik wynik = Centrala.getInstancja().getWynikLosowania(nrLosowania);
                for (Zakład zakład : kupon.getZakłady()) {
                    if (!zakład.czyAnulowany() && zakład.jestWażny()) {
                        int trafienia = zakład.sprawdźTrafienia(wynik.getWylosowaneLiczby());
                        if (trafienia >= 3) {
                            Kwota nagrodaZaZakład = obliczNagrodę(trafienia, wynik);
                            if (nagrodaZaZakład.porównaj(prógNagrodyWysokiej) >= 0) {
                                Kwota poOpodatkowaniu = odprowadźPodatekZaNagrodę(nagrodaZaZakład);
                                łącznaWygrana.dodaj(poOpodatkowaniu);
                            } else {
                                łącznaWygrana.dodaj(nagrodaZaZakład);
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

        kupon.zrealizuj();
    }

    private Kwota obliczNagrodę(int trafienia, Wynik wynik) throws NieprawidłoweDane {
        if (trafienia < 3) return new Kwota(0, 0);

        return wynik.getNagrodaZaTrafienia(trafienia);
    }

    private Kwota odprowadźPodatekZaNagrodę(Kwota nagrodaZaZakład) throws NieprawidłoweDane {
        Kwota podatek = new Kwota(nagrodaZaZakład);
        podatek.pomnóż(PROCENT_PODATKU_OD_NAGRODY);
        BudżetPaństwa.getInstancja().dodajPodatek(podatek);

        Kwota poOpodatkowaniu = new Kwota(nagrodaZaZakład);
        poOpodatkowaniu.odejmij(podatek);
        return poOpodatkowaniu;
    }
}