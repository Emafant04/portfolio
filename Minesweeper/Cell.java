import java.awt.*;
import javax.swing.*;

public class Cell extends JButton {
    boolean hasMine;
    boolean revealed;
    int neighborMines;

    public Cell() {
        super();
        this.hasMine = false;
        this.revealed = false;
        this.neighborMines = 0;
        setPreferredSize(new Dimension(50, 50));
    }
}
