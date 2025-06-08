package główny;

import totolotek.kupony.Blankiet;
import totolotek.kupony.Zakład;
import wyjątki.NieprawidłoweDane;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws NieprawidłoweDane {
        Zakład zakład1 = new Zakład(new int[]{1, 5, 7, 45, 23, 28});
        Zakład zakład2 = new Zakład(new int[]{2, 4, 6, 8, 10, 12});
        zakład2.anuluj();
        List<Zakład> zakłady = Arrays.asList(zakład1, zakład2);

        Blankiet blankiet = new Blankiet(zakłady, 3);
        System.out.println(blankiet);
    }


}