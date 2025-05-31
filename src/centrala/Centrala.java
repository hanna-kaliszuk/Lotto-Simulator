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
        Kwota pula = this.obliczPulę(losowanie, kumulacja);

        // 5. dodajemy losowanie do historii
        historiaLosowań.add(losowanie);
    }

    private Kwota obliczPulę(Losowanie losowanie, Kwota kumulacja) {
        int ileZakładów = losowanie.ileZakładów();

        Kwota pula = new Kwota(ileZakładów * 3, 0);
        pula.pomnóż(0.8); // bo 20% to podatek

        Kwota zysk = this.obliczZysk(pula);
        pula.odejmij(zysk);

        pula.dodaj(new Kwota(kumulacja));

        return pula;
    }

    private Kwota obliczZysk(Kwota pula) {
        Kwota zysk = new Kwota(pula);
        zysk.pomnóż(0.49);
        return zysk;
    }
}