package object;

import entity.Player;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_WATER_BUCKET extends Item
{
    boolean isPurified;
    boolean isEmpty;

    public OBJ_WATER_BUCKET(GamePanel gp) 
    {
        super(gp);
        this.name = "Water Bucket";
        this.isStackable = false;
        this.itemType = ItemType.CONSUMABLE;
        this.solidArea = new Rectangle(0, 0, 48, 48);
        this.isEmpty = true;
        this.isPurified = false;

        try 
        {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/bucket/bucket_empty.png"));
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }   

    public void consume(Player player)
    {
        if (!isEmpty)
        {
            try 
            {
                drink(player);
            } 
            catch (IOException ex) 
            {
                //
            }
        }
        else if (isEmpty)
        {
           gp.ui.addMessage("The bucket is empty!");
        }
    }

    public void fill(boolean nearWater) 
    {
        if (!isEmpty) 
        {
            System.out.println("The bucket is already filled.");
            return;
        }
        
        if (nearWater) 
        {
            isEmpty = false;
            isPurified = false; 
            try 
            {
                this.image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/bucket/bucket_filled.png"));
                System.out.println("You filled the bucket with water.");
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();
            }
        } 
        else 
        {
            System.out.println("You need to be near water to fill the bucket!");
        }
    }

    public void purify(boolean nearFire)
    {
        if (isEmpty)
        {
            gp.ui.addMessage("Bucket is empty. Nothing to purify!");
            return;
        }

        if (!nearFire)
        {
            gp.ui.addMessage("You must be near fire to purify water!");
            return;
        }

        if (!isPurified)
        {
            isPurified = true;
            gp.ui.addMessage("You purified the water");
        }
        else
        {
            gp.ui.addMessage("Water is already purified.");
        }
    }

    public void drink(Player player) throws IOException
    {
        if(player.getCurrentThirst() < player.getMaxThirst())
        {
            int newThirst = player.getCurrentThirst() + 30;

            if(newThirst > player.getMaxThirst())
            {
                newThirst = player.getMaxThirst();
            }
            
            player.setCurrentThirst(newThirst);
            if (!isPurified)
            {
                player.setPoisonStatus();
            }
            isEmpty = true;
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/bucket/bucket_empty.png"));
        } 
    }

    public void draw(Graphics2D g2, boolean isPlayer, boolean isMoving) 
    {
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
