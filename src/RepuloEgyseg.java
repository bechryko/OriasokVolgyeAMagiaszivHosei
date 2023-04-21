/**
 * A repülő egységeket jelző osztály.
 * Csak egy tulajdonság, inkább interface kellene, hogy legyen.
 */

public abstract class RepuloEgyseg extends Egyseg {
    public RepuloEgyseg(String nev, int ar, int minSebzes, int maxSebzes, int maxEletero, int sebesseg, int kezdemenyezes, Hos hos, String leiras) {
        super(nev, ar, minSebzes, maxSebzes, maxEletero, sebesseg, kezdemenyezes, hos, leiras);
    }
}
