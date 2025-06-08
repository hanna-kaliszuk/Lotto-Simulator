package totolotek.kupony;

import wyjątki.*;

import java.util.ArrayList;
import java.util.List;

public class Blankiet {
    private final int ileLosowań;
    private final List<Zakład> zakłady;

    public Blankiet(List<Zakład> zakłady, int losowania) throws NieprawidłoweDaneZakładu, NieprawidłoweDaneLosowania {
        if (zakłady == null || zakłady.isEmpty()) {
            throw new NieprawidłoweDaneZakładu("Lista zakładów nie może być pusta");
        }

        if (losowania < 1 || losowania > 10) {
            throw new NieprawidłoweDaneLosowania("Liczba losowań musi być w zakresie od 1 do 10");
        }

        this.zakłady = zakłady;
        this.ileLosowań = losowania;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < zakłady.size(); i++) {
            sb.append(String.format("%d\n", i));
            sb.append(zakłady.get(i));
        }

        sb.append("Liczba losowań: ");
        for (int i = 1; i <= 10; i++) {
            sb.append(" [ ");
            if (i == ileLosowań) {
                sb.append("--");
            } else {
                sb.append(String.format("%2d", i));
            }
            sb.append(" ] ");
        }

        return sb.toString();
    }

    public int ileWażnychZakładów() {
        int licznik = 0;

        for (Zakład z : zakłady) {
            if (z.jestWażny() && !z.czyAnulowany()) {
                licznik++;
            }
        }

        return licznik;
    }

    public int getIleLosowań() {
        return ileLosowań;
    }

    public List<Zakład> getZakłady() throws NieprawidłoweDaneZakładu {
        List<Zakład> kopia = new ArrayList<>(zakłady.size());

        for (Zakład z : zakłady) {
            kopia.add(new Zakład(z.getLiczby()));
        }

        return kopia;
    }
}