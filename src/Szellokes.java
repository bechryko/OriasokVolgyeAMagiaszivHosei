/**
 * A széllökés varázslat működésének a beállítása.
 */

public class Szellokes extends CelpontNelkuliVarazslat {
    public Szellokes(Hos hos) {
        super(UserString.getString("SZELLOKESNEV"), 50, 3, UserString.getString("SZELLOKESLEIRAS"), hos);
    }

    /**
     * Minden ellenséges egység hátralökése a megadott számú mezővel (az ellenfél kezdőoldala irányába).
     * Ha foglalt a mező, ahova lökné, eggyel kisebbet lök, ha az is foglalt, még eggyel kisebbet, stb.
     * Ha nincs egy szabad mező sem mögötte, nem történik semmi.
     * @param ellensegek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a varázslat sikeressége
     */
    @Override
    public boolean hasznal(Egyseg[] ellensegek, Egyseg[] szovetsegesek) {
        if(!super.mannaAr()) {
            return false;
        }
        int j = this.hos == Jatek.jatekos ? 1 : -1;
        int ellokes = Math.round((float)this.hos.getVarazsero() / 2);
        egysegCiklus:
        for(Egyseg e: ellensegek) {
            if (!e.el() || (e.pozicio.getX() == Poz.X_MAX && j == 1) || (e.pozicio.getX() == 0 && j == -1)) {
                continue;
            }
            Poz p = new Poz(Math.min(e.pozicio.getX() + ellokes * j, Poz.X_MAX), e.pozicio.getY());
            while (p.getEgyseg() != null || p.isFoglalt()) {
                p.mozog(new Poz(p.getX() - j, p.getY()));
                if (p.equals(e.pozicio)) {
                    continue egysegCiklus;
                }
            }
            uzenet("ELLOKES", e.nev, e.pozicio + "", p + "");
            e.pozicio.mozog(p);
        }
        return true;
    }
}
