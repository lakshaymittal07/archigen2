import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Archigen - Cost Analysis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        add(new CostPanel());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}