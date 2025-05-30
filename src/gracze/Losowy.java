package gracze;

import finanse.Kwota;
import kolektura.Kolektura;
import kupony.Kupon;
import kupony.Zakład;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Losowy extends Gracz {
    public Losowy(String imię, String nazwisko, int pesel){
        super(imię, nazwisko, pesel, generujLosoweŚrodki());
    }

    private static Kwota generujLosoweŚrodki() {
        int złote = random.nextInt(100000);
        int grosze = random.nextInt(100);

        return new Kwota(złote, grosze);
    }


    @Override
    public void kupKupon() {
        int liczbaKuponów = random.nextInt(100) + 1;

        for (int i = 0; i < liczbaKuponów; i++) {
            // wybieramy losową kolekturę
            Kolektura kolektura = this.wybierzKolekturę();

            // ile zakładów obstawi tym razem
            int liczbaZakładów = random.nextInt(8) + 1;

            // na ile losowań będzie kupon
            int liczbaLosowań = random.nextInt(10) + 1;

            // generujemy zakłady do kuponu i sam kupon
            List<Zakład> zakłady = new LinkedList<>();

            for (int k = 0; k < liczbaZakładów; k++) {
                Zakład z = new Zakład(generujLosoweLiczby(6));
                zakłady.add(z);
            }

            Kupon kupon = new Kupon(kolektura, zakłady, centrala.getNrNastępnegoLosowania(),
                    liczbaLosowań);


            // sprawdź, czy go stać na ten konkretny kupon
            if (!możeKupićKupon(kupon.getCena())) continue;

            // jak tak, wydaj graczowi kupon
            kolektura.sprzedajKupon(kupon);
            dodajKupon(kupon);
            this.odejmijŚrodki(kupon.getCena());
        }
    }

    private Kolektura wybierzKolekturę() {
        int indeksKolektury = random.nextInt(centrala.ileKolektur());
        return centrala.getKolektura(indeksKolektury);
    }


}
