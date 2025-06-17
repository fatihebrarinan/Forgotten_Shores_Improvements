package entity;

import java.awt.image.BufferedImage;
import main.GamePanel;
import java.awt.Rectangle;
import java.awt.Graphics2D;

public abstract class WorldObject {
    public GamePanel gp;
    public int worldX;
    public int worldY;
    public String name;
    public BufferedImage image;
    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, left1, left2, left3, left4, right1, right2,
            right3, right4, idle1, idle2, idle3, idle4;
    public boolean collision = false;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    public float scale = 1.0f;

    public WorldObject(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            int scaledWidth = (int) (gp.tileSize * scale);
            int scaledHeight = (int) (gp.tileSize * scale);

            int adjustedScreenX = screenX - (scaledWidth - gp.tileSize) / 2;
            int adjustedScreenY = screenY - (scaledHeight - gp.tileSize) / 2;

            g2.drawImage(this.down1, adjustedScreenX, adjustedScreenY, scaledWidth, scaledHeight, null);
        }
    }
}