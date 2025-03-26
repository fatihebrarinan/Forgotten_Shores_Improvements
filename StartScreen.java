import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class StartScreen extends JFrame{

    //instance variables
    private JPanel panel;
    private JButton startButton;
    private JButton creditButton;
    private BufferedImage image;

    //constructor
    public StartScreen () {

        try {
            this.image = ImageIO.read(new File ( "StartScreen.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        createPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        device.setFullScreenWindow(this);

        setVisible(true);        
    }

    class ClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
        
    }
    
    class Panel extends JPanel {
        protected void paintComponent ( Graphics g) {
            super.paintComponent(g);
            if ( image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private void createPanel() {
        this.panel = new Panel();
        panel.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel ();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        Dimension buttonSize = new Dimension( 200 , 100);

        this.startButton = new JButton("Start Game");
        startButton.setFont(new Font( "Arial" , Font.BOLD , 25));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setPreferredSize(buttonSize);
        startButton.setMaximumSize(buttonSize);
        startButton.setMinimumSize(buttonSize);
        startButton.addActionListener(new ClickListener());

        this.creditButton = new JButton("  Credits  ");
        creditButton.setFont(new Font( "Arial" , Font.BOLD , 25));
        creditButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        creditButton.setPreferredSize(buttonSize);
        creditButton.setMaximumSize(buttonSize);
        creditButton.setMinimumSize(buttonSize);
        //new aciton listener will be added 

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(startButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        centerPanel.add(creditButton);
        centerPanel.add(Box.createVerticalGlue());


        panel.add(centerPanel , BorderLayout.CENTER);
        panel.setOpaque(true);
        this.add(panel);
    }
}