package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_APPLE_TREE extends Item {
    private boolean isHarvestable = true;

    public OBJ_APPLE_TREE(GamePanel gp) {
        super(gp);

        this.itemType = ItemType.OTHER;
        this.name = "Apple Tree";
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

    public void interact(entity.Player player, int index) {
        object.OBJ_APPLE apple = new object.OBJ_APPLE(gp);
        for (Item item : player.inventory.getSlots()) {
            if (item == null || item instanceof OBJ_APPLE) {
                if (this.isHarvestable) {
                    try {
                        this.image = ImageIO.read(getClass().getResourceAsStream("/res/decorations/tree.png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.pickUpObject(apple, index);
                    isHarvestable = false;
                }
                return;
            }
        }
        player.pickUpObject(apple, index);

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

    public boolean getHarvestable() {
        return this.isHarvestable;
    }

    public void setHarvestable(boolean harvestable) {
        this.isHarvestable = harvestable;

        if (!harvestable) {
            try {
                this.image = ImageIO.read(getClass().getResourceAsStream("/res/decorations/tree.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
