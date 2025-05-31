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
        //   24zł to nagrody III stopnia
        // reszta to nagrody IV stopnia

        int wygraneI = trafienia.getOrDefault(6, 0);
        int wygraneII = trafienia.getOrDefault(5, 0);
        int wygraneIII = trafienia.getOrDefault(4, 0);
        int wygraneIV = trafienia.getOrDefault(3, 0)

        // stopień I
        Kwota stopieńI = obliczPierwszyStopień(pula, wygraneI);

        // stopień II
        Kwota stopieńII = obliczDrugiStopień(pula, wygraneII);

        // stopień III
        Kwota stopieńIII = obliczTrzeciStopień(wygraneIII);

        // stopień IV
        Kwota stopieńIV = obliczCzwartyStopień(pula, stopieńI, stopieńII, stopieńIII);
    }

    private Kwota obliczPierwszyStopień(Kwota pula, int ileTrafiło) {
        // 44% puli + ewentualne subwencje + ewentualne kumulacje
        Kwota gwarantowana = new Kwota(2000000, 0);
        Kwota nagroda = new Kwota(pula);
        nagroda.pomnóż(0.44);

        if (ileTrafiło == 0) {
            Kwota doKumulacji = new Kwota(nagroda);
            this.dodajKumulację(doKumulacji);
            return new Kwota(0, 0);
        } else {
            if (this.kumulacjaStopniaI.kwotaNieujemna()) {
                nagroda.dodaj(this.kumulacjaStopniaI);
                kumulacjaStopniaI = new Kwota(0, 0);
            }

            if (nagroda.porównaj(gwarantowana) < 0 ) { // jeżeli nagroda jest poniżej kwoty gwarantowanej
                Kwota różnica = new Kwota(gwarantowana);
                różnica.odejmij(nagroda); // tyle brakuje do 2 mln

                if (this.środkiFinansowe.porównaj(różnica) >= 0) { // jeżeli centrala ma własne środki, na pokrycie różnicy
                    this.środkiFinansowe.odejmij(różnica);
                    nagroda = new Kwota(gwarantowana);
                } else {
                    // koniecznie jest pobranie subwencji
                    Kwota doSubwencji = new Kwota(różnica);

                    // jeżeli centrala ma jakieś środki, to je wykorzystujemy w pierwszej kolejności
                    if (this.środkiFinansowe.kwotaNieujemna()) {
                        doSubwencji.odejmij(this.środkiFinansowe);
                        nagroda.dodaj(this.środkiFinansowe);
                        this.środkiFinansowe = new Kwota(0, 0);
                    }

                    // resztę pobieramy od państwa
                    budżetPaństwa.wydajSubwencję(doSubwencji);
                    nagroda = new Kwota(gwarantowana);
                }
            }

            return nagroda;
        }
    }

    private Kwota obliczDrugiStopień(Kwota pula, int ileTrafiło) {
        // 8% puli
        Kwota nagroda = new Kwota(pula);
        nagroda.pomnóż(0.08);

        if (ileTrafiło == 0) {
            Kwota doZysku = new Kwota(nagroda);
            this.dodajZysk(doZysku);
            return new Kwota(0, 0);
        } else {
            return nagroda;
        }
    }

    private void dodajZysk(Kwota kwota) {
        this.zysk.dodaj(kwota);
    }

    private void dodajKumulację(Kwota k) {
        this.kumulacjaStopniaI.dodaj(k);
    }
}
