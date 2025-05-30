package finanse;

public class BudżetPaństwa {
    private static BudżetPaństwa instancja;
    private Kwota pobranePodatki;
    private Kwota wydaneSubwencje;

    private BudżetPaństwa() {
        this.pobranePodatki = new Kwota(0, 0);
        this.wydaneSubwencje = new Kwota(0, 0);
    }

    public static BudżetPaństwa getInstancja() {
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
        return "Podatki: " + pobranePodatki + "\nSubwencje: " + wydaneSubwencje;
    }
}
