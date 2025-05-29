package centrala;

import centrala.losowanie.Losowanie;
import finanse.Kwota;
import kolektura.Kolektura;
import java.util.List;


public class Centrala {
    private Kwota środkiFinansowe;
    private List<Kolektura> listaKolektur;
    private int nrOstatniegoLosowania = 0;

    public Centrala(Kwota kwota, List<Kolektura> kolektury) {
        this.środkiFinansowe = kwota;
        this.listaKolektur = kolektury;
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

}
