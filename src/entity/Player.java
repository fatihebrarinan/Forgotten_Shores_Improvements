package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.Inventory;
import main.KeyHandler;
import monster.MON_Island_Native;
import object.Item;
import object.OBJ_SHIELD_WOOD;
import object.OBJ_SWORD_NORMAL;

public class Player extends Entity {

    // dialogue cooldown timer to combat with dialogue state being re-triggered
    // again
    private int dialogueCooldown = 0;
    private final int cooldownDuration = 120;

    private int damageCooldown = 0;
    private final int damageCooldownDuration = 30;

    public boolean attacking = false;
    private boolean isAttackingForCollision = false; // New flag

    // to track attack frames & duration of each frame
    private int attackFrameCount = 0;
    private final int attackDuration = 25;

    private int maxHealth;
    protected int currentHealth;
    private int maxHunger;
    private int currentHunger;

    private int hungerDecreaseCounter = 0;
    private final int hungerDecreaseInterval = 60; // 60 seconds or 1 minute for now further to be maybe changed

    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    static boolean wasMoving = false;
    int hasKey = 0;

    private int level;
    private int strength;
    private int dexterity;
    private int exp;
    private int expToNextLevel;
    private int coin;
    private Entity currentWeapon;
    private Entity currentShield;

    private int defense;
    private int attack;

    // new inventory
    public Inventory inventory = new Inventory();

    public Player(GamePanel aGP, KeyHandler aKeyHandler) {
        super(aGP);
        this.keyHandler = aKeyHandler;
        screenX = (gp.screenWidth / 2) - (gp.tileSize / 2);
        screenY = (gp.screenHeight / 2) - (gp.tileSize / 2);
        this.scale = 2.0f;

        this.solidArea = new Rectangle();

        solidArea.x = 8;
        solidArea.y = 8;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 36;

        this.attackArea = new Rectangle();

        attackArea.width = 36;
        attackArea.height = 36;

        this.isMovingEntity = true;

        setDefaultValues();
        System.out.println("Initial Health: " + currentHealth);
        getPlayerImage();

        // getPlayerAttackImage(); when sprites are ready remove the COMMENTS!
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23; // initial y
        worldY = gp.tileSize * 21; // initial x
        this.speed = 4; // initial movement speed
        this.direction = "down"; // initial direction where the player looks

        maxHealth = 100; // maximum health a player can have
        currentHealth = maxHealth; // initial health equals to max health ( 100 )

        maxHunger = 100; // maximum hunger a player can have
        currentHunger = maxHunger; // initial hunger equalts to max hunger ( 100 )

        invincible = false;
        invincibilityTimer = 0;

        // PLAYER STATS

        level = 1;

        strength = 1; // the more strength => more damage
        dexterity = 1; // the more dexterity => less damage taken
        exp = 0;
        expToNextLevel = 5;
        coin = 0;
        currentWeapon = new OBJ_SWORD_NORMAL(gp);
        currentShield = new OBJ_SHIELD_WOOD(gp);

        attack = getAttack();
        defense = getDefense();
    }

