package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

import entity.WorldObject;
import main.GamePanel;
import player.Player;

public class OBJ_CHEST extends Item implements Interactable {
    public OBJ_CHEST(GamePanel gp) {
        super(gp);
        this.name = "Chest";
        this.collision = true;
        this.itemType = ItemType.OTHER;
        this.solidArea = new Rectangle(0, 0, 48, 48);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.isStackable = false;
        this.isPickable = false;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/chest/chest.png")); // chest object
                                                                                                       // will be added
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interact(WorldObject worldObject, Player player) {
        if (player.haveKey) {
            for (int i = 0; i < gp.obj.length; i++) {
                if (gp.obj[i] == this) {
                    Item axe = new OBJ_AXE(gp);
                    axe.worldX = this.worldX;
                    axe.worldY = this.worldY;
                    gp.obj[i] = axe;
                    gp.ui.addMessage("Treasure opened!");
                    player.inventory.consumeItem("Key", 1);
                    player.haveKey = false;
                    break;
                }
            }
        } else {
            gp.ui.addMessage("You need a key to open the chest.");
        }
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
