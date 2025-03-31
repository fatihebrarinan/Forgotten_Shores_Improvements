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

    // Constructor
    public StartScreen() {
        try {
            this.image = ImageIO.read(getClass().getResource("/res/StartScreen/StartScreen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        createPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

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
            new CreditScreen(StartScreen.this);
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

        Dimension buttonSize = new Dimension(200, 100);

        this.startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 25));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setPreferredSize(buttonSize);
        startButton.setMaximumSize(buttonSize);
        startButton.setMinimumSize(buttonSize);
        startButton.setFocusable(false);
        startButton.addActionListener(new ClickListener());

        this.creditButton = new JButton("  Credits  ");
        creditButton.setFont(new Font("Arial", Font.BOLD, 25));
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
