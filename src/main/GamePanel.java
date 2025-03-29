package main;

import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable 
{

    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = scale * originalTileSize;
    public final int maxScreenCol = 22;
    public final int maxScreenRow = 16;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // FPS
    int fps = 60;
    
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this); //!

    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public SuperObject [] obj = new SuperObject[10]; // can be displayed 10 objects at the same time
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);

    // Game State (Pause/Unpause)
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    
    public GamePanel() 
    {
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        setUpGame();
    }

    public void setUpGame () {
        aSetter.setObject();
        gameState = playState;
    }
    public void startGameThread() 
    {
        
        gameThread = new Thread(this);
        gameThread.start();

    }

    // Former run method()
    
    /*public void run()
    {
        double drawInterval = 1000000000 / fps;
        double nextDrawTime = System.nanoTime() + drawInterval;    

        while(gameThread != null)
        {
            // Updates character position
            update();

            // Draws the screen with the updated information
            repaint();

            try
            {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0)
                {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }
    }*/

    public void run()
    {
        double drawInterval = 1000000000 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null)
        {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) 
            {
                update();
                repaint();
                drawCount++;
                delta--;
            }
            if (timer >= 1000000000)
            {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() 
    {
        if (gameState == playState)
        {
            player.update();
        }

        if(gameState == pauseState)
        {
            // Nothing since the game is paused
        }
        
        // update for other objects
        for (int i = 0; i < obj.length; i++) 
        {
            if (obj[i] != null) {
                obj[i].update();
            }
        }
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; // We can cast because we are passing a Graphics2D object

        tileM.draw(g2);

        for ( int i = 0 ; i < obj.length ; i++) {
            if ( obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        player.draw(g2);

        ui.draw(g2);
        
        g2.dispose();
    }
}
