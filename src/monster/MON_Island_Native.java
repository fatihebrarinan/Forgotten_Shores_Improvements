package monster;


imnport entity.Entity;
import main.GamePanel;

public class MON_Island_Native extends Entity
{

    public MON_Island_Native( GamePanel gp )
    {
        super(gp);

        name = "Island Native";
        speed = 1;
        maxLife = 4;
        life = maxLife;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    
}