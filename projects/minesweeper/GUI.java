import javax.swing.JFrame;

public class GUI {

  public static final String TITLE = "Grid Game Demo";

  public static void main(String[] args) {

    JFrame frame = new JFrame(TITLE);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Minesweeper(16, 30, 99));
    frame.pack();
    frame.setVisible(true);
    frame.setLocationRelativeTo(null);
  }
}
