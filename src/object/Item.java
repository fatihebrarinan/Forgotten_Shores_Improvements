package object;

import java.awt.Graphics2D;
import entity.WorldObject;
import main.GamePanel;
import player.Player;

public class Item extends WorldObject implements Cloneable {
    public boolean isStackable = false;
    public boolean isPickable = false;
    public int quantity = 1;

    public ItemType itemType;
    public float lightRadius;

    public enum ItemType {
        CONSUMABLE, TOOL, OTHER
    }

    public Item(GamePanel gp) {
        super(gp);
    }

    public void use(Player player) {
        // Default does nothing; consumables will override this
    }

    @Override
    public Item clone() {
        try {
            return (Item) super.clone();
        } catch (CloneNotSupportedException e) {
            // This should not happen since we are Cloneable
            throw new AssertionError();
        }
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
            screenX -= (scaledWidth - gp.tileSize) / 2;
            screenY -= (scaledHeight - gp.tileSize) / 2;
            g2.drawImage(this.image, screenX, screenY, scaledWidth, scaledHeight, null);
        }
    }

}
