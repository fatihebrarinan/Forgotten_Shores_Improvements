package object;

import entity.Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_APPLE_TREE extends Entity {
    private boolean isHarvestable = true;
    public OBJ_APPLE_TREE(GamePanel gp) {
        super(gp);

        this.name = "apple tree";
        this.scale = 2.3f;
        this.solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.collision = true; // Ensures the player can't walk through it

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/decorations/apple_tree.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public void interact(entity.Player player) {
        // Change the texture to a normal tree (without fruit)
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/decorations/tree.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(this.isHarvestable) {
            object.OBJ_APPLE apple = new object.OBJ_APPLE(gp);
            player.pickUpObject(apple , -1000000000);
            isHarvestable = false;
        }
        
    }

    @Override
    public void draw(Graphics2D g2, boolean isPlayer, boolean isMoving) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            int scaledWidth = (int) (gp.tileSize * scale);
            int scaledHeight = (int) (gp.tileSize * scale);
            screenX -= (scaledWidth - gp.tileSize) / 2;
            screenY -= (scaledHeight - gp.tileSize) / 2;
            g2.drawImage(this.image, screenX, screenY, scaledWidth, scaledHeight, null);
        }
    }
    
}
