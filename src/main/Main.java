package main;

import javax.swing.*;

/* TODO:
Fix player movement.
Add Proper biomes. Fix world generation.
Add "Start new world" option in the main menu that creates a world with a random seed.
Add a way to load saved worlds through the main menu.
Fix object generation in chunks.
Fix mob AI.
Add map that shows the preview of the world when "M" is pressed
Change the player idle so that it looks in the direction that was walked most recently.
Rename and organize Tile folder and tile related classes
Add sound effects
Add mining and pickaxes.
*/

public class Main {
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
        frame.setUndecorated(true); // if we want to get the top bar (like a windowed mod) --> change the boolean
                                    // parameter to false.

        GamePanel gamePanel = new GamePanel(false);
        frame.add(gamePanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gamePanel.startGameThread();
    }

    public static void loadGame() {
        frame = new JFrame("Forgotten Shores");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setUndecorated(true);

        GamePanel gamePanel = new GamePanel(true);
        frame.add(gamePanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gamePanel.startGameThread();
    }
}
