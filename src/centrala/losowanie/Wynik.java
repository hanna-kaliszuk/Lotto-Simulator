package centrala.losowanie;

import finanse.Kwota;

import java.util.Arrays;

public class Wynik {
    private final Losowanie losowanie;
    private final Kwota[] wygraneKwoty;
    private final Kwota[] puleNagród;
    private final int[] trafioneZakłady;

    public Wynik(Losowanie losowanie, Kwota[] wygraneKwoty, Kwota[] puleNagród, int[] trafioneZakłady) {
        this.losowanie = losowanie;
        this.wygraneKwoty = wygraneKwoty;
        this.puleNagród = puleNagród;
        this.trafioneZakłady = trafioneZakłady;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Wyniki losowania nr ").append(losowanie.getNrLosowania()).append(":\n");

        sb.append("Wylosowane liczby: ");
        sb.append(Arrays.toString(losowanie.getWylosowaneLiczby())).append("\n");

        sb.append("Pula nagród I stopnia: ").append(puleNagród[0]).append("\n");

        sb.append("Kwoty wygrane:\n");
        for (int i = 0; i < wygraneKwoty.length; i++) {
            if (wygraneKwoty[i].porównaj(new Kwota(0, 0)) > 0) {
                sb.append("Stopień ").append(i + 1).append(": ").append(wygraneKwoty[i]).append("\n");
            }
        }

        sb.append("Liczba trafionych zakładów:\n");
        for (int i = 0; i < trafioneZakłady.length; i++) {
            sb.append("Stopień ").append(i + 1).append(": ").append(trafioneZakłady[i]).append("\n");
        }

        sb.append("Pule nagród:\n");
        for (int i = 0; i < puleNagród.length; i++) {
            sb.append("Stopień ").append(i + 1).append(": ").append(puleNagród[i]).append("\n");
        }

        return sb.toString();
    }

    public void podajWynik() {
        System.out.println(this);
    }


    public int[] getWylosowaneLiczby() {
        return losowanie.getWylosowaneLiczby();
    }

    public int getNrLosowania() {
        return losowanie.getNrLosowania();
    }

    public Kwota getNagrodaZaTrafienia(int trafienia) {
        if (trafienia < 0 || trafienia > 6) {
            throw new IllegalArgumentException("Trafienia muszą być w zakresie od 0 do 6");
        }

        int index = trafienia - 3; // indeksowanie od 0
        if (index < 0 || index >= wygraneKwoty.length) {
            throw new IllegalArgumentException("Nieprawidłowa liczba trafień: " + trafienia);
        }

        Kwota pula = puleNagród[index];
        Kwota wygrana = new Kwota(pula);
        wygrana.podziel(trafioneZakłady[index]);
        return wygrana;
    }
}
