import javax.swing.JFrame;

public class GUI {

  public static final String TITLE = "Grid Game Demo";

  public static void main(String[] args) {

    JFrame frame = new JFrame(TITLE);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.setSize(1000, 1000);

    frame.add(new Minesweeper(16, 16, 40));

    frame.setVisible(true);
    frame.setLocationRelativeTo(null);
  }
}
