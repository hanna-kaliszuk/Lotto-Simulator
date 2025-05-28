package finanse.narzędzia;

public enum StopieńNagrody {
    I(6, 44.0), // 6 trafień, 44% puli
    II(5, 8.0), // 5 trafień, 8% puli
    III(4, 0), // 4 trafienia, pozostała część puli
    IV(3, 24_00); // 3 trafienia, stała nagroda 24 zł

    private final int wymaganieTrafienia;
    private final double procentPuli;

    StopieńNagrody(int trafienia, double procent) {
        this.wymaganieTrafienia = trafienia;
        this.procentPuli = procent;
    }
}
