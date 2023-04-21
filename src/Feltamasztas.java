/**
 * A feltámasztás varázslat működésének a beállítása.
 */

public class Feltamasztas extends EgysegCelpontuVarazslat {
    public final static int GYOGYITAS = 50;

    public Feltamasztas(Hos hos) {
        super(UserString.getString("FELTAMASZTASNEV"), 120, 6, UserString.getString("FELTAMASZTASLEIRAS", GYOGYITAS + ""), hos);
    }

    /**
     * A kiválasztott szövetséges egység maximum annyi életerővel való gyógyítása (és ezzel katonák újraélesztése), hogy a létszáma ne legyen több az eredetinél.
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
        int regiLetszam = e.getLetszam(), gyogyitas = Math.min(GYOGYITAS * this.hos.getVarazsero(), e.getMaxLetszam() * e.maxEletero - e.getEletero());
        e.setEletero(e.getEletero() + gyogyitas);
        uzenet("FELTAMASZTVA", e.nev, gyogyitas + "", (e.getLetszam() - regiLetszam) + "");
        return true;
    }
}
