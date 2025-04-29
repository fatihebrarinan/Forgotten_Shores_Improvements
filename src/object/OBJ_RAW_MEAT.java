package object;

import entity.Player;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_RAW_MEAT extends Item {
    
    public OBJ_RAW_MEAT(GamePanel gp) {
        super(gp);
        name = "Raw Meat";
        isStackable = true;
        itemType = ItemType.CONSUMABLE;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/porkchop/porkchop.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void use(Player player) 
    {
        // currently no poison is done whoever will do the cooking methods make it so raw meat decreases hunger or health change this code.
        int hungerIncrease = 20;
        player.setCurrentHunger(player.getCurrentHunger() + hungerIncrease);
        quantity--;
        gp.ui.addMessage("Ate raw meat, hunger +" + hungerIncrease);
    }
}