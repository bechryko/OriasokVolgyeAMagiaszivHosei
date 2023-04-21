/**
 * A grifflovag egység statjainak a beállítása. Speciális sebződést kezelő metódusokkal rendelkezik.
 */

public class Griff extends RepuloEgyseg {
    public Griff(Hos hos) {
        super(UserString.getString("GRIFFNEV"), 22, 4, 5, 30, 6, 10, hos, UserString.getString("GRIFFLEIRAS"));
    }

    /**
     * A hagyományos sebződést megvalósító metódus, csak utána a visszatámadás engedélyezése, amennyiben előtte is engedélyezve volt.
     * @param sebzes kapott sebzés mértéke
     * @param kritikus kritikus-e a támadás
     * @param kitol a támadó egység
     */
    @Override
    public void sebzodik(int sebzes, boolean kritikus, Egyseg kitol) {
        boolean visszatamadhat = this.visszatamad;
        super.sebzodik(sebzes, kritikus, kitol);
        this.visszatamad = visszatamadhat;
    }

    /**
     * Effekttel ellátott sebzés elszenvedése, majd speciális képesség aktiválása.
     * @param effekt a sebzésen lévő effekt típusa
     */
    @Override
    public void sebzodik(int sebzes, boolean kritikus, Egyseg kitol, Effekt effekt) {
        boolean visszatamadhat = this.visszatamad;
        super.sebzodik(sebzes, kritikus, kitol, effekt);
        this.visszatamad = visszatamadhat;
    }
}
