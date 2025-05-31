package centrala;

import centrala.losowanie.Losowanie;
import finanse.BudżetPaństwa;
import finanse.Kwota;
import kolektura.Kolektura;
import kupony.Kupon;
import kupony.Zakład;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Centrala {
    private static Centrala instancja;
    private Kwota środkiFinansowe;
    private Kwota zysk;
    private List<Kolektura> listaKolektur;
    private int nrOstatniegoLosowania = 0;
    private static BudżetPaństwa budżetPaństwa;
    private Kwota kumulacjaStopniaI;
    private List<Losowanie> historiaLosowań;

    private Centrala(Kwota kwota, List<Kolektura> kolektury) {
        this.środkiFinansowe = kwota;
        this.listaKolektur = kolektury;
        this.kumulacjaStopniaI = new Kwota(0, 0);
        this.zysk = new Kwota(0, 0);
        this.historiaLosowań = new LinkedList<>();

        budżetPaństwa = BudżetPaństwa.getInstancja();

    }

    public static void inicjalizuj(Kwota kwota, List<Kolektura> kolektury) {
        if (instancja == null) {
            instancja = new Centrala(kwota, kolektury);
        }
    }

    public static Centrala getInstancja() {
        if (instancja == null) {
            throw new IllegalStateException("Nie zainicjalizowano centrala");
        }

        return instancja;
    }


    public void przeprowadźLosowanie() {
        // 1. inkrementujemy nr losowania
        nrOstatniegoLosowania++;
        int nrLosowania = nrOstatniegoLosowania;

        // 2. generujemy nowe losowanie
        Losowanie losowanie = new Losowanie(nrLosowania);

        // 3. zbieramy wszystkie zakłady uczestniczące w losowaniu
        List<Zakład> uczestnicząceZakłady = new LinkedList<>();
        for (Kolektura kolektura : listaKolektur) {
            for (Kupon kupon : kolektura.getSprzedaneKupony()) {
                if (kupon.zawieraLosowanie(nrLosowania)) {
                    uczestnicząceZakłady.addAll(kupon.getZakłady());
                }
            }
        }

        // 4. obliczamy przychód i podatek
        Kwota przychódNetto = new Kwota(uczestnicząceZakłady.size() * 3L, 0);
        przychódNetto.pomnóż(0.8); // 20% podatku

        // 5. obliczamy pulę nagród (51% przychodu netto)
        Kwota pulaNagród = new Kwota(przychódNetto);
        pulaNagród.pomnóż(0.51);
        Kwota zysk = new Kwota(przychódNetto);
        zysk.odejmij(pulaNagród);
        this.dodajZysk(zysk); // zysk centrali

        // 6. obliczamy ile trafień poszczególnej liczby liczb
        Map<Integer, Integer> trafienia = new HashMap<>();
        for (Zakład zakład : uczestnicząceZakłady) {
            int traf = zakład.ileTrafień(losowanie.getWylosowaneLiczby());
            trafienia.put(traf, trafienia.getOrDefault(traf, 0) + 1);
        }

        // 7. obliczamy wysokości nagród dla poszczególnych stopni
        this.obliczNagrody(losowanie, pulaNagród, trafienia);

        // 8. dodajemy losowanie do historii losowań
        // do podania:
        // zwycięska szóstka,
        // pula nagród I stopnia
        // kwoty wygrane w każdym stopniu (tylko trafione)

    }

    public int getNrNastępnegoLosowania() {
        return this.nrOstatniegoLosowania + 1;
    }

    public int ileKolektur() {
        return listaKolektur.size();
    }

    public Kolektura getKolektura(int indeks) {
        return listaKolektur.get(indeks);
    }

    private void obliczNagrody(Losowanie losowanie, Kwota pula, Map<Integer, Integer> trafienia) {
        // 44% puli to nagrody I stopnia (min 2 000 000 zł)
        // 8% puli to nagrody II stopnia
        //   24zł to nagrody IV stopnia
        // reszta to nagrody III stopnia

        int wygraneI = trafienia.getOrDefault(6, 0);
        int wygraneII = trafienia.getOrDefault(5, 0);
        int wygraneIII = trafienia.getOrDefault(4, 0);
        int wygraneIV = trafienia.getOrDefault(3, 0)

        // obliczamy podstawowe pule nagród (44%, 8%)
        Kwota pulaI = new Kwota(pula);
        pulaI.pomnóż(0.44); // 44% na I stopień

        Kwota pulaII = new Kwota(pula);
        pulaII.pomnóż(0.08); // 8% na II stopień

        // obliczamy pulę IV stopnia
        Kwota pulaIV = new Kwota(wygraneIV * 24, 0); // 24 zł za każde trafienie IV stopnia

        // obliczamy pulę nagród III stopnia ( = pula - pozostałe stopnie)
        Kwota pulaIII = new Kwota(pula);
        pulaIII.odejmij(pulaI);
        pulaIII.odejmij(pulaII);
        pulaIII.odejmij(pulaIV);

        // sprawdzamy, czy pula I osiągnęła kwotę gwarantowaną
        pulaI = obsłużPulęI(pulaI, wygraneI);

        // sprawdzamy, czy pula III gwarantuje każdemu min. 36 zł wygranej
        pulaIII = obsłużPulęIII(pulaIII, wygraneIII);
    }

    private void dodajZysk(Kwota kwota) {
        this.zysk.dodaj(kwota);
    }

}
