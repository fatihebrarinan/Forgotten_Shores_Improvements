package tile_interactive;

import java.awt.Graphics2D;
import main.GamePanel;
import object.Item;
import object.OBJ_AXE;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import entity.Entity;
import entity.Player;

public class IT_DryTree extends InteractiveTile
{
    GamePanel gp;
    public int life;
    public int maxLife = 3;

    public IT_DryTree(GamePanel aGP)
    {
        super(aGP);
        this.gp = aGP;

        this.life = maxLife;
        this.scale = 2.3f;
        /*this.solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.collision = true;*/

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 8;
        solidArea.width = 30;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        this.collision = true;
        
        try 
        { 
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/tiles_interactive/tree.png"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        this.destructible = true;
    }

    public boolean isCorrectItem(Entity entity)
    {
        boolean isCorrectItem = false;

        int selectedSlot = ((Player)entity).inventory.getSelectedSlot();
        System.out.println("Selected slot: " + selectedSlot);
        Item selectedItem = ((Player)entity).inventory.getItem(selectedSlot);
        //System.out.println("Selected item: " + "axe");

        if(selectedItem instanceof OBJ_AXE)
        {
            isCorrectItem = true;
        }

        return isCorrectItem;
    }

    public InteractiveTile getDestroyedForm()
    {
        InteractiveTile tile = new IT_Trunk(gp);
        return tile;
    }

    @Override
    public void draw(Graphics2D g2, boolean isPlayer, boolean isMoving) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            int scaledWidth = (int) (gp.tileSize * scale);
            int scaledHeight = (int) (gp.tileSize * scale);
            screenX -= (scaledWidth - gp.tileSize) / 2;
            screenY -= (scaledHeight - gp.tileSize) / 2;
            g2.drawImage(this.image, screenX, screenY, scaledWidth, scaledHeight, null);
        }
    }
}
