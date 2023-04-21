/**
 * A sárkánytűz varázslat működésének a beállítása.
 */

public class Sarkanytuz extends PozCelpontuVarazslat {
    public final static int SEBZES = 25;

    public Sarkanytuz(Hos hos) {
        super(UserString.getString("TUZNEV"), 80, 7, UserString.getString("TUZLEIRAS", SEBZES + ""), hos);
    }

    /**
     * A kiválasztott mező sorában minden nem repülő egység (ellenséges és szövetséges) megsebzése és felgyújtása.
     * @param p a kiválasztott mező
     * @param ellensegek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a varázslat sikeressége
     */
    @Override
    public boolean hasznal(Poz p, Egyseg[] ellensegek, Egyseg[] szovetsegesek) {
        if(!super.mannaAr()) {
            return false;
        }
        int yKoord = p.getY();
        int sebzes = SEBZES * this.hos.getVarazsero();
        for(Egyseg e: ellensegek) {
            if(e.el() && e.pozicio.getY() == yKoord && !(e instanceof RepuloEgyseg)) {
                uzenet("SARKANYTUZ", e.nev, sebzes + "");
                e.setEletero(e.getEletero() - sebzes);
                e.eg = true;
            }
        }
        for(Egyseg e: szovetsegesek) {
            if(e.el() && e.pozicio.getY() == yKoord && !(e instanceof RepuloEgyseg)) {
                UserString.completeMessage("SARKANYTUZ", e.hos.getEllenfel().szin, e.nev, sebzes + "");
                e.setEletero(e.getEletero() - sebzes);
                e.eg = true;
            }
        }
        return true;
    }
}
