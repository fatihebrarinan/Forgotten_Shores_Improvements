package entity;

import java.awt.Rectangle;
import main.GamePanel;

public class Entity extends WorldObject {
    public int speed;
    public int actionLockCounter = 0;

    public int hp;

    public String direction;

    public EntitySpriteManager spriteManager = new EntitySpriteManager(this);

    public Rectangle attackArea;
    public boolean collisionOn = false;

    public boolean alive = true;
    public boolean dying = false;

    public boolean isMovingEntity = false;

    public String name;

    public boolean invincible = false;
    public int invincibilityTimer = 0;
    public int invincibilityDuration = 60;

    public Entity(GamePanel gp) {
        super(gp);
        this.solidArea = new Rectangle();
    }

    // NPC movement method for future NPCs implementation
    public void update() {
        if (isMovingEntity & !dying) {
            setAction();
            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkObject(this, false);
            gp.cChecker.checkEntity(this, gp.entityList);

            if (!this.collisionOn) {
                if (this.direction != null) {
                    if (this.direction.equals("up")) {
                        this.worldY -= this.speed;
                    } else if (direction.equals("down")) {
                        this.worldY += this.speed;
                    } else if (direction.equals("left")) {
                        this.worldX -= this.speed;
                    } else if (direction.equals("right")) {
                        this.worldX += this.speed;
                    }
                }
            }
        }

        spriteManager.updateAnimation();

        // Invincibility logic
        if (invincibilityTimer > 0) {
            invincibilityTimer--;

            if (invincibilityTimer == 0) {
                invincible = false;
            }
        }

    }

    public void reactToDamage() {

    }

    public void setAction() {

    }
}
