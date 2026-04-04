package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_DOOR extends Item {
    public OBJ_DOOR(GamePanel gp) {
        super(gp);
        this.name = "Door";
        this.itemType = ItemType.OTHER;
        this.solidArea = new Rectangle(0, 0, 48, 48);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.isPickable = false;
        this.isStackable = false;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("")); // door object will be added
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
