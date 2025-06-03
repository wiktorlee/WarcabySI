public class Pionek {

    public enum Strona {
        GRACZ,
        KOMPUTER
    }

    private Strona wlasciciel;
    private boolean damka;

    public Pionek(Strona wlasciciel) {
        this.wlasciciel = wlasciciel;
        this.damka = false;
    }

    public Strona getWlasciciela() {
        return wlasciciel;
    }

    public boolean isDamka() {
        return damka;
    }

    public void promoteDamke() {
        damka = true;
    }
}