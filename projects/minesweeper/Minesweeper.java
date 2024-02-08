import java.awt.Color;
import java.awt.Graphics2D;

public class Minesweeper extends GridGame {

  private boolean[][] bombs;

  public Minesweeper(int rows, int columns) {
    super(rows, columns, 10);
    bombs = new boolean[rows][columns];
    placeBombs((int)(rows * columns * 0.2));
  }

  public void paintCell(int row, int column, Graphics2D g) {
    if (bombs[row][column]) {
      g.setColor(Color.BLACK);
    } else {
      g.setColor(Color.GRAY);
    }
    g.fillRect(0, 0, cellWidth(), cellHeight());
  }

  public void cellClicked(int row, int col) {
    repaint();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Private helpers

  private void placeBombs(int number) {
    for (int i = 0; i < number; i++) {
      placeBomb();
    }
  }

  private void placeBomb() {
    int d = 1;
    int row = 0;
    int col = 0;
    for (int r = 0; r < getRows(); r++) {
      for (int c = 0; c < getColumns(); c++) {
        if (Math.random() < 1.0 / d++) {
          row = r;
          col = c;
        }
      }
    }
    bombs[row][col] = true;
  }
}
