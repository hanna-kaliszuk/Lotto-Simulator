package totolotek.finanse;

import wyjątki.NieprawidłoweDane;

public class BudżetPaństwa {
    private static BudżetPaństwa instancja;
    private Kwota pobranePodatki;
    private Kwota wydaneSubwencje;

    private BudżetPaństwa() throws NieprawidłoweDane {
        this.pobranePodatki = new Kwota(0, 0);
        this.wydaneSubwencje = new Kwota(0, 0);
    }

    public static BudżetPaństwa getInstancja() throws NieprawidłoweDane {
        if (instancja == null) {
            instancja = new BudżetPaństwa();
        }

        return instancja;
    }

    public void dodajPodatek(Kwota kwota) {
        pobranePodatki.dodaj(kwota);
    }

    public void wydajSubwencję(Kwota kwota) {
        wydaneSubwencje.dodaj(kwota);
    }

    public String raport() {
        String sb = "Pobrane podatki: " + pobranePodatki + "\n" +
                "Wydane subwencje: " + wydaneSubwencje + "\n";
        return sb;
    }

    @Override
    public String toString() {
        return "BudżetPaństwa{" +
                "pobranePodatki=" + pobranePodatki +
                ", wydaneSubwencje=" + wydaneSubwencje +
                '}';
    }

    public void resetujBudżet() throws NieprawidłoweDane {
        this.pobranePodatki = new Kwota(0, 0);
        this.wydaneSubwencje = new Kwota(0, 0);
    }

    public static void resetInstancji() {
        instancja = null;
    }
}
