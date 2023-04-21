import java.util.ArrayList;

/**
 * Az íjász egység statjainak a beállítása. Speciális célpontkereső és támadó metódussal rendelkezik.
 */

//lehetne távolsági interface, visszatámadásnál elég azt ellenőrizni
public class Ijasz extends Egyseg {
    public final static int HATOSUGAR = 3;

    public Ijasz(Hos hos) {
        super(UserString.getString("IJASZNEV"), 6, 2, 4, 6, 4, 9, hos, UserString.getString("IJASZLEIRAS", HATOSUGAR + ""));
    }

    /**
     * Hagyományos célpontkeresés kibővített hatótávolsággal. Ha egy ellenséges egység közvetlenül mellette áll, akkor nincs lehetséges célpontja.
     * @param p a pozíció, ahonnan a célpontot keresi
     * @param ellenfelek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a lehetséges célpontokat tartalmazó tömb
     */
    @Override
    public Egyseg[] celpontotKeres(Poz p, Egyseg[] ellenfelek, Egyseg[] szovetsegesek) {
        for(Egyseg e: ellenfelek) {
            if(e.el() && Poz.tavolsag(e.pozicio, p) == 1) {
                return new Egyseg[] {};
            }
        }
        ArrayList<Egyseg> cel = new ArrayList<>();
        for(Egyseg  e: ellenfelek) {
            if(e.el() && Poz.tavolsag(e.pozicio, p) <= HATOSUGAR) {
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

    /**
     * Távolsági effekttel ellátott hagyományos támadás.
     * @param celpont megtámadott egység
     */
    @Override
    public void tamad(Egyseg celpont) {
        boolean kritikus = Math.random() < this.hos.getSzerencse() * 0.05;
        int sebzes = this.sebzesKiszamitas(celpont, kritikus);
        if(this.mergezett) {
            sebzes = Math.round((float)sebzes / 2);
            this.mergezett = false;
        }
        celpont.sebzodik(sebzes, kritikus, this, Effekt.tavolsagi);
    }
}
