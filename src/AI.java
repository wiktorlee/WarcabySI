import java.util.List;

public class AI {

    public Ruch wybierzNajlepszyRuchMiniMax(Gra gra) {
        Plansza plansza = gra.getPlansza();
        List<Ruch> ruchyKomputera = plansza.getRuchyStrony(Pionek.Strona.KOMPUTER);

        Ruch najlepszyRuch = null;
        int najwyzszaOcena = Integer.MIN_VALUE;

        for (Ruch ruch : ruchyKomputera) {
            Plansza kopia = kopiujPlansze(plansza);
            wykonajRuchNaPlanszy(kopia, ruch);

            int ocena = minimax(kopia, 4, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
            // zapis najlepszego ruchu
            if (ocena > najwyzszaOcena) {
                najwyzszaOcena = ocena;
                najlepszyRuch = ruch;
            }
        }

        return najlepszyRuch;
    }

    private int minimax(Plansza plansza, int glebokosc, boolean czyMax, int alfa, int beta) {
        if (glebokosc == 0) {
            return ocenPlansze(plansza);
        }

        List<Ruch> ruchy;
        if (czyMax) {
            ruchy = plansza.getRuchyStrony(Pionek.Strona.KOMPUTER);
        } else {
            ruchy = plansza.getRuchyStrony(Pionek.Strona.GRACZ);
        }

        if (ruchy.isEmpty()) {
            if (czyMax) {
                return Integer.MIN_VALUE;
            } else {
                return Integer.MAX_VALUE;
            }
        }
        // maksymalizacja: tura komputera
        if (czyMax) {
            int najlepszyWynik = Integer.MIN_VALUE;

            for (Ruch ruch : ruchy) {
                Plansza kopia = kopiujPlansze(plansza);
                wykonajRuchNaPlanszy(kopia, ruch);

                int ocena = minimax(kopia, glebokosc - 1, false, alfa, beta);

                if (ocena > najlepszyWynik) {
                    najlepszyWynik = ocena;
                }

                if (ocena > alfa) {
                    alfa = ocena;
                }
                // przycinanie gałęzi
                if (beta <= alfa) {
                    break;
                }
            }

            return najlepszyWynik;

            // minimalizacja: tura gracza
        } else {
            int najlepszyWynik = Integer.MAX_VALUE;

            for (Ruch ruch : ruchy) {
                Plansza kopia = kopiujPlansze(plansza);
                wykonajRuchNaPlanszy(kopia, ruch);

                int ocena = minimax(kopia, glebokosc - 1, true, alfa, beta);

                if (ocena < najlepszyWynik) {
                    najlepszyWynik = ocena;
                }

                if (ocena < beta) {
                    beta = ocena;
                }
                // przycinanie gałęzi
                if (beta <= alfa) {
                    break;
                }
            }

            return najlepszyWynik;
        }
    }
    // ocenianie planszy: im wyższy wynik, tym lepszy stan dla komputera
    private int ocenPlansze(Plansza plansza) {
        int gracz = 0;
        int komputer = 0;

        for (int y = 0; y < Plansza.ROZMIAR; y++) {
            for (int x = 0; x < Plansza.ROZMIAR; x++) {
                Pionek p = plansza.getPionek(x, y);
                if (p != null) {
                    int wartosc = 0;

                    //PUNKTACJE

                    if (p.isDamka()) {
                        wartosc = 3;
                        if (x == 0 || x == Plansza.ROZMIAR - 1) {
                            wartosc = wartosc + 1;
                        }
                    } else {
                        wartosc = 1;
                        if (x == 0 || x == Plansza.ROZMIAR - 1) {
                            wartosc = wartosc + 1;
                        }
                        if (p.getWlasciciela() == Pionek.Strona.KOMPUTER && y >= 5) {
                            wartosc = wartosc + 1;
                        }
                        if (p.getWlasciciela() == Pionek.Strona.GRACZ && y <= 2) {
                            wartosc = wartosc + 1;
                        }
                    }

                    if (p.getWlasciciela() == Pionek.Strona.KOMPUTER) {
                        komputer = komputer + wartosc;
                    } else {
                        gracz = gracz + wartosc;
                    }
                }
            }
        }
        //zwycięstwo jednej ze stron
        if (gracz == 0) {
            return Integer.MAX_VALUE;
        }

        if (komputer == 0) {
            return Integer.MIN_VALUE;
        }

        return komputer - gracz;
    }
    // ruch na przekazanej planszy (bez wpływu na oryginalną planszę)
    private void wykonajRuchNaPlanszy(Plansza plansza, Ruch ruch) {
        Pionek pionek = plansza.getPionek(ruch.getFromX(), ruch.getFromY());
        plansza.setPionek(ruch.getFromX(), ruch.getFromY(), null);
        plansza.setPionek(ruch.getToX(), ruch.getToY(), pionek);

        List<int[]> zbite = ruch.getZbitePionki();
        for (int i = 0; i < zbite.size(); i++) {
            int[] pole = zbite.get(i);
            plansza.setPionek(pole[0], pole[1], null);
        }

        if (pionek.getWlasciciela() == Pionek.Strona.KOMPUTER && ruch.getToY() == Plansza.ROZMIAR - 1) {
            pionek.promoteDamke();
        }
    }
    // tworzenie kopii planszy (do symulacji ruchów bez modyfikacji oryginału)
    private Plansza kopiujPlansze(Plansza oryginal) {
        Plansza kopia = new Plansza();

        for (int y = 0; y < Plansza.ROZMIAR; y++) {
            for (int x = 0; x < Plansza.ROZMIAR; x++) {
                Pionek p = oryginal.getPionek(x, y);
                if (p != null) {
                    Pionek nowy = new Pionek(p.getWlasciciela());
                    if (p.isDamka()) {
                        nowy.promoteDamke();
                    }
                    kopia.setPionek(x, y, nowy);
                }
            }
        }

        return kopia;
    }
}
