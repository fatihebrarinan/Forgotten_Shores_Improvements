package object;

import entity.WorldObject;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import player.Player;

public class OBJ_SHELTER extends Item implements Interactable {

    // constructor

    public OBJ_SHELTER(GamePanel gp) {
        super(gp);
        this.name = "Shelter";
        this.itemType = ItemType.CONSUMABLE;
        this.isPickable = true;
        this.isStackable = false;
        this.collision = true;
        this.solidArea = new Rectangle(0, 0, 48, 48);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/shelter/shelter.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void interact(WorldObject worldObject, Player player) {
        gp.gameState = gp.sleepState;
        gp.player.setCurrentHealth(gp.player.getCurrentHealth() + 10); // health increases.
        gp.player.getSleepingImage();
    }

    @Override
    public void draw(Graphics2D g2) {
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
