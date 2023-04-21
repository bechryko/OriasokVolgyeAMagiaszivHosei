/**
 * A törpesárkány egység statjainak a beállítása speciális képesség nélkül.
 */

public class Torpesarkany extends RepuloEgyseg {
    public Torpesarkany(Hos hos) {
        super(UserString.getString("TORPESARKANYNEV"), 5, 2, 3, 7, 7, 13, hos, UserString.getString("TORPESARKANYLEIRAS"));
    }
}
