/**
 * A mező célpontú varázslatokat megvalósító absztrakt osztály. Használatukkor a célpont mezőt, valamint a szövetségeseket és ellenségeket tartalmazó tömböket kell átadni neki.
 */

public abstract class PozCelpontuVarazslat extends Varazslat {
    public PozCelpontuVarazslat(String nev, int ar, int manna, String leiras, Hos hos) {
        super(nev, ar, manna, leiras, hos);
    }

    /**
     * @param p a kiválasztott mező
     * @param ellensegek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a varázslat sikeressége
     */
    public abstract boolean hasznal(Poz p, Egyseg[] ellensegek, Egyseg[] szovetsegesek);

    @Override
    public String toString() {
        return super.toString() + "(" + UserString.getString("CELPONTPOZICIO") + ")";
    }
}
