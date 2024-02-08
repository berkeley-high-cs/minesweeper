import java.awt.Color;
import java.awt.Graphics2D;

public class Minesweeper extends GridGame {

  private boolean[][] bombs;
  private int[][] counts;
  private boolean lost = false;

  public Minesweeper(int rows, int columns, int numberOfBombs) {
    super(rows, columns, 10);
    bombs = new boolean[rows][columns];
    counts = new int[rows][columns];
    placeBombs(numberOfBombs);
    countAllNeighbors();
  }

  public void paintCell(int row, int column, Graphics2D g) {
    if (lost) {
      g.setColor(Color.RED);
      g.fillRect(0, 0, cellWidth(), cellHeight());
    } else {
      if (isBomb(row, column)) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, cellWidth(), cellHeight());
      } else {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, cellWidth(), cellHeight());
        if (!bombs[row][column]) {
          g.setColor(Color.BLUE);
          g.setFont(g.getFont().deriveFont((float)(cellHeight() * 3 / 4)));
          g.drawString("" + counts[row][column], cellWidth() / 3, cellHeight() * 3 / 4);
        }
      }
    }
  }

  public void cellClicked(int row, int col) {
    if (isBomb(row, col)) {
      lost = true;
    }
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

  private void countAllNeighbors() {
    for (int r = 0; r < getRows(); r++) {
      for (int c = 0; c < getColumns(); c++) {
        counts[r][c] = countNeighbors(r, c);
      }
    }
  }

  private int countNeighbors(int r, int c) {
    int count = 0;
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (i != 0 || j != 0) {
          if (isBomb(r + i, c + j)) {
            count++;
          }
        }
      }
    }
    return count;
  }

  private boolean isBomb(int r, int c) {
    return inBounds(r, c) && bombs[r][c];
  }

  private boolean inBounds(int r, int c) {
    return 0 <= r && r < getRows() && 0 <= c && c < getColumns();
  }
}
