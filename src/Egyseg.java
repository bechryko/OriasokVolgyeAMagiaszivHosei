import java.util.ArrayList;

/**
 * A támadáskor alkalmazható effektek típusa.
 */
enum Effekt {
    tavolsagi, tuzes, mergezo
}

/**
 * Általános, absztrakt egységmegvalósítás.
 */
public abstract class Egyseg implements Comparable<Egyseg> {
    public final String nev;
    public final int ar;
    protected int minSebzes;
    protected int maxSebzes;
    public final int maxEletero;
    public final int sebesseg;
    public final int kezdemenyezes;
    public final String leiras;

    //létszám lehetne getter
    protected int letszam;
    protected int maxLetszam;
    private int eletero;
    protected boolean visszatamad;
    public Poz pozicio;
    public Hos hos;

    public boolean elhelyezett = false;

    protected boolean mergezett = false;
    protected boolean eg = false;

    public Egyseg(String nev, int ar, int minSebzes, int maxSebzes, int maxEletero, int sebesseg, int kezdemenyezes, Hos hos, String leiras) {
        this.nev = nev;
        this.ar = ar;
        this.minSebzes = minSebzes;
        this.maxSebzes = maxSebzes;
        this.maxEletero = maxEletero;
        this.sebesseg = sebesseg;
        this.kezdemenyezes = kezdemenyezes;
        this.letszam = 0;
        this.maxLetszam = 0;
        this.eletero = 0;
        this.visszatamad = true;
        this.pozicio = new Poz(1, 0);
        this.hos = hos;
        this.leiras = leiras;
    }

