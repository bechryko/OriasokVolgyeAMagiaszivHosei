import java.util.ArrayList;

/**
 * A faóriás egység statjainak a beállítása. Speciális képessége minden kör végén aktiválódik.
 */

public class Tuzorias extends Egyseg {
    public final static int SEBZES_MENNYISEG = 10;

    public Tuzorias(Hos hos) {
        super(UserString.getString("TUZORIASNEV"), 30, 12, 17, 42, 3, 6, hos, UserString.getString("TUZORIASLEIRAS", SEBZES_MENNYISEG + ""));
    }

    /**
     * A hagyományos kör eleji képesség kiegészítve minden kör végi sebzéssel a közeli egységekre.
     * @param egysegek az összes, pályán lévő egységet tartalmazó lista
     */
    @Override
    public void korVegiKepesseg(ArrayList<Egyseg> egysegek) {
        super.korVegiKepesseg(egysegek);
        int sebzes = SEBZES_MENNYISEG * this.letszam;
        for(Egyseg e: egysegek) {
            if(e.el() && Poz.tavolsag(e, this) == 1) {
                UserString.completeMessage("TUZORIASAURA", e.hos.getEllenfel().szin, e.nev, sebzes + "");
                e.setEletero(e.getEletero() - sebzes);
            }
        }
    }
}
