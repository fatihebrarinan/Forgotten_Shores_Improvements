package object;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_CAMPFIRE extends SuperObject 
{
    private BufferedImage[] frames;
    private final int numFrames = 4;

    public OBJ_CAMPFIRE() {
        this.name = "Camp Fire";
        this.scale = 1.75f;
        frames = new BufferedImage[numFrames];
        loadFrames();
        this.image = frames[0];
    }

    private void loadFrames() {
        try {
            frames[0] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/campfire/fire_lit1.png"));
            frames[1] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/campfire/fire_lit2.png"));
            frames[2] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/campfire/fire_lit3.png"));
            frames[3] = ImageIO.read(getClass().getResourceAsStream("/res/Objects/campfire/fire_lit4.png"));
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum++;
            if (spriteNum > numFrames) {
                spriteNum = 1;
            }
            this.image = frames[spriteNum - 1];
            spriteCounter = 0;
        }
    }
}