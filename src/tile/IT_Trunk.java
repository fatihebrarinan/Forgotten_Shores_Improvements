package tile;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class IT_Trunk extends InteractiveTile {
    GamePanel gp;

    public IT_Trunk(GamePanel aGP) {
        super(aGP);
        this.gp = aGP;
        this.name = "Trunk";
        this.scale = 2.3f;
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 8;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        this.collision = true;

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/tiles_interactive/trunk.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // make sure the player can pass through (no solid area)
        this.solidArea = new Rectangle();
        this.solidArea.x = 0;
        this.solidArea.y = 0;
        this.solidAreaDefaultX = solidArea.x;
        this.solidAreaDefaultY = solidArea.y;
        this.solidArea.width = 0;
        this.solidArea.height = 0;
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
