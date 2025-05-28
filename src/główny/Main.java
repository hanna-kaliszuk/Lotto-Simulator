package główny;

import centrala.losowanie.Losowanie;

public class Main {
    private static void testLosowania() {
        for (int i = 1; i < 6; i++) {
            Losowanie l = new Losowanie();
            System.out.println(l);
        }

        Losowanie l = new Losowanie();
        System.out.println(l);
    }
    public static void main(String[] args) {
        System.out.printf("Hello and welcome!\n");
        testLosowania();
    }
}