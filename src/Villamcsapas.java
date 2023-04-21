/**
 * A villámcsapás varázslat működésének a beállítása.
 */

public class Villamcsapas extends EgysegCelpontuVarazslat {
    public final static int SEBZES = 20;
    public Villamcsapas(Hos hos) {
        super(UserString.getString("VILLAMCSAPASNEV"), 60, 5, UserString.getString("VILLAMCSAPASLEIRAS", SEBZES + ""), hos);
    }

    /**
     * A célpont ellenséges egységnek sebzés kiosztása.
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
        int sebzes = SEBZES * this.hos.getVarazsero();
        uzenet("VILLAM", e.nev, sebzes + "");
        e.setEletero(e.getEletero() - sebzes);
        return true;
    }
}
