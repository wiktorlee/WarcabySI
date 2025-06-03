import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GraPanel extends JPanel {

    private Gra gra;
    private AI ai;
    private int rozmiarPola;
    private int zaznaczoneX = -1;
    private int zaznaczoneY = -1;
    private List<Ruch> dostepneRuchy;
    private boolean graczGraBialymi;

    public GraPanel(Gra gra, int szerokosc, int wysokosc, boolean graczGraBialymi) {
        this.gra = gra;
        this.ai = new AI();
        this.rozmiarPola = Math.min(szerokosc, wysokosc) / Plansza.ROZMIAR;
        this.graczGraBialymi = graczGraBialymi;


        if (!graczGraBialymi) {
            Ruch ruchAI = ai.wybierzNajlepszyRuchMiniMax(gra);
            if (ruchAI != null) {
                gra.wykonajRuchKomputera(ruchAI);
                repaint();

                if (gra.isKoniecGry()) {
                    pokazKoniec();
                }
            }
        }

        addMouseListener(new MouseAdapter() {                // Obsługa kliknięć myszy
            @Override
            public void mousePressed(MouseEvent e) {
                if (!gra.isTuraGracza()) {
                    return;
                }

                int x = e.getX() / rozmiarPola;
                int y = e.getY() / rozmiarPola;
                Pionek p = gra.getPlansza().getPionek(x, y);

                if (zaznaczoneX == -1 && p != null && p.getWlasciciela() == Pionek.Strona.GRACZ) {
                    // sprawdzenie czy jakikolwiek pionek gracza ma bicie
                    List<Ruch> wszystkieRuchy = gra.getPlansza().getRuchyStrony(Pionek.Strona.GRACZ);
                    boolean istniejeBicie = false;

                    for (Ruch r : wszystkieRuchy) {
                        if (r.isBicie()) {
                            istniejeBicie = true;
                            break;
                        }
                    }


                    List<Ruch> ruchyTegoPionka = gra.getPlansza().getRuchyPionka(x, y);

                    if (!istniejeBicie) {
                        // jeśli nie ma żadnego bicia, zaznacz pionek normalnie
                        zaznaczoneX = x;
                        zaznaczoneY = y;
                        dostepneRuchy = ruchyTegoPionka;
                        repaint();
                    } else {
                        // Jeśli są bicia – sprawdzamy, czy ten pionek ma bicie
                        List<Ruch> tylkoBicia = new ArrayList<>();
                        for (Ruch r : ruchyTegoPionka) {
                            if (r.isBicie()) {
                                tylkoBicia.add(r);
                            }
                        }

                        if (!tylkoBicia.isEmpty()) {
                            // zaznaczamy pionek tylko wtedy kiedy ma bicie
                            zaznaczoneX = x;
                            zaznaczoneY = y;
                            dostepneRuchy = tylkoBicia;
                            repaint();
                        }
                        // jesli nie ma bicia przy przymusie bicia, to ignorujemy
                    }
                }


                //czy kliknięcie to legalny ruch
                else if (zaznaczoneX != -1) {
                    for (Ruch r : dostepneRuchy) {
                        if (r.getToX() == x && r.getToY() == y) {
                            boolean koniecTury = gra.wykonajRuchGracza(r);
                            repaint();

                            if (gra.isKoniecGry()) {
                                pokazKoniec();
                                return;
                            }
                            //czy gracz ma dalsze bicia do wykonania
                            if (!koniecTury) {
                                zaznaczoneX = r.getToX();
                                zaznaczoneY = r.getToY();

                                List<Ruch> kolejne = gra.getPlansza().getRuchyPionka(zaznaczoneX, zaznaczoneY);
                                List<Ruch> tylkoBicia = new ArrayList<>();

                                for (Ruch ruch : kolejne) {
                                    if (ruch.isBicie()) {
                                        tylkoBicia.add(ruch);
                                    }
                                }

                                if (!tylkoBicia.isEmpty()) {
                                    dostepneRuchy = tylkoBicia;
                                    repaint();
                                    return;
                                }
                            }
                            // Koniec tury gracza
                            zaznaczoneX = -1;
                            zaznaczoneY = -1;
                            dostepneRuchy = null;

                            Ruch ruchAI = ai.wybierzNajlepszyRuchMiniMax(gra);
                            if (ruchAI != null) {
                                gra.wykonajRuchKomputera(ruchAI);
                                repaint();

                                if (gra.isKoniecGry()) {
                                    pokazKoniec();
                                    return;
                                }
                            }

                            return;
                        }
                    }

                    zaznaczoneX = -1;
                    zaznaczoneY = -1;
                    dostepneRuchy = null;
                    repaint();
                }
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(rozmiarPola * Plansza.ROZMIAR, rozmiarPola * Plansza.ROZMIAR);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color jasnePole = new Color(222, 184, 135);
        Color ciemnePole = new Color(139, 69, 19);

        for (int y = 0; y < Plansza.ROZMIAR; y++) {
            for (int x = 0; x < Plansza.ROZMIAR; x++) {
                if ((x + y) % 2 == 0) {
                    g.setColor(jasnePole);
                } else {
                    g.setColor(ciemnePole);
                }

                g.fillRect(x * rozmiarPola, y * rozmiarPola, rozmiarPola, rozmiarPola);

                Pionek p = gra.getPlansza().getPionek(x, y);
                if (p != null) {
                    boolean czyGracz = (p.getWlasciciela() == Pionek.Strona.GRACZ);

                    if (czyGracz == graczGraBialymi) {
                        g.setColor(Color.WHITE);
                    } else {
                        g.setColor(Color.BLACK);
                    }

                    g.fillOval(x * rozmiarPola + 10, y * rozmiarPola + 10,
                            rozmiarPola - 20, rozmiarPola - 20);

                    if (p.isDamka()) {
                        g.setColor(Color.RED);
                        g.drawString("D", x * rozmiarPola + rozmiarPola / 2 - 4,
                                y * rozmiarPola + rozmiarPola / 2 + 4);
                    }
                }
            }
        }

        if (dostepneRuchy != null) {
            g.setColor(Color.GREEN);
            for (Ruch r : dostepneRuchy) {
                g.drawRect(r.getToX() * rozmiarPola + 5,
                        r.getToY() * rozmiarPola + 5,
                        rozmiarPola - 10, rozmiarPola - 10);
            }
        }
    }

    private void pokazKoniec() {
        if (gra.isRemis()) {
            JOptionPane.showMessageDialog(this,
                    "Gra zakończona remisem – żadna ze stron nie ma możliwych ruchów.",
                    "Koniec gry!",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            boolean komputerNieMaRuchu = gra.getPlansza()
                    .getRuchyStrony(Pionek.Strona.KOMPUTER).isEmpty();

            if (komputerNieMaRuchu) {
                JOptionPane.showMessageDialog(this,
                        "Gratulacje! Wygrałeś!",
                        "Koniec gry!",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Porażka! Komputer wygrał.",
                        "Koniec gry!",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        System.exit(0);
    }
}
