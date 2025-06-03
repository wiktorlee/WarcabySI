import javax.swing.*;
import java.awt.*;

public class OknoGry extends JFrame {

    public OknoGry() {
        setTitle("Warcaby");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Image ikona = Toolkit.getDefaultToolkit().getImage(getClass().getResource("PWr.png"));
        setIconImage(ikona);

        Dimension ekran = Toolkit.getDefaultToolkit().getScreenSize();  // pobranie rozmiaru ekranu użytkownika
        int bok = (int) (Math.min(ekran.width, ekran.height) * 0.8);    // obliczenie długości boku planszy


        boolean graczBialy = true;   //panel wyboru koloru
        int wybor = JOptionPane.showOptionDialog(null, "Wybierz kolor pionków:", "Wybór koloru", 0, 3, null, new String[]{"Białe", "Czarne"}, "Białe");

        if (wybor == 1) {
            graczBialy = false;
        }

        Gra gra = new Gra();
        GraPanel panel = new GraPanel(gra, bok, bok, graczBialy);
        add(panel);
        pack();

        setSize(bok + getInsets().left + getInsets().right,bok + getInsets().top + getInsets().bottom);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
