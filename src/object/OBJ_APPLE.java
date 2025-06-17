package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import player.Player;

public class OBJ_APPLE extends Item {

    public OBJ_APPLE(GamePanel gp) {
        super(gp);
        this.name = "Apple";
        this.isStackable = true;
        this.isPickable = true;
        this.itemType = ItemType.CONSUMABLE;
        this.solidArea = new Rectangle(0, 0, 48, 48);
        this.quantity = 1;

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/decorations/apple.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void use(Player player) {
        if (player.getCurrentHunger() == player.getMaxHunger()) {
            heal(player);
        } else if (player.getCurrentHunger() < player.getMaxHunger()) {
            eat(player);
        }
        quantity--;
    }

    public void heal(player.Player player) {
        if (player.getCurrentHealth() < player.getMaxHealth()) {
            int newHealth = player.getCurrentHealth() + 10;
            if (newHealth > player.getMaxHealth()) {
                newHealth = player.getMaxHealth();
            }
            player.setCurrentHealth(newHealth);
            quantity--;
        }
    }

    public void eat(Player player) {
        if (player.getCurrentHunger() < player.getMaxHunger()) {
            int newHunger = player.getCurrentHunger() + 10;
            if (newHunger > player.getMaxHunger()) {
                newHunger = player.getMaxHunger();
            }
            player.setCurrentHunger(newHunger);
            quantity--;
        }
    }

}