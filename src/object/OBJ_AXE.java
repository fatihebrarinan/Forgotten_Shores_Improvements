package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_AXE extends Item {

    // constructor
    public OBJ_AXE(GamePanel gp) {
        super(gp);
        this.name = "Axe";
        this.itemType = ItemType.TOOL;
        this.scale = 1.2f;
        this.solidArea = new Rectangle(0, 0, 48, 48);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.isPickable = true;
        this.isStackable = false;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/axe/axe.png")); // axe object will be
                                                                                                   // added
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
