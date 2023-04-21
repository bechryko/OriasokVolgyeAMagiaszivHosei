/**
 * Az áldozat varázslat működésének a beállítása.
 */

public class Aldozat extends EgysegCelpontuVarazslat {
    public Aldozat(Hos hos) {
        super(UserString.getString("ALDOZATNEV"), 85, 5, UserString.getString("ALDOZATLEIRAS"), hos);
    }

    /**
     * A kiválasztott szövetséges egységből egy katona feláldozása, majd az életerejével egyenlő sebzés kiosztása minden ellenséges egységnek.
     * @param e a célpont egység
     * @param ellensegek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a varázslat sikeressége
     */
    @Override
    public boolean hasznal(Egyseg e, Egyseg[] ellensegek, Egyseg[] szovetsegesek) {
        if(e.hos != this.hos) {
            System.out.println(Jatek.RED + UserString.getString("ROSSZCELPONTSZOVETSEGES") + Jatek.RESET);
            return false;
        }
        if(!super.mannaAr()) {
            return false;
        }
        int sebzes = e.maxEletero - e.getSerules();
        uzenet("ALDOZAS", e.nev);
        e.setEletero(e.getEletero() - sebzes);
        for(Egyseg ell: ellensegek) {
            if(!ell.el()) {
                continue;
            }
            uzenet("VERSEBZES", ell.nev, sebzes + "");
            ell.setEletero(ell.getEletero() - sebzes);
        }
        return true;
    }
}
