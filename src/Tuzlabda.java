/**
 * A tűzlabda varázslat működésének a beállítása.
 */

public class Tuzlabda extends PozCelpontuVarazslat {
    public final static int SEBZES = 20;

    public Tuzlabda(Hos hos) {
        super(UserString.getString("TUZLABDANEV"), 100, 6, UserString.getString("TUZLABDALEIRAS", SEBZES + ""), hos);
    }

    /**
     * A kiválasztott pozíció körüli mezőkön minden egység (ellenséges és szövetséges) sebzése és felgyújtása.
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
        int sebzes = SEBZES * this.hos.getVarazsero();
        for(Egyseg e: ellensegek) {
            if(e.el() && Poz.tavolsag(p, e.pozicio) <= 1) {
                uzenet("TUZLABDA", e.nev, sebzes + "");
                e.setEletero(e.getEletero() - sebzes);
                e.eg = true;
            }
        }
        for(Egyseg e: szovetsegesek) {
            if(e.el() && Poz.tavolsag(p, e.pozicio) <= 1) {
                UserString.completeMessage("TUZLABDA", e.hos.getEllenfel().szin, e.nev, sebzes + "");
                e.setEletero(e.getEletero() - sebzes);
                e.eg = true;
            }
        }
        return true;
    }
}
