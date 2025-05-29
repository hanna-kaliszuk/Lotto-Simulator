package kupony;

import java.util.ArrayList;
import java.util.List;

public class Blankiet {
    private static final int MAX_ZAKŁADY = 8;

    private boolean[] liczbaLosowań;
    private List<Zakład> zakłady;

    public Blankiet() {
        this.zakłady = new ArrayList<>();
        this.liczbaLosowań = new boolean[10];
    }

    public void dodajZakład(Zakład zakład) {
        if (zakłady.size() < MAX_ZAKŁADY) {
            if (zakład.ważny())
                zakłady.add(zakład);
        }
    }

    public void zaznaczLiczbęLosowań(int liczba) {
        if (liczba >= 1 && liczba <= 10) {
            liczbaLosowań[liczba - 1] = true;
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
            if (!liczbaLosowań[i - 1]) {
                sb.append(String.format("%2d", i));
            } else {
                sb.append("--");
            }
            sb.append(" ] ");
        }

        return sb.toString();
    }


}
