package tile_interactive;

import entity.Entity;
import entity.Player;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import main.GamePanel;
import object.OBJ_AXE;
import object.OBJ_WOOD;

public class IT_DryTree extends InteractiveTile
{
    GamePanel gp;
    public int life;
    public int maxLife = 4;
    private BufferedImage heartImage;
    private boolean destroyed = false;

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
            this.heartImage = ImageIO.read(getClass().getResourceAsStream("/res/gameUI/heart.png"));
        }

        catch (IOException e) 
        {
            e.printStackTrace();
        }
        this.destructible = true;
    }

    public boolean isCorrectItem(Entity entity)
    {
        if (entity instanceof Player)
        {
            Player player = (Player) entity;
            return player.getCurrentWeapon() instanceof OBJ_AXE;
        }
        return false;
    }

    public InteractiveTile getDestroyedForm()
    {
        InteractiveTile tile = new IT_Trunk(gp);
        tile.worldX = this.worldX;
        tile.worldY = this.worldY;
        return tile;
    }

    public void onDestroy(int tileIndex) 
    {
        if (destroyed) 
        {
            return;
        }
        destroyed = true;

        // spawn 2 OBJ_WOOD items at the tree's location
        int woodsSpawned = 0;
        for (int i = 0; i < 2 && woodsSpawned < 2; i++) 
        {
            OBJ_WOOD wood = new OBJ_WOOD(gp);

            wood.worldX = this.worldX + (i * gp.tileSize / 4);
            wood.worldY = this.worldY + (i * gp.tileSize / 4);
            boolean placed = false;
            for (int j = 0; j < gp.obj.length; j++) {
                if (gp.obj[j] == null) {
                    gp.obj[j] = wood;
                    woodsSpawned++;
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                
                // try to clear an old object to make space
                for (int j = 0; j < gp.obj.length && woodsSpawned < 2; j++) {
                    if (gp.obj[j] != null && !(gp.obj[j] instanceof OBJ_WOOD)) {
                        gp.obj[j] = wood;
                        woodsSpawned++;
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g2, boolean isPlayer, boolean isMoving) {
        if (destroyed) {
            System.out.println("Skipping draw for destroyed tree at worldX=" + worldX + ", worldY=" + worldY);
            return;
        }
    
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
    
            // Draw health bar only if life is between 0 and maxLife
            if (life > 0 && life < maxLife) {
                int healthBarDiameter = 40;
                int healthBarX = screenX + (scaledWidth - healthBarDiameter) / 2;
                int healthBarY = screenY - healthBarDiameter - 10;
    
                System.out.println("Drawing tree health bar: life = " + life + "/" + maxLife);
    
                g2.setColor(Color.GRAY);
                g2.fillOval(healthBarX, healthBarY, healthBarDiameter, healthBarDiameter);
    
                double healthPercentage = (double) life / maxLife;
                double arcAngle = 360 * healthPercentage;
                g2.setColor(Color.RED);
                g2.setStroke(new java.awt.BasicStroke(4));
                Arc2D.Double arc = new Arc2D.Double(healthBarX, healthBarY, healthBarDiameter, healthBarDiameter, 90, -arcAngle, Arc2D.OPEN);
                g2.draw(arc);
                g2.setStroke(new java.awt.BasicStroke(1));
    
                int heartSize = 20;
                int heartX = healthBarX + (healthBarDiameter - heartSize) / 2;
                int heartY = healthBarY + (healthBarDiameter - heartSize) / 2;
                g2.drawImage(heartImage, heartX, heartY, heartSize, heartSize, null);
            }
        }
    }
}
