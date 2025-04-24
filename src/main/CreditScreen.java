package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CreditScreen extends JDialog {

    public static final int FRAME_WIDTH = 600;
    public static final int FRAME_HEIGHT = 800;
    private Font font;
    private BufferedImage image;
    private JPanel creditPanel;
    private boolean firstLabel = true ;

    public CreditScreen(StartScreen screen , Font font) {
        super(screen, "Credits", true);
        this.font = font.deriveFont(15);
        try {
            this.image = ImageIO.read(getClass().getResource("/res/CreditScreen/CreditScreen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(d);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(screen);

        this.creditPanel = new CreditPanel();
        add(creditPanel);
        addKeyListener(new keyListener());

        createLabel("CREDITS");
        createLabel("Ali Batu Sarıca");
        createLabel("Çağan Tanrıverdı");
        createLabel("Eren Gürbüz");
        createLabel("Fatih Ebrar İnan");
        createLabel("Kaan Uz");

        setUndecorated(true);
        setResizable(false);
        setVisible(true);
    }

    class CreditPanel extends JPanel {
        public CreditPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);  
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
            g.setColor(new Color(180, 200, 255));
            g.setFont(font.deriveFont(Font.PLAIN, 30));
            g.drawString("Press ESC to exit", 20, 30); 
        }
        
    }

    class keyListener implements KeyListener {
        
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();
            if ( code == KeyEvent.VK_ESCAPE) {
                dispose();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
        
    }

    private void createLabel(String name) {
        JLabel label = new JLabel(name);
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setForeground(new Color(180, 200, 255));
        if ( this.firstLabel) {
            label.setFont(this.font.deriveFont(Font.BOLD ,150));
            firstLabel = false;
        }
        else {
            label.setFont(this.font.deriveFont(Font.PLAIN ,30));
            creditPanel.add(Box.createVerticalStrut(5));
        }
        creditPanel.add(label);
    }
}
