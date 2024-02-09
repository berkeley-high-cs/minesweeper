import java.awt.*;
import java.awt.Graphics2D;

public class Minesweeper extends GridGame {

  private static final boolean debugBombs = false;

  private int numberOfBombs;
  private boolean[][] bombs;
  private boolean [][] revealed;
  private boolean [][] flags;
  private int[][] counts;
  private boolean lost = false;

  private boolean firstClick = true;

  public Minesweeper(int rows, int columns, int numberOfBombs) {
    super(rows, columns, 10);
    this.numberOfBombs = numberOfBombs;
    bombs = new boolean[rows][columns];
    revealed = new boolean[rows][columns];
    flags = new boolean[rows][columns];
    counts = new int[rows][columns];
  }

  // Set up the bombs but make sure there isn't one at r,c
  private void setupBombs(int r, int c) {
    placeBombs(r, c);
    countAllNeighbors();
  }

  public void paintCell(int row, int column, Graphics2D g) {
      if (flags[row][column]) {
        //fillCell(g, Color.RED);
        drawFlag(g);
      } else if ((debugBombs || lost) && isBomb(row, column)) {
        drawBomb(g);
      } else {
        if (revealed[row][column]) {
          fillCell(g, new Color(192, 192, 192));
          if (!bombs[row][column] && counts[row][column] > 0) {
            g.setColor(Color.BLUE);
            g.setFont(g.getFont().deriveFont((float)(cellHeight() * 3 / 4)));
            g.drawString("" + counts[row][column], cellWidth() / 3, cellHeight() * 3 / 4);
          }
        } else {
          fillCell(g, Color.GRAY);
        }
      }
  }

  public void cellClicked(int row, int col, boolean rightClicked) {
    if (firstClick) {
      setupBombs(row, col);
      firstClick = false;
    }
    if (rightClicked) {
      flags[row][col] = !flags[row][col];
    } else {
      if (isBomb(row, col)) {
        lost = true;
      } else {
        revealFrom(row, col, true, new boolean[getRows()][getColumns()]);
      }
    }
    repaint();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Private helpers

  private void revealFrom(int r, int c, boolean firstCell, boolean[][] seen) {
    if (!seen[r][c]) {
      seen[r][c] = true;

      if (!isBomb(r, c)) {
        revealed[r][c] = true;
        if (firstCell || counts[r][c] == 0) {
          for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
              if (inBounds(r + i, c + j)) {
                revealFrom(r + i, c + j, false, seen);
              }
            }
          }
        }
      }
    }
  }

  private void fillCell(Graphics2D g, Color color) {
    g.setColor(color);
    g.fillRect(0, 0, cellWidth(), cellHeight());
  }

  private void drawBomb(Graphics2D g) {
    fillCell(g, Color.YELLOW);
    int w = (int) (cellWidth() * 0.75);
    int h = (int) (cellHeight() * 0.75);
    int px = cellWidth() - w;
    int py = cellHeight() - h;
    g.setColor(Color.BLACK);
    g.fillOval(px / 2, py / 2, w, h);
  }

  private void drawFlag(Graphics2D g) {
    int w = cellWidth();
    int h = cellHeight();
    int left = (int)(w * 0.33);
    int right = left + 4;
    int top = (int)(h * 0.25);
    int bottom = (int)(h * 0.85);
    int pointX = (int)(w * 0.8);
    int flagHeight = 8;

    Polygon pole = new Polygon();
    pole.addPoint(left, bottom);
    pole.addPoint(left, top);
    pole.addPoint(right, top);
    pole.addPoint(right, bottom);

    Polygon flag = new Polygon();
    flag.addPoint(right, top);
    flag.addPoint(pointX, top + flagHeight);
    flag.addPoint(left, top + flagHeight * 2);

    fillCell(g, Color.GRAY);
    g.setColor(Color.RED);
    g.fillPolygon(pole);
    g.fillPolygon(flag);
  }

  private void placeBombs(int r, int c) {
    for (int i = 0; i < numberOfBombs; i++) {
      placeBomb(r, c);
    }
  }

  private void placeBomb(int noR, int noC) {
    int d = 1;
    int row = 0;
    int col = 0;
    for (int r = 0; r < getRows(); r++) {
      for (int c = 0; c < getColumns(); c++) {
        if (r != noR || c != noC) {
          if (Math.random() < 1.0 / d++) {
            row = r;
            col = c;
          }
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
