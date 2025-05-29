package finanse;

public class BudżetPaństwa {
    private Kwota pobranePodatki;
    private Kwota wydaneSubwencje;

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
