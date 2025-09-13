import javax.swing.*;

public class Minesweeper {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Campo Minato");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new GameBoard(10, 10, 10)); // griglia 10x10 con 10 mine
            frame.pack();
            frame.setVisible(true);
        });
    }
}
