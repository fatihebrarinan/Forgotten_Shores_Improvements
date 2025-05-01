package object;

import entity.Player;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_RAW_MEAT extends Item {

    boolean isCooked;
    
    public OBJ_RAW_MEAT(GamePanel gp) {
        super(gp);
        name = "Meat";
        isStackable = true;
        this.solidArea = new Rectangle(0, 0, 48, 48);
        itemType = ItemType.CONSUMABLE;
        this.isCooked = false;
        try 
        {
            image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/porkchop/porkchop.png"));
            scaleImages(scale);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void use(Player player) 
    {
        if (isCooked)
        {
            int hungerIncrease = 25;
            player.setCurrentHunger(player.getCurrentHunger() + hungerIncrease);
            quantity--;
            gp.ui.addMessage("Ate cooked meat");
        }
        else if (!isCooked)
        {
            int hungerIncrease = 15;
            player.setCurrentHunger(player.getCurrentHunger() + hungerIncrease);
            quantity--;
            poison(player);
            gp.ui.addMessage("Ate raw meat, you have been poisoned!");
        }
    }

    public void cook()
    {
        if (isCooked)
        {
            gp.ui.addMessage("The meat is already cooked!");
            return;
        }
        
        isCooked = true;
        gp.ui.addMessage("You cooked the meat.");
        
    }

    public void poison(Player player)
    {
        player.setPoisonStatus();
    }
}
