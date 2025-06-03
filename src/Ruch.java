import java.util.ArrayList;
import java.util.List;

public class Ruch {

    private int fromX, fromY;
    private int toX, toY;
    private boolean czyBicie;
    private List<int[]> zbitePionki;

    public Ruch(int fromX, int fromY, int toX, int toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.czyBicie = false;
        this.zbitePionki = new ArrayList<>();
    }

    public void setBicie() {
        this.czyBicie = true;
    }

    public void addZbitego(int x, int y) {
        zbitePionki.add(new int[] {x, y});
    }

    public int getFromX() {
        return fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public int getToX() {
        return toX;
    }

    public int getToY() {
        return toY;
    }

    public boolean isBicie() {
        return czyBicie;
    }

    public List<int[]> getZbitePionki() {
        return zbitePionki;
    }
}
