package kupony;

import java.util.ArrayList;
import java.util.List;

public class Blankiet {
    private static final int MAX_ZAKŁADY = 8;

    private int ileLosowań;
    private List<Zakład> zakłady;

    public Blankiet(int losowania) {
        this.zakłady = new ArrayList<>();
        this.ileLosowań = losowania;
    }

    public void dodajZakład(Zakład zakład) {
        if (zakłady.size() < MAX_ZAKŁADY) {
            if (zakład.czyWażny())
                zakłady.add(zakład);
        }
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
}
