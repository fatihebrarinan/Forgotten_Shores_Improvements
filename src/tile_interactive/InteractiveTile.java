package tile_interactive;

import entity.Entity;
import main.GamePanel;

public class InteractiveTile extends Entity
{
    GamePanel gp;
    public boolean destructible = false;

    public InteractiveTile(GamePanel aGP)
    {
        super(aGP);
        this.gp = aGP;
    }

    public boolean isCorrectItem(Entity entity)
    {
        boolean isCorrectItem = false;
        return isCorrectItem;
    }

    public InteractiveTile getDestroyedForm()
    {
        InteractiveTile tile = null;
        return tile;
    }

    public void update()
    {
        if(this.invincible)
        {
            this.invincibilityDuration++;
            if(this.invincibilityDuration > 20)
            {
                this.invincible = false;
                this.invincibilityDuration = 0;
            }
        }
    }
}
