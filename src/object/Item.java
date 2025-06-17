package object;

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

    public void draw(java.awt.Graphics2D g2) {
        g2.drawImage(this.down1, worldX, worldY, gp.tileSize, gp.tileSize, null);
    }
}
