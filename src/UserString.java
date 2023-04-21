import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//kellene println metódus
/**
 * Egy kezelőosztály a felhasználó számára megjelenő szövegekhez.
 * Forráskódban ez az egyetlen angol nyelvű osztály, mert nem kapcsolódik szorosan a játék megvalósításához.
 */

public class UserString {
    private static final Map<String, String> strings = new HashMap<>();
    private static final String EXTENSION = "txt";
    public static ArrayList<String> messages = new ArrayList<>();
    public static boolean accentsEnabled = true;
    private final static ArrayList<String> ACCENT_KEYS = new ArrayList<>(Arrays.asList("BAA", "SAA", "BEA", "SEA", "BIA", "SIA", "BOA", "SOA", "BUA", "SUA", "BOAA", "SOAA", "BUAA", "SUAA", "BODD", "SODD", "BUDD", "SUDD"));

    /**
     * Az összes szöveg beolvasása a megfelelő asszociatív tömbbe.
     */
    public static void initialise() {
        for(String location: new String[] {"nevek", "altalanos", "elokeszuletek", "vasarlas", "taktikai", "csata"}) {
            BufferedReader br;
            //try-with-resources
            try {
                br = new BufferedReader(new FileReader("text/" + location + "." + EXTENSION));
                String input, key = "";
                StringBuilder sb = null;
                while(true) {
                    input = br.readLine();
                    if(input == null || input.startsWith("___")) {
                        if(sb != null || input == null) {
                            if(UserString.strings.containsKey(key)) {
                                System.err.println("Duplikalt kulcs: " + key);
                            }
                            UserString.strings.put(key, sb.toString());
                            if(input == null) {
                                break;
                            }
                        }
                        sb = new StringBuilder("");
                        key = input.replaceAll("___", "");
                        br.readLine();
                        continue;
                    }
                    if(sb == null) {
                        System.err.println("HIBA: " + location + " fajl nem megfeleloen kezdodik!");
                        continue;
                    }
                    sb.append(sb.toString().equals("") ? "" : System.lineSeparator()).append(input);
                }
            } catch(IOException e) {
                System.err.println("A fajllal (" + location + ") gondok vannak!");
            }
        }
    }

    /**
     * Az adott kulcshoz tartozó szöveg lekérése, majd az argumentumok behelyettesítése a szövegbe.
     * @param key a keresett szöveg kulcsa
     * @param args a szövegbe helyettesítendő argumentumok
     * @return a kész szöveg
     */
    public static String getString(String key, String ...args) {
        String str = UserString.strings.get(key);
        if(str == null) {
            System.err.println("Nincs ertek a kovetkezo kulcshoz: " + key);
            return Jatek.RED_BACKGROUND + "HIBA!" + Jatek.RESET;
        }
        for(int i = 0; i < args.length; i++) {
            str = str.replaceAll(("\\$" + i), args[i]);
        }
        if(!accentsEnabled && !ACCENT_KEYS.contains(key)) {
            str = deleteAccents(str);
        }
        return str.trim();
    }

    /**
     * Színkóddal ellátott szöveg hozzáadása az üzenetekhez.
     * @param key a szöveg kulcsa
     * @param color a szöveg színe (ANSI)
     * @param args a szövegbe helyettesítendő argumentumok
     */
    public static void completeMessage(String key, String color, String ...args) {
        messages.add(color + getString(key, args) + Jatek.RESET);
    }

    /**
     * Az üzenetek kiírása.
     */
    public static void printMessages() {
        System.out.println();
        for(String m: messages) {
            System.out.println(m);
        }
        messages = new ArrayList<>();
    }

    /**
     * Egy játékelem nevének átalakítása felhasználói input formátumúra.
     * @param str a játékelem neve
     * @return az átalakított szöveg
     */
    public static String toInput(String str) {
        return deleteAccents(str).toLowerCase().replaceAll(" ", "_");
    }

    /**
     * A megadott szövegben való magyar ékezetes betűk lecserélése ékezet nélküli változataikra.
     * @param str az ékezetes szöveg
     * @return az ékezetmentes szöveg
     */
    public static String deleteAccents(String str) {
        for(String replaceable: ACCENT_KEYS) {
            String replacement = replaceable.substring(1, 2);
            if(replaceable.charAt(0) == 'S') {
                replacement = replacement.toLowerCase();
            }
            str = str.replaceAll(UserString.getString(replaceable), replacement);
        }
        return str;
    }
}
