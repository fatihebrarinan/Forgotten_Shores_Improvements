package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_DOOR extends Item {
    public OBJ_DOOR(GamePanel gp) {
        super(gp);
        this.name = "Door";
        this.solidArea = new Rectangle(0, 0, 48, 48);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.isStackable = false;
        this.collision = true;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("")); // door object will be added
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
