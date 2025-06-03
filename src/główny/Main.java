package główny;

import centrala.Centrala;
import finanse.Kwota;

public class Main {
    public static void main(String[] args) {
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
        Centrala Centrala = centrala.Centrala.getInstancja();
    }
}