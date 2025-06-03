import java.util.List;

public class Gra {

    private Plansza plansza;
    private boolean turaGracza; // czy obecnie jest tura gracza (true) czy komputera (false)

    public Gra() {
        this.plansza = new Plansza();
        this.turaGracza = true;
    }

    public Plansza getPlansza() {
        return plansza;
    }

    public boolean isTuraGracza() {
        return turaGracza;
    }

    public boolean isKoniecGry() {
        List<Ruch> ruchyGracza = plansza.getRuchyStrony(Pionek.Strona.GRACZ);
        List<Ruch> ruchyKomputera = plansza.getRuchyStrony(Pionek.Strona.KOMPUTER);
        return ruchyGracza.isEmpty() || ruchyKomputera.isEmpty();
    }

    public boolean isRemis() {
        List<Ruch> ruchyGracza = plansza.getRuchyStrony(Pionek.Strona.GRACZ);
        List<Ruch> ruchyKomputera = plansza.getRuchyStrony(Pionek.Strona.KOMPUTER);
        return ruchyGracza.isEmpty() && ruchyKomputera.isEmpty();
    }

    // ruch gracza; Zwraca true, jeśli ruch kończy turę; false jeśli gracz musi kontynuować bicie
    public boolean wykonajRuchGracza(Ruch ruch) {
        Pionek pionek = plansza.getPionek(ruch.getFromX(), ruch.getFromY());
        plansza.setPionek(ruch.getFromX(), ruch.getFromY(), null);
        plansza.setPionek(ruch.getToX(), ruch.getToY(), pionek);

        List<int[]> zbite = ruch.getZbitePionki(); // usunięcie zbitych pionków
        for (int i = 0; i < zbite.size(); i++) {
            int[] z = zbite.get(i);
            plansza.setPionek(z[0], z[1], null);
        }

        // awans na damkę
        if (pionek.getWlasciciela() == Pionek.Strona.GRACZ) {
            if (ruch.getToY() == 0) {
                pionek.promoteDamke();
            }
        }

        // sprawdzenie, czy możliwe jest kolejne bicie tym samym pionkiem
        if (ruch.isBicie()) {
            List<Ruch> kolejne = plansza.getRuchyPionka(ruch.getToX(), ruch.getToY());
            for (int i = 0; i < kolejne.size(); i++) {
                if (kolejne.get(i).isBicie()) {
                    return false; // gracz musi kontynuować bicie
                }
            }
        }

        turaGracza = false;
        return true;
    }

    // ruch komputera
    public void wykonajRuchKomputera(Ruch ruch) {
        Pionek pionek = plansza.getPionek(ruch.getFromX(), ruch.getFromY());
        plansza.setPionek(ruch.getFromX(), ruch.getFromY(), null);
        plansza.setPionek(ruch.getToX(), ruch.getToY(), pionek);

        List<int[]> zbite = ruch.getZbitePionki();
        for (int i = 0; i < zbite.size(); i++) {
            int[] z = zbite.get(i);
            plansza.setPionek(z[0], z[1], null);
        }

        if (pionek.getWlasciciela() == Pionek.Strona.KOMPUTER) {
            if (ruch.getToY() == Plansza.ROZMIAR - 1) {
                pionek.promoteDamke();
            }
        }

        if (ruch.isBicie()) {
            List<Ruch> kolejne = plansza.getRuchyPionka(ruch.getToX(), ruch.getToY());
            for (int i = 0; i < kolejne.size(); i++) {
                Ruch r = kolejne.get(i);
                if (r.isBicie()) {
                    wykonajRuchKomputera(r);
                    return;
                }
            }
        }

        turaGracza = true;
    }
}
