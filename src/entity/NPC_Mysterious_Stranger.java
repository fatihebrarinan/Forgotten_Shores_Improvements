package entity;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class NPC_Mysterious_Stranger extends Entity {
    private GamePanel gp;
    public String dialogue = "Hello stranger, I hope you are not injured as the others who were in that plane. It is good that you are still alive. \n However, if I were you, I wouldn't take a deep breath yet. \n You should be very careful because there are scary creatures that may harm you in here. \n There are also limited resources in this island so that you should use them sparingly. \n You may try to create something new from what you will find to survive. \n You should be careful when consuming your hunting products or water that you gather from nature. \n These are only some advices from a native who lives here for a long long time... \n I've seen many try. \n I hope you'll be the one who survives... and escapes."; // dialogue box for the npc

    public NPC_Mysterious_Stranger(GamePanel gp) {
        super(gp);
        this.gp = gp;
        this.worldX = gp.tileSize * 21; // temporary locations for the npc
        this.worldY = gp.tileSize * 21;
        this.speed = 0;
        this.scale = 1.65f;
        this.isMovingEntity = false;
        loadSprites();
        scaleImages(scale);

        this.solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }

    private void loadSprites() {
        try {
            idle1 = ImageIO
                    .read(getClass().getResourceAsStream("/res/NPC/MysteriousStranger/mysterious_stranger_idle1.png"));
            idle2 = ImageIO
                    .read(getClass().getResourceAsStream("/res/NPC/MysteriousStranger/mysterious_stranger_idle2.png"));
            idle3 = ImageIO
                    .read(getClass().getResourceAsStream("/res/NPC/MysteriousStranger/mysterious_stranger_idle3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        spriteCounter++;
        if (spriteCounter > 50) {
            spriteNum++;
            if (spriteNum > 3) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

}