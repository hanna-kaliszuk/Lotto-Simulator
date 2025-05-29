package gracze;

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
        // generuj losowy zakład
        Zakład zakład = this.obstawZakład();

        // stwórz kupon z 1 zakładem na najbliższe losowanie
        Kupon kupon = new Kupon(ulubionaKolektura, zakład, centrala.getNrNastępnegoLosowania(), 1);

        // sprawdź, czy gracz ma wystarczające środki na zakup kuponu
        if (!możeKupićKupon(kupon.getCena())) return;

        // jeżeli tak, to wydajemy mu kupon
        else {
            ulubionaKolektura.sprzedajKupon(kupon);
            dodajKupon(kupon);
            odejmijŚrodki(kupon.getCena());
        }

    }

    @Override
    public Zakład obstawZakład() {
        return new Zakład(generujLosoweLiczby(6));
    }
}
