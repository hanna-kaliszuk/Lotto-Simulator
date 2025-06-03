package kolektura;

import centrala.Centrala;
import finanse.BudżetPaństwa;
import finanse.Kwota;
import gracze.Gracz;
import kupony.Blankiet;
import kupony.Kupon;
import kupony.Zakład;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Kolektura {
    private List<Kupon> sprzedaneKupony;
    private static int następnyNumer = 1;
    private final int nrKolektury;
    private static final Centrala centrala = Centrala.getInstancja();
    private static final BudżetPaństwa budżetPaństwa = BudżetPaństwa.getInstancja();
    private static final Random random = new Random();

    public Kolektura() {
        this.nrKolektury = następnyNumer++;
        centrala.zarejestrujKolekturę(this);
        this.sprzedaneKupony = new LinkedList<>();
    }

    public void sprzedajKuponZBlankietu(Blankiet blankiet, Gracz gracz) {
        int najbliższeLosowanie = centrala.getNrNastępnegoLosowania();

        Kwota cena = blankiet.getCena();

        if (gracz.jestWypłacalny(cena)) {
            gracz.zapłać(cena);
        } else {
            throw new IllegalArgumentException("Gracz nie ma wystarczających środków na zakup kuponu.");
        }

        Kupon kupon = new Kupon();
        sprzedaneKupony.add(kupon);

        this.przekażŚrodki(cena);
        gracz.odbierzKupon(kupon);
    }

    public void sprzedajKuponChybiłTrafił(int liczbaZakładów, int liczbaLosowań, Gracz gracz) {
        Kwota cena = new Kwota(3, 0);
        cena.pomnóż(liczbaZakładów);
        cena.pomnóż(liczbaLosowań);

        if (gracz.jestWypłacalny(cena)) {
            gracz.zapłać(cena);
        } else {
            throw new IllegalArgumentException("Gracz nie ma wystarczających środków na zakup kuponu.");
        }

        List<Zakład> zakłady = new ArrayList<>(liczbaZakładów);

        for (int i = 0; i < liczbaZakładów; i++) {
            zakłady.add(new Zakład(generujLosoweLiczby()));
        }

        int najbliższeLosowanie = centrala.getNrNastępnegoLosowania();

        Kupon kupon = new Kupon();
        sprzedaneKupony.add(kupon);

        this.przekażŚrodki(cena);
        gracz.odbierzKupon(kupon);
    }



    public List<Kupon> getSprzedaneKupony() {
        return sprzedaneKupony;
    }
}