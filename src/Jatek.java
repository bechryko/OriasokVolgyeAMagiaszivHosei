import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A játék főosztálya, tartalmazza a main metódust és a játék fázisait megvalósító metódusokat.
 * Itt történik a felhasználói inputok kezelése is.
 * Itt jönnek létre a színes konzolos kiíratáshoz szükséges ANSI színkódok.
 */

public class Jatek {
    static final String RESET = "\u001B[0m";
    static final String BLACK = "\u001B[30m";
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String BLUE = "\u001B[34m";
    static final String PURPLE = "\u001B[35m";
    static final String CYAN = "\u001B[36m";
    static final String WHITE = "\u001B[37m";
    static final String BLACK_BACKGROUND = "\u001B[40m";
    static final String RED_BACKGROUND = "\u001B[41m";
    static final String GREEN_BACKGROUND = "\u001B[42m";
    static final String YELLOW_BACKGROUND = "\u001B[43m";
    static final String BLUE_BACKGROUND = "\u001B[44m";
    static final String PURPLE_BACKGROUND = "\u001B[45m";
    static final String CYAN_BACKGROUND = "\u001B[46m";
    static final String WHITE_BACKGROUND = "\u001B[47m";

    /**
     * @return véletlen szám [min, max] intervallumban
     */
    static int random(int min, int max) {
        return (int)Math.floor(Math.random() * (max + 1 - min)) + min;
    }

    /**
     * @return véletlen szám [0, max) intervallumban
     */
    static int random(int max) {
        return random(0, max - 1);
    }

    /**
     * @param egysegek egységeket tartalmazó lista
     * @param hos kiválasztott hős
     * @return a kiválasztott hős egy véletlen egysége a listából
     */
    //szebb lenne egy tömböt/listát létrehozni, abba pakolni az első ciklusban az egységeket, majd abból egy random sorszámút visszaadni
    //hiba esetén null-t kellene visszaadni
    static Egyseg random(ArrayList<Egyseg> egysegek, Hos hos) {
        int hosEgysegei = 0;
        for(Egyseg e: egysegek) {
            if(e.hos == hos && e.el()) {
                hosEgysegei++;
            }
        }
        int index = random(hosEgysegei);
        for(Egyseg e: egysegek) {
            if(e.hos == hos && e.el() && index-- == 0) {
                return e;
            }
        }
        System.err.println("Hiba tortent random egysegvalasztaskor! (lista elemszam: " + egysegek.size() + ")");
        return egysegek.get(0);
    }

    public static Hos jatekos;
    public static Hos ellenfel;
    static BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

    //a hősnek kellene egységszám adattag
    static int jatekosEgysegei = 0, gepEgysegei = 0;
    static String elozoParancs;

    static String input() throws IOException {
        return userInput.readLine().toLowerCase();
    }

    /**
     * A játék fázisaiként szolgáló keretet kezelő metódus.
     * A program belépési pontja.
     * @param args speciális -nincsEkezet kapcsolót lehet használni, hogy a játékban megjelenő szövegek ékezetmentesek legyenek
     */
    public static void main(String[] args) throws IOException {
        UserString.initialise();

        if(args.length >= 1 && args[0].equals("-nincsEkezet")) {
            UserString.accentsEnabled = false;
            System.out.println(UserString.getString("NINCSEKEZET"));
        }
        konzoltTisztit();

        System.out.println(System.lineSeparator() + CYAN_BACKGROUND + BLACK + UserString.getString("JATEKCIM") + RESET + System.lineSeparator());
        System.out.println(UserString.getString("KEZDES", UserString.getString("HELYSZIN" + (random(5)))));
        input();

        Jatek.elokeszuletek();
        Jatek.vasarlas();
        Jatek.taktikaiFazis();
        Jatek.csata();

        System.out.println(UserString.getString("NYOMJENTERTAVEGEHEZ"));
        input();
    }

    /**
     * Az előkészületi fázis során a felhasználó kiválasztja a frakcióját és a nehézségi szintet.
     * Mindezt a gép is megteszi automatikusan.
     */
    public static void elokeszuletek() throws IOException {
        System.out.println(UserString.getString("FRAKCIOVALASZTASKEZDET"));
        String[] fajSzinek = new String[] {YELLOW, GREEN, RED, CYAN};
        String[] fajok = new String[] {"ember", "orias", "demon", "sarkany"};
        for(int i = 0; i < 4; i++) {
            System.out.println(fajSzinek[i] + UserString.getString("FRAKCIO" + fajok[i].toUpperCase()));
        }
        System.out.print(RESET);
        String jatekosFaja;
        fajValasztoCiklus:
        while(true) {
            System.out.println(UserString.getString("FRAKCIOBEMENET") + System.lineSeparator() + RED_BACKGROUND + UserString.getString("PARANCSFONTOS") + RESET);
            jatekosFaja = input();
            for(String faj: fajok) {
                if(jatekosFaja.equals(faj)) {
                    break fajValasztoCiklus;
                }
            }
        }
        System.out.println(UserString.getString("NEHEZSEG"));
        String nehezseg;
        do {
            System.out.println(UserString.getString("NEHEZSEGBEMENET"));
            nehezseg = input();
        } while(!(nehezseg.equals("1") || nehezseg.equals("2") || nehezseg.equals("3")));
        jatekos = new Hos(jatekosFaja, Integer.parseInt(nehezseg), GREEN);
        String ellenfelFaja;
        do {
            ellenfelFaja = fajok[random(4)];
        } while(ellenfelFaja.equals(jatekosFaja));
        ellenfel = new Hos(ellenfelFaja, 2, RED);
        konzoltTisztit();
    }

