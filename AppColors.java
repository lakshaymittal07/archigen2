import java.awt.Color;
import java.awt.Font;

public class AppColors {
    public static final Color PANEL = new Color(24, 26, 32);
    public static final Color SURFACE = new Color(32, 34, 40);
    public static final Color BORDER = new Color(64, 68, 80);
    public static final Color MUTED = new Color(150, 150, 150);
    public static final Color TEXT = new Color(220, 220, 220);
    public static final Color TEXT2 = new Color(200, 200, 200);

    public static final Color BLUE = new Color(70, 130, 180);
    public static final Color GREEN = new Color(46, 204, 113);
    public static final Color AMBER = new Color(255, 193, 7);
    public static final Color RED = new Color(231, 76, 60);

    public static final Font F_SMALL = new Font("Segoe UI", Font.PLAIN, 10);
    public static final Font F_BOLD_SM = new Font("Segoe UI", Font.BOLD, 11);

    public static Color alpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }
}