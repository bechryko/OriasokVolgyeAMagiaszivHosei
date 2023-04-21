/**
 * Az Égi Üvöltés isteni varázslat működésének a beállítása.
 */

public class EgiUvoltes extends CelpontNelkuliVarazslat {
    public EgiUvoltes(Hos hos) {
        super(UserString.getString("EGIUVOLTESNEV"), ISTENI_VARAZSLAT_AR, 3, UserString.getString("EGIUVOLTESLEIRAS"), hos);
    }

    /**
     * A szövetséges egységek újratámadásának engedélyezése, az ellenségekének blokkolása.
     * @param ellensegek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a varázslat sikeressége
     */
    @Override
    public boolean hasznal(Egyseg[] ellensegek, Egyseg[] szovetsegesek) {
        if(!super.mannaAr()) {
            return false;
        }
        uzenet("EGIUVOLTES");
        for(Egyseg e: szovetsegesek) {
            if(!e.visszatamad) {
                e.visszatamad = true;
                uzenet("SARKANYLELKESITES", e.nev);
            }
        }
        for(Egyseg e: ellensegek) {
            if(e.visszatamad) {
                e.visszatamad = false;
                uzenet("SARKANYFELEMLITES", e.nev);
            }
        }
        return true;
    }
}
