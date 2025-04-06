package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_APPLE extends Item {

    public OBJ_APPLE(GamePanel gp) {
        super(gp);
        this.name = "Apple";
        this.isStackable = true;
        this.quantity = 5;

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/decorations/apple.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void heal(entity.Player player) {
        if (player.getCurrentHealth() < player.getMaxHealth()) {
            int newHealth = player.getCurrentHealth() + 10;
            if (newHealth > player.getMaxHealth()) {
                newHealth = player.getMaxHealth();
            }
            player.setCurrentHealth(newHealth);
            quantity--;
        }
    }
}