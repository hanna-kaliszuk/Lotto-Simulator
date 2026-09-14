package main.java.totolotek.centrala;

import java.util.LinkedList;
import java.util.List;

import main.java.totolotek.centrala.losowanie.*;
import main.java.totolotek.finanse.BudżetPaństwa;
import main.java.totolotek.finanse.Kwota;
import main.java.totolotek.kolektura.Kolektura;
import main.java.wyjątki.NieprawidłoweDane;

public class Centrala {
    private static Centrala instancja;

    private Kwota środki;
    private Kwota kumulacja;

    private List<Wynik> historiaLosowań;
    private List<Kolektura> listaKolektur;

    private int nrOstatniegoLosowania;

    private Centrala(Kwota kwota) throws NieprawidłoweDane {
        this.środki = kwota;
        this.kumulacja = new Kwota(0, 0);
        this.historiaLosowań = new LinkedList<>();
        this.listaKolektur = new LinkedList<>();
        this.nrOstatniegoLosowania = 0;
    }

    public static void resetInstancji() {
        instancja = null;
    }

    public static void inicjalizujCentralę(Kwota k) throws NieprawidłoweDane {
        if (instancja == null) {
            instancja = new Centrala(k);
        }
    }

    public static Centrala getInstancja() {
        if (instancja == null) {
            throw new IllegalStateException("Nie zainicjalizowano centrali");
        }

        return instancja;
    }

    public void przeprowadźLosowanie() throws NieprawidłoweDane {
        // 1. inkrementujemy nr losowania
        nrOstatniegoLosowania++;
        int nrLosowania = nrOstatniegoLosowania;

        // 2. generujemy nowe losowanie
        Losowanie losowanie = new Losowanie(nrLosowania);

        // 3. zbieramy wszystkie kupony uczestniczące w losowaniu
        losowanie.zbierzKupony(listaKolektur);

        // 4. obliczamy liczbę zakładów i sprawdzamy, czy są jakieś biorące udział w losowaniu
        int ileZakładów = losowanie.ileZakładów();
        if (ileZakładów == 0) {
            // zapisujemy pusty wynik
            Kwota[] pustePule = {new Kwota(0, 0), new Kwota(0, 0), new Kwota(0, 0), new Kwota(0, 0)};
            Kwota[] pusteWygrane = {new Kwota(0, 0), new Kwota(0, 0), new Kwota(0, 0), new Kwota(0, 0)};
            int[] pusteTrafienia = {0, 0, 0, 0};

            Wynik wynik = new Wynik(losowanie, pusteWygrane, pustePule, pusteTrafienia);
            historiaLosowań.add(wynik);
            return;
        }

        // 5. obliczamy liczbę trafień dla każdego stopnia
        int zI = losowanie.getTrafienia(1);
        int zII = losowanie.getTrafienia(2);
        int zIII = losowanie.getTrafienia(3);
        int zIV = losowanie.getTrafienia(4);

        int[] trafioneZakłady = {zI, zII, zIII, zIV};

        // 6. obliczamy pulę bazową
        Kwota pulaBazowa = this.obliczPulęBazową(losowanie);

        // 7. obliczamy pule dla poszczególnych stopni
        Kwota pI = this.obliczPulęI(pulaBazowa);
        Kwota pII = this.obliczPulęII(pulaBazowa);
        Kwota pIV = this.obliczPulęIV(losowanie);

        // 8. obliczamy całkowitą pulę
        Kwota pula = this.obliczPulę(pulaBazowa);

        // 9. obliczamy pulę III stopnia (reszta)
        Kwota pIII = this.obliczPulęIII(pula, pI, pII, pIV);

        // 10. zapewniamy próg gwarantowany dla I stopnia
        Kwota gwarantowanaI = new Kwota(2_000_000, 0);
        if (pI.porównaj(gwarantowanaI) < 0) pI = gwarantowanaI;

        // 11. zapewniamy próg gwarantowany dla III stopnia
        Kwota gwarantowanaIII = new Kwota(36 * zIII, 0);
        if (pIII.porównaj(gwarantowanaIII) < 0) pIII = gwarantowanaIII;

        Kwota[] pule = {pI, pII, pIII, pIV};

        // 13. obliczamy wygrane każdego stopnia
        Kwota wI = new Kwota(0, 0);
        if (zI == 0) {
            Kwota nowaKumulacja = new Kwota(pI);
            nowaKumulacja.odejmij(kumulacja);
            kumulacja = new Kwota(pI);
        } else {
            kumulacja = new Kwota(0, 0);
            wI = new Kwota(pI);
            wI.podziel(zI);
        }

        Kwota wII = new Kwota(0, 0);
        if (zII > 0) {
            wII = new Kwota(pII);
            wII.podziel(zII);
        }

        Kwota wIII = new Kwota(0, 0);
        if (zIII > 0) {
            wIII = new Kwota(pIII);
            wIII.podziel(zIII);
        }

        Kwota wIV = new Kwota(0, 0);
        if (zIV > 0) {
            wIV = new Kwota(pIV);
            wIV.podziel(zIV);
        }

        Kwota[] wygrane = {wI, wII, wIII, wIV};

        // 14. dodajemy wynik losowania do historii
        Wynik wynik = new Wynik(losowanie, wygrane, pule, trafioneZakłady);
        historiaLosowań.add(wynik);
    }

