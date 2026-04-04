package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import player.Player;

public class OBJ_BOAT extends PickableItem implements Consumable {
    public OBJ_BOAT(GamePanel gp) {
        super(gp);
        this.name = "Boat";
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
    public boolean consume(Player player) {
        player.hasBoat = true;
        return true;
    }
}
