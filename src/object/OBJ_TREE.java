package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_TREE extends BreakableItem {

    // constructor
    public OBJ_TREE(GamePanel gp) {
        super(gp, 3, "Axe");
        this.name = "tree";
        this.scale = 2.3f;
        this.isStackable = false;
        this.solidArea = new Rectangle(8, 8, 30, 30);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.collision = true;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/decorations/tree.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onBreak() {
        // Spawn trunk
        OBJ_TRUNK trunk = new OBJ_TRUNK(gp);
        trunk.worldX = this.worldX;
        trunk.worldY = this.worldY;
        for (int j = 0; j < gp.obj.length; j++) {
            if (gp.obj[j] == null) {
                gp.obj[j] = trunk;
                break;
            }
        }

        // Spawn 3 wood
        int woodsSpawned = 0;
        for (int i = 0; i < 3; i++) {
            OBJ_WOOD wood = new OBJ_WOOD(gp);
            wood.worldX = this.worldX + (i * gp.tileSize / 4);
            wood.worldY = this.worldY + (i * gp.tileSize / 4);

            for (int j = 0; j < gp.obj.length; j++) {
                if (gp.obj[j] == null) {
                    gp.obj[j] = wood;
                    woodsSpawned++;
                    break;
                }
            }
        }
    }
}
