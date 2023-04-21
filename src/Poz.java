import java.util.ArrayList;

/**
 * A játék mezőit és azok funkcióit megvalósító osztály.
 */

public class Poz {
    private int x;
    private int y;

    public final static int X_MAX = 11;
    public final static int Y_MAX = 9;

    //set jobb lenne
    static ArrayList<Poz> foglalt = new ArrayList<>();

    public Poz(int x, int y) {
        this.x = Math.max(0, Math.min(X_MAX, x));
        this.y = Math.max(0, Math.min(Y_MAX, y));
    }

    public void mozog(Poz p) {
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * @param str egy mező formátumú szöveg (pl. a9)
     * @return a szöveg által jelölt mező
     */
    //97 <=> 'a'
    static Poz fromString(String str) {
        return new Poz(str.charAt(0) - 97, Integer.parseInt(str.substring(1)));
    }

    /**
     * @param str egy szöveg
     * @return az adott szöveg mező formátumú-e
     */
    //VÉGZETES HIBA!!
    static boolean isMezo(String str) {
        try {
            return str.charAt(0) >= 'a' && str.charAt(0) <= 'a' + X_MAX && Integer.parseInt(str.substring(1)) >= 0 && Integer.parseInt(str.substring(1)) <= Y_MAX;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    //ezmiez
    static int tavolsag(Egyseg e1, Egyseg e2) {
        return Math.max(Math.abs(e1.pozicio.getX() - e2.pozicio.getX()), Math.abs(e1.pozicio.getY() - e2.pozicio.getY()));
    }
    static int tavolsag(Poz p1, Poz p2) {
        return Math.max(Math.abs(p1.getX() - p2.getX()), Math.abs(p1.getY() - p2.getY()));
    }
    //lehetne poz-egyseg és egyseg-poz paraméterű metódus is

    /**
     * @return az ezen a mezőn álló egység, vagy ha üres, akkor null
     */
    public Egyseg getEgyseg() {
        for(Egyseg e: Jatek.ellenfel.egysegek) {
            if(e.elhelyezett && e.el() && e.pozicio.equals(this)) {
                return e;
            }
        }
        for(Egyseg e: Jatek.jatekos.egysegek) {
            if(e.elhelyezett && e.el() && e.pozicio.equals(this)) {
                return e;
            }
        }
        return null;
    }

    /**
     * @return ez a mező "foglalt" lett-e pl. bozót varázslat által
     */
    public boolean isFoglalt() {
        for(Poz poz: foglalt) {
            if(poz.equals(this)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return ((char)(x + 97)) + "" + (y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poz poz = (Poz) o;
        return x == poz.x && y == poz.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
