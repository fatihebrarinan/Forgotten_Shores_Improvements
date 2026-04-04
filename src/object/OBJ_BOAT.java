package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import player.Player;

public class OBJ_BOAT extends PickableItem {
    public OBJ_BOAT(GamePanel gp) {
        super(gp);
        this.name = "Boat";
        this.itemType = ItemType.CONSUMABLE;
        this.collision = true;
        this.solidArea = new Rectangle(0, 0, 48, 48);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.scale = 2.0f;
        this.isStackable = false;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/boat/boat.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void use(Player player) {
        player.hasBoat = true;
    }
}
