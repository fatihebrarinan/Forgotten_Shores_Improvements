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
        WEAPON, SHIELD, CONSUMABLE, LIGHTER, OTHER
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
        Item newItem;
    
        if (this instanceof object.OBJ_TORCH) {
            newItem = new object.OBJ_TORCH(gp);
        } 
        else if (this instanceof object.OBJ_SWORD_NORMAL) {
            newItem = new object.OBJ_SWORD_NORMAL(gp);
        } 
        else if (this instanceof object.OBJ_SHIELD_WOOD) {
            newItem = new object.OBJ_SHIELD_WOOD(gp);
        } 
        else {
            newItem = new Item(gp); 
        }
    
        newItem.name = this.name;
        newItem.image = this.image;
        newItem.quantity = this.quantity;
        newItem.isStackable = this.isStackable;
        newItem.itemType = this.itemType;
    
        return newItem;
    }
}
