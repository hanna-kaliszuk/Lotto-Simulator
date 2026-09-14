package main.java.totolotek.kupony;

import java.util.ArrayList;
import java.util.List;

import main.java.wyjątki.*;

public class Blankiet {
    private static final int MIN_LOSOWAŃ = 1;
    private static final int MAX_LOSOWAŃ = 10;

    private final int ileLosowań;
    private final List<Zakład> zakłady;

    public Blankiet(List<Zakład> zakłady, int losowania) throws NieprawidłoweDane {
        if (zakłady == null || zakłady.isEmpty()) {
            throw new NieprawidłoweDane("Lista zakładów nie może być pusta");
        }

        if (losowania < MIN_LOSOWAŃ || losowania > MAX_LOSOWAŃ) {
            throw new NieprawidłoweDane("Liczba losowań musi być w zakresie od 1 do 10. Podano: " + losowania);
        }

        this.zakłady = zakłady;
        this.ileLosowań = losowania;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < zakłady.size(); i++) {
            sb.append(String.format("%d\n", i + 1));
            sb.append(zakłady.get(i));
        }

        sb.append("Liczba losowań: ");
        for (int i = MIN_LOSOWAŃ; i <= MAX_LOSOWAŃ; i++) {
            sb.append(" [ ");
            sb.append(i == ileLosowań ? "--" : String.format("%2d", i));
            sb.append(" ] ");
        }

        return sb.toString();
    }

    public int ileWażnychZakładów() {
        int licznik = 0;

        for (Zakład z : zakłady) {
            if (z.jestWażny() && !z.czyAnulowany()) licznik++;
        }

        return licznik;
    }

    public int getIleLosowań() {
        return ileLosowań;
    }

    public List<Zakład> getZakłady() throws NieprawidłoweDane {
        List<Zakład> kopia = new ArrayList<>(zakłady.size());

        for (Zakład z : zakłady) {
            kopia.add(new Zakład(z.getLiczby()));
        }

        return kopia;
    }
}