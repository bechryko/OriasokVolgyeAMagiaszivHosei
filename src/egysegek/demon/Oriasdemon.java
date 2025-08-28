package egysegek.demon;

import egysegek.Egyseg;
import hosok.Hos;
import ui.UserString;

/**
 * Az óriásdémon egység statjainak a beállítása. A speciális képessége az Egysegek.Egyseg osztályban van megvalósítva.
 */

public class Oriasdemon extends Egyseg {
    public Oriasdemon(Hos hos) {
        super(UserString.getString("ORIASDEMONNEV"), 20, 4, 5, 37, 3, 6, hos, UserString.getString("ORIASDEMONLEIRAS"));
    }
}
