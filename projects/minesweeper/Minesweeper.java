import java.awt.Color;
import java.awt.Graphics2D;

public class Minesweeper extends GridGame {

  private boolean[][] bombs;

  public Minesweeper(int rows, int columns) {
    super(rows, columns, 10);
    bombs = new boolean[rows][columns];
  }

  public void paintCell(int row, int column, Graphics2D g) {
    g.setColor(Color.GRAY);
    g.fillRect(0, 0, cellWidth(), cellHeight());
  }

  public void cellClicked(int row, int col) {
    repaint();
  }
}
