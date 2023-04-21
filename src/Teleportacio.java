/**
 * A teleportáció varázslat működésének a beállítása.
 */

public class Teleportacio extends EgysegCelpontuVarazslat {
    public Teleportacio(Hos hos) {
        super(UserString.getString("TELEPORTNEV"), 35, 4, UserString.getString("TELEPORTLEIRAS"), hos);
    }

    /**
     * A kiválasztott szövetséges egység teleportálása az ellenfél kezdőoldala felé a megadott számú mezővel.
     * Ha foglalt a mező, ahova teleportálna, eggyel közelebb teleportál, ha az is foglalt, még eggyel közelebb, stb.
     * Ha nincs egy szabad mező sem előtte, nem történik semmi.
     * @param e a célpont egység
     * @param ellensegek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a varázslat sikeressége
     */
    @Override
    public boolean hasznal(Egyseg e, Egyseg[] ellensegek, Egyseg[] szovetsegesek) {
        if(e.hos != this.hos || !e.el()) {
            System.out.println(Jatek.RED + UserString.getString("ROSSZCELPONTSZOVETSEGES") + Jatek.RESET);
            return false;
        }
        int j = e.hos == Jatek.jatekos ? 1 : -1;
        if((e.pozicio.getX() == Poz.X_MAX && j == 1) || (e.pozicio.getX() == 0 && j == -1)) {
            System.out.println(Jatek.RED + UserString.getString("NINCSHELY") + Jatek.RESET);
            return false;
        }
        Poz p = new Poz(Math.min(e.pozicio.getX() + this.hos.getVarazsero() * j, Poz.X_MAX), e.pozicio.getY());
        while(p.getEgyseg() != null || p.isFoglalt()) {
            p.mozog(new Poz(p.getX() - j, p.getY()));
            if(p.equals(e.pozicio)) {
                System.out.println(Jatek.RED + UserString.getString("NINCSHELY") + Jatek.RESET);
                return false;
            }
        }
        if(!super.mannaAr()) {
            return false;
        }
        uzenet("TELEPORT", e.nev, e.pozicio + "", p + "");
        e.pozicio.mozog(p);
        return true;
    }
}
