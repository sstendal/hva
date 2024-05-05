package hva;

import hva.config.ConfigLoader;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class HvaTrayIcon {

    public static void createAndShow(Controller controller) {

        if (!SystemTray.isSupported()) {
            System.err.println("SystemTray is not supported");
            return;
        }

        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(createImage("/images/eye_64.png", "Hva"));
        final SystemTray tray = SystemTray.getSystemTray();


        popup.add(createAboutMenuItem(controller));

        popup.addSeparator();

        popup.add(createShowInputMenuItem(controller));
        popup.add(createOpenFileMenuItem(controller));
        popup.add(createOpenConfigFileMenuItem(controller));

        popup.addSeparator();

        popup.add(createExitMenuItem(tray, trayIcon, controller));

        trayIcon.setPopupMenu(popup);
        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("TrayIcon could not be added.");
        }

    }

    private static MenuItem createExitMenuItem(SystemTray tray, TrayIcon trayIcon, Controller controller) {
        MenuItem item = new MenuItem("Avslutt");
        item.addActionListener(e -> {
            tray.remove(trayIcon);
            controller.onExit();
        });
        return item;
    }

    private static MenuItem createAboutMenuItem(Controller controller) {

        MenuItem item = new MenuItem("Om Hva");
        item.addActionListener(e -> controller.onShowAbout());

        return item;
    }

    private static MenuItem createOpenFileMenuItem(Controller controller) {

        MenuItem item = new MenuItem("Mine registreringer");
        item.addActionListener(e -> OutputFile.openDataFile());

        return item;
    }

    private static MenuItem createOpenConfigFileMenuItem(Controller controller) {

        MenuItem item = new MenuItem("Config");
        item.addActionListener(e -> ConfigLoader.openConfigFile());

        return item;
    }

    private static MenuItem createShowInputMenuItem(Controller controller) {

        MenuItem item = new MenuItem("Åpne dialogen");
        item.addActionListener(e -> controller.onShowInput());

        return item;
    }

    static Image createImage(String path, String description) {
        URL imageURL = HvaTrayIcon.class.getResource(path);
        return (new ImageIcon(imageURL, description)).getImage();
    }
}
