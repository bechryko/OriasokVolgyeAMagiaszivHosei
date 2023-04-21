/**
 * A mérgező sárkány egység statjainak a beállítása. Speciális támadó metódussal rendelkezik.
 */

public class MergezoSarkany extends RepuloEgyseg {
    public MergezoSarkany(Hos hos) {
        super(UserString.getString("MERGEZOSARKANYNEV"), 15, 5, 6, 15, 5, 11, hos, UserString.getString("MERGEZOSARKANYLEIRAS"));
    }

    /**
     * Mérgező effekttel ellátott hagyományos támadás.
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
        celpont.sebzodik(sebzes, kritikus, this, Effekt.mergezo);
    }
}
