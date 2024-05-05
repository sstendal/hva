package hva;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class Hva {

    public static void main(String args[]) {
        try {

            //System.setProperty("apple.awt.UIElement", "true");

            Log.info("Starting Hva");

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("swing.boldMetal", false);

            OutputFile.create();

            Controller controller = new Controller();

            if (SystemTray.isSupported()) {
                SwingUtilities.invokeLater(() -> HvaTrayIcon.createAndShow(controller));
            } else {
                throw new RuntimeException("System tray is not supported");
            }

        } catch (Exception e) {
            String msg = "System error";
            Log.error(msg, e);
            JOptionPane.showMessageDialog(null, msg, "Hva?", ERROR_MESSAGE);
            System.exit(1);
        }
    }

}
