/**
 * A bárd egység statjainak a beállítása. A speciális képessége az Egyseg osztályban van megvalósítva.
 */

public class Bard extends Egyseg {
    public final static int KRITIKUS_TOBBSZOROZES = 4;

    public Bard(Hos hos) {
        super(UserString.getString("BARDNEV"), 4, 1, 2, 8, 4, 3, hos, UserString.getString("BARDLEIRAS", KRITIKUS_TOBBSZOROZES + ""));
    }
}
