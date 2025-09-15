import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GameBoard extends JPanel {

    private final int rows;
    private final int cols;
    private final int mines;
    private final Cell[][] grid;
    private int totalSafeCells=0;
    private int revealedSafeCells=0;


    public GameBoard(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.setLayout(new GridLayout(rows, cols));
        grid = new Cell[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = new Cell();
                grid[r][c] = cell;
                add(cell);

                int rr = r, cc = c;
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            reveal(rr, cc);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            toggleFlag(rr, cc);
                        }
                    }
                });
            }
        }

        placeMines();
        calculateNeighbors();

        // conta le celle sicure
        totalSafeCells = rows * cols - mines;
        revealedSafeCells = 0;
    }

    private void placeMines() {
        Random rand = new Random();
        int placed = 0;
        while (placed < mines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if (!grid[r][c].hasMine) {
                grid[r][c].hasMine = true;
                placed++;
            }
        }
    }

    private void calculateNeighbors() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c].hasMine) continue;
                int count = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = r + dr, nc = c + dc;
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                            if (grid[nr][nc].hasMine) count++;
                        }
                    }
                }
                grid[r][c].neighborMines = count;
            }
        }
    }

    private void reveal(int r, int c) {
        Cell cell = grid[r][c];
        if (cell.revealed || cell.getText().equals("F")) return;
    
        cell.revealed = true;
    
        if (cell.hasMine) {
            cell.setText("*");
            int scelta = JOptionPane.showOptionDialog(
        this,
        "Hai perso! Vuoi iniziare una nuova partita?",
        "Game Over",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.INFORMATION_MESSAGE,
        null,
        new String[]{"Nuova partita", "Esci"},
        "Nuova partita"
        );

        revealAll();

        if (scelta == JOptionPane.YES_OPTION) {
            SwingUtilities.getWindowAncestor(this).dispose(); // chiude la finestra attuale
            Minesweeper.main(null); // riavvia il gioco
        } else {
            System.exit(0); // termina completamente
        }
        return;


        }
        revealedSafeCells++;
        checkWin();
    
        if (cell.neighborMines > 0) {
            cell.setText(String.valueOf(cell.neighborMines));
            cell.setBackground(Color.LIGHT_GRAY);
            cell.setEnabled(true); // 🔹 rimane abilitato così il numero si vede
        } else {
            // Caso vuoto → cella bianca senza puntini
            cell.setText("");
            cell.setEnabled(false);
            cell.setBackground(Color.WHITE);
    
            // Rivela anche i vicini
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int nr = r + dr, nc = c + dc;
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                        if (!grid[nr][nc].revealed) reveal(nr, nc);
                    }
                }
            }
        }
    }
    

    private void toggleFlag(int r, int c) {
        Cell cell = grid[r][c];
        if (!cell.revealed) {
            if (cell.getText().equals("F")) {
                cell.setText("");
            } else {
                cell.setText("F");
            }
        }
    }

    private void revealAll() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c].hasMine) grid[r][c].setText("*");
            }
        }
    }
    private void checkWin() {
        if (revealedSafeCells == totalSafeCells) {
            revealAll();

            int scelta = JOptionPane.showOptionDialog(
                this,
                "Hai vinto! Vuoi iniziare una nuova partita?",
                "Vittoria 🎉",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Nuova partita", "Esci"},
                "Nuova partita"
            );

            if (scelta == JOptionPane.YES_OPTION) {
                SwingUtilities.getWindowAncestor(this).dispose();
                Minesweeper.main(null);
            } else {
                System.exit(0);
            }
        }
    }
}