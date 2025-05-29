package gracze;

import finanse.Kwota;
import kupony.Zakład;

import java.util.Random;

public class Losowy extends Gracz {
    public Losowy(String imię, String nazwisko, int pesel){
        super(imię, nazwisko, pesel, generujLosoweŚrodki());
    }

    private static Kwota generujLosoweŚrodki() {
        int złote = random.nextInt(100000);
        int grosze = random.nextInt(100);

        return new Kwota(złote, grosze);
    }


    @Override
    public void kupKupon() {
        int liczbaKuponów = random.nextInt(100) + 1;


    }
}
