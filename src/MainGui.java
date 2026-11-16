import View.MainFrame;

import javax.swing.SwingUtilities;

public class MainGui {
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {

        }
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
