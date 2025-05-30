package finanse;

public class Kwota {
    private long grosze;

    public Kwota(long złote, int grosze) {
        if (grosze < 0 || grosze >= 100) {
            throw new IllegalArgumentException("Grosze muszą być z zakresu 0-99");
        }
        this.grosze = 100 * złote + grosze;
    }

    // Konstruktor pomocniczy dla operacji wewnętrznych
    private Kwota(long totalGrosze) {
        this.grosze = totalGrosze;
    }

    public void dodaj(Kwota inna) {
        this.grosze += inna.grosze;
    }

    public void odejmij(Kwota inna) {
        this.grosze -= inna.grosze;
    }

    public void pomnóż(double mnożnik) {
        this.grosze = Math.round(grosze * mnożnik);
    }

    public void pomnóż(int mnożnik) {
        this.grosze *= mnożnik;
    }

    public int getGrosze() {
        return (int) Math.abs(grosze % 100);
    }

    public long getZłote() {
        return grosze / 100;
    }

    // Metoda do sprawdzenia, czy kwota jest ujemna
    public boolean saldoUjemne() {
        return grosze < 0;
    }

    // Metoda do porównywania kwot
    public int porównaj(Kwota inna) {
        return Long.compare(this.grosze, inna.grosze);
    }

    // Metoda do sprawdzenia, czy kwota jest > 0
    public boolean kwotaNieujemna() {
        Kwota zero = new Kwota(0, 0);
        return this.porównaj(zero) > 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Kwota kwota = (Kwota) obj;
        return grosze == kwota.grosze;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(grosze);
    }

    @Override
    public String toString() {
        long złote = getZłote();
        int grosze = getGrosze();

        if (saldoUjemne() && złote == 0) {
            // Przypadek gdy mamy ujemne grosze ale złote to 0 (np. -50 groszy)
            return String.format("-%d zł %02d gr", Math.abs(złote), grosze);
        } else {
            return String.format("%d zł %02d gr", złote, grosze);
        }
    }
}