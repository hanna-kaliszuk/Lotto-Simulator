package kolektura;

import centrala.Centrala;
import kupony.Kupon;

import java.util.Arrays;
import java.util.List;

public class Kolektura {
    private List<Kupon> sprzedaneKupony;
    private int nrKolektury;
    private static Centrala centrala = Centrala.getInstancja();

    public Kolektura(int nr) {
        this.nrKolektury = nr;
        centrala.zarejestrujKolekturę(this);
    }

    public List<Kupon> getSprzedaneKupony() {
        return sprzedaneKupony;
    }
}