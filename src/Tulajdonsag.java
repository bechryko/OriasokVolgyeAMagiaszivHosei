/**
 * A hősök tulajdonságait megvalósító osztály.
 */

public class Tulajdonsag {
    public final String nev;
    private int szint = 1;
    public final String leiras;

    public Tulajdonsag(String nev, String leiras) {
        this.nev = nev;
        this.leiras = leiras;
    }

    public void fejleszt(int db) {
        this.szint += db;
    }

    public int getSzint() {
        return szint;
    }
}
