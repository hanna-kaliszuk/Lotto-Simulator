package main.java.główny;

import java.util.ArrayList;

import main.java.totolotek.centrala.Centrala;
import main.java.totolotek.finanse.BudżetPaństwa;
import main.java.totolotek.finanse.Kwota;
import main.java.totolotek.gracze.Stałoliczbowy;
import main.java.totolotek.kolektura.Kolektura;
import main.java.wyjątki.BrakŚrodków;
import main.java.wyjątki.MożliwaPróbaOszustwa;
import main.java.wyjątki.NieprawidłoweDane;

// do przedstawienia szczegółowego działania systemu dla jednego gracza i jednego losowania, z początkowymi ujemnymi
// środkami w centrali, aby pokazać działanie subwencji
public class Main {
    public static void main(String[] args) throws NieprawidłoweDane, BrakŚrodków {
        System.out.println("======== PREZENTACJA DZIAŁANIA SYSTEMU ========");

        // reset i inicjalizacja systemu
        Centrala.resetInstancji();
        BudżetPaństwa.resetInstancji();
        Kolektura.resetujNumerację();

        // inicjalizacja Centrali z kapitałem początkowym
        Centrala.inicjalizujCentralę(new Kwota(-10_000, 0));
        Centrala centrala = Centrala.getInstancja();

        System.out.println("~~~ LOSOWANIE NR 1 ~~~");
        System.out.println("Gracze kupują kupony...");

        // utworzenie kolektury
        Kolektura kolektura = new Kolektura();
        ArrayList<Kolektura> kolektury = new ArrayList<>();
        kolektury.add(kolektura);

        // utworzenie gracza i zakup kuponu
        Stałoliczbowy gracz = new Stałoliczbowy("Jan", "Kowalski", 123456789,
                new Kwota(50, 0), new int[]{1, 2, 3, 4, 5, 6}, kolektury);

        System.out.println("Stan gracza przed zakupem: " + gracz.getŚrodkiFinansowe());
        System.out.println("Stan Centrali przed zakupem: " + centrala.sprawozdanieFinansowe());

        // gracz kupuje kupon
        gracz.kupKupon();

        System.out.println("Stan gracza po zakupie: " + gracz.getŚrodkiFinansowe());
        System.out.println("Stan Centrali po zakupie: " + centrala.sprawozdanieFinansowe());

        // przeprowadzenie losowania
        System.out.println("\nPrzeprowadzono losowanie nr 1");
        centrala.przeprowadźLosowanie();

        // wyświetlenie wyników losowania
        centrala.wypiszLosowania();

        System.out.println("Gracze sprawdzają wygrane...");

        // gracz sprawdza i odbiera nagrodę
        try {
            System.out.println("Stan gracza przed odebraniem nagrody: " + gracz.getŚrodkiFinansowe());
            System.out.println("Stan Centrali przed wypłatą nagrody: " + centrala.sprawozdanieFinansowe());

            gracz.odbierzNagrodę(gracz.getKupony(0), kolektura);

            System.out.println("Stan gracza po odebraniu nagrody: " + gracz.getŚrodkiFinansowe());
            System.out.println("Stan Centrali po wypłacie nagrody: " + centrala.sprawozdanieFinansowe());
        } catch (MożliwaPróbaOszustwa e) {
            System.out.println("Gracz próbował oszukać: " + e.getMessage());
            e.printStackTrace();
        } catch (NieprawidłoweDane e) {
            System.out.println("Błąd przy odbieraniu nagrody: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println(BudżetPaństwa.getInstancja().raport());
    }
}