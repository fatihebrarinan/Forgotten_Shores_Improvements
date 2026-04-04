package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_STONE extends Item {
    public OBJ_STONE(GamePanel gp) {
        super(gp);
        this.name = "Stone";
        this.itemType = ItemType.OTHER;
        this.scale = 2.3f;
        this.solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.isStackable = true;
        this.isPickable = true;
        this.collision = true; // Ensures the player can't walk through it

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/decorations/stone.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}