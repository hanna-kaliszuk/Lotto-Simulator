package gracze;

import centrala.Centrala;
import finanse.Kwota;
import kupony.Kupon;
import kupony.Zakład;

import java.util.*;

public abstract class Gracz {
    protected String imię;
    protected String nazwisko;
    protected int pesel;
    protected Kwota środki;
    protected List<Kupon> posiadaneKupony;
    protected Centrala centrala;
    protected static final Random random = new Random();

    public Gracz(String imię, String nazwisko, int pesel, Kwota środki) {
        this.imię = imię;
        this.nazwisko = nazwisko;
        this.pesel = pesel;
        this.środki = środki;
        this.posiadaneKupony = new LinkedList<>(); // początkowo gracz nie ma żadnych kuponów
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // informacje podstawowe o graczu
        sb.append(imię).append(" ").append(nazwisko).append(" (PESEL: ").append(pesel).append(")\n");

        // informacje o środkach
        sb.append("Środki: ").append(środki.toString()).append("\n");

        // informacje o kuponach
        if (posiadaneKupony.isEmpty()) {
            sb.append("Nie posiada żadnych kuponów.");
        } else {
            sb.append("Posiadane kupony:\n");
            for (Kupon kupon : posiadaneKupony) {
                sb.append(" - ").append(kupon.getIdentyfikator());

                // informacja o wykorzystaniu kuponu
                if (kupon.czyWykorzystany()) {
                    sb.append(" (zrealizowany)");
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public abstract void kupKupon();

    public abstract Zakład obstawZakład();

    protected void dodajKupon(Kupon kupon) {
        posiadaneKupony.add(kupon);
    }

    protected boolean możeKupićKupon(Kwota cenaKuponu) {
        return maWystarczająceŚrodki(cenaKuponu);
    }

    private boolean maWystarczająceŚrodki(Kwota cena) {
        return środki.porównaj(cena) >= 0;
    }

    protected void odejmijŚrodki(Kwota kwota) {
        this.środki.odejmij(kwota);
    }

    protected int[] generujLosoweLiczby(int liczba) {
        int[] liczby = new int[liczba];
        Set<Integer> wylosowane = new HashSet<>();

        int i = 0;
        while (i < liczba) {
            int losowa = random.nextInt(49) + 1; // liczby od 1 do 49

            if (wylosowane.add(losowa)) {
                liczby[i] = losowa;
                i++;
            }
        }

        // posortuj liczby dla lepszej czytelności
        Arrays.sort(liczby);
        return liczby;

    }


}
