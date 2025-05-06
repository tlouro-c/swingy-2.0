package tc.tlouro_c.util;

import javax.swing.*;
import java.awt.*;

public class Window {

    private JFrame frame;
    public static final int VH = 720;
    public static final int VW = 960;

    public JFrame getFrame() {
        return frame;
    }

    public void mountNewFrame() {
        frame = new JFrame("Swingy By tlouro-c");
        frame.setSize(VW, VH);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
//        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void disposeFrame() {
        if (frame != null) {
            frame.dispose();
            frame = null;
        }
    }

    private Window() {
    }

    private static class Holder {
        private static final Window INSTANCE = new Window();
    }

    public static Window getInstance() {
        return Holder.INSTANCE;
    }

}
