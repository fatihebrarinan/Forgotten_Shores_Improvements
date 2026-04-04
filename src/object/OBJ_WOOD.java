package object;

import main.GamePanel;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_WOOD extends PickableItem {
    GamePanel gp;

    public OBJ_WOOD(GamePanel gp) {
        super(gp);
        this.gp = gp;
        this.scale = 0.5f;
        this.name = "Wood";
        quantity = 1;
        isStackable = true;
        scale = 1.0f;
        collision = false;

        solidArea.x = 8;
        solidArea.y = 8;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/wood/wood.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load image...");
        }
    }
}