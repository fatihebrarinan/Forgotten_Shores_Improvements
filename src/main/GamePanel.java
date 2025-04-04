package main;

import entity.Entity;
import entity.NPC_Mysterious_Stranger;
import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import monster.MON_Island_Native;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

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


    // dialogue
    public final int dialogueState = 3;

    // FPS
    int fps = 60;

    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this); 

    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public Entity[] obj = new Entity[10]; // can be displayed 10 objects at the same time
    public Entity[] npc = new Entity[10]; // 10 npcs can be displayed
    public Entity[] monster = new Entity[10]; // 10 monsters can be displayed at the same time
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);

    // Game State (Pause/Unpause)
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(keyH);
        this.setFocusable(true);
        setUpGame();
    }

    public void setUpGame() 
    {
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        gameState = playState;
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();

    }

    // Former run method()

    /*
     * public void run()
     * {
     * double drawInterval = 1000000000 / fps;
     * double nextDrawTime = System.nanoTime() + drawInterval;
     * 
     * while(gameThread != null)
     * {
     * // Updates character position
     * update();
     * 
     * // Draws the screen with the updated information
     * repaint();
     * 
     * try
     * {
     * double remainingTime = nextDrawTime - System.nanoTime();
     * remainingTime = remainingTime / 1000000;
     * 
     * if (remainingTime < 0)
     * {
     * remainingTime = 0;
     * }
     * 
     * Thread.sleep((long) remainingTime);
     * nextDrawTime += drawInterval;
     * }
     * catch (InterruptedException ex)
     * {
     * ex.printStackTrace();
     * }
     * }
     * }
     */

    public void run() {
        double drawInterval = 1000000000 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                drawCount++;
                delta--;
            }
            if (timer >= 1000000000) {
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

            player.collisionOn = false;  
            cChecker.checkTile(player);
            cChecker.checkObject(player, true);
            int playerMonsterIndex = cChecker.checkEntity(player, monster); 
            cChecker.checkEntity(player, npc);


            if (playerMonsterIndex != 999 && monster[playerMonsterIndex] instanceof MON_Island_Native) 
            {
                if (!player.isInvincible() && !player.isAttackingForCollision()) 
                { 
                    player.contactMonster(((MON_Island_Native) monster[playerMonsterIndex]).getDamage());
                }
            }


            player.update();
            
            for (int i = 0; i < npc.length; i++) 
            {
                if (npc[i] != null && npc[i] instanceof NPC_Mysterious_Stranger) 
                {
                    ((NPC_Mysterious_Stranger)npc[i]).update();
                }
            }

            for (int i = 0; i < monster.length; i++) 
            {
                if (monster[i] != null) 
                {
                    if (monster[i].alive && !monster[i].dying)
                    {
                        int monsterPlayerIndex = cChecker.checkEntity(monster[i], new Entity[]{player});
                        if (monsterPlayerIndex != 999 && !player.isInvincible() && !player.isAttackingForCollision()) 
                        {
                            player.contactMonster(((MON_Island_Native) monster[i]).getDamage());
                        }
                        monster[i].update();  
                        
                    }
                    else if (!monster[i].dying)
                    {
                        monster[i].update();    
                    }
                    else if (!monster[i].alive)
                    {
                        monster[i] = null;  
                    }

                }
            }
        }

        if (gameState == pauseState) {
            
            // Nothing since the game is paused
        }

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].update();
            }
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // We can cast because we are passing a Graphics2D object

        // tile 
        tileM.draw(g2);

        // creating a list that will hold all objects
        List<Entity> entitiesToDraw = new ArrayList<>();


        for ( Entity objEntity : obj ) 
        {
            if ( objEntity != null ) 
            {
                entitiesToDraw.add( objEntity );
            }
        }


        for ( Entity npcEntity : npc ) 
        {
            if ( npcEntity != null ) 
            {
                entitiesToDraw.add( npcEntity );
            }
        }

        for ( Entity monsterEntity : monster)
        {
            if ( monsterEntity != null )
            {
                entitiesToDraw.add( monsterEntity );
            }
        }

        entitiesToDraw.add(player);

        // sorting the entities by their worldY value
        // lower worldY are drawn first
        for ( int j = 0; j < entitiesToDraw.size() - 1; j++ ) 
        {

            for (int i = 0; i < entitiesToDraw.size() - 1 - j; i++) 
            {
                
                if ( entitiesToDraw.get(i).worldY > entitiesToDraw.get(i + 1).worldY ) 
                {
                    Entity temp = entitiesToDraw.get(i);
                    entitiesToDraw.set(i, entitiesToDraw.get(i + 1));
                    entitiesToDraw.set(i + 1, temp);
                }
            }
        }


        for ( Entity entity : entitiesToDraw ) 
        {
            if ( entity == player ) 
            {
                
                boolean isMoving = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;
                entity.draw(g2, true, isMoving);
            } else 
            {
                
                entity.draw(g2, false, false);
            }
        }


        // ui
        ui.draw(g2);

        g2.dispose();
    }
}
