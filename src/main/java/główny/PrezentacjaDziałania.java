package main.java.główny;

import java.util.*;

import main.java.totolotek.centrala.Centrala;
import main.java.totolotek.centrala.losowanie.Wynik;
import main.java.totolotek.finanse.BudżetPaństwa;
import main.java.totolotek.finanse.Kwota;
import main.java.totolotek.gracze.*;
import main.java.totolotek.kolektura.Kolektura;
import main.java.totolotek.kupony.Blankiet;
import main.java.totolotek.kupony.Kupon;
import main.java.totolotek.kupony.Zakład;
import main.java.wyjątki.BrakŚrodków;
import main.java.wyjątki.NieprawidłoweDane;

public class PrezentacjaDziałania {
    private static final Random random = new Random();
    private static final int LICZBA_KOLEKTUR = 10;
    private static final int LICZBA_GRACZY_KAŻDEGO_TYPU = 200;
    private static final int LICZBA_LOSOWAŃ = 20;

    private PrezentacjaDziałania() {
        // prywatny konstruktor, aby nie można było tworzyć instancji tej klasy
}
    public static void main(String[] args) {
        try {
            Kwota kwotaPoczątkowa = new Kwota(-1000000, 0);
            prezentujDziałanie(kwotaPoczątkowa);
        } catch (Exception e) {
            System.err.println("Wystąpił błąd: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void prezentujDziałanie(Kwota kwota) throws NieprawidłoweDane {
        // inicjalizacja systemów
        inicjalizujSystemy(kwota);

        // utworzenie 10 kolektur
        List<Kolektura> kolektury = utwórzKolektury();

        // utworzenie 200 graczy każdego rodzaju
        // przydzielenie graczy do kolektur
        List<Gracz> gracze = stwórzGraczy(kolektury);

        przeprowadźSymulację(kolektury, gracze);

        wypiszPodsumowanie();
    }

    private static void inicjalizujSystemy(Kwota kwota) throws NieprawidłoweDane {
        System.out.println("======== PREZENTACJA DZIAŁANIA SYSTEMU ========");

        resetujSystemy();
        Centrala.inicjalizujCentralę(kwota);
    }

    private static void resetujSystemy() {
        Centrala.resetInstancji();
        Kolektura.resetujNumerację();
        BudżetPaństwa.resetInstancji();
    }

    private static List<Kolektura> utwórzKolektury() {
        List<Kolektura> kolektury = new ArrayList<>(LICZBA_KOLEKTUR);

        for (int i = 0; i < LICZBA_KOLEKTUR; i++) {
            kolektury.add(new Kolektura());
        }

        return kolektury;
    }

    private static List<Gracz> stwórzGraczy(List<Kolektura> kolektury) throws NieprawidłoweDane {
        List<Gracz> gracze = new LinkedList<>(); // bo są 4 typy graczy
        gracze.addAll(stwórzGraczyMinimalistów(kolektury));
        gracze.addAll(stwórzGraczyLosowych(kolektury));
        gracze.addAll(stwórzGraczyStałoliczbowych(kolektury));
        gracze.addAll(stwórzGraczyStałoblankietowych(kolektury));
        return gracze;
    }

    private static List<Minimalista> stwórzGraczyMinimalistów(List<Kolektura> kolektury) throws NieprawidłoweDane {
        List<Minimalista> minimaliści = new ArrayList<>(LICZBA_GRACZY_KAŻDEGO_TYPU);

        for (int i = 0; i < LICZBA_GRACZY_KAŻDEGO_TYPU; i++) {
            Kolektura ulubiona = kolektury.get(i % 10);
            Minimalista minimalista = new Minimalista("Minimalista" + i, "Nazwisko" + i,
                    10000000 + i, new Kwota(1000, 0), ulubiona);
            minimaliści.add(minimalista);
        }

        return minimaliści;
    }

    private static List<Losowy> stwórzGraczyLosowych(List<Kolektura> kolektury) throws NieprawidłoweDane {
        List<Losowy> losowi = new ArrayList<>(LICZBA_GRACZY_KAŻDEGO_TYPU);

        for (int i = 0; i < LICZBA_GRACZY_KAŻDEGO_TYPU; i++) {
            Losowy losowy = new Losowy("Losowy" + i, "Nazwisko" + i,
                    20000000 + i, new ArrayList<>(kolektury));
            losowi.add(losowy);
        }

        return losowi;
    }

    private static List<Gracz> stwórzGraczyStałoliczbowych(List<Kolektura> kolektury) throws NieprawidłoweDane {
        List<Gracz> stałoliczbowi = new ArrayList<>();

        for (int i = 0; i < LICZBA_GRACZY_KAŻDEGO_TYPU; i++) {
            ArrayList<Kolektura> ulubioneKolektury = generujKolekturyZRotacją(kolektury, i, 1, 4);
            int[] ulubioneLiczby = generujUlubioneLiczby(i);

            Gracz stałoliczbowy = new Stałoliczbowy(
                    "Stałoliczbowy" + i,
                    "Nazwisko" + i,
                    300000000 + i,
                    new Kwota(2000, 0),
                    ulubioneLiczby,
                    ulubioneKolektury
            );
            stałoliczbowi.add(stałoliczbowy);
        }

        return stałoliczbowi;
    }

    private static List<Gracz> stwórzGraczyStałoblankietowych(List<Kolektura> kolektury) throws NieprawidłoweDane {
        List<Gracz> stałoblankietowi = new ArrayList<>();

        for (int i = 0; i < LICZBA_GRACZY_KAŻDEGO_TYPU; i++) {
            ArrayList<Kolektura> ulubioneKolektury = generujKolekturyZRotacją(kolektury, i, 1, 3, 2);
            Blankiet blankiet = stwórzBlankiet(i);

            Gracz stałoblankietowy = new Stałoblankietowy(
                    "Stałoblankietowy" + i,
                    "Nazwisko" + i,
                    400000000 + i,
                    new Kwota(1500, 0),
                    blankiet,
                    2,
                    ulubioneKolektury
            );
            stałoblankietowi.add(stałoblankietowy);
        }

        return stałoblankietowi;
    }

    private static ArrayList<Kolektura> generujKolekturyZRotacją(List<Kolektura> kolektury,
                                                                 int indeksGracza, int min, int max) {
        return generujKolekturyZRotacją(kolektury, indeksGracza, min, max, 1);
    }

    private static ArrayList<Kolektura> generujKolekturyZRotacją(List<Kolektura> kolektury,
                                                                 int indeksGracza, int min, int max, int krok) {
        ArrayList<Kolektura> ulubioneKolektury = new ArrayList<>();
        int ileUlubionych = random.nextInt(max - min + 1) + min;

        for (int j = 0; j < ileUlubionych; j++) {
            int indeksKolektury = (indeksGracza + j * krok) % LICZBA_KOLEKTUR;
            Kolektura kolektura = kolektury.get(indeksKolektury);
            if (!ulubioneKolektury.contains(kolektura)) {
                ulubioneKolektury.add(kolektura);
            }
        }

        return ulubioneKolektury;
    }

    private static int[] generujUlubioneLiczby(int indeks) {
        return new int[]{
                1 + (indeks % 6),
                7 + (indeks % 6),
                13 + (indeks % 6),
                19 + (indeks % 6),
                25 + (indeks % 6),
                31 + (indeks % 6)
        };
    }

    private static Blankiet stwórzBlankiet(int indeks) throws NieprawidłoweDane {
        List<Zakład> zakłady = List.of(
                new Zakład(generujUlubioneLiczby(indeks))
        );
        return new Blankiet(zakłady, 1);
    }

    private static void przeprowadźSymulację(List<Kolektura> kolektury, List<Gracz> gracze) throws NieprawidłoweDane {
        Centrala centrala = Centrala.getInstancja();

        for (int nrLosowania = 1; nrLosowania <= LICZBA_LOSOWAŃ; nrLosowania++) {
            System.out.println("~~~ LOSOWANIE NR " + nrLosowania + " ~~~");

            pozwólGraczomKupowaćKupony(gracze);
            przeprowadźLosowanie(centrala, nrLosowania);
            pozwólGraczomOdbieraćNagrody(gracze, kolektury, centrala, nrLosowania);
        }
    }

    private static void pozwólGraczomKupowaćKupony(List<Gracz> wszyscyGracze) {
        System.out.println("Gracze kupują kupony...");

        for (Gracz gracz : wszyscyGracze) {
            try {
                gracz.kupKupon();
            } catch (BrakŚrodków e) {
                continue;
            } catch (NieprawidłoweDane e) {
                System.err.println("Błąd przy kupowaniu kuponu: " + e.getMessage());
            }
        }
    }

    private static void przeprowadźLosowanie(Centrala centrala, int numerLosowania) throws NieprawidłoweDane {
        centrala.przeprowadźLosowanie();
        System.out.println("Przeprowadzono losowanie nr " + numerLosowania);
    }

    private static void pozwólGraczomOdbieraćNagrody(List<Gracz> wszyscyGracze, List<Kolektura> kolektury,
                                                   Centrala centrala, int aktualnyNumerLosowania) {
        System.out.println("Gracze sprawdzają wygrane...");

        for (Gracz gracz : wszyscyGracze) {
            List<Kupon> kuponyDoSprawdzenia = znajdźKuponyDoSprawdzenia(gracz);
            sprawdźIOdbierzNagrodę(gracz, kuponyDoSprawdzenia, kolektury, centrala, aktualnyNumerLosowania);
        }
    }

    private static List<Kupon> znajdźKuponyDoSprawdzenia(Gracz gracz) {
        List<Kupon> kuponyDoSprawdzenia = new ArrayList<>();

        for (int j = 0; j < gracz.ileKuponów(); j++) {
            Kupon kupon = gracz.getKupony(j);
            if (kupon.czyWziąłUdziałWeWszystkichLosowaniach() && !kupon.czyZrealizowany()) {
                kuponyDoSprawdzenia.add(kupon);
            }
        }

        return kuponyDoSprawdzenia;
    }

    private static void sprawdźIOdbierzNagrodę(Gracz gracz, List<Kupon> kuponyDoSprawdzenia, List<Kolektura> kolektury,
                                              Centrala centrala, int aktualnyNumerLosowania) {

        for (Kupon kupon : kuponyDoSprawdzenia) {
            try {
                if (sprawdźCzyKuponWygrał(kupon, centrala, aktualnyNumerLosowania)) {
                    Kolektura kolektura = znajdźKolekturęKuponu(kupon, kolektury);
                    if (kolektura != null) {
                        gracz.odbierzNagrodę(kupon, kolektura);
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
    }

    private static void wypiszPodsumowanie() throws NieprawidłoweDane {
        System.out.println("\n=== PODSUMOWANIE KOŃCOWE ===\n");

        wypiszHistorieLosowań();
        wypiszBudżetPaństwa();
        wypiszStanFinansowyCentrali();
    }

    private static void wypiszHistorieLosowań() {
        System.out.println("HISTORIA LOSOWAŃ:");
        Centrala.getInstancja().wypiszLosowania();
        System.out.println();
    }

    private static void wypiszBudżetPaństwa() throws NieprawidłoweDane {
        BudżetPaństwa budżet = BudżetPaństwa.getInstancja();
        System.out.println(budżet);
        System.out.println();
    }

    private static void wypiszStanFinansowyCentrali() {
        System.out.println("STAN FINANSOWY CENTRALI:");
        System.out.println(Centrala.getInstancja().sprawozdanieFinansowe());
    }

    private static boolean sprawdźCzyKuponWygrał(Kupon kupon, Centrala centrala, int aktualnyNumerLosowania) {
        try {
            List<Integer> numerLosowańKuponu = kupon.getNrLosowań();

            for (int nrLosowania : numerLosowańKuponu) {
                if (nrLosowania <= aktualnyNumerLosowania) {
                    Wynik wynik = centrala.getWynikLosowania(nrLosowania);
                    int[] wylosowaneLiczby = wynik.getWylosowaneLiczby();

                    for (Zakład zakład : kupon.getZakłady()) {
                        if (!zakład.czyAnulowany() && zakład.jestWażny()) {
                            int trafienia = zakład.sprawdźTrafienia(wylosowaneLiczby);
                            if (trafienia >= 3) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // W przypadku błędu zakładamy brak wygranej
        }
        return false;
    }

    private static Kolektura znajdźKolekturęKuponu(Kupon kupon, List<Kolektura> kolektury) {
        for (Kolektura kolektura : kolektury) {
            if (kolektura.getSprzedaneKupony().contains(kupon)) {
                return kolektura;
            }
        }
        return null;
    }
}