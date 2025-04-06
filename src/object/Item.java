package object;

import main.GamePanel;
import entity.Entity;

public class Item extends Entity 
{
    public boolean isStackable = false; 
    public int quantity = 1;           

    public Item( GamePanel gp ) 
    {
        super(gp);
    }
}
