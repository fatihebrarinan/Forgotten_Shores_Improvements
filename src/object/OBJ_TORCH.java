package object;

import entity.Entity;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_TORCH extends Entity {
    private BufferedImage[] frames;
    private final int numFrames = 7;

    public OBJ_TORCH(GamePanel gp) {
        super(gp);
        this.name = "Torch";
        this.scale = 1.2f;
        frames = new BufferedImage[numFrames];
        loadFrames();
        this.image = frames[0];
        this.solidArea = new Rectangle(0, 0, 48, 48);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
    }

    private void loadFrames() {
        try {
            frames[0] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/torch/torch_aimation1.png"));
            frames[1] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/torch/torch_aimation2.png"));
            frames[2] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/torch/torch_aimation3.png"));
            frames[3] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/torch/torch_aimation4.png"));
            frames[4] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/torch/torch_aimation5.png"));
            frames[5] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/torch/torch_aimation6.png"));
            frames[6] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/torch/torch_aimation7.png"));

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