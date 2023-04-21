/**
 * A gyémántóriás egység statjainak a beállítása. Speciális sebződést kezelő metódusokkal rendelkezik.
 */

public class Gyemantorias extends Egyseg {
    public Gyemantorias(Hos hos) {
        super(UserString.getString("GYEMANTORIASNEV"), 80, 18, 22, 160, 2, 1, hos, UserString.getString("GYEMANTORIASLEIRAS"));
    }

    /**
     * Kritikus támadás elszenvedése esetén csak visszatámadás, különben a hagyományos sebződést megvalósító metódus használata.
     * @param sebzes kapott sebzés mértéke
     * @param kritikus kritikus-e a támadás
     * @param kitol a támadó egység
     */
    @Override
    public void sebzodik(int sebzes, boolean kritikus, Egyseg kitol) {
        if(kritikus) {
            uzenet("GYEMANTBOR", kitol.nev);
            if(this.visszatamad) {
                this.visszaTamad(kitol);
                this.visszatamad = false;
            }
        } else {
            super.sebzodik(sebzes, false, kitol);
        }
    }

    /**
     * Effekttel ellátott sebzés elszenvedése. (Kritikus, távolsági támadás esetén nem csinál semmit.)
     * @param effekt a sebzésen lévő effekt típusa
     */
    @Override
    public void sebzodik(int sebzes, boolean kritikus, Egyseg kitol, Effekt effekt) {
        if(kritikus) {
            uzenet("GYEMANTBOR", kitol.nev);
            if(this.visszatamad && effekt != Effekt.tavolsagi) {
                this.visszaTamad(kitol);
                this.visszatamad = false;
            }
        } else {
            super.sebzodik(sebzes, false, kitol, effekt);
        }
    }
}
