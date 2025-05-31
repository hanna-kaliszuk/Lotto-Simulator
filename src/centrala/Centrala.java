package centrala;

import finanse.BudżetPaństwa;
import finanse.Kwota;

import java.util.LinkedList;

public class Centrala {
    private static Centrala instancja;

    private final static BudżetPaństwa budżetPaństwa = BudżetPaństwa.getInstancja();
    private Kwota środki;
    private Kwota kumulacja;
    private Kwota zysk;

    private List<Losowanie> historiaLosowań;
    private List<Kolektura> listaKolektur;

    private int nrOstatniegoLosowania;

    private Centrala(Kwota kwota) {
        this.środki = kwota;
        this.kumulacja = new Kwota(0, 0);
        this.zysk = new Kwota(0, 0);
        this.historiaLosowań = new LinkedList<Losowanie>();;
        this.listaKolektur = new LinkedList<Kolektura>();
        this.nrOstatniegoLosowania = 0;

    }

    public static void inicjalizujCentralę(Kwota k) {
        if (instancja == null) {
            instancja = new Centrala(k);
        }
    }

    public void przeprowadźLosowanie() {
        // 1. inkrementujemy nr losowania
        nrOstatniegoLosowania++;
        int nrLosowania = nrOstatniegoLosowania;

        // 2. generujemy nowe losowanie
        Losowanie losowanie = new Losowanie(nrLosowania);

        // 3. zbieramy wszystkie kupony uczestniczące w losowaniu
        losowanie.zbierzKupony(listaKolektur);

        // 4. obliczamy pulę nagród
        Kwota pulaBazowa = this.obliczPulęBazową(losowanie);
        Kwota pula = this.obliczPulę(pulaBazowa);

        // 5. obliczamy wysokości pul dla poszczególnych stopni
        Kwota pI = this.obliczPulęI(pulaBazowa);
        Kwota pII = this.obliczPulęII(pulaBazowa);
        Kwota pIV = this.obliczPulęIV(losowanie);
        Kwota pIII = this.obliczPulęIII(pula, pI, pII, pIV);

        // 6. zapewniamy próg gwarantowany dla I i III stopnia
        Kwota gwarantowanaI = new Kwota(2_000_000, 0);
        if (pI.porównaj(gwarantowanaI) < 0) pI = gwarantowanaI;

        Kwota gwarantowanaIII = new Kwota(36 * losowanie.getTrafienia(3),0);
        if (pIII.porównaj(gwarantowanaIII) < 0) pIII = gwarantowanaIII;

        //


        historiaLosowań.add(losowanie);
    }

    private Kwota obliczPulęBazową(Losowanie losowanie) {
        int ileZakładów = losowanie.ileZakładów();

        Kwota pula = new Kwota(ileZakładów * 3, 0);
        pula.pomnóż(0.8); // bo 20% to podatek

        Kwota zysk = this.obliczZysk(pula);
        pula.odejmij(zysk);

        return pula;
    }

    private Kwota obliczZysk(Kwota pula) {
        Kwota zysk = new Kwota(pula);
        zysk.pomnóż(0.49);
        return zysk;
    }

    private Kwota obliczPulę(Kwota p) {
        Kwota pula = new Kwota(p);
        pula.dodaj(kumulacja);
        return pula;
    }

    private Kwota obliczPulęI(Kwota pb) {
        Kwota pI = new Kwota(pb);
        pI.pomnóż(0.44);
        pI.dodaj(kumulacja);
        return pI;
    }

    private Kwota obliczPulęII(Kwota pb) {
        Kwota pII = new Kwota(pb);
        pII.pomnóż(0.08);
        return pII;
    }

    private Kwota obliczPulęIII(Kwota pula, Kwota pI, Kwota pII, Kwota pIV) {
        Kwota pIII = new Kwota(pula);
        pIII.odejmij(pI);
        pIII.odejmij(pII);
        pIII.odejmij(pIV);
        return pIII;
    }

    private Kwota obliczPulęIV(Losowanie losowanie) {
        int ileTrafień = losowanie.getTrafienia(4);
        return new Kwota(24 * ileTrafień, 0);
    }
}