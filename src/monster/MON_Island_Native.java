package monster;


import entity.Entity;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import main.GamePanel;

public class MON_Island_Native extends Entity
{
    private int damage = 10;
    public int maxLife = 4;
    public int life = maxLife;
    
    public MON_Island_Native( GamePanel gp )
    {

        super(gp);

        direction = "down"; // initial direction
        name = "Island Native";
        speed = 1;
        this.scale = 2.0f;
        this.isMovingEntity = true;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 8;
        solidArea.width = 30;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;


        getImage();
    }

    public void getImage()
    {
        try
        {
            this.up1 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_north1.png"));
            this.up2 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_north2.png"));
            this.up3 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_north3.png"));
            this.up4 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_north4.png"));
            this.down1 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_south1.png"));
            this.down2 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_south2.png"));
            this.down3 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_south3.png"));
            this.down4 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_south4.png"));
            this.left1 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_left1.png"));
            this.left2 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_left2.png"));
            this.left3 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_left3.png"));
            this.left4 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_left4.png"));
            this.right1 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_right1.png"));
            this.right2 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_right2.png"));
            this.right3 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_right3.png"));
            this.right4 = ImageIO.read(getClass().getResourceAsStream("/res/monster/Island_Native/island_native_right4.png"));
            
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setAction() {
        actionLockCounter++;
    
        if ( actionLockCounter >= 90 ) 
        {  
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

    public int getDamage()
    {
        return damage;
    }

    public int getLife() {

        return life;
    }

    
}