    //if-ek helyett else-if-ek, mert az események függetlenek egymástól
    /**
     * A vásárlási fázis alatt a felhasználó beszerzi a szükséges egységeket, varázslatokat és tulajdonságokat.
     * Mindezt a gép is megteszi automatikusan.
     */
    public static void vasarlas() throws IOException {
        elozoParancs = null;
        konzoltTisztit();
        System.out.println(UserString.getString("BOLTKEZDET") + System.lineSeparator());
        String parancs;
        String[] parancsok;
        boolean voltVasarlas = true, egysegVasarolva = false;
        foVasarloCiklus:
        for(int it = 0;; it++) {
            if(voltVasarlas) {
                if(it > 0) {
                    konzoltTisztit();
                }
                System.out.println(UserString.getString("VASAROLT"));
                seregetKiir(jatekos);
                System.out.println(System.lineSeparator() + YELLOW + UserString.getString("ARANY", jatekos.getArany() + "") + RESET + System.lineSeparator());
                voltVasarlas = false;
            }
            parancs = input();
            if(!parancs.equals(UserString.deleteAccents(parancs))) {
                System.out.println(RED_BACKGROUND + UserString.getString("PARANCSFONTOS") + RESET);
                continue;
            }
            if(parancs.equals("ujra") && elozoParancs != null) {
                parancs = elozoParancs;
            }
            parancsok = parancs.split(" ");
            if(parancs.equals("p")) {
                System.out.println(UserString.getString("BOLTPARANCSLISTA"));
                elozoParancs = parancs;
                continue;
            }
            if(parancs.equals("vasarol")) {
                System.out.println(UserString.getString("BOLTKATEGORIALISTA"));
                elozoParancs = parancs;
                continue;
            }
            if(parancsok.length == 1) {
                parancs = elozoParancs + " " + parancs;
                parancsok = parancs.split(" ");
            }
            elozoParancs = parancs;
            if(parancs.equals("vasarol egyseg")) {
                boolean van = false;
                for(Egyseg e: jatekos.egysegek) {
                    if(e.ar <= jatekos.getArany()) {
                        System.out.println(e.nev);
                        van = true;
                    }
                }
                if(!van) {
                    System.out.println(UserString.getString("NINCSTERMEK"));
                }
                continue;
            }
            if(parancs.equals("vasarol varazslat")) {
                boolean van = false;
                for(Varazslat v: jatekos.varazslatok) {
                    if(v.ar <= jatekos.getArany() && !v.isMegvan()) {
                        System.out.println(v.nev);
                        van = true;
                    }
                }
                if(!van) {
                    System.out.println(UserString.getString("NINCSTERMEK"));
                }
                continue;
            }
            if(parancs.equals("vasarol tulajdonsag")) {
                int ar = jatekos.tulajdonsagArSzamitas(1);
                if(ar <= jatekos.getArany()) {
                    for(Tulajdonsag t: jatekos.tulajdonsagok) {
                        System.out.println(t.nev);
                    }
                    System.out.println(UserString.getString("KOVETKEZOTULAJDONSAG", ar + ""));
                } else {
                    System.out.println(UserString.getString("NINCSTERMEK"));
                }
                continue;
            }
            if(parancsok.length < 3 || !parancsok[0].equals("vasarol")) {
                if(parancs.equals("vasarlas vege")) {
                    if(egysegVasarolva) {
                        break;
                    } else {
                        boolean van = false;
                        for(Egyseg e: jatekos.egysegek) {
                            if(e.ar <= jatekos.getArany()) {
                                van = true;
                                break;
                            }
                        }
                        if(van) {
                            System.out.println(UserString.getString("NINCSEGYSEG"));
                        } else {
                            System.out.println(UserString.getString("NEMLEHETEGYSEG"));
                            System.exit(0);
                        }
                    }
                } else {
                    System.out.println(UserString.getString("PARANCSP"));
                }
                continue;
            }
            if("egyseg".equals(parancsok[1])) {
                if(parancsok.length == 3 || parancsok.length == 4) {
                    boolean talalt = false;
                    Egyseg vasarolando = null;
                    for(Egyseg e: jatekos.egysegek) {
                        if(UserString.toInput(e.nev).equals(parancsok[2])) {
                            if(parancsok.length == 3) {
                                System.out.println(e.aruKiiratas());
                                continue foVasarloCiklus;
                            }
                            vasarolando = e;
                            talalt = true;
                            break;
                        }
                    }
                    try {
                        if (!talalt) {
                            System.out.println(RED + UserString.getString("NINCSILYENEGYSEG") + RESET);
                            continue;
                        } else if (parancsok.length == 4 && Integer.parseInt(parancsok[3]) > 0) {
                            if (jatekos.vasarol(vasarolando, Integer.parseInt(parancsok[3]))) {
                                System.out.println(UserString.getString("MEGVASAROLVA", vasarolando.nev, parancsok[3]));
                                voltVasarlas = true;
                                egysegVasarolva = true;
                            } else {
                                System.out.println(UserString.getString("KEVESPENZ", vasarolando.nev, vasarolando.ar * Integer.parseInt(parancsok[3]) + "", jatekos.getArany() + ""));
                            }
                            continue;
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println(UserString.getString("PARANCSP"));
                        continue;
                    }
                }
            }
            if("varazslat".equals(parancsok[1])) {
                if(parancsok.length == 3 || parancsok.length == 4) {
                    boolean talalt = false;
                    Varazslat vasarolando = null;
                    for(Varazslat v: jatekos.varazslatok) {
                        if(UserString.toInput(v.nev).equals(parancsok[2])) {
                            if(parancsok.length == 3) {
                                System.out.println(v.leiras);
                                continue foVasarloCiklus;
                            }
                            vasarolando = v;
                            talalt = true;
                            break;
                        }
                    }
                    if(!talalt) {
                        System.out.println(RED + UserString.getString("NINCSILYENVARAZSLAT") + RESET);
                        continue;
                    } else if(parancsok.length == 4 && parancsok[3].equals("ok")) {
                        if(vasarolando.isMegvan()) {
                            System.out.println(UserString.getString("VARAZSLATMEGVAN", vasarolando.nev));
                            continue;
                        }
                        if(jatekos.vasarol(vasarolando)) {
                            System.out.println(UserString.getString("MEGVASAROLVA", vasarolando.nev, "1"));
                            voltVasarlas = true;
                        } else {
                            System.out.println(UserString.getString("KEVESPENZ", vasarolando.nev, vasarolando.ar + "", jatekos.getArany() + ""));
                        }
                        continue;
                    }
                }
            }
            if("tulajdonsag".equals(parancsok[1])) {
                if(parancsok.length == 3 || parancsok.length == 4) {
                    boolean talalt = false;
                    Tulajdonsag vasarolando = null;
                    for(Tulajdonsag t: jatekos.tulajdonsagok) {
                        if(UserString.toInput(t.nev).equals(parancsok[2])) {
                            if(parancsok.length == 3) {
                                System.out.println(t.leiras);
                                continue foVasarloCiklus;
                            }
                            vasarolando = t;
                            talalt = true;
                            break;
                        }
                    }
                    try {
                        if (!talalt) {
                            System.out.println(RED + UserString.getString("NINCSILYENTULAJDONSAG") + RESET);
                            continue;
                        } else if (parancsok.length == 4 && Integer.parseInt(parancsok[3]) > 0) {
                            if (vasarolando.getSzint() + Integer.parseInt(parancsok[3]) > 10) {
                                System.out.println(UserString.getString("MAX10"));
                            } else if (jatekos.vasarol(vasarolando, Integer.parseInt(parancsok[3]))) {
                                System.out.println(UserString.getString("MEGVASAROLVA", vasarolando.nev, parancsok[3]));
                                voltVasarlas = true;
                            } else {
                                System.out.println(UserString.getString("KEVESPENZ", vasarolando.nev, jatekos.tulajdonsagArSzamitas(Integer.parseInt(parancsok[3])) + "", jatekos.getArany() + ""));
                            }
                            continue;
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println(UserString.getString("PARANCSP"));
                        continue;
                    }
                }
            }
            System.out.println(UserString.getString("PARANCSP"));
        }
        konzoltTisztit();
        System.out.println(UserString.getString("SHOPEND", jatekos.getArany() + ""));

        for(int i = 0; i < 11; i++) {
            ellenfel.vasarol(ellenfel.tulajdonsagok[random(6)], 1);
        }
        ellenfel.vasarol(ellenfel.varazslatok[random(ellenfel.varazslatok.length)]);
        int tudVenni = 4;
        while(tudVenni != 0) {
            int melyik = random(tudVenni);
            for(int i = 0; i < ellenfel.egysegek.length; i++) {
                if(ellenfel.getArany() >= ellenfel.egysegek[i].ar) {
                    if(melyik-- == 0) {
                        ellenfel.vasarol(ellenfel.egysegek[i], 1);
                    }
                }
            }
            tudVenni = 0;
            for(int i = 0; i < ellenfel.egysegek.length; i++) {
                if(ellenfel.getArany() >= ellenfel.egysegek[i].ar) {
                    tudVenni++;
                }
            }
        }
    }

    /**
     * A taktikai fázis során először a gép automatikusan lehelyezi az egységeit, majd a felhasználó is megteszi ugyanezt.
     * Itt tekintheti meg a felhasználó az ellenfél egységeit, varázslatait és tulajdonságait.
     */
    public static void taktikaiFazis() throws IOException {
        System.out.println(UserString.getString("TAKTIKAIKEZDET") + System.lineSeparator());
        for(Egyseg e: ellenfel.egysegek) {
            Poz p;
            do {
                p = new Poz(random(Poz.X_MAX - 1, Poz.X_MAX), random(Poz.Y_MAX + 1));
            } while(p.getEgyseg() != null);
            e.pozicio = p;
            e.elhelyezett = true;
        }

        int egysegSzam = 0;
        for(Egyseg e: jatekos.egysegek) {
            if(e.el()) {
                egysegSzam++;
            }
        }
        String ellenfelNev = UserString.getString("ELLENFELNEV" + random(5));
        ArrayList<Poz> kijelolt = new ArrayList<>();
        for(int j = 0; j <= Poz.Y_MAX; j++) {
            kijelolt.add(new Poz(0, j));
            kijelolt.add(new Poz(1, j));
        }
        for(int i = 0, j = 0; i < egysegSzam; j++) {
            if(j > 0) {
                konzoltTisztit();
            }
            System.out.println(jatekos.szin + UserString.getString("JATEKOS"));
            seregetKiir(jatekos);
            System.out.println(System.lineSeparator() + ellenfel.szin + UserString.getString("ELLENFEL", ellenfelNev));
            seregetKiir(ellenfel);
            System.out.println(RESET);
            palyatKiir(kijelolt);
            System.out.println(System.lineSeparator() + UserString.getString("PARANCSP"));
            String parancs;
            while(true) {
                parancs = input();
                if(!parancs.equals(UserString.deleteAccents(parancs))) {
                    System.out.println(RED_BACKGROUND + UserString.getString("PARANCSFONTOS") + RESET);
                    continue;
                }
                if(parancs.equals("todo") || parancs.equals("lerak")) {
                    System.out.println(UserString.getString("LERAKANDO"));
                    for(Egyseg e: jatekos.egysegek) {
                        if(e.el() && e.pozicio.getX() != 0) {
                            System.out.println(e.nev);
                        }
                    }
                    continue;
                }
                if(parancs.equals("p")) {
                    System.out.println(UserString.getString("TAKTIKAIPARANCSLISTA"));
                    continue;
                }
                String[] parancsok = parancs.split(" ");
                if(parancsok.length == 2) {
                    Egyseg lerakando = null;
                    for(Egyseg e: jatekos.egysegek) {
                        if(e.el() && UserString.toInput(e.nev).equals(parancsok[0])) {
                            lerakando = e;
                        }
                    }
                    if(lerakando == null) {
                        System.out.println(UserString.getString("NINCSILYENEGYSEG2"));
                        continue;
                    }
                    Poz p;
                    if(!Poz.isMezo(parancsok[1]) || !kijelolt.contains(p = Poz.fromString(parancsok[1]))) {
                        System.out.println(UserString.getString("ROSSZMEZO"));
                        continue;
                    }
                    if(lerakando.elhelyezett) {
                        kijelolt.add(lerakando.pozicio);
                    } else {
                        lerakando.elhelyezett = true;
                        i++;
                    }
                    lerakando.pozicio = p;
                    kijelolt.remove(p);
                    break;
                }
                System.out.println(UserString.getString("PARANCSP"));
            }
        }
        konzoltTisztit();
        System.out.println(UserString.getString("TAKTIKAIVEGE"));
        String parancs;
        do {
            System.out.println(UserString.getString("OK"));
            parancs = input();
            if(parancs.startsWith("nem")) {
                System.out.println(UserString.getString("NEEEEEEEE"));
            }
        } while(!parancs.equals("ok"));
    }

    /**
     * A csata előkészületeit és lefolyását végző metódus.
     */
    public static void csata() throws IOException {
        elozoParancs = null;
        ArrayList<Egyseg> egysegek = new ArrayList<>(); //linkedList-nek kellene lennie (végigiterálunk rajta, törlünk belőle, mást nem)
        for(Egyseg e: ellenfel.egysegek) {
            if(e.el()) {
                egysegek.add(e);
                gepEgysegei++;
            }
        }
        for(Egyseg e: jatekos.egysegek) {
            if(e.el()) {
                egysegek.add(e);
                jatekosEgysegei++;
            }
        }
        Collections.sort(egysegek);

        csataCiklus:
        for(int kor = 1;; kor++) {
            jatekos.cselekedett = ellenfel.cselekedett = false;
            for(Egyseg e: egysegek) {
                if(!e.el()) {
                    continue;
                }
                //csúnya, hogy bele van égetve a forráskódba, hogy az egyik a játékos, a másik a gép (nehéz pl. multiplayerre átírni)
                if(e.hos == jatekos) {
                    jatekosLep(e, kor, egysegek);
                } else {
                    gepLep(e, egysegek);
                }
                //ezt nem kellene mindig megnézni: hős adattagja, setter -> ha 0 lesz, saját exceptiont dob, amit a csata metódus elkap, és vége a játéknak
                if(gepEgysegei == 0 || jatekosEgysegei == 0) {
                    break csataCiklus;
                }
            }
            for(Egyseg e: egysegek) {
                if(e.el()) {
                    e.korVegiKepesseg(egysegek);
                }
            }
            if(gepEgysegei == 0 || jatekosEgysegei == 0) {
                break;
            }
            for(int i = 0; i < egysegek.size(); i++) {
                if(!egysegek.get(i).el()) {
                    egysegek.remove(i--);
                }
            }
        }
        UserString.printMessages();
        System.out.println();

        // ?:-os kifejezésekkel sokkal szebb lenne
        if(jatekosEgysegei == 0) {
            if(gepEgysegei == 0) {
                System.out.println(UserString.getString("DONTETLEN"));
            } else {
                System.out.println(UserString.getString("VESZTETT"));
            }
        } else {
            System.out.println(UserString.getString("NYERT"));
        }
        System.out.println(UserString.getString("NYOMJENTERT"));
        input();
    }

    /**
     * A játékos egy körben való cselekedeteit beolvasó és feldolgozó metódus.
     * @param e a játékos soron következő egysége
     * @param kor az aktuális kör sorszáma
     * @param egysegek az összes pályán lévő egységet tartalmazó lista
     */
    public static void jatekosLep(Egyseg e, int kor, ArrayList<Egyseg> egysegek) throws IOException {
        boolean utolsoe = false;
        for(Egyseg egy: egysegek) {
            if(egy.hos != jatekos) {
                continue;
            }
            utolsoe = egy == e;
        }
        ArrayList<Poz> kijelolt = new ArrayList<>();
        foCiklus:
        while(true) {
            if(jatekosEgysegei == 0 || gepEgysegei == 0) {
                return;
            }
            konzoltTisztit();
            System.out.println(UserString.getString("KORSZAM", kor + ""));
            for (Egyseg egy: egysegek) {
                if (egy.el()) {
                    System.out.println((egy == e ? GREEN_BACKGROUND + BLACK : egy.hos.szin) + egy + RESET);
                }
            }
            System.out.println(System.lineSeparator() + CYAN + UserString.getString("MANNAMENNYISEG", jatekos.getManna() + "") + RESET + System.lineSeparator());
            palyatKiir(kijelolt);
            kijelolt = new ArrayList<>();

            UserString.printMessages();

            if(utolsoe && !jatekos.cselekedett) {
                System.out.println(System.lineSeparator() + YELLOW + UserString.getString("UTOLSOAKORBEN") + RESET);
            }

            String parancs;
            String[] parancsok;
            while(true) {
                parancs = input();
                if(!parancs.equals(UserString.deleteAccents(parancs))) {
                    System.out.println(RED_BACKGROUND + UserString.getString("PARANCSFONTOS") + RESET);
                    continue;
                }
                if(elozoParancs != null && parancs.equals("ujra")) {
                    parancs = elozoParancs;
                }
                if(parancs.equals("p")) {
                    System.out.println(UserString.getString("CSATAPARANCSLISTA"));
                    elozoParancs = parancs;
                    continue;
                }
                if(parancs.equals("passz") || parancs.equals("egyseg passz")) {
                    elozoParancs = parancs;
                    break foCiklus;
                }
                if(elozoParancs != null && Poz.isMezo(parancs)) {
                    parancs = elozoParancs + " " + parancs;
                }
                parancsok = parancs.split(" ");
                if(parancsok.length == 1 || parancsok.length > 4) {
                    System.out.println(UserString.getString("PARANCSP"));
                    elozoParancs = parancs;
                    continue;
                }
                if(elozoParancs != null && parancsok[0].equals("ujra") && parancsok.length == 2) {
                    String ujUtasitas = parancsok[1];
                    parancsok = elozoParancs.split(" ");
                    parancsok[0] = ujUtasitas;
                }
                elozoParancs = String.join(" ", parancsok);
                if(parancsok[0].equals("hos")) {
                    if(jatekos.cselekedett) {
                        System.out.println(RED + UserString.getString("HOSCSELEKEDETT") + RESET);
                        continue;
                    }
                    if(parancsok[1].equals("tamad")) {
                        if(parancsok.length == 2) {
                            for(Egyseg egy: ellenfel.egysegek) {
                                if(!egy.el()) {
                                    continue;
                                }
                                UserString.messages.add(egy.nev + " (" + egy.pozicio + ")");
                                kijelolt.add(egy.pozicio);
                            }
                            continue foCiklus;
                        }
                        if(parancsok.length == 3 && Poz.isMezo(parancsok[2])) {
                            Egyseg celpont = Poz.fromString(parancsok[2]).getEgyseg();
                            if(celpont == null || celpont.hos == jatekos) {
                                System.out.println(UserString.getString("NEMHOSTAMADHATO"));
                                continue;
                            }
                            jatekos.tamad(celpont);
                            jatekos.cselekedett = true;
                            continue foCiklus;
                        }
                    } else if(parancsok[1].equals("varazsol")) {
                        if(parancsok.length == 2) {
                            boolean van = false;
                            for(Varazslat v: jatekos.varazslatok) {
                                if(v.isMegvan() && v.manna <= jatekos.getManna()) {
                                    System.out.println(v);
                                    van = true;
                                }
                            }
                            if(!van) {
                                System.out.println(UserString.getString("NINCSVARAZSLAT"));
                            }
                            continue;
                        }
                        if(parancsok.length == 4) {
                            Varazslat var = null;
                            for(Varazslat v: jatekos.varazslatok) {
                                if(UserString.toInput(v.nev).equals(parancsok[2]) && v.isMegvan()) {
                                    var = v;
                                    break;
                                }
                            }
                            if(var == null) {
                                System.out.println(UserString.getString("NINCSILYENVARAZSLAT2"));
                                continue;
                            }
                            if(var.manna > jatekos.getManna()) {
                                System.out.println(UserString.getString("NINCSELEGMANNA"));
                                continue;
                            }
                            if(var instanceof CelpontNelkuliVarazslat) {
                                CelpontNelkuliVarazslat var2 = (CelpontNelkuliVarazslat)var;
                                if (!parancsok[3].equals("ok")) {
                                    System.out.println(UserString.getString("CELPONTNELKULIVARAZSLAT"));
                                    continue;
                                }
                                jatekos.cselekedett = var2.hasznal(ellenfel.egysegek, jatekos.egysegek);
                                if (jatekos.cselekedett) {
                                    continue foCiklus;
                                }
                                continue;
                            }
                            if(var instanceof EgysegCelpontuVarazslat) {
                                EgysegCelpontuVarazslat var2 = (EgysegCelpontuVarazslat)var;
                                if (!Poz.isMezo(parancsok[3])) {
                                    System.out.println(UserString.getString("NINCSMEZO"));
                                    continue;
                                }
                                Egyseg egy = Poz.fromString(parancsok[3]).getEgyseg();
                                if (egy == null) {
                                    System.out.println(UserString.getString("URESMEZO"));
                                    continue;
                                }
                                jatekos.cselekedett = var2.hasznal(egy, ellenfel.egysegek, jatekos.egysegek);
                                if (jatekos.cselekedett) {
                                    continue foCiklus;
                                }
                                continue;
                            }
                            if(var instanceof PozCelpontuVarazslat) {
                                PozCelpontuVarazslat var2 = (PozCelpontuVarazslat)var;
                                if (!Poz.isMezo(parancsok[3])) {
                                    System.out.println(UserString.getString("NINCSMEZO"));
                                    continue;
                                }
                                jatekos.cselekedett = var2.hasznal(Poz.fromString(parancsok[3]), ellenfel.egysegek, jatekos.egysegek);
                                if (jatekos.cselekedett) {
                                    continue foCiklus;
                                }
                                continue;
                            }
                            System.err.println("Hibas a varazslat tipusa (nincs besorolva egyik celpontkategoriaba sem)!");
                        }
                    }
                } else if(parancsok[0].equals("egyseg")) {
                    if(parancsok[1].equals("mozog")) {
                        if(parancsok.length == 2) {
                            ArrayList<Poz> mezok = new ArrayList<>();
                            e.utatKeres(e.pozicio, mezok);
                            if(mezok.size() == 0) {
                                System.out.println(UserString.getString("NINCSMOZGAS"));
                                continue;
                            }
                            int mezoSzam = 0;
                            for(Poz p: mezok) {
                                mezoSzam++;
                                kijelolt.add(p);
                            }
                            UserString.completeMessage("VANMOZGAS", RESET, e.nev, mezoSzam + "");
                            continue foCiklus;
                        } else if(parancsok.length == 3 && Poz.isMezo(parancsok[2])) {
                            if(e.mozog(Poz.fromString(parancsok[2]))) {
                                break foCiklus;
                            }
                            System.out.println(UserString.getString("NEMLEPHET"));
                            continue;
                        }
                    } else if(parancsok[1].equals("tamad")) {
                        Egyseg[] celok = e.celpontotKeres(ellenfel.egysegek, jatekos.egysegek);
                        if(parancsok.length == 2) {
                            for(Egyseg egy: celok) {
                                UserString.messages.add(egy.nev + " (" + egy.pozicio + ")");
                                kijelolt.add(egy.pozicio);
                            }
                            if(kijelolt.size() == 0) {
                                System.out.println(UserString.getString("NINCSCELPONT"));
                                continue;
                            }
                            continue foCiklus;
                        }
                        if(parancsok.length == 3 && Poz.isMezo(parancsok[2])) {
                            Egyseg celpont = Poz.fromString(parancsok[2]).getEgyseg();
                            for(Egyseg egy: celok) {
                                if(celpont == egy) {
                                    e.tamad(celpont);
                                    break foCiklus;
                                }
                            }
                            System.out.println(UserString.getString("NEMTAMADHATO"));
                            continue;
                        }
                    }
                }
                System.out.println(UserString.getString("PARANCSP"));
            }
        }
    }

    /**
     * A gép egy körben való cselekedeteit kitaláló és elvégző metódus.
     * @param e a gép soron következő egysége
     * @param egysegek az összes pályán lévő egységet tartalmazó lista
     */
    public static void gepLep(Egyseg e, ArrayList<Egyseg> egysegek) {
        if(!ellenfel.cselekedett) {
            Varazslat varazslat = null;
            for(Varazslat v: ellenfel.varazslatok) {
                if(v.isMegvan()) {
                    varazslat = v;
                    break;
                }
            }
            if(varazslat == null) {
                System.err.println("Az ellenfel varazslatanak a beallitasa hibas!");
                return;
            }
            if(varazslat instanceof Verhold && varazslat.manna <= ellenfel.getManna()) {
                ellenfel.cselekedett = ((Verhold)varazslat).hasznal(jatekos.egysegek, ellenfel.egysegek);
            } else {
                boolean utolsoASoron = false;
                for (Egyseg egy : egysegek) {
                    if (egy.hos == ellenfel) {
                        utolsoASoron = egy == e;
                    }
                }
                if (Math.random() < 0.75 || utolsoASoron) {
                    if(varazslat.manna <= ellenfel.getManna()) {
                        if(varazslat instanceof CelpontNelkuliVarazslat) {
                            if(Math.random() < 0.5) {
                                ((CelpontNelkuliVarazslat) varazslat).hasznal(jatekos.egysegek, ellenfel.egysegek);
                            } else {
                                ellenfel.tamad(random(egysegek, jatekos));
                            }
                        } else if(varazslat instanceof PozCelpontuVarazslat) {
                            if (varazslat instanceof Bozot) {
                                Poz p;
                                do {
                                    p = new Poz(random(Poz.X_MAX + 1), random(Poz.Y_MAX + 1));
                                } while (p.getEgyseg() != null || p.isFoglalt());
                                ((Bozot)varazslat).hasznal(p, jatekos.egysegek, ellenfel.egysegek);
                            } else {
                                ((PozCelpontuVarazslat)varazslat).hasznal(random(egysegek, jatekos).pozicio, jatekos.egysegek, ellenfel.egysegek);
                            }
                        } else if(varazslat instanceof EgysegCelpontuVarazslat) {
                            if (varazslat instanceof Villamcsapas || varazslat instanceof FeketeTuz) {
                                ((EgysegCelpontuVarazslat)varazslat).hasznal(random(egysegek, jatekos), jatekos.egysegek, ellenfel.egysegek);
                            } else {
                                if (varazslat instanceof Teleportacio) {
                                    ArrayList<Egyseg> tudTeleportalni = new ArrayList<>();
                                    egysegCiklus:
                                    for (Egyseg egy : ellenfel.egysegek) {
                                        if (!egy.el() || egy.celpontotKeres(jatekos.egysegek, ellenfel.egysegek).length > 0) {
                                            continue;
                                        }
                                        Poz p;
                                        for (int i = 1; i <= ellenfel.getVarazsero(); i++) {
                                            p = new Poz(egy.pozicio.getX() - i, egy.pozicio.getY());
                                            if (!p.isFoglalt() && p.getEgyseg() == null) {
                                                tudTeleportalni.add(egy);
                                                continue egysegCiklus;
                                            }
                                        }
                                    }
                                    if (tudTeleportalni.size() > 0) {
                                        ((Teleportacio)varazslat).hasznal(random(tudTeleportalni, ellenfel), jatekos.egysegek, ellenfel.egysegek);
                                    } else {
                                        ellenfel.tamad(random(egysegek, jatekos));
                                    }
                                } else {
                                    ((EgysegCelpontuVarazslat)varazslat).hasznal(random(egysegek, ellenfel), jatekos.egysegek, ellenfel.egysegek);
                                }
                            }
                        }
                    } else {
                        ellenfel.tamad(random(egysegek, jatekos));
                    }
                    ellenfel.cselekedett = true;
                }
            }
        }
        if(jatekosEgysegei == 0 || gepEgysegei == 0) {
            return;
        }
        Egyseg[] celpontok = e.celpontotKeres(jatekos.egysegek, ellenfel.egysegek);
        if(celpontok.length > 0) {
            if(e instanceof DemonTudos) {
                ArrayList<Egyseg> tamadni = new ArrayList<>();
                for(Egyseg egy: celpontok) {
                    if(egy.hos == jatekos || egy.getEletero() > e.getMaxSebzes() * e.getLetszam()) {
                        tamadni.add(egy);
                    }
                }
                if(tamadni.size() > 0) {
                    e.tamad(tamadni.get(random(tamadni.size())));
                    return;
                }
            } else {
                e.tamad(celpontok[random(celpontok.length)]);
                return;
            }
        }
        ArrayList<Poz> utiCelok = new ArrayList<>();
        e.utatKeres(e.pozicio, utiCelok);
        Egyseg[] ujCelpontok;
        for(Poz p: utiCelok) {
            ujCelpontok = e.celpontotKeres(p, jatekos.egysegek, (e instanceof DemonTudos ? jatekos : ellenfel).egysegek);
            if(ujCelpontok.length > 0) {
                e.mozog(p);
                return;
            }
        }
        for(int i = 0; i < utiCelok.size() && utiCelok.size() > 1; i++) {
            if(utiCelok.get(i).getX() > Poz.X_MAX - e.sebesseg) {
                utiCelok.remove(i--);
            }
        }
        e.mozog(utiCelok.get(random(utiCelok.size())));
    }

    /**
     * A pálya kirajzolása a játékos számára.
     * @param kijelolt megkülönböztetően kijelölendő mezők listája
     */
    private static void palyatKiir(ArrayList<Poz> kijelolt) {
        System.out.print("  ");
        for(int i = 0; i <= Poz.X_MAX; i++) {
            System.out.print((" " + (char)(i + 97) + " ").toUpperCase());
        }
        System.out.println();
        for(int i = 0; i <= Poz.Y_MAX; i++) {
            System.out.print(i + " ");
            for(int j = 0; j <= Poz.X_MAX; j++) {
                Poz p = new Poz(j, i);
                if(kijelolt.contains(p)) {
                    System.out.print(YELLOW_BACKGROUND);
                }
                if(p.isFoglalt()) {
                    System.out.print(" # " + RESET);
                    continue;
                }
                Egyseg e = p.getEgyseg();
                if(e == null || !e.elhelyezett || !e.el()) {
                    System.out.print(" . " + RESET);
                    continue;
                }
                System.out.print(" " + e.hos.szin + e.kezdobetu() + RESET + " ");
            }
            System.out.println(" " + i);
        }
        System.out.print("  ");
        for(int i = 0; i <= Poz.X_MAX; i++) {
            System.out.print((" " + (char)(i + 97) + " ").toUpperCase());
        }
    }

    /**
     * Az adott hőshöz tartozó egységek, varázslatok és tulajdonságok részletes kiírása.
     * @param h a hős
     */
    private static void seregetKiir(Hos h) {
        boolean van = false;
        System.out.println(UserString.getString("EGYSEGCIM"));
        for(Egyseg e: h.egysegek) {
            if(e.el()) {
                System.out.println(e.kiir());
                van = true;
            }
        }
        if(!van) {
            System.out.println("Nincs");
        }
        System.out.println();
        van = false;
        System.out.println(UserString.getString("VARAZSLATCIM"));
        for(Varazslat v: h.varazslatok) {
            if(v.isMegvan()) {
                System.out.println(v.nev);
                van = true;
            }
        }
        if(!van) {
            System.out.println("Nincs");
        }
        System.out.println();
        System.out.println(UserString.getString("TULAJDONSAGCIM"));
        for(Tulajdonsag t: h.tulajdonsagok) {
            System.out.println(t.nev + ": " + t.getSzint());
        }
    }

    private static void konzoltTisztit() {
        System.out.print(System.lineSeparator() + "\033[H\033[2J");
        System.out.flush();
    }
}