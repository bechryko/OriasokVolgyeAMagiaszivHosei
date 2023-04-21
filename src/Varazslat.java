/**
 * Általános, absztrakt varázslatmegvalósítás.
 */

public abstract class Varazslat {
    public final String nev;
    public final int ar;
    public final int manna;
    public final String leiras;
    public final Hos hos;

    private boolean megvan;

    public final static int ISTENI_VARAZSLAT_AR = 150;

    public Varazslat(String nev, int ar, int manna, String leiras, Hos hos) {
        this.nev = nev;
        this.ar = ar;
        this.manna = manna;
        this.leiras = leiras;
        this.hos = hos;
        this.megvan = false;
    }

    /**
     * Ellenfél köre közbeni üzenet készítése a varázslat játékosának színével.
     * @param kulcs az üzenetbe helyezendő szöveg kulcsa
     * @param argumentumok az üzenetbe helyezendő szöveg argumentumai
     */
    public void uzenet(String kulcs, String ...argumentumok) {
        UserString.completeMessage(kulcs, this.hos.szin, argumentumok);
    }

    /**
     * A varázslat használatakor a manna levonásáért felelős metódus.
     * Ideális esetben csak akkor van meghívva, ha van a hősnek elég mannája a varázslat használatához.
     * @return volt-e elég mannája a hősnek
     */
    protected boolean mannaAr() {
        if(this.hos.getManna() < this.manna) {
            System.err.println("Hibas mannaellenorzes Jatek.java-ban!");
            return false;
        }
        this.hos.setManna(this.hos.getManna() - this.manna);
        uzenet("MANNA", this.manna + "");
        return true;
    }

    public void megvasarol() {
        this.megvan = true;
    }

    public boolean isMegvan() {
        return this.megvan;
    }

    @Override
    public String toString() {
        return this.nev + " (" + this.manna + "): " + this.leiras;
    }
}
