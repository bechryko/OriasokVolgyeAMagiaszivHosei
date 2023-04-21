/**
 * Az agyaagóriás egység statjainak a beállítása speciális képesség nélkül.
 */

public class Agyagorias extends Egyseg {
    public Agyagorias(Hos hos) {
        super(UserString.getString("AGYAGORIASNEV"), 10, 3, 3, 26, 3, 4, hos, UserString.getString("AGYAGORIASLEIRAS"));
    }
}
