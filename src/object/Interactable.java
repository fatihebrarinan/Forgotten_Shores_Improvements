package object;

import entity.Entity;
import player.Player;

public interface Interactable {
    public void interact(Entity entity, Player player);
}
