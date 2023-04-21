/**
 * A bozót varázslat működésének a beállítása.
 */

public class Bozot extends PozCelpontuVarazslat {
    public Bozot(Hos hos) {
        super(UserString.getString("BOZOTNEV"), 30, 3, UserString.getString("BOZOTLEIRAS"), hos);
    }

    /**
     * A kiválasztott mező "lefoglalása". (Onnantól kezdve nem lehet rálépni.)
     * @param p a kiválasztott mező
     * @param ellensegek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a varázslat sikeressége
     */
    @Override
    public boolean hasznal(Poz p, Egyseg[] ellensegek, Egyseg[] szovetsegesek) {
        if(p.isFoglalt() || p.getEgyseg() != null) {
            System.out.println(Jatek.RED + UserString.getString("ROSSZCELPONTURESMEZO") + Jatek.RESET);
            return false;
        }
        if(!super.mannaAr()) {
            return false;
        }
        Poz.foglalt.add(p);
        uzenet("BOZOT", p + "");
        return true;
    }
}
