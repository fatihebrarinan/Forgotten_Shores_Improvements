package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_BUSH extends Item {
    // constructor
    public OBJ_BUSH(GamePanel gp) {
        super(gp);
        this.name = "bush";
        this.itemType = ItemType.OTHER;
        this.scale = 2.3f;
        this.isPickable = false;
        this.isStackable = false;
        // this.solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        // this.solidAreaDefaultX = this.solidArea.x;
        // this.solidAreaDefaultY = this.solidArea.y;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/decorations/bush.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
