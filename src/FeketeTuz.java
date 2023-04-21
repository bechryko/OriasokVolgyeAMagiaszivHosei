/**
 * A fekete tűz varázslat működésének a beállítása.
 */

public class FeketeTuz extends EgysegCelpontuVarazslat {
    public FeketeTuz(Hos hos) {
        super(UserString.getString("FEKETETUZNEV"), 100, 8, UserString.getString("FEKETETUZLEIRAS"), hos);
    }

    /**
     * A kiválasztott ellenséges egység annyi életerővel való megsebzése, amennyi az egység katonáinak egyharmadának az életereje.
     * @param e a célpont egység
     * @param ellensegek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a varázslat sikeressége
     */
    @Override
    public boolean hasznal(Egyseg e, Egyseg[] ellensegek, Egyseg[] szovetsegesek) {
        if(e.hos == this.hos) {
            System.out.println(Jatek.RED + UserString.getString("ROSSZCELPONTELLENSEG") + Jatek.RESET);
            return false;
        }
        if(!super.mannaAr()) {
            return false;
        }
        int sebzes = e.getLetszam() / 3 * e.maxEletero - e.getSerules();
        uzenet("FEKETETUZ", e.nev, e.getLetszam() / 3 + "");
        e.setEletero(e.getEletero() - sebzes);
        return true;
    }
}
