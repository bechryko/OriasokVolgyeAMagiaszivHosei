import java.util.ArrayList;

/**
 * A Démon Szeme tudós egység statjainak a beállítása. Speciális célpontkereső és támadó metódussal rendelkezik.
 */

public class DemonTudos extends Egyseg{
    public final static int EROSITES_SZAZALEK = 50;

    public DemonTudos(Hos hos) {
        super(UserString.getString("DEMONTUDOSNEV"), 14, 2, 6, 13, 5, 8, hos, UserString.getString("DEMONTUDOSLEIRAS", EROSITES_SZAZALEK + ""));
    }

    /**
     * A hagyományos célpontkeresés mellett még a közvetlenül mellette álló szövetségeseket is képes megtámadni.
     * @param p a pozíció, ahonnan a célpontot keresi
     * @param ellenfelek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a lehetséges célpontokat tartalmazó tömb
     */
    @Override
    public Egyseg[] celpontotKeres(Poz p, Egyseg[] ellenfelek, Egyseg[] szovetsegesek) {
        ArrayList<Egyseg> cel = new ArrayList<>();
        for(Egyseg e: ellenfelek) {
            if(e.el() && Poz.tavolsag(e.pozicio, p) == 1) {
                if(e instanceof Oriasdemon) {
                    return new Egyseg[] {e};
                }
                cel.add(e);
            }
        }
        for(Egyseg s: szovetsegesek) {
            if(s.el() && Poz.tavolsag(s.pozicio, p) == 1) {
                cel.add(s);
            }
        }
        Egyseg[] ret = new Egyseg[cel.size()];
        for(int i = 0; i < ret.length; i++) {
            ret[i] = cel.get(i);
        }
        return ret;
    }

    /**
     * Általános célpontkereső metódus az egység jelenlegi pozíciójára meghívva.
     */
    @Override
    public Egyseg[] celpontotKeres(Egyseg[] ellenfelek, Egyseg[] szovetsegesek) {
        return this.celpontotKeres(this.pozicio, ellenfelek, szovetsegesek);
    }

    /**
     * A hagyományos támadás kiegészítve azzal, hogy ha szövetségest támad meg, akkor megnövelje annak a támadásértékét.
     * @param celpont a támadott célpont egység
     */
    @Override
    public void tamad(Egyseg celpont) {
        super.tamad(celpont);
        if(celpont.el() && celpont.hos == this.hos) {
            int regiMinSebzes = celpont.minSebzes, regiMaxSebzes = celpont.maxSebzes;
            celpont.minSebzes *= 1 + (float)EROSITES_SZAZALEK / 100;
            celpont.maxSebzes *= 1 + (float)EROSITES_SZAZALEK / 100;
            uzenet("TUDOSEROSITES", celpont.nev, regiMinSebzes + "-" + regiMaxSebzes, celpont.minSebzes + "-" + celpont.maxSebzes);
        }
    }
}
