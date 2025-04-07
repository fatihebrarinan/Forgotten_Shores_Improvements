package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_CAMPFIRE extends Item {
    private BufferedImage[] frames;
    private final int numFrames = 4;

    public OBJ_CAMPFIRE(GamePanel gp) {
        super(gp);
        this.name = "Camp Fire";
        this.scale = 1.75f;
        this.collision = true;
        this.itemType = ItemType.OTHER;
        frames = new BufferedImage[numFrames];
        loadFrames();
        this.image = frames[0];
        this.solidArea = new Rectangle(0, 0, 48, 48);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
    }

    private void loadFrames() {
        try {
            frames[0] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/campfire/fire_lit1.png"));
            frames[1] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/campfire/fire_lit2.png"));
            frames[2] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/campfire/fire_lit3.png"));
            frames[3] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/campfire/fire_lit4.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        spriteCounter++;
        if (spriteCounter > 18) {
            spriteNum++;
            if (spriteNum > numFrames) {
                spriteNum = 1;
            }
            this.image = frames[spriteNum - 1];
            spriteCounter = 0;
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