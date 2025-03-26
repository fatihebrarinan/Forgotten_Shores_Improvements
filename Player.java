package entity;

import java.io.IOException;

import javax.imageio.ImageIO;

import java.io.*;

import main.Panel;
import main.KeyHandler;

public class Player extends Entity
{
    KeyHandler keyHandler;
    Panel gp;

    public Player(Panel aGP, KeyHandler aKeyHandler)
    {
        this.gp = aGP;
        this.keyHandler = aKeyHandler;

        setDefaultValues(); // initial values of player
    }

    public void setDefaultValues()
    {
        this.x = 100; // initial y
        this.y = 100; // initial x
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
            this.up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up1.png"));
            this.up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up1.png"));
            this.down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up1.png"));
            this.down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up1.png"));
            this.left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up1.png"));
            this.left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up1.png"));
            this.right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up1.png"));
            this.right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up1.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**  
     * this method updates the player's direction and speed according to key input
     */
    public void update() 
    {
        if(keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) // to prevent the movement animation of the char when nothing is pressed
        {
            if(keyHandler.upPressed)
            {
                this.direction = "up";
                this.y -= this.speed;
            }

            else if(keyHandler.downPressed)
            {
                this.direction = "down";
                this.y += this.speed;
            }

            else if(keyHandler.leftPressed)
            {
                this.direction = "left";
                this.x -= this.speed;
            }

            else if(keyHandler.rightPressed)
            {
                this.direction = "right";
                this.x += this.speed;
            }

            this.spriteCounter++;
            if(this.spriteCounter > 12) // it is ready to change the image (12 may change)
            {
                if(this.spriteNum == 1)
                {
                    this.spriteNum = 2;
                }
                else if(this.spriteNum == 2)
                {
                    this.spriteNum = 1;
                }
                this.spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2)
    {
        BufferedImage image = null; // to shut the compiler

        if(this.direction.equals("up"));
        {
            if(this.spriteNum == 1)
            {
                image = this.up1;
            }
            if(this.spriteNum == 2)
            {
                image = this.up2;
            }
        }

        else if(this.direction.equals("down"))
        {
            if(this.spriteNum == 1)
            {
                image = this.down1;
            }
            if(this.spriteNum == 2)
            {
                image = this.down2;
            }
        }

        else if(this.direction.equals("left"))
        {
            if(this.spriteNum == 1)
            {
                image = this.left1;
            }
            if(this.spriteNum == 2)
            {
                image = this.left2;
            }
        }

        else if(this.direction.equals("right"))
        {
            if(this.spriteNum == 1)
            {
                image = this.right1;
            }
            if(this.spriteNum == 2)
            {
                image = this.right2;
            }
        }

        g2.drawImage(image, this.x, this.y, this.gp.tileSize, this.gp.tileSize, null);
    }
}