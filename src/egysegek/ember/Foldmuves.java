package egysegek.ember;

import egysegek.Egyseg;
import hosok.Hos;
import ui.UserString;

/**
 * A földműves egység statjainak a beállítása speciális képesség nélkül.
 */

public class Foldmuves extends Egyseg {
    public Foldmuves(Hos hos) {
        super(UserString.getString("FOLDMUVESNEV"), 2, 1, 1, 3, 4, 8, hos, UserString.getString("FOLDMUVESLEIRAS"));
    }
}
