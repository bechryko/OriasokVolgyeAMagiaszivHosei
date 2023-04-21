/**
 * Az egység célpontú varázslatokat megvalósító absztrakt osztály. Használatukkor a célpont egységet, valamint a szövetségeseket és ellenségeket tartalmazó tömböket kell átadni neki.
 */

public abstract class EgysegCelpontuVarazslat extends Varazslat {
    public EgysegCelpontuVarazslat(String nev, int ar, int manna, String leiras, Hos hos) {
        super(nev, ar, manna, leiras, hos);
    }

    /**
     * @param e a célpont egység
     * @param ellensegek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a varázslat sikeressége
     */
    public abstract boolean hasznal(Egyseg e, Egyseg[] ellensegek, Egyseg[] szovetsegesek);

    @Override
    public String toString() {
        return super.toString() + "(" + UserString.getString("CELPONTEGYSEG") + ")";
    }
}
