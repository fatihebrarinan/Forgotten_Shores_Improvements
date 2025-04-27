package object;

import main.GamePanel;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_STONE extends Item {
    public OBJ_STONE(GamePanel gp) {
        super(gp);
        this.name = "Stone";
        this.itemType = ItemType.CONSUMABLE;
        this.quantity = 1;
        this.isStackable = true;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/objects/stone.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}