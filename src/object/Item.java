package object;

import entity.Entity;
import entity.Player;
import main.GamePanel;

public class Item extends Entity implements Cloneable
{
    public boolean isStackable = false; 
    public int quantity = 1;    
    
    public ItemType itemType;

    public enum ItemType
    {
        WEAPON, SHIELD, CONSUMABLE, OTHER
    }

    public Item( GamePanel gp ) 
    {
        super(gp);
    }

    public void use(Player player) {
        // Default does nothing; consumables will override this
    }
    public Item clone() {
        try {
            return (Item) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
