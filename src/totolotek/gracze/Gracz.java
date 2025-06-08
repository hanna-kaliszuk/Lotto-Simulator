package totolotek.gracze;

import totolotek.finanse.Kwota;
import totolotek.kolektura.Kolektura;
import totolotek.kupony.Kupon;
import wyjątki.BrakŚrodków;
import wyjątki.MożliwaPróbaOszustwa;
import wyjątki.NieprawidłoweDane;

import java.util.LinkedList;
import java.util.List;

public abstract class Gracz {
    private final String imię;
    private final String nazwisko;
    private final int pesel;
    private Kwota środki;
    protected List<Kupon> zakupioneKupony;
    protected List<Kolektura> ulubioneKolektury;

    public Gracz(String imię, String nazwisko, int pesel, Kwota środki) throws NieprawidłoweDane {
        if (imię == null) {
            throw new NieprawidłoweDane("Imię nie może być null.");
        }
        if (nazwisko == null) {
            throw new NieprawidłoweDane("Nazwisko nie może być null.");
        }
        if (pesel <= 0) {
            throw new NieprawidłoweDane("PESEL musi być dodatnią liczbą całkowitą.");
        }
        if (środki == null) {
            throw new NieprawidłoweDane("Środki finansowe nie mogą być null.");
        }

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
            sb.append("Posiadane totolotek.kupony:\n");

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

    public abstract void kupKupon() throws NieprawidłoweDane, BrakŚrodków;

    public void odbierzNagrodę(Kupon kupon, Kolektura kolektura) throws NieprawidłoweDane, MożliwaPróbaOszustwa {
        kolektura.wydajNagrodę(this, kupon);
    }

    public void odbierzŚrodki(Kwota łącznaWygrana) {
            this.środki.dodaj(łącznaWygrana);
    }

    public void oddajKupon(Kupon kupon) {
        if (zakupioneKupony.contains(kupon)) {
            zakupioneKupony.remove(kupon);
        } else {
            throw new IllegalArgumentException("Kupon nie należy do tego gracza.");
        }
    }

    // metody do testów

    public int ileKuponów() {
        return zakupioneKupony.size();
    }

    public Kupon getKupony(int nr) {
        if (nr < 0 || nr >= zakupioneKupony.size()) {
            throw new IndexOutOfBoundsException("Nieprawidłowy numer kuponu: " + nr);
        }
        return zakupioneKupony.get(nr);
    }

    public String getImię() {
        return imię;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public long getPesel() {
        return pesel;
    }

    public Kwota getŚrodkiFinansowe() throws NieprawidłoweDane {
        return new Kwota(środki);
    }

    public int ileUlubionych() {
        return ulubioneKolektury.size();
    }
}