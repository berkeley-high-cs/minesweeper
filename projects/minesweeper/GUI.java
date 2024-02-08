import javax.swing.JFrame;

public class GUI {

  public static final String TITLE = "Grid Game Demo";

  public static void main(String[] args) {

    // Make a JFrame, i.e. the main window of your application.
    JFrame frame = new JFrame(TITLE);

    // Without this line, the program won't quit even if you close the window.
    // In some applications where you open and close windows you wouldn't want
    // to quit just because the window closes but in this case we do.
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Set it to a specific size. There are other ways to control the size of
    // the window, such as making it fill the screen or making it just big
    // enough to hold the components added to it.
    frame.setSize(400, 400);

    // Use this if you want your window to fill up the screen.
    // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    // Use this to make the frame size to fit the components we put in it.
    // frame.pack()

    // Add an instance of your actual game class here.
    frame.add(new Demo(6, 6));

    // Make the frame actually appear.
    frame.setVisible(true);
  }
}
