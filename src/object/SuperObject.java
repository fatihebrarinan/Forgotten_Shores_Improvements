package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class SuperObject 
{
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public float scale = 1.0f; 
    public Rectangle solidArea = new Rectangle( 0 , 0 , 48 , 48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    public void update() {
        
    }

    public void draw(Graphics2D g2, GamePanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)  
            {


            int scaledWidth = (int) (gp.tileSize * scale);
            int scaledHeight = (int) (gp.tileSize * scale);
            
            screenX -= (scaledWidth - gp.tileSize) / 2;
            screenY -= (scaledHeight - gp.tileSize) / 2;

            g2.drawImage(image, screenX, screenY, scaledWidth, scaledHeight, null);
        }
    }
}