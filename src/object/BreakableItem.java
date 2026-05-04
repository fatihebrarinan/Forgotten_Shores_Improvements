package object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import main.GamePanel;

public abstract class BreakableItem extends Item implements Breakable {
    public int life;
    public int maxLife;
    public boolean destroyed = false;
    private String requiredToolName;

    public BreakableItem(GamePanel gp, int maxLife, String requiredToolName) {
        super(gp);
        this.maxLife = maxLife;
        this.life = maxLife;
        this.requiredToolName = requiredToolName;
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
            for (int i = 0; i < gp.obj.length; i++) {
                if (gp.obj[i] == this) {
                    gp.obj[i] = null;
                    break;
                }
            }
        }
    }

    protected abstract void onBreak();

    @Override
    public void draw(Graphics2D g2) {
        if (destroyed) {
            return;
        }

        super.draw(g2); // Draw the item itself

        // Draw health bar if object is not completely broken
        if (life > 0) {
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
            if (gp.ui.heartImage != null) {
                g2.drawImage(gp.ui.heartImage, heartX, heartY, heartSize, heartSize, null);
            }
        }
    }
}
