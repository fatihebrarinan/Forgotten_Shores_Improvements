package object;
import entity.Entity;
import entity.Player;
import java.awt.Rectangle;
import main.GamePanel;

public class Item extends Entity implements Cloneable
{
    public boolean isStackable = false; 
    public int quantity = 1;    
    
    public ItemType itemType;

    public enum ItemType
    {
        CONSUMABLE, OTHER
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
        
            if (this instanceof object.OBJ_APPLE) {
                newItem = new object.OBJ_APPLE(gp);
            } else if (this instanceof object.OBJ_AXE) {
                newItem = new object.OBJ_AXE(gp);
            } else if (this instanceof object.OBJ_CAMPFIRE) {
                newItem = new object.OBJ_CAMPFIRE(gp);
            } else if (this instanceof object.OBJ_CHEST) {
                newItem = new object.OBJ_CHEST(gp);
            } else if (this instanceof object.OBJ_KEY) {
                newItem = new object.OBJ_KEY(gp);
            } else if (this instanceof object.OBJ_RAW_MEAT) {
                newItem = new object.OBJ_RAW_MEAT(gp);
            } else if (this instanceof object.OBJ_SPEAR) {
                newItem = new object.OBJ_SPEAR(gp);
            } else if (this instanceof object.OBJ_STONE) {
                newItem = new object.OBJ_STONE(gp);
            } else if (this instanceof object.OBJ_TORCH) {
                newItem = new object.OBJ_TORCH(gp);
            } else if (this instanceof object.OBJ_WATER_BUCKET) {
                newItem = new object.OBJ_WATER_BUCKET(gp);
            } else if (this instanceof object.OBJ_WOOD) {
                newItem = new object.OBJ_WOOD(gp);
            } else if (this instanceof object.OBJ_BOAT) {
                newItem = new object.OBJ_BOAT(gp);
            } else if (this instanceof object.OBJ_SHELTER) {  
                newItem = new object.OBJ_SHELTER(gp);
            } else {
                newItem = new Item(gp);
            }
        
            newItem.name = this.name;
            newItem.image = this.image;
            newItem.down1 = this.down1;
            newItem.idle1 = this.idle1;
            newItem.quantity = this.quantity;
            newItem.isStackable = this.isStackable;
            newItem.itemType = this.itemType;
            newItem.solidArea = new Rectangle(this.solidArea);
            newItem.collision = this.collision;
            newItem.alive = true;
        
            return newItem;
    }
    public static Item createItemByName ( String name , GamePanel gp) {
        Item obj = null;
        switch (name) {
            case "Apple":
                obj = new OBJ_APPLE(gp);
                break;
            case "Axe":
                obj = new OBJ_AXE(gp);
                break;
            case "Camp Fire":
                obj = new OBJ_CAMPFIRE(gp);
                break;
            case "Key":
                obj = new OBJ_KEY(gp);
                break;
            case "Meat":
                obj = new OBJ_RAW_MEAT(gp);
                break;
            case "Shelter":
                obj = new OBJ_SHELTER(gp);
                break;
            case "Spear":
                obj = new OBJ_SPEAR(gp);
                break;
            case "Stone":
                obj = new OBJ_STONE(gp);
                break;
            case "Torch":
                obj = new OBJ_TORCH(gp);
                break;
            case "Water Bucket":
                obj = new OBJ_WATER_BUCKET(gp);
                break;
            case "Wood":
                obj = new OBJ_WOOD(gp);
                break;
            case "Boat":
                obj = new OBJ_BOAT(gp);
                break;
        }
        return obj;
    }
}