    private Kwota obliczPulęBazową(Losowanie losowanie) throws NieprawidłoweDane {
        int ileZakładów = losowanie.ileZakładów();

        Kwota pula = new Kwota(ileZakładów * 3, 0);
        pula.pomnóż(0.8); // bo 20% to podatek

        Kwota zysk = this.obliczZysk(pula);
        pula.odejmij(zysk);

        return pula;
    }

    private Kwota obliczZysk(Kwota pula) throws NieprawidłoweDane {
        Kwota zysk = new Kwota(pula);
        zysk.pomnóż(0.49);
        return zysk;
    }

    private Kwota obliczPulę(Kwota p) throws NieprawidłoweDane {
        Kwota pula = new Kwota(p);
        pula.dodaj(kumulacja);
        return pula;
    }

    private Kwota obliczPulęI(Kwota pb) throws NieprawidłoweDane {
        Kwota pI = new Kwota(pb);
        pI.pomnóż(0.44);
        pI.dodaj(kumulacja);
        return pI;
    }

    private Kwota obliczPulęII(Kwota pb) throws NieprawidłoweDane {
        Kwota pII = new Kwota(pb);
        pII.pomnóż(0.08);
        return pII;
    }

    private Kwota obliczPulęIII(Kwota pula, Kwota pI, Kwota pII, Kwota pIV) throws NieprawidłoweDane {
        Kwota pIII = new Kwota(pula);
        pIII.odejmij(pI);
        pIII.odejmij(pII);
        pIII.odejmij(pIV);
        return pIII;
    }

    private Kwota obliczPulęIV(Losowanie losowanie) throws NieprawidłoweDane {
        int ileTrafień = losowanie.getTrafienia(4);
        return new Kwota(24 * ileTrafień, 0);
    }

    public void wypiszLosowania() {
        for (Wynik w : historiaLosowań) {
            w.podajWynik();
        }
    }

    public String sprawozdanieFinansowe() {
        return "Centrala posiada obecnie: " + środki;
    }

    public void zarejestrujKolekturę(Kolektura k) {
        listaKolektur.add(k);
    }

    public void wydajNagrodę(Kwota nagroda) throws NieprawidłoweDane {
        Kwota zero = new Kwota(0, 0);
        if (nagroda.porównaj(zero) == 0) {
            return;
        }

        if (środki.porównaj(nagroda) >= 0) {
            środki.odejmij(nagroda);
        } else {
            Kwota różnica = new Kwota(nagroda);
            różnica.odejmij(środki);
            środki = new Kwota(0, 0);
            BudżetPaństwa.getInstancja().wydajSubwencję(różnica);
        }
    }

    public int getNrNastępnegoLosowania() {
        return nrOstatniegoLosowania + 1;
    }

    public void dodajŚrodki(Kwota doCentali) throws NieprawidłoweDane {
        if (doCentali.kwotaNieujemna()) {
            this.środki.dodaj(doCentali);
        } else {
            throw new NieprawidłoweDane("Kwota do dodania musi być nieujemna.");
        }
    }

    public Wynik getWynikLosowania(int nrLosowania) throws NieprawidłoweDane {
        if (nrLosowania < 1 || nrLosowania > nrOstatniegoLosowania) {
            throw new NieprawidłoweDane("Nieprawidłowy numer losowania: " + nrLosowania);
        }

        // binary search do znalezienia odpowiedniego losowania
        int lewy = 0;
        int prawy = historiaLosowań.size() - 1;

        while (lewy <= prawy) {
            int środek = (lewy + prawy) / 2;
            Wynik wynik = historiaLosowań.get(środek);

            if (wynik.getNrLosowania() == nrLosowania) {
                return wynik;
            } else if (wynik.getNrLosowania() < nrLosowania) {
                lewy = środek + 1;
            } else {
                prawy = środek - 1;
            }
        }

        throw new NieprawidłoweDane("Nie znaleziono wyniku dla losowania nr: " + nrLosowania);
    }

    // do testowania
    public int ileKolektur() {
        return listaKolektur.size();
    }
}