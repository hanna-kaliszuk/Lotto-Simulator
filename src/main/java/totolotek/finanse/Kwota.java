package main.java.totolotek.finanse;

import main.java.wyjątki.NieprawidłoweDane;

public class Kwota {
    private long grosze;

    public Kwota(long złote, int grosze) throws NieprawidłoweDane {
        if (grosze < 0 || grosze >= 100) {
            throw new NieprawidłoweDane("Grosze muszą być z zakresu 0-99. Podano: " + grosze);
        }

        this.grosze = 100 * złote + grosze;
    }

    public Kwota(Kwota k) throws NieprawidłoweDane {
        this(k.getZłote(), k.getGrosze());
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

    public int porównaj(Kwota inna) {
        return Long.compare(this.grosze, inna.grosze);
    }

    public boolean kwotaNieujemna() throws NieprawidłoweDane {
        Kwota zero = new Kwota(0, 0);
        return this.porównaj(zero) >= 0;
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

    public void podziel(int dzielnik) {
        if (dzielnik == 0) {
            throw new IllegalArgumentException("Nie można dzielić przez zero");
        }

        this.grosze = this.grosze / dzielnik;
    }

    public Kwota getPodatek() throws NieprawidłoweDane {
        Kwota podatek = new Kwota(this);
        podatek.pomnóż(0.20);
        return podatek;
    }

    @Override
    public String toString() {
        long złote = getZłote();
        int grosze = getGrosze();
        return String.format("%d zł %02d gr", złote, grosze);
    }
}