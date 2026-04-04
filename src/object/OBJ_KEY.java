package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_KEY extends PickableItem {
    public OBJ_KEY(GamePanel gp) {
        super(gp);
        this.name = "Key";
        this.scale = 1.0f;
        this.itemType = ItemType.OTHER;
        this.solidArea = new Rectangle(0, 0, 48, 48);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        isStackable = false;

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/key/key.png")); // temporary
                                                                                                   // key
                                                                                                   // object
                                                                                                   // may be
                                                                                                   // changed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
