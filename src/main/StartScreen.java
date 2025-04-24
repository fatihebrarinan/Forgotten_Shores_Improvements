package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class StartScreen extends JFrame {

    // Instance variables
    private JPanel panel;
    private JButton startButton;
    private JButton creditButton;
    private BufferedImage image;
    private Font retroFont;

    // Constructor
    public StartScreen() {
        try {
            this.image = ImageIO.read(getClass().getResource("/res/StartScreen/StartScreen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            retroFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/res/fonts/Jersey15-Regular.ttf")).deriveFont(60f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(retroFont);
        createPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setTitle("FORGOTTEN SHORES");
        setResizable(false);
        setVisible(true);
    }

    class ClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Dispose the start screen
            dispose();
            // Launch the game frame using the Main class's static method
            Main.startGame();
        }
    }

    class ClickListener2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new CreditScreen(StartScreen.this , retroFont);
        }

    }

    class Panel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private void createPanel() {
        this.panel = new Panel();
        panel.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        Dimension buttonSize = new Dimension(300, 100);

        this.startButton = new JButton("START GAME");
        startButton.setBackground(new Color(0xFFD34E));
        startButton.setForeground(new Color(0x4E2E1F));
        startButton.setBorder(BorderFactory.createLineBorder(new Color(0x996633), 3));
        startButton.setFont(retroFont);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setPreferredSize(buttonSize);
        startButton.setMaximumSize(buttonSize);
        startButton.setMinimumSize(buttonSize);
        startButton.setFocusable(false);
        startButton.addActionListener(new ClickListener());

        this.creditButton = new JButton("  CREDITS  ");
        creditButton.setBackground(new Color(0xFFD34E));
        creditButton.setForeground(new Color(0x4E2E1F));
        creditButton.setBorder(BorderFactory.createLineBorder(new Color(0x996633), 3));
        creditButton.setFont(retroFont);
        creditButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        creditButton.setPreferredSize(buttonSize);
        creditButton.setMaximumSize(buttonSize);
        creditButton.setMinimumSize(buttonSize);
        creditButton.setFocusable(false);
        creditButton.addActionListener(new ClickListener2());

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(startButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(creditButton);
        centerPanel.add(Box.createVerticalGlue());

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.setOpaque(true);
        this.add(panel);
    }
}
