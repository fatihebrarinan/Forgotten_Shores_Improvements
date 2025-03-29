package object;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_TORCH extends SuperObject 
{
    private BufferedImage[] frames;
    private final int numFrames = 7;

    public OBJ_TORCH() {
        this.name = "Torch";
        this.scale = 1.2f;
        frames = new BufferedImage[numFrames];
        loadFrames();
        this.image = frames[0];
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
            
        } catch (IOException e) 
        {
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
}