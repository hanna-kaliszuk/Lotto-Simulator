package kupony;

import java.util.ArrayList;
import java.util.List;

public class Blankiet {
    private int ileLosowań;
    private List<Zakład> zakłady;

    public Blankiet(List<Zakład> zakłady, int losowania) {
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
            if (i - 1 == ileLosowań) {
                sb.append(String.format("%2d", i));
            } else {
                sb.append("--");
            }
            sb.append(" ] ");
        }

        return sb.toString();
    }

    public List<Zakład> getListaZakładów() {
        List<Zakład> kopia = new ArrayList<>(zakłady.size());

        for (Zakład z : zakłady) {
            kopia.add(z.kopia());
        }

        return kopia;
    }

    public int naIleLosowań() {
        return ileLosowań;
    }

    public int ileZakładów() {
        return zakłady.size();
    }
}
