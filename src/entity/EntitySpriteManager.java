package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class EntitySpriteManager {

    public BufferedImage attackUp1, attackUp2, attackLeft1, attackLeft2, attackDown1,
            attackDown2, attackRight1, attackRight2;

    public BufferedImage scaledUp1, scaledUp2, scaledDown1, scaledDown2,
            scaledLeft1, scaledLeft2, scaledRight1, scaledRight2,
            scaledIdle1, scaledIdle2, scaledIdle3, scaledIdle4,
            scaledAttackUp1, scaledAttackUp2, scaledAttackDown1, scaledAttackDown2,
            scaledAttackLeft1, scaledAttackLeft2, scaledAttackRight1, scaledAttackRight2,
            scaledImage;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public int dyingCounter = 0;
    public int hpBarCounter = 0;
    public boolean hpBarStatus = false;

    private Entity entity;

    public EntitySpriteManager(Entity entity) {
        this.entity = entity;
    }

    // Method to scale images once during initialization
    public void scaleImages(float scale) {
        int scaledWidth = (int) (entity.gp.tileSize * scale);
        int scaledHeight = (int) (entity.gp.tileSize * scale);

        scaledUp1 = scaleImage(entity.up1, scaledWidth, scaledHeight);
        scaledUp2 = scaleImage(entity.up2, scaledWidth, scaledHeight);
        scaledDown1 = scaleImage(entity.down1, scaledWidth, scaledHeight);
        scaledDown2 = scaleImage(entity.down2, scaledWidth, scaledHeight);
        scaledLeft1 = scaleImage(entity.left1, scaledWidth, scaledHeight);
        scaledLeft2 = scaleImage(entity.left2, scaledWidth, scaledHeight);
        scaledRight1 = scaleImage(entity.right1, scaledWidth, scaledHeight);
        scaledRight2 = scaleImage(entity.right2, scaledWidth, scaledHeight);
        scaledIdle1 = scaleImage(entity.idle1, scaledWidth, scaledHeight);
        scaledIdle2 = scaleImage(entity.idle2, scaledWidth, scaledHeight);
        scaledIdle3 = scaleImage(entity.idle3, scaledWidth, scaledHeight);
        scaledIdle4 = scaleImage(entity.idle4, scaledWidth, scaledHeight);
        scaledAttackUp1 = scaleImage(attackUp1, scaledWidth, scaledHeight);
        scaledAttackUp2 = scaleImage(attackUp2, scaledWidth, scaledHeight);
        scaledAttackDown1 = scaleImage(attackDown1, scaledWidth, scaledHeight);
        scaledAttackDown2 = scaleImage(attackDown2, scaledWidth, scaledHeight);
        scaledAttackLeft1 = scaleImage(attackLeft1, scaledWidth, scaledHeight);
        scaledAttackLeft2 = scaleImage(attackLeft2, scaledWidth, scaledHeight);
        scaledAttackRight1 = scaleImage(attackRight1, scaledWidth, scaledHeight);
        scaledAttackRight2 = scaleImage(attackRight2, scaledWidth, scaledHeight);
        scaledImage = scaleImage(entity.image, scaledWidth, scaledHeight); // For OBJ_RAW_MEAT
    }

    private BufferedImage scaleImage(BufferedImage original, int width, int height) {
        if (original == null)
            return null;
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return scaled;
    }

    public void updateAnimation() {
        // Alternated spriteNum for animation
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if (entity.dying) {
            dyingCounter++;
        }
    }
}
