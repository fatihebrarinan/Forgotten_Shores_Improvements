package main;

import entity.Entity;
import entity.Mob;
import entity.WorldObject;
import environment.LightingManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JPanel;

import object.Item;

import player.Player;
import player.PlayerCollisionManager;
import save.SaveStorage;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    public boolean drawHitboxes; // debug boolean make this true !! IN CONSTRUCTOR !! if there are any errors
                                 // with hit boxes.

    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = scale * originalTileSize;
    public final int maxScreenCol = 22;
    public final int maxScreenRow = 16;
    // public final int screenWidth = tileSize * maxScreenCol;
    // public final int screenHeight = tileSize * maxScreenRow;
    // SCREEN DIMENSIONS
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public final int screenWidth = screenSize.width;
    public final int screenHeight = screenSize.height;

    // WORLD SETTINGS
    public final int maxWorldCol = 250;
    public final int maxWorldRow = 250;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // FOR FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    // FPS
    int fps = 60;
    public int currentFPS;

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);

    Thread gameThread;
    public PlayerCollisionManager cChecker = new PlayerCollisionManager(this);
    public Player player;
    public WorldObject[] obj = new WorldObject[1000]; // can be displayed 300 objects at the same time
    public Entity[] monster = new Entity[10]; // 10 monsters can be displayed at the same time
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public JDialog pausePanel = new PauseScreen(this);
    public LightingManager eManager = new LightingManager(this);
    public CraftingScreen craftingScreen;
    public boolean isLoadGame;
    public SaveStorage saveStorage = new SaveStorage(this);
    // gameState is the state controller. playState, dialogueState, craftingState,
    // pause State, gameOverState
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int gameOverState = 5;
    public final int sleepState = 6;
    public final int craftingState = 7;

    public GamePanel(boolean isLoadGame) {
        this.isLoadGame = isLoadGame;
        this.player = new Player(this, keyH, isLoadGame);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(keyH);
        this.setFocusable(true);
        drawHitboxes = false; // !!!!! MAKE THIS FALSE IF YOU DONT WANT HITBOXES TO BE DRAWN !!!!!!!!
        setUpGame();
        requestFocusInWindow();
        System.out.println("Screen resolution: " + Toolkit.getDefaultToolkit().getScreenSize());
        if (isLoadGame) {
            saveStorage.loadGame();
            System.out.println("Game loaded after setUpGame!");
        }
    }

    public void setUpGame() {

        Arrays.fill(obj, null);
        Arrays.fill(monster, null);

        aSetter.setObject();
        aSetter.setMonster();
        aSetter.setPigs();
        eManager.setup();
        gameState = playState;

        // Creating a blank buffered image which is as large as our screen
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics(); // first we will draw our game to tempScreen, then fit it to full
                                                    // screen.

        craftingScreen = new CraftingScreen(this);
        setFullScreen();
    }

    public void setFullScreen() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("mac")) {
            Main.frame.dispose();
            Main.frame.setUndecorated(true);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // true full screen dimensions
            Main.frame.setSize(screenSize);
            Main.frame.setLocation(0, 0);

            Main.frame.setVisible(true);

            screenWidth2 = screenSize.width;
            screenHeight2 = screenSize.height;
        } else {
            Main.frame.dispose();
            Main.frame.setUndecorated(true);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            screenWidth2 = screenSize.width;
            screenHeight2 = screenSize.height;

            Main.frame.setSize(screenWidth2, screenHeight2);
            Main.frame.setLocation(0, 0);
            Main.frame.setVisible(true);
        }
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();

    }

    public void run() {
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        long lastFpsTime = System.currentTimeMillis();
        int fpsCounter = 0;

        while (gameThread != null) {
            long startTime = System.nanoTime();

            update();

            synchronized (tempScreen) {
                drawToTempScreen();
            }

            repaint();
            fpsCounter++;

            // FPS display
            if (System.currentTimeMillis() - lastFpsTime >= 1000) {
                currentFPS = fpsCounter;
                fpsCounter = 0;
                lastFpsTime += 1000;
                System.out.println("FPS: " + currentFPS);
            }

            // Consistent pacing (instead of sleepTime = remaining time)
            try {
                Thread.sleep(2); // Fixed pacing wins here
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Optional: prevent runaway if frame took too long
            while (System.nanoTime() - startTime < OPTIMAL_TIME) {
                Thread.yield(); // Chill until next frame time
            }
        }
    }

    // update method is where the entire game logic is updated.
    public void update() {
        if (isLoadGame) {
            saveStorage.loadGame();
            isLoadGame = false;
        }

        if (keyH.cPressed) {
            if (gameState == playState) {
                gameState = craftingState;
            } else if (gameState == craftingState) {
                gameState = playState;
                craftingScreen.hoverReset(); // Avoid stuck craft mode
            }
            keyH.cPressed = false;
        }

        if (gameState == playState) {
            if (keyH.onePressed) {
                player.inventory.setSelectedSlot(0);
                keyH.onePressed = false;
            }
            if (keyH.twoPressed) {
                player.inventory.setSelectedSlot(1);
                keyH.twoPressed = false;
            }
            if (keyH.threePressed) {
                player.inventory.setSelectedSlot(2);
                keyH.threePressed = false;
            }
            if (keyH.fourPressed) {
                player.inventory.setSelectedSlot(3);
                keyH.fourPressed = false;
            }
            if (keyH.fivePressed) {
                player.inventory.setSelectedSlot(4);
                keyH.fivePressed = false;
            }

            player.collisionOn = false;
            cChecker.checkTile(player);
            int playerMonsterIndex = cChecker.checkEntity(player, monster);

            if (playerMonsterIndex != 999 && monster[playerMonsterIndex] instanceof Mob) {
                //TODO: Player takes damage.
            }

            player.update();

            for (WorldObject objEntity : obj) {
                if (objEntity != null && objEntity instanceof Entity) {
                    ((Entity) objEntity).update();
                }
            }
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if (monster[i].alive && !monster[i].dying) {
                        monster[i].update();
                    }
                    if (!monster[i].alive) {
                        monster[i] = null;
                    }
                }
            }

        } else if (gameState == pauseState) {
            // Nothing for now
        }

        eManager.update();

        if (gameState == craftingState) {
            craftingScreen.update();
        }

        if (gameState == gameOverState) {
            if (keyH.rPressed) {
                restartGame();
                gameState = playState;
                keyH.rPressed = false;
            }
            if (keyH.escPressed) {
                System.exit(0);
            }
        }
    }

    public void drawToTempScreen() {
        // START DRAW
        tileM.draw(g2);

        // creating a list that will hold all objects
        List<WorldObject> objectsToDraw = new ArrayList<>();

        for (WorldObject objEntity : obj) {
            if (objEntity != null) {
                objectsToDraw.add(objEntity);
            }
        }

        for (Entity monsterEntity : monster) {
            if (monsterEntity != null) {
                objectsToDraw.add(monsterEntity);
            }
        }

        objectsToDraw.add(player);

        // sorting the entities by their worldY value
        // lower worldY are drawn first
        objectsToDraw.sort(Comparator.comparingInt(o -> o.worldY));

        for (WorldObject object : objectsToDraw) {
            if (object instanceof Entity) {
                boolean isMoving = false;
                if (object instanceof Player) {
                    isMoving = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;
                } else {
                    isMoving = ((Entity) object).isMovingEntity;
                }
                ((Entity) object).draw(g2, object instanceof Player, isMoving);
            } else if (object instanceof Item) {
                ((Item) object).draw(g2);
            }
        }

        eManager.draw(g2);
        // UI
        ui.draw(g2);
        // END DRAW
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
    }

    public void removeObject(WorldObject anEntity) {
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] == anEntity) {
                obj[i] = null;
                arrangeObj(i);
                break;
            }
        }
    }

    private void arrangeObj(int index) {
        for (int i = index; i < obj.length - 1; i++) {
            obj[i] = obj[i + 1];
        }
        obj[obj.length - 1] = null;
    }

    // Currently revives the player.
    public void restartGame() {
        player.setDefaultValues();
        player.restartPlayer();
        aSetter.setMonster();
        aSetter.setObject();
    }

}