    public int getAttack() {
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense() {
        return dexterity * currentShield.defenseValue;
    }

    public void getPlayerImage() {
        try {
            /**
             * Image URLs will be changed according to our images
             */
            this.up1 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_north1.png"));
            this.up2 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_north2.png"));
            this.up3 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_north3.png"));
            this.up4 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_north4.png"));
            this.down1 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_south1.png"));
            this.down2 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_south2.png"));
            this.down3 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_south3.png"));
            this.down4 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_south4.png"));
            this.left1 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_left1.png"));
            this.left2 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_left2.png"));
            this.left3 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_left3.png"));
            this.left4 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_left4.png"));
            this.right1 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_right1.png"));
            this.right2 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_right2.png"));
            this.right3 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_right3.png"));
            this.right4 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_right4.png"));

            this.idle1 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_idle1.png"));
            this.idle2 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_idle2.png"));
            this.idle3 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_idle3.png"));
            this.idle4 = ImageIO.read(getClass().getResourceAsStream("/res/Player/player_idle4.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getCurrentImage() {
        return idle1;
    }

    /*
     * SINCE THE SPRITES ARENT READY REMOVING PLAYER ATTACK ANIMATIONS FOR TESTING
     * THE GAME
     * public void getPlayerAttackImage()
     * {
     * try
     * {
     * this.attackUp1 =
     * ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1"));
     * this.attackUp2 =
     * ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1"));
     * this.attackDown1 =
     * ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1"));
     * this.attackDown2 =
     * ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1"));
     * this.attackLeft1 =
     * ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1"));
     * this.attackLeft2 =
     * ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1"));
     * this.attackRight1 =
     * ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1"));
     * this.attackRight2 =
     * ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1"));
     * }catch(IOException e)
     * {
     * e.printStackTrace();
     * }
     * }
     */

    /**
     * this method updates the player's direction and speed according to key input
     */
    public void update() {

        super.update();

        if (dialogueCooldown > 0) {
            dialogueCooldown--;
        }

        if (invincibilityTimer > 0) {

            invincibilityTimer--;

            if (invincibilityTimer == 0) {
                invincible = false; // End invincibility
            }
        }

        // decreasing hunger over time
        hungerDecreaseCounter++;

        if (hungerDecreaseCounter >= hungerDecreaseInterval) {
            if (currentHunger > 0) {
                currentHunger--;
            }
            hungerDecreaseCounter = 0;
        }

        if (keyHandler.leftClicked) {
            attacking = true;
            keyHandler.leftClicked = false;
        }

        // Check object collision
        int objectIndex = gp.cChecker.checkObject(this, true);

        if (objectIndex != 999) 
        {
            gp.ui.showTooltip = true; 
            
            if (keyHandler.fPressed) 
            { 
                pickUpObject(objectIndex); 
                keyHandler.fPressed = false; 
            }

        } else 
        {
            gp.ui.showTooltip = false; 
        }

        if (attacking) {
            attacking();
            attackFrameCount++;
            if (attackFrameCount >= attackDuration) {
                attacking = false;
                attackFrameCount = 0;
            }
        }

        if (!attacking) {
            boolean isMoving = (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed
                    || keyHandler.rightPressed);

            if (isMoving || keyHandler.leftClicked) {
                if (!wasMoving) {
                    spriteNum = 1;
                }
                if (this.attacking) {
                    attacking();
                }
                if (keyHandler.upPressed) {
                    this.direction = "up";
                } else if (keyHandler.downPressed) {
                    this.direction = "down";
                } else if (keyHandler.leftPressed) {
                    this.direction = "left";
                } else if (keyHandler.rightPressed) {
                    this.direction = "right";
                }

                // Check tile collision
                collisionOn = false;
                gp.cChecker.checkTile(this);

                // Check Object collision
                gp.cChecker.checkObject(this, true);

                // Check NPC collision
                int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
                interactNPC(npcIndex);

                // Check monster collision
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);

                // Check event
                // gp.eHandler.checkEvent(); // SHOULD BE ADDED!!!

                this.gp.keyH.leftClicked = false; // leftClicked should be added.

                if (!this.collisionOn && !keyHandler.leftClicked) { // Without this !keyHandler.enterPressed statement,
                                                                    // player moves when enter is pressed.
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

                this.spriteCounter++;
                if (this.spriteCounter > 12) {
                    if (this.spriteNum == 1) {
                        this.spriteNum = 2;
                    } else if (this.spriteNum == 2) {
                        this.spriteNum = 1;
                    }
                    this.spriteCounter = 0;
                }
            } else {
                if (wasMoving) {
                    spriteNum = 1;
                }

                this.spriteCounter++;
                if (this.spriteCounter > 20) {
                    this.spriteNum++;
                    if (this.spriteNum > 4) {
                        this.spriteNum = 1;
                    }
                    this.spriteCounter = 0;
                }
            }

            wasMoving = isMoving;
        }
    }

    public void interactNPC(int i) {
        if ((i != 999) && (dialogueCooldown == 0) && (gp.gameState != gp.dialogueState)) {
            System.out.println("npc hit.");
            gp.gameState = gp.dialogueState;
            gp.ui.showTooltip = false;
            dialogueCooldown = cooldownDuration;
        }
    }

    public void attacking() {
        spriteCounter++;

        if (spriteCounter <= 5) // from 0th frame to 5th frame second sprite will be shown.
        {
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) // from 6th frame to 25th frame second sprite will be shown.
        {
            spriteNum = 2;

            // Save the current data
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust player's worldX/Y for attackArea
            switch (direction) {
                case "up":
                    worldY -= attackArea.height;
                    break;
                case "down":
                    worldY += gp.tileSize;
                    break;
                case "left":
                    worldX -= attackArea.width;
                    break;
                case "right":
                    worldX += gp.tileSize;
                    break;
            }

            // attackArea becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            // Check monster collision with the updated worldX, worldY, and solidArea
            isAttackingForCollision = true;
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            System.out.println("Attacking - Monster Index: " + monsterIndex); // debug statement please remove when fix
            isAttackingForCollision = false;

            // After checking collision, restore the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if (spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            if (gp.obj[i] != null) {
                // Check if the object is actually an Item before trying to pick it up
                if (gp.obj[i] instanceof Item) {
                    pickUpObject((Item)gp.obj[i]);
                    gp.obj[i] = null;
                } else {
                    // If it's not an Item, try to interact with it
                    gp.obj[i].interact(this);
                }
            }
        } else {
            gp.ui.addMessage("There is nothing to pick up!");
        }
    }

    public void pickUpObject(Item item) {
        if (item.isStackable) {
            // Try to add to existing stack
            for (int j = 0; j < inventory.size(); j++) {
                if (inventory.get(j) != null && inventory.get(j).name.equals(item.name)) {
                    inventory.get(j).quantity += item.quantity;
                    gp.playSE(1);
                    return;
                }
            }
        }
        // Try to add to empty slot
        for (int j = 0; j < inventory.size(); j++) {
            if (inventory.get(j) == null) {
                inventory.set(j, item);
                gp.playSE(1);
                return;
            }
        }
        gp.ui.addMessage("Your inventory is full!");
    }

    public void contactMonster(int damage) {
        if (currentHealth > 0 && !invincible) {
            currentHealth -= damage;

            if (currentHealth < 0) {
                currentHealth = 0;
            }

            invincible = true;
            invincibilityTimer = invincibilityDuration;
            System.out.println("Health: " + currentHealth + ", Invincible: " + invincible); // debug statement remove if
                                                                                            // issue fixed please.
        }
    }

    public void damageMonster(int i) {
        if (i != 999) {
            if (gp.monster[i] instanceof MON_Island_Native) {
                MON_Island_Native monster = (MON_Island_Native) gp.monster[i];

                if (!monster.invincible) {
                    monster.life -= 1;
                    monster.invincible = true;
                    monster.invincibilityTimer = monster.invincibilityDuration;
                    monster.reactToDamage(); // Monster tries to flee from the player

                    System.out.println("Monster " + i + " hit life remaining: " + monster.life); // debug remove when
                                                                                                 // fix

                    if (monster.life <= 0) {
                        monster.dying = true;
                        monster.dyingCounter = 0;
                    }
                }
            }
        }
    }

    public boolean isInvincible() {
        return invincible;
    }

    // getter setter for hunger and health bar
    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHunger() {
        return currentHunger;
    }

    public int getMaxHunger() {
        return maxHunger;
    }

    public boolean isAttackingForCollision() {
        return isAttackingForCollision;
    }

    // getter & setters
    public int getLevel() {
        return level;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getExp() {
        return exp;
    }

    public int getExpToNextLevel() {
        return expToNextLevel;
    }

    public int getCoin() {
        return coin;
    }

    public Entity getCurrentWeapon() {
        return currentWeapon;
    }

    public Entity getCurrentShield() {
        return currentShield;
    }

    public void setCurrentHealth(int health) {
        this.currentHealth = health;
        if (this.currentHealth > maxHealth) {
            this.currentHealth = maxHealth;
        } else if (this.currentHealth < 0) {
            this.currentHealth = 0;
        }
    }
}
