package gracze;

import finanse.Kwota;
import kupony.Kupon;

import java.util.LinkedList;
import java.util.List;

public abstract class Gracz {
    private String imię;
    private String nazwisko;
    private int pesel;
    private Kwota środki;
    private List<Kupon> zakupioneKupony;

    public Gracz(String imię, String nazwisko, int pesel, Kwota środki) {
        this.imię = imię;
        this.nazwisko = nazwisko;
        this.pesel = pesel;
        this.środki = środki;
        this.zakupioneKupony = new LinkedList<>(); // początkowo gracz nie ma żadnych kuponów
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // informacje podstawowe o graczu
        sb.append(imię).append(" ").append(nazwisko).append(" (PESEL: ").append(pesel).append(")\n");

        // informacje o środkach
        sb.append("Środki: ").append(środki.toString()).append("\n");

        // informacje o kuponach
        if (zakupioneKupony.isEmpty()) {
            sb.append("Nie posiada żadnych kuponów.");
        } else {
            sb.append("Posiadane kupony:\n");

            for (Kupon kupon : zakupioneKupony) {
                sb.append(" - ").append(kupon.getIdentyfikator());
            }
        }

        return sb.toString();
    }


    public boolean jestWypłacalny(Kwota kwota) {
        return (this.środki.porównaj(kwota) >= 0);
    }


    public void zapłać(Kwota cena) {
        this.środki.odejmij(cena);
    }

    public void odbierzKupon(Kupon kupon) {
        zakupioneKupony.add(kupon);
    }
}