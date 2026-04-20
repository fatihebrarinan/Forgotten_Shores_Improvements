package object;

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
        this.solidArea = new Rectangle(0, 0, 48, 48);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.isStackable = false;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/chest/chest.png")); // chest object
                                                                                                       // will be added
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Check if player has a key in inventory
    public boolean playerHasKey(Player player) {
        for (int i = 0; i < player.inventory.getSlots().length; i++) {
            if (player.inventory.getSlots()[i] != null && player.inventory.getSlots()[i].name.equals("Key")) {
                return true;
            }
        }
        return false;
    }

    // For now generate a new axe
    @Override
    public void interact(WorldObject worldObject, Player player) {
        if (playerHasKey(player)) {
            for (int i = 0; i < gp.obj.length; i++) {
                if (gp.obj[i] == this) {
                    Item axe = new OBJ_AXE(gp);
                    axe.worldX = this.worldX;
                    axe.worldY = this.worldY;
                    gp.obj[i] = axe;
                    gp.ui.addMessage("Treasure opened!");
                    player.inventory.consumeItem("Key", 1);
                    break;
                }
            }
        } else {
            gp.ui.addMessage("You need a key to open the chest.");
        }
    }
}
