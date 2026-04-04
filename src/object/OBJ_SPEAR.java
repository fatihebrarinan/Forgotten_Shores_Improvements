package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_SPEAR extends PickableItem {

    // constructor
    public OBJ_SPEAR(GamePanel gp) {
        super(gp);
        this.name = "Spear";
        this.itemType = ItemType.TOOL;
        this.isStackable = false;
        this.scale = 1.2f;
        this.solidArea = new Rectangle(0, 0, 48, 48);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/spear/spear.png")); // spear object
                                                                                                       // will be added
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
