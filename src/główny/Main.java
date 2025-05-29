package główny;

import centrala.losowanie.Losowanie;
import kupony.Kupon;
import kupony.Zakład;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static void testLosowania() {
        for (int i = 1; i < 2137; i++) {
            Losowanie l = new Losowanie();
            System.out.println(l);
        }

        Losowanie l = new Losowanie();
        System.out.println(l);
    }

    private static void testZakładów() {
        Zakład z = new Zakład(new int[] {1, 2, 3, 4, 5, 6});
        int[] wylosowane = new int[] {-2, -4, -6, -8, 10, 12};
        System.out.println(z.ileTrafień(wylosowane));
        System.out.println(z);
    }
    public static void main(String[] args) {
        System.out.printf("Hello and welcome!\n");
        //testLosowania();
        //testZakładów();
        Zakład z = new Zakład(new int[] {11, 12, 19, 23, 33, 43});
        Zakład z2 = new Zakład(new int[] {4, 15, 24, 33, 35, 44 });
        List<Zakład> zakłady = new ArrayList<>();
        zakłady.add(z);
        zakłady.add(z2);

        Kupon k = new Kupon(1, zakłady, 8, 4);
        System.out.println(k);
    }
}