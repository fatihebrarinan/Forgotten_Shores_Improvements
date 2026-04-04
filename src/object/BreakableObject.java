package object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public abstract class BreakableObject extends Item implements Breakable {
    public int life;
    public int maxLife;
    protected BufferedImage heartImage;
    public boolean destroyed = false;
    private String requiredToolName;

    public BreakableObject(GamePanel gp, int maxLife, String requiredToolName) {
        super(gp);
        this.maxLife = maxLife;
        this.life = maxLife;
        this.requiredToolName = requiredToolName;
        try {
            this.heartImage = ImageIO.read(getClass().getResourceAsStream("/res/gameUI/heart.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load heart image for breakable object.");
        }
    }

    @Override
    public String getRequiredToolName() {
        return requiredToolName;
    }

    @Override
    public void breakObject() {
        if (destroyed) {
            return;
        }

        life--;

        if (life <= 0) {
            destroyed = true;
            onBreak();
        }
    }

    protected abstract void onBreak();

    @Override
    public void draw(Graphics2D g2) {
        if (destroyed) {
            return;
        }

        super.draw(g2); // Draw the item itself

        // Draw health bar only if life is between 0 and maxLife
        if (life > 0 && life < maxLife) {
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            int scaledWidth = (int) (gp.tileSize * scale);
            int scaledHeight = (int) (gp.tileSize * scale);
            screenX -= (scaledWidth - gp.tileSize) / 2;
            screenY -= (scaledHeight - gp.tileSize) / 2;

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
            if (heartImage != null) {
                g2.drawImage(heartImage, heartX, heartY, heartSize, heartSize, null);
            }
        }
    }
}
