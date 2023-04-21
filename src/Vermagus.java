import java.util.ArrayList;

/**
 * A vérmágus egység statjainak a beállítása. Speciális célpontkereső metódussal rendelkezik.
 */

public class Vermagus extends Egyseg{
    public Vermagus(Hos hos) {
        super(UserString.getString("VERMAGUSNEV"), 8, 3, 4, 8, 3, 666, hos, UserString.getString("VERMAGUSLEIRAS"));
    }

    /**
     * A pályán bármelyik ellenséges egységet megtámadhatja, ezeket gyűjti össze.
     * Potenciális hiba lehet, hogy nem veszi figyelembe, hogy áll-e mellette óriásdémon. Erre a játék jelenlegi állapotában azért nincs szükség, mert a két egység egy frakcióban van, így nem kerülhetnek ellentétes oldalra a csatában.
     * @param p a pozíció, ahonnan a célpontot keresi
     * @param ellenfelek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a lehetséges célpontokat tartalmazó tömb
     */
    @Override
    public Egyseg[] celpontotKeres(Poz p, Egyseg[] ellenfelek, Egyseg[] szovetsegesek) {
        ArrayList<Egyseg> cel = new ArrayList<>();
        for(Egyseg e: ellenfelek) {
            if(e.el()) {
                cel.add(e);
            }
        }
        Egyseg[] ret = new Egyseg[cel.size()];
        for(int i = 0; i < ret.length; i++) {
            ret[i] = cel.get(i);
        }
        return ret;
    }
    @Override
    public Egyseg[] celpontotKeres(Egyseg[] ellenfelek, Egyseg[] szovetsegesek) {
        return this.celpontotKeres(this.pozicio, ellenfelek, szovetsegesek);
    }
}
