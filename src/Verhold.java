/**
 * A Vérhold isteni varázslat működésének a beállítása.
 */

public class Verhold extends CelpontNelkuliVarazslat {
    public final static int EROSITES_SZAZALEK = 12;

    public Verhold(Hos hos) {
        super(UserString.getString("VERHOLDNEV"), ISTENI_VARAZSLAT_AR, 14, UserString.getString("VERHOLDLEIRAS", EROSITES_SZAZALEK + ""), hos);
    }

    /**
     * A hős mannájának lenullázása és minden szövetséges egység támadásának a megnövelése.
     * @param ellensegek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a varázslat sikeressége
     */
    @Override
    public boolean hasznal(Egyseg[] ellensegek, Egyseg[] szovetsegesek) {
        if(!super.mannaAr()) {
            return false;
        }
        uzenet("VERHOLD");
        this.hos.setManna(0);
        float erosites = 1 + (float)this.hos.getVarazsero() / 100 * EROSITES_SZAZALEK;
        for(Egyseg s: szovetsegesek) {
            if(!s.el()) {
                continue;
            }
            int regiMinSebzes = s.minSebzes, regiMaxSebzes = s.maxSebzes;
            s.minSebzes = Math.round((float)s.minSebzes * erosites);
            s.maxSebzes = Math.round((float)s.maxSebzes * erosites);
            uzenet("VERHOLDERO", s.nev, regiMinSebzes + "-" + regiMaxSebzes, s.minSebzes + "-" + s.maxSebzes);
        }
        return true;
    }
}
