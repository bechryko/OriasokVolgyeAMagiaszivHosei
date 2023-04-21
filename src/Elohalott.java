/**
 * Az élőhalott ember egység statjainak a beállítása speciális képesség nélkül.
 */

public class Elohalott extends Egyseg{
    public Elohalott(Hos hos) {
        super(UserString.getString("ELOHALOTTNEV"), 3, 1, 2, 5, 4, 3, hos, UserString.getString("ELOHALOTTLEIRAS"));
    }
}
