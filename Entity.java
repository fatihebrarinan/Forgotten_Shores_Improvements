package entity;

import java.awt.image.BufferedImage;
import javax.imageio.*;

import main.Panel;
import main.KeyHandler;

public class Entity
{
    public int worldX; // x-coordinate of entity
    public int worldY; // y-coordinate of entity
    public int speed; // movement speed of entity

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2; // representing the images which will swap during movements
    public String direction; // where entity looks

    public int spriteCounter = 0; // 
    public int spriteNum = 1; // for example: is it up1 or up2

    public Rectangle solidArea; // part that cannot colide
    public boolean collisionOn = false;
}
