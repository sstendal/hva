package hva;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Dialog extends JFrame {

    private static final String SKIN_FILE = "hva.jpg";
    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    private final DialogCanvas canvas;
    private final History history = new History();
    private boolean suspended = false;
    private boolean shouldBeVisible = false;


    public Dialog(Controller controller) {

        canvas = new DialogCanvas(skin());

        setUndecorated(true);

        setIcon();

        setLayout();

        setPosition();

        getRootPane().setDefaultButton(canvas.okButton);

        addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                canvas.inputText.requestFocusInWindow();
            }

            public void focusLost(FocusEvent focusevent) {
            }

        });

        canvas.cancelButton.addActionListener(e -> {
            history.setPositionToLast();
            controller.onCancel();
            canvas.setTextField("");
        });

        canvas.okButton.addActionListener(e -> {
            String text = canvas.inputText.getText();
            history.add(text);
            controller.onInput(text);
            canvas.setTextField("");
        });

        canvas.inputText.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 38) { // Arrow down
                    history.next();
                }
                if (e.getKeyCode() == 40) { // Arrow up
                    history.previous();
                }
                if (e.getKeyCode() == 40 || e.getKeyCode() == 38) {
                    Optional<String> historyText = history.get();
                    canvas.setTextField(historyText.orElseGet(() -> ""));
                }
                super.keyPressed(e);
            }

            public void keyTyped(KeyEvent evt) {
                if (evt.getKeyChar() == '\033') {
                    history.setPositionToLast();
                    controller.onCancel();
                    canvas.setTextField("");
                }
            }

        });
    }

    private void setPosition() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getSize().width) / 2, (screenSize.height - getSize().height) / 2);
    }

    private Optional<Image> skin() {
        if(new File(SKIN_FILE).exists()) {
            return Optional.of(Toolkit.getDefaultToolkit().createImage(SKIN_FILE));
        } else {
            return Optional.empty();
        }
    }

    private void setLayout() {
        FlowLayout f = new FlowLayout();
        f.setHgap(0);
        f.setVgap(0);
        getContentPane().setLayout(f);
        getContentPane().add(canvas);
        pack();
    }

    private void setIcon() {
        Image image = new ImageIcon(getClass().getResource("/images/eye_256.png")).getImage();
        this.setIconImage(image);
    }

    public void showDialog(LocalDateTime last) {

        String timeString = last.format(timeFormat);

        canvas.setLabel("Siden " + timeString + " har jeg jobbet med...");

        this.shouldBeVisible = true;
        if(!suspended) {
            this.setAlwaysOnTop(true);
            this.setVisible(true);
            requestFocusInWindow();
        }
    }

    public void hideDialog() {
        this.shouldBeVisible = false;
        this.setVisible(false);
    }

    public void setSuspended(boolean value) {
        this.suspended = value;
        if(!this.suspended && this.shouldBeVisible && !this.isVisible()) {
            this.setAlwaysOnTop(true);
            this.setVisible(true);
            requestFocusInWindow();
        }
        if(this.suspended && this.isVisible()) {
            this.setVisible(false);
        }
    }

    public void resetTextField() {
        canvas.setTextField("");
    }

    private class DialogCanvas extends JPanel {

        private final int DIALOG_WIDTH = 280;
        private final int MARGIN = 20;

        private final int LABEL_HEIGHT = 25;

        private final int TEXTFIELD_HEIGHT = 25;

        private final int BUTTON_HEIGHT = 25;
        private final int BUTTON_WIDTH = 75;
        private final int BUTTON_MARGIN = 10;

        private final int OK_BUTTON_LEFT = DIALOG_WIDTH - MARGIN * 2 - BUTTON_WIDTH * 2 - BUTTON_MARGIN;
        private final int CANCEL_BUTTON_LEFT = OK_BUTTON_LEFT + BUTTON_WIDTH + BUTTON_MARGIN;
        private final int TEXTFIELD_TOP = MARGIN + LABEL_HEIGHT;
        private final int TEXTFIELD_WIDTH = DIALOG_WIDTH - MARGIN - MARGIN;
        private final int LABEL_WIDTH = DIALOG_WIDTH - MARGIN - MARGIN;
        private final int BUTTON_TOP = TEXTFIELD_TOP + TEXTFIELD_HEIGHT + BUTTON_MARGIN;
        private final int DIALOG_HEIGHT = BUTTON_TOP + BUTTON_HEIGHT + MARGIN;

        private final Dimension dimension = new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT);

        private JButton okButton;
        private JButton cancelButton;
        private final JTextField inputText;
        private final Optional<Image> image;
        private final JLabel label;


        public DialogCanvas(Optional<Image> image) {
            setLayout(null);
            this.image = image;

            label = new JLabel();
            label.setForeground(Color.black);
            label.setBounds(MARGIN, MARGIN, LABEL_WIDTH, LABEL_HEIGHT);
            add(label);

            inputText = new JTextField();
            inputText.setBounds(MARGIN, TEXTFIELD_TOP, TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
            add(inputText);

            okButton = new JButton();
            okButton.setText("OK");
            okButton.setBounds(OK_BUTTON_LEFT, BUTTON_TOP, BUTTON_WIDTH, BUTTON_HEIGHT);
            add(okButton);

            cancelButton = new JButton();
            cancelButton.setText("Avbryt");
            cancelButton.setBounds(CANCEL_BUTTON_LEFT, BUTTON_TOP, BUTTON_WIDTH, BUTTON_HEIGHT);
            add(cancelButton);
        }

        public void setLabel(String text) {
            label.setText(text);
        }

        public void setTextField(String text) {
            inputText.setText(text);
            inputText.setSelectionStart(0);
            inputText.setSelectionEnd(inputText.getText().length());
        }

        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            super.paint(g);
        }

        protected void paintComponent(Graphics g) {
            if (image.isPresent()) {
                g.drawImage(image.get(), 0, 0, image.get().getWidth(this), image.get().getHeight(this), this);
            } else {
                super.paintComponent(g);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        }

        public Dimension getPreferredSize() {
            return dimension;
        }


    }


}
