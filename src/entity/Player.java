package entity;

import java.awt.Rectangle;
import java.io.*;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity
{

    // dialogue cooldown timer to combat with dialogue state being re-triggered again
    private int dialogueCooldown = 0;
    private final int cooldownDuration = 30;

    private int maxHealth;
    private int currentHealth;
    private int maxHunger;
    private int currentHunger;
    private int hungerDecreaseCounter = 0;
    private final int hungerDecreaseInterval = 60; // 60 seconds or 1 minute for now further to be maybe changed

    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    static boolean wasMoving = false;
    int hasKey = 0;

    public Player(GamePanel aGP, KeyHandler aKeyHandler)
    {
        super(aGP);
        this.keyHandler = aKeyHandler;
        screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
        screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
        this.scale = 2.0f;

        this.solidArea = new Rectangle();
        
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues(); 
        getPlayerImage();
    }

    public void setDefaultValues()
    {
        worldX = gp.tileSize * 23; // initial y
        worldY = gp.tileSize * 21; // initial x
        this.speed = 4; // initial movement speed
        this.direction = "down"; // initial direction where the player looks

        maxHealth = 100; // maximum health a player can have
        currentHealth = maxHealth; // initial health equals to max health ( 100 )

        maxHunger = 100; // maximum hunger a player can have
        currentHunger = maxHunger; // initial hunger equalts to max hunger ( 100 )
    }

    public void getPlayerImage()
    {
        try
        {
            /**
             * Image URLs will be changed according to our images
             */
            this.up1 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_north1.png"));
            this.up2 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_north2.png"));
            this.up3 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_north3.png"));
            this.up4 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_north4.png"));
            this.down1 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_south1.png"));
            this.down2 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_south2.png"));
            this.down3 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_south3.png"));
            this.down4 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_south4.png"));
            this.left1 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_left1.png"));
            this.left2 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_left2.png"));
            this.left3 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_left3.png"));
            this.left4 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_left4.png"));
            this.right1 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_right1.png"));
            this.right2 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_right2.png"));
            this.right3 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_right3.png"));
            this.right4 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_right4.png"));

            this.idle1 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_idle1.png"));
            this.idle2 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_idle2.png"));
            this.idle3 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_idle3.png"));
            this.idle4 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_idle4.png"));
            
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**  
     * this method updates the player's direction and speed according to key input
     */
    public void update() 
    {

        if ( dialogueCooldown > 0 )
        {
            dialogueCooldown--;
        }

        // decreasing hunger over time
        hungerDecreaseCounter++;

        if ( hungerDecreaseCounter >= hungerDecreaseInterval )
        {
            if ( currentHunger > 0 )
            {
                currentHunger--;
            } 
            hungerDecreaseCounter = 0;
        }

        int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        interactNPC(npcIndex);

        boolean isMoving = (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed);
    
        if (isMoving) 
        {
            if (!wasMoving) {
                spriteNum = 1;
            }
    
            if (keyHandler.upPressed) {
                this.direction = "up";
            } else if (keyHandler.downPressed) {
                this.direction = "down";
            } else if (keyHandler.leftPressed) {
                this.direction = "left";
            } else if (keyHandler.rightPressed) {
                this.direction = "right";
            }
    

            this.collisionOn = false;
            this.gp.cChecker.checkTile(this);

            npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);
    
            if (!this.collisionOn) {
                if (this.direction.equals("up")) {
                    this.worldY -= this.speed;
                } else if (direction.equals("down")) {
                    this.worldY += this.speed;
                } else if (direction.equals("left")) {
                    this.worldX -= this.speed;
                } else if (direction.equals("right")) {
                    this.worldX += this.speed;
                }
            }
    
            this.spriteCounter++;
            if (this.spriteCounter > 12) {
                if (this.spriteNum == 1) {
                    this.spriteNum = 2;
                } else if (this.spriteNum == 2) {
                    this.spriteNum = 1;
                }
                this.spriteCounter = 0;
            }
        } else {
            if (wasMoving) {
                spriteNum = 1;
            }
    
            this.spriteCounter++;
            if (this.spriteCounter > 20) {
                this.spriteNum++;
                if (this.spriteNum > 4) {
                    this.spriteNum = 1;
                }
                this.spriteCounter = 0;
            }
        }
    

        int objectIndex = gp.cChecker.checkObject(this, true);
        if (objectIndex != 999) 
        {
            gp.ui.showTooltip = true; 
        } else 
        {
            gp.ui.showTooltip = false; 
        }

        if (keyHandler.fPressed) {
            objectIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objectIndex);
            keyHandler.fPressed = false;
        }
    
        wasMoving = isMoving;
    }

    public void interactNPC( int i )
    {
        if ( ( i != 999 ) && ( dialogueCooldown == 0 ) && ( gp.gameState != gp.dialogueState) ) 
        {
            System.out.println("npc hit.");
            gp.gameState = gp.dialogueState; 
            gp.ui.showTooltip = false; 
            dialogueCooldown = cooldownDuration; 
        }
    }
    
    
    public void pickUpObject ( int i ) {
        if ( i != 999) {
            String objectName = gp.obj[i].name;
            switch ( objectName) {
                case "Axe":
                    break;
                case "Camp Fire":
                    break;
                case "Door":
                    if ( hasKey > 0) {
                        gp.obj[i] = null;
                        hasKey--;
                    }
                    break;
                case "Key":
                    hasKey++;
                    gp.obj[i] = null;
                    break;
                case "Shelter":
                    break;
                case "Spear":
                    break;
                case "Torch":
                    break;
                case "Chest":
                    break;
            }

        }
    }

    // getter setter for hunger and health bar
    public int getCurrentHealth() 
    { 
        return currentHealth; 
    }
    public int getMaxHealth() 
    { 
        return maxHealth; 
    }
    public int getCurrentHunger() 
    { 
        return currentHunger; 
    }
    public int getMaxHunger() 
    { 
        return maxHunger; 
    }
}