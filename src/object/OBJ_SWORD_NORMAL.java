package object;

import java.awt.Graphics2D;
import main.GamePanel;

public class OBJ_SWORD_NORMAL extends Item {

    public OBJ_SWORD_NORMAL(GamePanel gp) {
        super(gp);

        name = "Normal Sword";

        // try{
        // this.image = ImageIO.read(getClass().getResourceAsStream("URL")); // sword
        // object will be added
        // }
        // catch (IOException e){
        // e.printStackTrace();
        // }

        attackValue = 1;

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
