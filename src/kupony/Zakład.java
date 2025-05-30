package kupony;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Zakład {
    private int[] liczby;
    private boolean ważny;
    private boolean anulowany;

    public Zakład(int[] liczby) {
        if (liczby.length == 6) {
            this.liczby = Arrays.copyOf(liczby, liczby.length);
            Arrays.sort(this.liczby);
            this.ważny = true;
        } else {
            this.ważny = false;
            this.liczby = null;
        }

        this.anulowany = false;
    }

    public Zakład() {
    }

    public int ileTrafień(int[] wylosowane) {
        int trafienia = 0;
        for (int liczba : liczby) {
            if (Arrays.binarySearch(wylosowane, liczba) >= 0) {
                trafienia++;
            }
        }

        return trafienia;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Set<Integer> zaznaczoneLiczby = new HashSet<>();
        for (int liczba : liczby) {
            zaznaczoneLiczby.add(liczba);
        }
        
        // wypisujemy 1-49 w rzędach
        for (int wiersz = 0; wiersz < 5; wiersz++) {
            int startLiczby = wiersz * 10 + 1;
            int endLiczby = Math.min(startLiczby + 9, 49);
            
            for (int liczba = startLiczby; liczba <= endLiczby; liczba++) {
                sb.append(" [ ");
                
                if (zaznaczoneLiczby.contains(liczba)) {
                    sb.append("--");
                } else {
                    sb.append(String.format("%2d", liczba));
                }
                
                sb.append(" ] ");
            }
            
            if (wiersz < 4) {
                sb.append("\n");
            }
        }
        
        if (!anulowany) {
        sb.append("\n [    ] anuluj");
        } else {
            sb.append("\n [ -- ] anuluj");
        }
        
        return sb.toString();
    }

    public void anulujZakład() {
        this.anulowany = true;
    }

    public boolean czyWażny() {
        return ważny;
    }

    private boolean czyAnulowany() {
        return this.anulowany;
    }

    Zakład kopia() {
        Zakład kopia = new Zakład();
        kopia.liczby = this.liczby.clone();
        kopia.anulowany = this.czyAnulowany();
        kopia.ważny = this.czyWażny();

        return kopia;
    }
}