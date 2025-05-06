package tc.tlouro_c.util;

import javax.swing.*;
import java.awt.*;

public class StyledComponents {

    // Modern Font
    private static final Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);

    // Colors
    private static final Color PRIMARY_COLOR = new Color(60, 120, 200);
    private static final Color TEXT_COLOR = Color.BLACK;

    /**
     * Creates a modern, styled button with fixed size and hover effect placeholder (can be enhanced).
     */
    public static JButton button(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(DEFAULT_FONT);
        button.setPreferredSize(new Dimension(300, 45));
        button.setMaximumSize(new Dimension(300, 45));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates a clean title label.
     */
    public static JLabel titleLabel(String text, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(TITLE_FONT);
        label.setForeground(color);
        label.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        return label;
    }

    /**
     * Creates a simple styled text label.
     */
    public static JLabel textLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(DEFAULT_FONT);
        label.setForeground(color);
        return label;
    }

    public static JLabel statLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Monospaced", Font.BOLD, 13));
        label.setForeground(new Color(200, 100, 100));
        return label;
    }

    public static Component verticalSpace(int height) {
        return Box.createVerticalStrut(height);
    }
}

