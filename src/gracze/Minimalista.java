package gracze;

import finanse.BudżetPaństwa;
import finanse.Kwota;
import kolektura.Kolektura;
import kupony.Kupon;
import kupony.Zakład;

public class Minimalista extends Gracz {
    private Kolektura ulubionaKolektura;

    public Minimalista(String imię, String nazwisko, int pesel, Kwota środki, Kolektura ulubiona) {
        super(imię, nazwisko, pesel, środki);
        this.ulubionaKolektura = ulubiona;
    }

    @Override
    public void kupKupon() {
        // sprawdzamy, czy stać go na zakup kuponu
        Kwota cena = new Kwota(3, 0);
        if (!maWystarczająceŚrodki(cena)) return;

        // jeżeli tak, to pobieramy zapłatę i wydajemy kupon
        this.odejmijŚrodki(cena);
        Kupon kupon = ulubionaKolektura.sprzedajKuponChybiłTrafił(1, 1);
        dodajKupon(kupon);
    }
}
