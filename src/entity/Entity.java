package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity
{
    public int worldX; // x-coordinate of entity
    public int worldY; // y-coordinate of entity
    public int speed; // movement speed of entity

    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, left1, left2, left3, left4, right1, right2, right3, right4; // representing the images which will swap during movements
    public String direction; // where entity looks

    public int spriteCounter = 0; // 
    public int spriteNum = 1; // for example: is it up1 or up2

    public Rectangle solidArea; // part that cannot colide
    public boolean collisionOn = false;
}
