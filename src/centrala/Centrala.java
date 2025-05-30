package centrala;

import centrala.losowanie.Losowanie;
import finanse.Kwota;
import kolektura.Kolektura;

import java.util.ArrayList;
import java.util.List;


public class Centrala {
    private static Centrala instancja;
    private Kwota środkiFinansowe;
    private List<Kolektura> listaKolektur;
    private int nrOstatniegoLosowania = 0;

    private Centrala(Kwota kwota, List<Kolektura> kolektury) {
        this.środkiFinansowe = kwota;
        this.listaKolektur = kolektury;
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
        Losowanie losowanie = new Losowanie();
        this.nrOstatniegoLosowania = losowanie.getNrLosowania();
    }

    public Kwota obliczPulęNagród() {

    }

    public int getNrNastępnegoLosowania() {
        return this.nrOstatniegoLosowania + 1;
    }

    public List<Kolektura> getListaKolektur() {
        List<Kolektura> kopia = new ArrayList<>(listaKolektur.size());

        for (Kolektura k : listaKolektur) {
            kopia.add(new Kolektura(k)); // pamiętaj, żeby dodać do kolektury odpowiedni konstruktor, ktróry bierze
            // (Kolektura) jako argument
        }
        return kopia;
    }

    public int ileKolektur() {
        return listaKolektur.size();
    }

    public Kolektura getKolektura(int indeks) {
        return listaKolektur.get(indeks);
    }
}
