package object;

import entity.WorldObject;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import player.Player;

public class OBJ_SHELTER extends PickableItem implements Interactable {

    // constructor

    public OBJ_SHELTER(GamePanel gp) {
        super(gp);
        this.name = "Shelter";
        this.itemType = ItemType.CONSUMABLE;
        this.isStackable = false;
        this.collision = true;
        this.solidArea = new Rectangle(0, 0, 48, 48);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/shelter/shelter.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void interact(WorldObject worldObject, Player player) {
        gp.gameState = gp.sleepState;
        gp.player.setCurrentHealth(gp.player.getCurrentHealth() + 10); // health increases.
        gp.player.getSleepingImage();
    }
}
