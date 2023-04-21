import java.util.ArrayList;

/**
 * A faóriás egység statjainak a beállítása. Speciális képessége minden kör végén aktiválódik.
 */

public class Faorias extends Egyseg {
    public final static int GYOGYITAS_MENNYISEG = 10;

    public Faorias(Hos hos) {
        super(UserString.getString("FAORIASNEV"), 14, 4, 5, 25, 4, 6, hos, UserString.getString("FAORIASLEIRAS", GYOGYITAS_MENNYISEG + ""));
    }

    /**
     * A hagyományos kör eleji képesség kiegészítve minden kör végi gyógyítással a közeli egységekre.
     * @param egysegek az összes, pályán lévő egységet tartalmazó lista
     */
    @Override
    public void korVegiKepesseg(ArrayList<Egyseg> egysegek) {
        super.korVegiKepesseg(egysegek);
        for(Egyseg e: egysegek) {
            if(Poz.tavolsag(e, this) == 1 && e.getSerules() > 0) {
                int gyogyitas = Math.min(e.getSerules(), GYOGYITAS_MENNYISEG * this.letszam);
                UserString.completeMessage("FAORIASGYOGYITAS", e.hos.szin, e.nev, gyogyitas + "");
                e.setEletero(e.getEletero() + gyogyitas);
            }
        }
    }
}
