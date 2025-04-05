package main;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CreditScreen extends JDialog {

    public static final int FRAME_WIDTH = 200;
    public static final int FRAME_HEIGHT = 200;

    public CreditScreen(StartScreen screen) {
        super(screen, "Credits", true);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(screen);

        setLayout(new GridLayout(5, 1));

        createLabel("Ali Batu Sarica");
        createLabel("Çağan Tanriverdi");
        createLabel("Eren Gürbüz");
        createLabel("Fatih Ebrar İnan");
        createLabel("Kaan Uz");

        setResizable(false);
        setVisible(true);
    }

    private void createLabel(String name) {
        JLabel label = new JLabel(name);
        label.setFont(new Font("Monospaced", Font.PLAIN, 16));
        add(label);
    }
}
