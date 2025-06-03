package kupony;

import java.util.List;

public class Blankiet {
    private int ileLosowań;
    private List<Zakład> zakłady;

    public Blankiet(List<Zakład> zakłady, int losowania) {
        this.zakłady = zakłady;
        int ileLosowań = losowania;
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
            if (i - 1 == ileLosowań) {
                sb.append(String.format("%2d", i));
            } else {
                sb.append("--");
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
}