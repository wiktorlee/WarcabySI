import java.util.ArrayList;
import java.util.List;

public class Plansza {

    private Pionek[][] pola;                // Tablica 8x8 przechowująca pionki na planszy
    public static final int ROZMIAR = 8;

    public Plansza() {
        pola = new Pionek[ROZMIAR][ROZMIAR];
        setPoczatkoweUlozenie();
    }

    public void setPoczatkoweUlozenie() {
        int x = 0;
        int y = 0;

        while (y < ROZMIAR) {
            x = 0;
            while (x < ROZMIAR) {
                pola[y][x] = null;                  // Ustawianie pionków w standardowym początkowym układzie
                x = x + 1;
            }
            y = y + 1;
        }

        y = 0;
        while (y < 3) {
            x = 0;
            while (x < ROZMIAR) {
                int suma = x + y;
                if (suma % 2 == 1) {
                    Pionek p = new Pionek(Pionek.Strona.KOMPUTER);
                    pola[y][x] = p;
                }
                x = x + 1;
            }
            y = y + 1;
        }

        y = 5;
        while (y < ROZMIAR) {
            x = 0;
            while (x < ROZMIAR) {
                int suma = x + y;
                if (suma % 2 == 1) {
                    Pionek p = new Pionek(Pionek.Strona.GRACZ);
                    pola[y][x] = p;
                }
                x = x + 1;
            }
            y = y + 1;
        }
    }

    public Pionek getPionek(int x, int y) {
        return pola[y][x];
    }

    public void setPionek(int x, int y, Pionek pionek) {
        pola[y][x] = pionek;
    }

    public boolean czyPoleNaPlanszy(int x, int y) {
        if (x >= 0 && x < ROZMIAR && y >= 0 && y < ROZMIAR) {
            return true;
        }
        return false;
    }

    public List<Ruch> getRuchyPionka(int x, int y) {
        List<Ruch> lista = new ArrayList<Ruch>();            // lista możliwych ruchów pionka
        Pionek p = getPionek(x, y);

        if (p == null) {
            return lista;
        }

        int[][] kierunki;

        if (p.isDamka()) {
            kierunki = new int[4][2];

            kierunki[0][0] = -1;
            kierunki[0][1] = -1;

            kierunki[1][0] = 1;                  // możliwe ruchy
            kierunki[1][1] = -1;

            kierunki[2][0] = -1;
            kierunki[2][1] = 1;

            kierunki[3][0] = 1;
            kierunki[3][1] = 1;

        } else {
            if (p.getWlasciciela() == Pionek.Strona.GRACZ) {
                kierunki = new int[2][2];

                kierunki[0][0] = -1;
                kierunki[0][1] = -1;

                kierunki[1][0] = 1;
                kierunki[1][1] = -1;

            } else {
                kierunki = new int[2][2];

                kierunki[0][0] = -1;
                kierunki[0][1] = 1;

                kierunki[1][0] = 1;
                kierunki[1][1] = 1;
            }
        }

        int i = 0;
        while (i < kierunki.length) {
            int dx = kierunki[i][0];
            int dy = kierunki[i][1];

            int mx = x + dx;
            int my = y + dy;
            int tx = x + 2 * dx;
            int ty = y + 2 * dy;
                                                                // sprawdzanie bicia
            if (czyPoleNaPlanszy(tx, ty)) {
                Pionek srodek = getPionek(mx, my);
                if (srodek != null) {
                    if (srodek.getWlasciciela() != p.getWlasciciela()) {
                        if (getPionek(tx, ty) == null) {
                            Ruch r = new Ruch(x, y, tx, ty);
                            r.setBicie();
                            r.addZbitego(mx, my);
                            lista.add(r);
                        }
                    }
                }
            }

            i = i + 1;
        }

        if (lista.isEmpty() == false) {
            return lista;
        }

        i = 0;
        while (i < kierunki.length) {
            int dx = kierunki[i][0];
            int dy = kierunki[i][1];

            int nx = x + dx;
            int ny = y + dy;

            if (czyPoleNaPlanszy(nx, ny)) {
                if (getPionek(nx, ny) == null) {
                    Ruch r = new Ruch(x, y, nx, ny);
                    lista.add(r);
                }
            }

            i = i + 1;
        }

        return lista;
    }

    public List<Ruch> getRuchyStrony(Pionek.Strona strona) {
        List<Ruch> wszystkie = new ArrayList<Ruch>();
        List<Ruch> bicia = new ArrayList<Ruch>();

        int y = 0;
        while (y < ROZMIAR) {
            int x = 0;
            while (x < ROZMIAR) {
                Pionek p = getPionek(x, y);

                if (p != null) {
                    if (p.getWlasciciela() == strona) {
                        List<Ruch> ruchy = getRuchyPionka(x, y);

                        int i = 0;
                        while (i < ruchy.size()) {
                            Ruch r = ruchy.get(i);

                            if (r.isBicie()) {
                                bicia.add(r);
                            } else {
                                wszystkie.add(r);
                            }

                            i = i + 1;
                        }
                    }
                }

                x = x + 1;
            }
            y = y + 1;
        }

        if (bicia.isEmpty() == false) {
            return bicia;
        } else {
            return wszystkie;
        }
    }
}
