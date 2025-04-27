package object;

import entity.Entity;
import entity.Player;
import main.GamePanel;

public class Item extends Entity implements Cloneable
{
    public boolean isStackable = false; 
    public int quantity = 1;    
    
    public ItemType itemType;
    public boolean isEquipped = false;

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

    public Item clone() 
    {
        Item newItem = new Item(gp);
        newItem.name = this.name;
        newItem.image = this.image;
        newItem.quantity = this.quantity;
        newItem.isStackable = this.isStackable;
        return newItem;
    }
}
