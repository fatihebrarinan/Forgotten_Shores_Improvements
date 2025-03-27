package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity
{
    KeyHandler keyHandler;
    GamePanel gp;

    public final int screenX;
    public final int screenY;
    static boolean wasMoving = false;
    public float scale = 2.0f;

    public Player(GamePanel aGP, KeyHandler aKeyHandler)
    {
        this.gp = aGP;
        this.keyHandler = aKeyHandler;
        screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
        screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);

        this.solidArea = new Rectangle();
        
        solidArea.x = 8;
        solidArea.y = 16;
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
    public void update() {
        boolean isMoving = ( keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed );
        

    
        if (isMoving) {

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
    

        wasMoving = isMoving;
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        boolean isMoving = ( keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed );
    
        if (!isMoving) {

            switch (spriteNum) {
                case 1:
                    image = idle1;
                    break;
                case 2:
                    image = idle2;
                    break;
                case 3:
                    image = idle3;
                    break;
                case 4:
                    image = idle4;
                    break;
                default:
                    image = idle1; 
                    break;
            }
        } else {
 
            int walkingFrame = (spriteNum == 1 || spriteNum == 2) ? spriteNum : 1; 
            
            if (this.direction.equals("up")) {
                if (walkingFrame == 1) {
                    image = this.up1;
                } else {
                    image = this.up2;
                }
            } else if (this.direction.equals("down")) {
                if (walkingFrame == 1) {
                    image = this.down1;
                } else {
                    image = this.down2;
                }
            } else if (this.direction.equals("left")) {
                if (walkingFrame == 1) {
                    image = this.left1;
                } else {
                    image = this.left2;
                }
            } else if (this.direction.equals("right")) {
                if (walkingFrame == 1) {
                    image = this.right1;
                } else {
                    image = this.right2;
                }
            }
        }

        int scaledWidth = (int) (gp.tileSize * scale);
        int scaledHeight = (int) (gp.tileSize * scale);
        
        int adjustedScreenX = screenX - (scaledWidth - gp.tileSize) / 2;
        int adjustedScreenY = screenY - (scaledHeight - gp.tileSize) / 2;
    
        g2.drawImage(image, adjustedScreenX, adjustedScreenY, scaledWidth, scaledHeight, null);
    }
}