    /**
     * Megtámadható célpontok keresése. Ha van a közvetlen közelében ellenséges óriásdémon egység, akkor a speciális képességének alkalmazása.
     * @param p a pozíció, ahonnan a célpontot keresi
     * @param ellenfelek az ellenséges egységeket tartalmazó tömb
     * @param szovetsegesek a szövetséges egységeket tartalmazó tömb
     * @return a lehetséges célpontokat tartalmazó tömb
     */
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
        Egyseg[] ret = new Egyseg[cel.size()];
        for(int i = 0; i < ret.length; i++) {
            ret[i] = cel.get(i);
        }
        return ret;
    }
    /**
     * Általános célpontkereső metódus az egység jelenlegi pozíciójára meghívva.
     */
    public Egyseg[] celpontotKeres(Egyseg[] ellenfelek, Egyseg[] szovetsegesek) {
        return this.celpontotKeres(this.pozicio, ellenfelek, szovetsegesek);
    }

    /**
     * A sebzés kiszámítása. Ha van az egység közvetlen közelében szövetséges bárd egység, akkor a speciális képességének alkalmazása.
     * @param celpont megtámadott egység
     * @param kritikus kritikus-e a támadás
     * @return a sebzés mértéke
     */
    public int sebzesKiszamitas(Egyseg celpont, boolean kritikus) {
        int sebzes = Jatek.random(this.minSebzes * this.letszam, this.maxSebzes * this.letszam);
        float sebzes2 = (float)sebzes * (1 + (float)this.hos.getTamadas() / 10);
        sebzes = Math.round(sebzes2 * (1 - (float)celpont.hos.getVedekezes() / 20));
        if(kritikus) {
            boolean vanBard = false;
            for(Egyseg e: this.hos.egysegek) {
                if(e.el() && Poz.tavolsag(e, this) == 1 && e instanceof Bard) {
                    sebzes *= Bard.KRITIKUS_TOBBSZOROZES;
                    vanBard = true;
                    uzenet("BARDDAL");
                    break;
                }
            }
            if(!vanBard) {
                sebzes *= 2;
            }
        }
        return sebzes;
    }

    /**
     * Kritikus támadás eldöntése és a célpont megsebezése.
     * @param celpont megtámadott egység
     */
    public void tamad(Egyseg celpont) {
        boolean kritikus = Math.random() < this.hos.getSzerencse() * 0.05;
        int sebzes = this.sebzesKiszamitas(celpont, kritikus);
        if(this.mergezett) {
            sebzes = Math.round((float)sebzes / 2);
            this.mergezett = false;
        }
        celpont.sebzodik(sebzes, kritikus, this);
    }

    /**
     * Sebzés elszenvedése, majd túlélő katonák esetén visszatámadás.
     * @param sebzes kapott sebzés mértéke
     * @param kritikus kritikus-e a támadás
     * @param kitol a támadó egység
     */
    public void sebzodik(int sebzes, boolean kritikus, Egyseg kitol) {
        UserString.completeMessage("EGYSEGSEBZES", this.hos.getEllenfel().szin, kitol.nev, this.nev, sebzes + "", kritikus ? UserString.getString("KRITIKUSCSAPAS") : "");
        this.setEletero(this.getEletero() - sebzes);
        if(this.letszam == 0) {
            return;
        }
        if(this.visszatamad) {
            this.visszaTamad(kitol);
            this.visszatamad = false;
        }
    }

    /**
     * Effekttel ellátott sebzés elszenvedése.
     * @param effekt a sebzésen lévő effekt típusa
     */
    public void sebzodik(int sebzes, boolean kritikus, Egyseg kitol, Effekt effekt) {
        UserString.completeMessage("EGYSEGSEBZES", this.hos.getEllenfel().szin, kitol.nev, this.nev, sebzes + "", kritikus ? UserString.getString("KRITIKUSCSAPAS") : "");
        this.setEletero(this.getEletero() - sebzes);
        if(this.letszam == 0) {
            return;
        }
        if(this.visszatamad && effekt != Effekt.tavolsagi) {
            this.visszaTamad(kitol);
            this.visszatamad = false;
        }
        if(effekt == Effekt.mergezo) {
            UserString.completeMessage("MERGEZOCSAPAS", this.hos.getEllenfel().szin, this.nev);
            this.mergezett = true;
        }
        //else-if kellene
        if(effekt == Effekt.tuzes) {
            UserString.completeMessage("TUZESCSAPAS", this.hos.getEllenfel().szin, this.nev);
            this.eg = true;
        }
    }

    /**
     * Kritikus visszatámadás eldöntése és a célpont visszasebzése.
     * @param kit a visszatámadott egység
     */
    public void visszaTamad(Egyseg kit) {
        boolean kritikus = Math.random() < this.hos.getSzerencse() * 0.05;
        int sebzes = Math.round((float)this.sebzesKiszamitas(kit, kritikus) / 2);
        uzenet("VISSZATAMADAS", this.nev, kit.nev, sebzes + "", kritikus ? UserString.getString("KRITIKUSCSAPAS") : "");
        kit.setEletero(kit.getEletero() - sebzes);
    }

    /**
     * A minden kör végén aktiválandó képesség. Alapesetben csak a visszatámadás engedélyezése és az égés általi sebzés elszenvedése.
     * @param egysegek az összes, pályán lévő egységet tartalmazó lista
     */
    public void korVegiKepesseg(ArrayList<Egyseg> egysegek) {
        this.visszatamad = true;
        if(this.eg) {
            int sebzes = this.letszam;
            UserString.completeMessage("EGES", this.hos.getEllenfel().szin, this.nev, sebzes + "");
            this.setEletero(this.getEletero() - sebzes);
            this.eg = false;
        }
    }

    /**
     * Új életerőérték beállítása, az egység látszámának kezelése.
     * @param eletero új életerőérték
     */
    public void setEletero(int eletero) {
        if(this.letszam == 0) {
            System.err.println("Ez az egyseg mar meghalt.");
            return;
        }
        this.eletero = Math.max(0, Math.min(this.maxEletero * this.maxLetszam, eletero));
        this.letszam = (int)Math.ceil((float)this.eletero / this.maxEletero);
        if(this.letszam == 0) {
            UserString.completeMessage("MEGHALT", this.hos.getEllenfel().szin, this.nev);
            if(this.hos == Jatek.jatekos) {
                Jatek.jatekosEgysegei--;
            } else {
                Jatek.gepEgysegei--;
            }
        }
    }

    /**
     * @param p a kiválasztott mező
     * @return sikeres volt-e a mozgás
     */
    //SET!!
    public boolean mozog(Poz p) {
        ArrayList<Poz> mezok = new ArrayList<>();
        utatKeres(this.pozicio, mezok);
        if(mezok.contains(p)) {
            this.pozicio.mozog(p);
            uzenet("MOZGOTT", this.nev, this.pozicio + "");
            return true;
        }
        return false;
    }

    /**
     * Azoknak a mezőknek az összegyűjtése, ahová az egység léphet.
     * @param p kiinduló pozíció
     * @param mezok a lista, ahova az elérhető mezőket gyűjti
     */
    public void utatKeres(Poz p, ArrayList<Poz> mezok) {
        this.utatKeres(p, this.sebesseg, mezok);
        mezok.remove(p);
    }

    /**
     * Rekurzív útkereső metódus.
     * @param p pozíció, ahonnan a további elérhető pozíciókat keresi
     * @param tavolsag hátralévő távolság, amennyit még haladhat
     * @param mezok a lista, ahova az elérhető mezőket gyűjti
     */
    private void utatKeres(Poz p, int tavolsag, ArrayList<Poz> mezok) {
        //távolság + isFoglalt lekezelése, majd p.getEgyseg-et kimenteni, majd maradék feltételek
        if(tavolsag < 0 || p.isFoglalt() || (p.getEgyseg() != null && p.getEgyseg() != this)) {
            return;
        }
        if(!mezok.contains(p)) {
            mezok.add(p);
        }
        for(int i = p.getX() - 1; i <= p.getX() + 1; i++) {
            for(int j = p.getY() - 1; j <= p.getY() + 1; j++) {
                utatKeres(new Poz(i, j), tavolsag - 1, mezok);
            }
        }
    }

    /**
     * Ellenfél köre közbeni üzenet készítése az egység játékosának színével.
     * @param kulcs az üzenetbe helyezendő szöveg kulcsa
     * @param argumentumok az üzenetbe helyezendő szöveg argumentumai
     */
    public void uzenet(String kulcs, String ...argumentumok) {
        UserString.completeMessage(kulcs, this.hos.szin, argumentumok);
    }

    //üres string kezelése
    public char kezdobetu() {
        return this.nev.charAt(0);
    }

    public int getMinSebzes() {
        return minSebzes;
    }

    public int getMaxSebzes() {
        return maxSebzes;
    }

    public int getLetszam() {
        return letszam;
    }

    public int getMaxLetszam() {
        return maxLetszam;
    }

    public int getEletero() {
        return eletero;
    }

    public int getSerules() {
        return this.maxEletero * this.letszam - this.eletero;
    }

    public boolean el() {
        return this.letszam > 0;
    }

    /**
     * Egységvásárlás során a létszám és a hozzá tartozó adattagok megnövelése.
     * @param szint megvásárolt katonák száma
     */
    public void fejleszt(int szint) {
        this.maxLetszam = (this.letszam += szint);
        this.eletero = this.letszam * this.maxEletero;
    }

    /**
     * Egységek kezdeményezés és morál alapján történő összehasonlító metódusa.
     * @param e másik egység
     * @return mennyivel hamarabb következik ez az egység egy körben
     */
    @Override
    public int compareTo(Egyseg e) {
        return (e.kezdemenyezes + e.hos.getMoral()) - (this.kezdemenyezes + this.hos.getMoral());
    }

    /**
     * Az egység aktuális statisztikáinak szövegbe foglalása.
     * @return az egység szövegesen
     */
    @Override
    public String toString() {
        String str = UserString.getString("EGYSEGLEIRAS", this.nev, this.letszam + "", this.maxLetszam + "", this.minSebzes * this.letszam + "", this.maxSebzes * this.letszam + "", this.eletero + "", this.maxEletero * this.letszam + "", this.sebesseg + "", this.kezdemenyezes + "", UserString.getString(this.visszatamad ? "VANVISSZATAMADAS" : "NINCSVISSZATAMADAS"));
        if(this.mergezett) {
            str += " " + UserString.getString("MERGEZETT");
        }
        if(this.eg) {
            str += " " + UserString.getString("EGO");
        }
        return str;
    }

    /**
     * Az egység tulajdonságainak szövegbe foglalása vásárláshoz.
     * @return az egység szövegesen
     */
    //írja is ki, lol
    public String aruKiiratas() {
        String str = UserString.getString("EGYSEGBOLTLEIRAS", this.nev, this.ar + "", this.minSebzes + "", this.maxSebzes + "", this.maxEletero + "", this.sebesseg + "", this.kezdemenyezes + "");
        str += System.lineSeparator() + this.leiras;
        return str;
    }

    /**
     * Az egység részletes szövegbe foglalása.
     * @return az egység szövegesen
     */
    //itt is
    public String kiir() {
        return UserString.getString("EGYSEGLEIRAS", this.nev, this.letszam + "", this.maxLetszam + "", this.minSebzes * this.letszam + "", this.maxSebzes * this.letszam + "", this.eletero + "", this.maxEletero * this.letszam + "", this.sebesseg + "", this.kezdemenyezes + "", "");
    }
}
