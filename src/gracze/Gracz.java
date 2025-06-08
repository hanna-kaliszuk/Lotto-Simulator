package gracze;

import finanse.Kwota;
import kolektura.Kolektura;
import kupony.Kupon;

import java.util.LinkedList;
import java.util.List;

public abstract class Gracz {
    private final String imię;
    private final String nazwisko;
    private final int pesel;
    private Kwota środki;
    protected List<Kupon> zakupioneKupony;
    protected List<Kolektura> ulubioneKolektury;

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

    public abstract void kupKupon();

    public void odbierzNagrodę(Kupon kupon, Kolektura kolektura) {
        kolektura.wydajNagrodę(this, kupon);
    }

    public void odbierzŚrodki(Kwota łącznaWygrana) {
        if (łącznaWygrana != null) {
            this.środki.dodaj(łącznaWygrana);
        } else {
            throw new IllegalArgumentException("Łączna wygrana nie może być null.");
        }
    }

    public void oddajKupon(Kupon kupon) {
        if (zakupioneKupony.contains(kupon)) {
            zakupioneKupony.remove(kupon);
        } else {
            throw new IllegalArgumentException("Kupon nie należy do tego gracza.");
        }
    }
}