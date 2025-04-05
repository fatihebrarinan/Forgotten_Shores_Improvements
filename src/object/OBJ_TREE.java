package object;

import entity.Entity;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_TREE extends Entity {
    // constructor
    public OBJ_TREE(GamePanel gp) {
        super(gp);
        this.name = "tree";
        this.scale = 2.3f;
        this.solidArea = new Rectangle(0, 0, 48, 48);
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
    public void draw(Graphics2D g2, boolean isPlayer, boolean isMoving) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            int scaledWidth = (int) (gp.tileSize * scale);
            int scaledHeight = (int) (gp.tileSize * scale);
            screenX -= (scaledWidth - gp.tileSize) / 2;
            screenY -= (scaledHeight - gp.tileSize) / 2;
            g2.drawImage(this.image, screenX, screenY, scaledWidth, scaledHeight, null);
        }
    }
}
