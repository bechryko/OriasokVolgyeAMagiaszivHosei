/**
 * Az óriásdémon egység statjainak a beállítása. A speciális képessége az Egyseg osztályban van megvalósítva.
 */

public class Oriasdemon extends Egyseg{
    public Oriasdemon(Hos hos) {
        super(UserString.getString("ORIASDEMONNEV"), 20, 4, 5, 37, 3, 6, hos, UserString.getString("ORIASDEMONLEIRAS"));
    }
}
