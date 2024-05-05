package hva;

import hva.config.ConfigLoader;

import javax.swing.*;
import java.time.LocalDateTime;

public class Controller {

    private LocalDateTime last;
    private HvaTimer timer;
    private Dialog dialog;

    public Controller() {
        last = LocalDateTime.now();
        dialog = new Dialog(this);
        timer = new HvaTimer(this::onShowInput);
        this.onShowInput();
    }

    public void onInput(String text) {
        if (text == null || text.length() == 0 || ChangeDelayCommand.executeCommand(text, this::onSetDelay)) {
            dialog.resetTextField();
        } else {
            dialog.hideDialog();
            LocalDateTime now = LocalDateTime.now();
            OutputFile.recordEntry(text, last, now);
            last = now;
            timer.start();
        }
    }

    public void onCancel() {
        dialog.setVisible(false);
        timer.start();
    }

    public void onExit() {
        Log.info("Quit");
        System.exit(0);
    }

    public void onShowAbout() {
        String about = "\"Hva\" brukes til å huske hva du har jobbet med slik at det \nblir lettere å føre timer på slutten av dagen eller slutten av uka.\nHver gang \"Hva\" dukker opp kan du skrive et stikkord om \nhjelper deg å huske hva du har gjort siden sist.";
        String version = "Versjon 4.0";
        String author = "Sigurd Stendal, sigurd@stendal.io";

        String text = "\n" + about + "\n\n" + version + "\n\n" + author;
        infoMessage(text);
    }

    public void onSetDelay(int delay, String delayAsText) {
        timer.setCustomDelay(delay);
        infoMessage("Endrer intervallet til " + delayAsText);
        ConfigLoader.setDelay(delay);
    }

    public void onShowInput() {
        if (!dialog.isVisible()) {
            Log.info("Show dialog");
            dialog.showDialog(last);
        }
    }

    private void infoMessage(String message) {
        dialog.setSuspended(true);
        ImageIcon image = new ImageIcon(getClass().getResource("/images/eye_128.png"));

        JOptionPane.showMessageDialog(null, message, "Hva", JOptionPane.INFORMATION_MESSAGE, image);
        dialog.setSuspended(false);
    }


}
