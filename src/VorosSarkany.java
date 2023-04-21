/**
 * A vörös sárkány egység statjainak a beállítása. Speciális támadó metódussal rendelkezik.
 */

public class VorosSarkany extends RepuloEgyseg {
    public VorosSarkany(Hos hos) {
        super(UserString.getString("VOROSSARKANYNEV"), 18, 6, 9, 25, 5, 7, hos, UserString.getString("VOROSSARKANYLEIRAS"));
    }

    /**
     * Tüzes effekttel ellátott hagyományos támadás.
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
        celpont.sebzodik(sebzes, kritikus, this, Effekt.tuzes);
    }
}
