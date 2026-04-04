package object;

import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.WorldObject;
import main.GamePanel;
import player.Player;

public class OBJ_APPLE_TREE extends BreakableItem implements Interactable {
    private boolean hasApple = true;

    public OBJ_APPLE_TREE(GamePanel gp) {
        super(gp, 3, "Axe");

        this.itemType = ItemType.OTHER;
        this.name = "Apple Tree";
        this.scale = 2.3f;
        this.solidArea = new Rectangle(8, 8, 30, 30);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.collision = true;

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/decorations/apple_tree.png"));
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

        // Spawn 5 apples
        int applesSpawned = 0;
        for (int i = 0; i < 5; i++) {
            OBJ_APPLE apple = new OBJ_APPLE(gp);
            apple.worldX = this.worldX + (i * gp.tileSize / 8);
            apple.worldY = this.worldY + (i * gp.tileSize / 8);
            apple.quantity = 1;

            for (int j = 0; j < gp.obj.length; j++) {
                if (gp.obj[j] == null) {
                    gp.obj[j] = apple;
                    applesSpawned++;
                    break;
                }
            }
        }

        // Spawn 2 wood
        int woodsSpawned = 0;
        for (int i = 0; i < 2; i++) {
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

    @Override
    public void interact(WorldObject worldObject, Player player) {
        if (hasApple) {
            // Spawn 5 apples
            int applesSpawned = 0;
            for (int i = 0; i < 5; i++) {
                OBJ_APPLE apple = new OBJ_APPLE(gp);
                apple.worldX = this.worldX + (i * gp.tileSize / 8);
                apple.worldY = this.worldY + (i * gp.tileSize / 8);

                for (int j = 0; j < gp.obj.length; j++) {
                    if (gp.obj[j] == null) {
                        gp.obj[j] = apple;
                        applesSpawned++;
                        break;
                    }
                }
            }
            // Transform the apple tree to a normal tree
            OBJ_TREE tree = new OBJ_TREE(gp);
            tree.worldX = this.worldX;
            tree.worldY = this.worldY;
            for (int j = 0; j < gp.obj.length; j++) {
                if (gp.obj[j] == null) {
                    gp.obj[j] = tree;
                    break;
                }
            }
            // Remove the apple tree
            for (int j = 0; j < gp.obj.length; j++) {
                if (gp.obj[j] == this) {
                    gp.obj[j] = null;
                    break;
                }
            }
            gp.ui.addMessage("You collected 5 apples!");
            hasApple = false;
        } else {
            gp.ui.addMessage("There are no apples on this tree.");
        }
    }
}
