/**
 * Az Élet Kertje isteni varázslat működésének a beállítása.
 */

public class EletKertje extends CelpontNelkuliVarazslat {
    public EletKertje(Hos hos) {
        super(UserString.getString("KERTNEV"), ISTENI_VARAZSLAT_AR, 2, UserString.getString("KERTLEIRAS"), hos);
    }

    /**
     * Minden szövetséges teljes meggyógyítása.
     * @param ellensegek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a varázslat sikeressége
     */
    @Override
    public boolean hasznal(Egyseg[] ellensegek, Egyseg[] szovetsegesek) {
        if(!super.mannaAr()) {
            return false;
        }
        uzenet("ELETKERTJE");
        for(Egyseg e: szovetsegesek) {
            if(e.getSerules() == 0) {
                continue;
            }
            uzenet("ELETKERTJEGYOGYITAS", e.nev, e.getSerules() + "");
            e.setEletero(e.getEletero() + e.getSerules());
        }
        return true;
    }
}
