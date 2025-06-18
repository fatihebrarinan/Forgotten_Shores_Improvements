package object;

import entity.WorldObject;
import player.Player;

public interface Interactable {
    public void interact(WorldObject worldObject, Player player);
}
