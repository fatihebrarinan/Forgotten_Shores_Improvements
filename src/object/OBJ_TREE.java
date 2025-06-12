package object;

import entity.Entity;
import entity.Player;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;


public class OBJ_TREE extends Item implements Harvestable {
    public int life;
    public int maxLife = 3;
    private BufferedImage heartImage;
    private boolean destroyed = false;

    // constructor
    public OBJ_TREE(GamePanel gp) {
        super(gp);
        this.name = "tree";
        this.scale = 2.3f;
        this.life = maxLife;
        this.solidArea = new Rectangle(8, 8, 30, 30);
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.collision = true;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/res/decorations/tree.png"));
            this.heartImage = ImageIO.read(getClass().getResourceAsStream("/res/gameUI/heart.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void harvest() {
        if (destroyed) {
            return;
        }
        destroyed = true;

        // Spawn trunk
        OBJ_TRUNK trunk = new OBJ_TRUNK(gp);
        trunk.worldX = this.worldX;
        trunk.worldY = this.worldY;
        for (int j = 0; j < gp.obj.length; j++) {
            if (gp.obj[j] == null) {
                gp.obj[j] = trunk;
                break;
            }
        }

        // Spawn 3 wood
        int woodsSpawned = 0;
        for (int i = 0; i < 3; i++) {
            OBJ_WOOD wood = new OBJ_WOOD(gp);
            wood.worldX = this.worldX + (i * gp.tileSize / 4);
            wood.worldY = this.worldY + (i * gp.tileSize / 4);

            for (int j = 0; j < gp.obj.length; j++) {
                if (gp.obj[j] == null) {
                    gp.obj[j] = wood;
                    woodsSpawned++;
                    break;
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g2, boolean isPlayer, boolean isMoving) {
        if (destroyed) {
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

                g2.setColor(Color.GRAY);
                g2.fillOval(healthBarX, healthBarY, healthBarDiameter, healthBarDiameter);

                double healthPercentage = (double) life / maxLife;
                double arcAngle = 360 * healthPercentage;
                g2.setColor(Color.RED);
                g2.setStroke(new java.awt.BasicStroke(4));
                Arc2D.Double arc = new Arc2D.Double(healthBarX, healthBarY, healthBarDiameter, healthBarDiameter, 90,
                        -arcAngle, Arc2D.OPEN);
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
