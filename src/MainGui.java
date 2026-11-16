import View.MainFrame;

import javax.swing.SwingUtilities;

public class MainGui {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
