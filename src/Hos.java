/**
 * A hősöket, azaz a két felet megvalósító osztály.
 */

public class Hos {
    public final Tulajdonsag[] tulajdonsagok;
    public Varazslat[] varazslatok;
    public Egyseg[] egysegek;
    public final String szin;

    private int arany;
    private int manna;
    public boolean cselekedett = false;

    private int tulajdonsagAr = 5;

    /**
     * @param faj a hős faja, amitől függenek a varázslatai és egységei
     * @param nehezseg a játék nehézsége a hős számára, amitől a kezdőaranyának mennyisége függ
     * @param szin a játék során az adott hőshöz köthető események kiírásának színe
     */
    public Hos(String faj, int nehezseg, String szin) {
        this.tulajdonsagok = new Tulajdonsag[6];
        String[] tulajdonsagNevek = new String[]{"tamadas", "vedekezes", "varazsero", "tudas", "moral", "szerencse"};
        for(int i = 0; i < 6; i++) {
            this.tulajdonsagok[i] = new Tulajdonsag(UserString.getString(tulajdonsagNevek[i].toUpperCase() + "NEV"), UserString.getString(tulajdonsagNevek[i].toUpperCase() + "LEIRAS"));
        }
        this.varazslatok = new Varazslat[3];
        switch(faj) {
            case "ember":
                this.egysegek = new Egyseg[] {
                        new Foldmuves(this),
                        new Ijasz(this),
                        new Griff(this),
                        new Bard(this)
                };
                this.varazslatok = new Varazslat[] {
                        new Villamcsapas(this),
                        new FeketeTuz(this),
                        new Teleportacio(this)
                };
                break;
            case "orias":
                this.egysegek = new Egyseg[] {
                        new Agyagorias(this),
                        new Tuzorias(this),
                        new Gyemantorias(this),
                        new Faorias(this)
                };
                this.varazslatok = new Varazslat[] {
                        new Tuzlabda(this),
                        new Bozot(this),
                        new EletKertje(this)
                };
                break;
            case "demon":
                this.egysegek = new Egyseg[] {
                        new Elohalott(this),
                        new Vermagus(this),
                        new Oriasdemon(this),
                        new DemonTudos(this)
                };
                this.varazslatok = new Varazslat[] {
                        new Feltamasztas(this),
                        new Aldozat(this),
                        new Verhold(this)
                };
                break;
            case "sarkany":
                this.egysegek = new Egyseg[] {
                        new Torpesarkany(this),
                        new VorosSarkany(this),
                        new PancelozottSarkany(this),
                        new MergezoSarkany(this)
                };
                this.varazslatok = new Varazslat[] {
                        new Szellokes(this),
                        new Sarkanytuz(this),
                        new EgiUvoltes(this)
                };
                break;
            default:
                System.err.println("Ervenytelen faj: " + faj);
        }
        switch(nehezseg) {
            case 1: this.arany = 1300; break;
            case 2: this.arany = 1000; break;
            case 3: this.arany = 700; break;
            default:
                System.err.println("Ervenytelen nehezsegi szint: " + nehezseg);
        }
        this.szin = szin;
        this.manna = 10;
    }

    /**
     * Hőstámadás megvalósítása, ami figyelmen kívül hagyja az ellenfél hősének a védelmét.
     * @param e a célpont egység
     */
    public void tamad(Egyseg e) {
        UserString.completeMessage("HOSTAMADAS", this.szin, e.nev, 10 * this.getTamadas() + "");
        e.setEletero(e.getEletero() - 10 * this.getTamadas());
        this.cselekedett = true;
    }

    /**
     * Egységvásárlás.
     * @param e a kiválasztott egység (termék)
     * @param db vásárolni kívánt darabszám
     * @return a vásárlás sikeressége
     */
    public boolean vasarol(Egyseg e, int db) {
        if(this.arany < e.ar * db) {
            return false;
        }
        this.arany -= e.ar * db;
        e.fejleszt(db);
        return true;
    }

    /**
     * Varázslatvásárlás.
     * @param v a kiválasztott varázslat (termék)
     * @return a vásárlás sikeressége
     */
    public boolean vasarol(Varazslat v) {
        if(this.arany < v.ar || v.hos != this) {
            return false;
        }
        this.arany -= v.ar;
        v.megvasarol();
        return true;
    }

    /**
     * Tulajdonságvásárlás. Tudás esetén manna növelése.
     * @param t a kiválasztott tulajdonság (termék)
     * @param db vásárolni kívánt szintszám
     * @return a vásárlás sikeressége
     */
    public boolean vasarol(Tulajdonsag t, int db) {
        if(this.arany < this.tulajdonsagArSzamitas(db, false)) {
            return false;
        }
        this.arany -= this.tulajdonsagArSzamitas(db, true);
        t.fejleszt(db);
        if(t.nev.equals(UserString.getString("TUDASNEV"))) {
            this.manna += db * 10;
        }
        return true;
    }

    /**
     * A következő, megvásárolandó tulajdonságpontok árának kiszámítása.
     * @param db megvásárolandó darabszám
     * @param vegleges tényleges vásárlás történt-e (false: csak az ár lekérése történt)
     * @return a tulajdonságpontok ára
     */
    private int tulajdonsagArSzamitas(int db, boolean vegleges) {
        int ar = 0, ujar = this.tulajdonsagAr;
        for(int i = 0; i < db; i++, ujar = (int)Math.ceil(1.1 * ujar)) {
            ar += ujar;
        }
        if(vegleges) {
            this.tulajdonsagAr = ujar;
        }
        return ar;
    }
    public int tulajdonsagArSzamitas(int db) {
        return this.tulajdonsagArSzamitas(db, false);
    }

    public int getArany() {
        return arany;
    }
    public int getTamadas() {
        return this.tulajdonsagok[0].getSzint();
    }
    public int getVedekezes() {
        return this.tulajdonsagok[1].getSzint();
    }
    public int getVarazsero() {
        return this.tulajdonsagok[2].getSzint();
    }
    public int getMoral() {
        return this.tulajdonsagok[4].getSzint();
    }
    public int getSzerencse() {
        return this.tulajdonsagok[5].getSzint();
    }

    public void setManna(int value) {
        this.manna = value;
    }
    public int getManna() {
        return this.manna;
    }

    /**
     * Lekéri az ezzel a hőssel szemben álló hőst.
     * @return ellenfél hőse
     */
    public Hos getEllenfel() {
        if(Jatek.jatekos == this) {
            return Jatek.ellenfel;
        }
        return Jatek.jatekos;
    }
}
