package main;

import javax.swing.*;

public class Main 
{
    public static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Launch the StartScreen first
            new StartScreen();
        });
    }

    // This static method creates and shows the game window.
    public static void startGame() {
        frame = new JFrame("Forgotten Shores");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setUndecorated(true); // if we want to get the top bar (like a windowed mod) --> change the boolean parameter to false.

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gamePanel.startGameThread();
    }
}
