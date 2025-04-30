package monster;

import entity.Entity;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import main.GamePanel;
import object.OBJ_RAW_MEAT;

public class MON_Pig extends Entity 
{
    private int maxLife = 3;
    public int life = maxLife;
    private int normalSpeed = 1;
    private int fleeSpeed = 3;
    private int fleeDuration = 120; 
    private boolean hasDroppedMeat = false;
    private int fleeCounter = 0;
    private boolean isFleeing = false;
    public boolean hpBarStatus = false;
    public int hpBarCounter = 0;

    public MON_Pig(GamePanel gp) 
    {
        super(gp);
        name = "Pig";
        speed = normalSpeed;
        direction = "down";
        this.scale = 1.2f;
        this.isMovingEntity = true;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 8;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    // pig animations was lost so untill drawn again we will use static images no amination.
    public void getImage() {
        try {
            this.up1 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_north.png"));
            this.up2 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_north.png"));
            this.up3 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_north.png"));
            this.up4 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_north.png"));
            this.down1 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_south.png"));
            this.down2 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_south.png"));
            this.down3 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_south.png"));
            this.down4 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_south.png"));
            this.left1 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_left.png"));
            this.left2 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_left.png"));
            this.left3 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_left.png"));
            this.left4 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_left.png"));
            this.right1 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_right.png"));
            this.right2 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_right.png"));
            this.right3 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_right.png"));
            this.right4 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Pig/pig_right.png"));
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    @Override
    public void setAction() {
        actionLockCounter++;

        if (actionLockCounter >= 90) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) {
                direction = "up";
            } else if (i <= 50) {
                direction = "down";
            } else if (i <= 75) {
                direction = "left";
            } else {
                direction = "right";
            }

            actionLockCounter = 0;
        }
    }

    @Override
    public void reactToDamage() {
        // Start fleeing
        isFleeing = true;
        fleeCounter = fleeDuration;
        speed = fleeSpeed;
        hpBarStatus = true;
        hpBarCounter = 0;
    }

    @Override
    public void update() {
        super.update();

        if (isFleeing) {
            fleeCounter--;
            if (fleeCounter <= 0) {
                isFleeing = false;
                speed = normalSpeed;
            }
        }

        if (life <= 0 && !hasDroppedMeat) {
            dying = true;
            dyingCounter = 0;
            dropRawMeat();
            hasDroppedMeat = true;
        }

        if (dying && alive && life <= 0 && !hasDroppedMeat) 
        {
            dropRawMeat();
            hasDroppedMeat = true;
        }

        if (hpBarStatus) {
            hpBarCounter++;
            if (hpBarCounter > 600) {
                hpBarCounter = 0;
                hpBarStatus = false;
            }
        }
    }

    private void dropRawMeat() {
        Random random = new Random();
        int meatCount = random.nextInt(2) + 1;
    
        for (int i = 0; i < meatCount; i++) {
            OBJ_RAW_MEAT meat = new OBJ_RAW_MEAT(gp);
            meat.worldX = this.worldX + (i * gp.tileSize / 2);
            meat.worldY = this.worldY;
            meat.scale = 1.0f;
            boolean added = false;
            for (int j = 0; j < gp.obj.length; j++) {
                if (gp.obj[j] == null) {
                    gp.obj[j] = meat;
                    added = true;
                    break;
                }
            }
        }
    }

    public int getLife() {
        return life;
    }
}