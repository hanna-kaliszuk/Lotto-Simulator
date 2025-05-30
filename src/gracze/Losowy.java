package gracze;

import finanse.Kwota;
import kolektura.Kolektura;
import kupony.Kupon;
import kupony.Zakład;

import java.util.ArrayList;
import java.util.List;

public class Losowy extends Gracz {
    public Losowy(String imię, String nazwisko, int pesel){
        super(imię, nazwisko, pesel, generujLosoweŚrodki());
    }

    @Override
    public void kupKupon() {
        int liczbaKuponów = random.nextInt(100) + 1;

        for (int i = 0; i < liczbaKuponów; i++) {
            // wybieramy losową kolekturę
            Kolektura kolektura = this.wybierzKolekturę();

            // ile zakładów obstawi tym razem (od 1 do 8)
            int liczbaZakładów = random.nextInt(8) + 1;

            // na ile losowań będzie kupon (od 1 do 10)
            int liczbaLosowań = random.nextInt(10) + 1;


            // sprawdź, czy go stać na ten konkretny kupon
            // jeżeli nie, przejdź do generowania kolejnego, losowego kuponu
            Kwota cena = new Kwota(3, 0);
            cena.pomnóż(liczbaLosowań);
            cena.pomnóż(liczbaZakładów);
            if (!maWystarczająceŚrodki(cena)) continue;

            // jak tak, pobierz zapłatę i wydaj kupon
            this.odejmijŚrodki(cena);
            Kupon kupon = kolektura.sprzedajKuponChybiłTrafił(liczbaZakładów, liczbaLosowań);
            dodajKupon(kupon);
        }
    }

    private static Kwota generujLosoweŚrodki() {
        int złote = random.nextInt(100000);
        int grosze = random.nextInt(100);

        return new Kwota(złote, grosze);
    }

    private Kolektura wybierzKolekturę() {
        int indeksKolektury = random.nextInt(centrala.ileKolektur());
        return centrala.getKolektura(indeksKolektury);
    }
}
