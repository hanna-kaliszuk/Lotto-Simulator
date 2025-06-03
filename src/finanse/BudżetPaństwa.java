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
        StringBuilder sb = new StringBuilder();
        sb.append("Pobrane podatki: ").append(pobranePodatki).append("\n");
        sb.append("Wydane subwencje: ").append(wydaneSubwencje).append("\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "BudżetPaństwa{" +
                "pobranePodatki=" + pobranePodatki +
                ", wydaneSubwencje=" + wydaneSubwencje +
                '}';
    }

    public void resetujBudżet() {
        this.pobranePodatki = new Kwota(0, 0);
        this.wydaneSubwencje = new Kwota(0, 0);
    }

    public static void resetInstancji() {
        instancja = null;
    }
}
