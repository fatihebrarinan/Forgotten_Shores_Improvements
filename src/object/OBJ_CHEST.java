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
}
