package object;

import main.GamePanel;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_WOOD extends Item {
    GamePanel gp;

    public OBJ_WOOD(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "Wood";
        itemType = ItemType.OTHER; // enum
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

    @Override
    public void draw(Graphics2D g2, boolean isPlayer, boolean isMoving) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            int scaledWidth = (int) (gp.tileSize * scale);
            int scaledHeight = (int) (gp.tileSize * scale);
            if (image != null) {
                g2.drawImage(image, screenX, screenY, scaledWidth, scaledHeight, null);
            }
        }
    }
}