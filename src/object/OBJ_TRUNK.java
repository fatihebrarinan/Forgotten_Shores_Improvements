package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_TRUNK extends Item {
    public OBJ_TRUNK(GamePanel gp) {
        super(gp);
        this.name = "Trunk";
        this.isPickable = false;
        this.scale = 2.3f;
        this.solidArea = new Rectangle(0, 0, 0, 0); // No collision
        this.solidAreaDefaultX = solidArea.x;
        this.solidAreaDefaultY = solidArea.y;
        this.collision = false;

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/tiles_interactive/trunk.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 