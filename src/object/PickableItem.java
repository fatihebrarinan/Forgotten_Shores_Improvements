package object;

import main.GamePanel;
import player.Player;

public abstract class PickableItem extends Item implements Pickable {
    public PickableItem(GamePanel gp) {
        super(gp);
    }

    @Override
    public boolean pickUp(Player player) {
        return player.pickUpItem(this);
    }
}
