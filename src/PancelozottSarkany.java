/**
 * A páncélozott sárkány egység statjainak a beállítása. Speciális életerő-setter metódussal rendelkezik.
 */

public class PancelozottSarkany extends RepuloEgyseg {
    public final static int PANCEL_VEDELEM = 2;

    public PancelozottSarkany(Hos hos) {
        super(UserString.getString("PANCELOZOTTSARKANYNEV"), 14, 4, 6, 23, 3, 5, hos, UserString.getString("PANCELOZOTTSARKANYLEIRAS", PANCEL_VEDELEM + ""));
    }

    /**
     * A hagyományos életerő-beállítás, csak ha az életerő változása sebződést feltételez, alkalmazódik a speciális képessége (sebzéscsökkentés).
     * @param eletero új életerőérték
     */
    @Override
    public void setEletero(int eletero) {
        int vedelem = 0;
        if(eletero < this.getEletero()) {
            int sebzes = this.getEletero() - eletero;
            sebzes = Math.max(1, sebzes - PANCEL_VEDELEM * (this.getMaxLetszam() - this.getLetszam()));
            vedelem = this.getEletero() - eletero - sebzes;
            uzenet("PANCELOZOTTPIKKELYEK", vedelem + "");
        }
        super.setEletero(eletero + vedelem);
    }
}
