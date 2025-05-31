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
        this.obliczPuleNagród(losowanie, pulaNagród, trafienia);

        // 8. aktualizujemy wygrane dla każdego zakładu, który miał >= 3 trafienia
        for (Kolektura kolektura : listaKolektur) {
            for (Kupon kupon : kolektura.getSprzedaneKupony()) {
                if (kupon.zawieraLosowanie(nrLosowania)) {
                    for (Zakład zakład : kupon.getZakłady()) {
                        int ileTrafił = zakład.ileTrafień(losowanie.getWylosowaneLiczby());

                        if (ileTrafił >= 3) {
                            int stopień = 0;
                            if (ileTrafił == 6) stopień = 1;      // I stopień
                            else if (ileTrafił == 5) stopień = 2; // II stopień
                            else if (ileTrafił == 4) stopień = 3; // III stopień
                            else if (ileTrafił == 3) stopień = 4; // IV stopień

                            kupon.dodajWygraną(nrLosowania, stopień);
                        }
                    }
                }
            }
        }


        // 9. dodajemy losowanie do historii losowań
        historiaLosowań.add(losowanie);
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

    private void obliczPuleNagród(Losowanie losowanie, Kwota pula, Map<Integer, Integer> trafienia) {
        // 44% puli to nagrody I stopnia (min 2 000 000 zł)
        // 8% puli to nagrody II stopnia
        //   24zł to nagrody IV stopnia
        // reszta to nagrody III stopnia

        int wygraneI = trafienia.getOrDefault(6, 0);
        int wygraneII = trafienia.getOrDefault(5, 0);
        int wygraneIII = trafienia.getOrDefault(4, 0);
        int wygraneIV = trafienia.getOrDefault(3, 0);

        // obliczamy podstawowe pule nagród (44%, 8%)
        Kwota pulaI = new Kwota(pula);
        pulaI.pomnóż(0.44); // 44% na I stopień

        Kwota pulaII = new Kwota(pula);
        pulaII.pomnóż(0.08); // 8% na II stopień

        // obliczamy pulę IV stopnia
        Kwota pulaIV = new Kwota(wygraneIV * 24, 0); // 24 zł za każde trafienie IV stopnia

        // sprawdzamy, ile potrzebujemy do III puli przynajmniej
        Kwota minPulaIII = new Kwota(wygraneIII * 36, 0);
        Kwota pozostała = obliczPozostałąPulę(pula, pulaI, pulaII, pulaIV);

        // oblicz stosunek minPulaIII do pozostałej, dostępnej kwoty
        Kwota pulaIII = zagospodarujPulęIII(minPulaIII, pozostała);

        Kwota doPobraniaOdPaństwa = this.obliczDeficyt(pula, pulaI, pulaII, pulaIII, pulaIV);

        if (doPobraniaOdPaństwa.kwotaNieujemna()) {
            budżetPaństwa.wydajSubwencję(doPobraniaOdPaństwa);
        }


    }

    private Kwota obliczPozostałąPulę(Kwota pula, Kwota pulaI, Kwota pulaII, Kwota pulaIV) {
        Kwota pozostała = new Kwota(pula);
        pozostała.odejmij(pulaI);
        pozostała.odejmij(pulaII);
        pozostała.odejmij(pulaIV);
        return pozostała;
    }

    private Kwota zagospodarujPulęIII(Kwota min, Kwota jest) {
        if (min.kwotaNieujemna()) { // jak potrzebuje więcej niż 0
            if (min.porównaj(jest) <= 0) { // jak potrzebuję <= kwotę niż mam dostępną, to korzystam z tego, co jest dostępne
                return new Kwota(jest);
            } else { // a jak potrzebuję więcej, to dostają po 36 zł
                return new Kwota(min);
            }
        } else {
            return new Kwota(0, 0);
        }
    }

    private Kwota obliczDeficyt(Kwota pula, Kwota stopieńI, Kwota stopieńII, Kwota stopieńIII, Kwota stopieńIV) {
        // sprawdzamy, czy pulaI >= 2_000_000
        Kwota gwarantowana = new Kwota(2_000_000, 0);
        Kwota potrzebna = new Kwota(0, 0);

        // sumujemy, ile pieniędzy potrzebujemy na opłacenie nagród każdego stopnia
        if (stopieńI.porównaj(gwarantowana) < 0) { // jeżeli w I puli jest mniej niż gwarantowane 2 mln, to musimy je zapewnić
            potrzebna.dodaj(gwarantowana);
        } else {
            potrzebna.dodaj(stopieńI);
        }

        potrzebna.dodaj(stopieńII);
        potrzebna.dodaj(stopieńIII);
        potrzebna.dodaj(stopieńIV);

        // sprawdzamy, czy tyle jest w puli
        if (pula.porównaj(potrzebna) >= 0) { // w puli jest wystarczająca kwota, więc nie ma deficytu
            return new Kwota(0, 0);
        }
        // obliczamy ile brakuje
        Kwota różnica = new Kwota(potrzebna);
        różnica.odejmij(pula);

        // sprawdzamy, czy środki centrali to pokryją
        if (this.środkiFinansowe.porównaj(różnica) >= 0) { // tak
            this.środkiFinansowe.odejmij(różnica);
            return new Kwota(0, 0);
        }

        if (this.środkiFinansowe.kwotaNieujemna()) { // jeżeli centrala ma jakiekolwiek środki, to z nich korzystamy
            różnica.dodaj(this.środkiFinansowe);
            środkiFinansowe = new Kwota(0, 0);
        }

        return różnica; // zwracamy tyle, ile brakuje
    }

    private void dodajZysk(Kwota kwota) {
        this.zysk.dodaj(kwota);
    }

    public Losowanie getLosowanie(int nr) {
        for (Losowanie losowanie : historiaLosowań) {
            if (losowanie.getNrLosowania() == nr) {
                return losowanie;
            }
        }

        return null;
    }
}
