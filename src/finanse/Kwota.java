package finanse;

public class Kwota {
    private long grosze;

    public Kwota(long złote, int grosze) {
        this.grosze = 100 * złote + grosze;
    }

    public Kwota dodaj(Kwota inna) {
        return new Kwota(0, (int) (this.grosze + inna.grosze));
    }

    public Kwota odejmij(Kwota inna) {
        return new Kwota(0, (int) (this.grosze - inna.grosze));
    }

    public Kwota pomnóż(double mnożnik) {
        return new Kwota(0, (int) (grosze * mnożnik));
    }

    public int getGrosze() {
        return (int) grosze % 100;
    }

    public long getZłote() {
        return grosze / 100;
    }

    @Override
    public String toString() {
        return String.format("%d zł %02d gr", getZłote(), getGrosze());
    }
}
