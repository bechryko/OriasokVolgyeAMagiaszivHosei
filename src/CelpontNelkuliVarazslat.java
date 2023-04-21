/**
 * A célpont nélküli varázslatokat megvalósító absztrakt osztály. Használatukkor csak a szövetségeseket és ellenségeket tartalmazó tömböket kell átadni neki.
 */

public abstract class CelpontNelkuliVarazslat extends Varazslat {
    public CelpontNelkuliVarazslat(String nev, int ar, int manna, String leiras, Hos hos) {
        super(nev, ar, manna, leiras, hos);
    }

    /**
     * @param ellensegek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a varázslat sikeressége
     */
    //minden varázslatnál felesleges
    public abstract boolean hasznal(Egyseg[] ellensegek, Egyseg[] szovetsegesek);

    @Override
    public String toString() {
        return super.toString() + "(" + UserString.getString("CELPONTNINCS") + ")";
    }
}
