package kupony;

import java.util.List;

public class Blankiet {
    private int ileLosowań;
    private List<Zakład> zakłady;

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