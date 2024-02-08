import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * A super class for grid based games like Concentration, Boggle, Minesweeper,
 * etc. Mostly exists to hide some of the complexity of Swing and allow you to
 * focus on the logic of your application in terms of drawing individual cells
 * in the grid and responding to clicks on cells. Also provides a method for
 * one-shot delayed actions.
 */
public abstract class GridGame extends JPanel {

  private final int rows;
  private final int columns;
  private final int padding;

  /**
   * Construct a GridGame with the given dimensions and padding.
   * @param rows, the number of rows in the grid.
   * @param columns, the number of columns in the grid.
   * @param padding, the padding, in pixels, between cells.
   */
  public GridGame(int rows, int columns, int padding) {
    this.rows = rows;
    this.columns = columns;
    this.padding = padding;

    // This sets things up so when the user clicks the mouse in this component
    // it runs the dispatchMousePress method. The evt argument is created by
    // Swing to represents the event. As you'll see in dispatchMousePress we use
    // to to figure out the x,y coordinates within the component where the click
    // occurred.
    addMouseListener(
      new MouseAdapter() {
        public void mousePressed(MouseEvent evt) {
          dispatchMousePress(evt);
        }
      }
    );
  }

  /**
   * Construct a GridGame with the given dimensions and a padding of 0.
   * @param rows, the number of rows in the grid.
   * @param columns, the number of columns in the grid.
   */
  public GridGame(int rows, int cols) {
    this(rows, cols, 0);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Abstract methods -- implemented by subclasses

  /**
   * Called to draw the given cell.
   * @param row, the row of the cell being drawn
   * @param col, the column of the cell being drawn
   * @param g, the Graphics object to use
   */
  public abstract void paintCell(int row, int col, Graphics2D g);

  /**
   * Called whenever a cell is clicked.
   * @param row, the row of the cell that was clicked.
   * @param col, the column of the cell that was clicked.
   */
  public abstract void cellClicked(int row, int col);


  //////////////////////////////////////////////////////////////////////////////
  // This method lets us be part of the Swing framework.

  /**
   * Paint the component by painting each of the cells using the paintCell
   * method.
   */
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g.create();
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < columns; c++) {
        paintOneCell(r, c, g2);
      }
    }
    g2.dispose();
  }


  //////////////////////////////////////////////////////////////////////////////
  // This method is a utility that we provide for our subclasses to schedule
  // things to happen after a delay

  /**
   * After millis milliseconds run the given Runnable. The simplest way to use
   * this method is something like:
   *
   *   after(500, () -> { doTheThing(); });
   *
   * The () -> {} is a bit of special syntax called a "lambda" which you can use
   * without worrying too much about how it fits into the rest of Java.
   *
   * @param millis, the delay before the Runnable is run
   * @param r, the Runnable to run
   */
  public void after(int millis, Runnable r) {
    Timer timer = new Timer(millis, e -> r.run());
    timer.setRepeats(false);
    timer.start();
  }


  //////////////////////////////////////////////////////////////////////////////
  // Getters. Subclasses may need this information to figure out how to paint a
  // cell.

  /**
   * Get the number of rows in the grid.
   * @return number of rows.
   */
  public int getRows() {
    return rows;
  }

  /**
   * Get the number of columns in the grid.
   * @return number of columns.
   */
  public int getColumns() {
    return columns;
  }

  /**
   * Get the pixels of padding between cells in the grid.
   * @return padding in pixels.
   */
  public int getPadding() {
    return padding;
  }

  /**
   * Get the width of one cell in the grid. Does not include padding between
   * cells.
   * @return the cell width in pixels
   */
  public final int cellWidth() {
    return (getWidth() - (padding * (columns + 1))) / columns;
  }

  /**
   * Get the height of one cell in the grid. Does not include padding between
   * cells.
   * @return the cell height in pixels
   */
  public final int cellHeight() {
    return (getHeight() - (padding * (rows + 1))) / rows;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Private helper methods called from methods above.

  private void dispatchMousePress(MouseEvent evt) {
    // Figure out what cell the mouse click occurred
    int adjX = evt.getX() - padding / 2;
    int adjY = evt.getY() - padding / 2;
    int row = Math.clamp(adjY / (cellHeight() + padding), 0, rows - 1);
    int col = Math.clamp(adjX / (cellWidth() + padding), 0, columns - 1);

    // Call the method with the row and column we figured out so our subclass
    // can do something. This is an abstraction so classes that extend GridGame
    // don't have to do the semi-complicated math above.
    cellClicked(row, col);
  }

  private void paintOneCell(int row, int column, Graphics2D g) {
    // A bit of jiggery-pokery to figure out what area the given cell occupies
    // and then clip the Graphics2D object to just that area so we can pass it
    // into paintCell which the subclass will implement to paint whataver it
    // wants in the cell.
    int x = Math.round(column * (cellWidth() + padding) + padding);
    int y = Math.round(row * (cellHeight() + padding) + padding);

    g.setClip(x, y, cellWidth(), cellHeight());
    g.translate(x, y);
    paintCell(row, column, g);
    g.translate(-x, -y);
  }
}
