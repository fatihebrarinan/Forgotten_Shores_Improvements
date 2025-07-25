package player;

import environment.Lighting;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

import entity.Entity;
import entity.WorldObject;
import entity.Attackable;
import main.GamePanel;
import main.Inventory;
import main.KeyHandler;
import object.Harvestable;
import object.Interactable;
import object.Item;
import object.OBJ_AXE;
import object.OBJ_CAMPFIRE;
import object.OBJ_KEY;

public class Player extends Entity {

    // dialogue cooldown timer to combat with dialogue state being re-triggered
    // again
    public int dialogueCooldown = 0;
    public final int cooldownDuration = 120;

    // private int damageCooldown = 0;
    // private final int damageCooldownDuration = 30;

    public boolean attacking = false;
    private boolean isAttackingForCollision = false; // New flag
    private boolean hasDamagedMonster = false;

    // to track attack frames & duration of each frame
    private int attackFrameCount = 0;
    private final int attackDuration = 25;

    private int harvestCooldown = 0;
    private final int harvestCooldownDuration = 30; // 0.5 seconds cooldown

    private int maxHealth;
    protected int currentHealth;

    private int maxHunger;
    private int currentHunger;

    private int maxThirst;
    private int currentThirst;

    private boolean isPoisoned;
    private int poisonCounter = 0;
    private int poisonTurn = 0;
    private int poisonInterval = 70;

    private int hungerDecreaseCounter = 0;
    private final int hungerDecreaseInterval = 120; // 60 seconds or 1 minute for now further to be maybe changed

    private int thirstDecreaseCounter = 0;
    private final int thirstDecreaseInterval = 100; // 60 seconds or 1 minute for now further to be maybe changed

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

    public boolean lightUpdated = false;
    public boolean hasBoat = false;

    private int defense;
    private int attack;
    public boolean isLoadGame;
    public boolean canSleep = false; // An indicator for player to determine if he/she can sleep -- if it is night,
                                     // player will be able to sleep

    // new inventory
    public Inventory inventory = new Inventory(gp);
    public boolean haveKey;

    public Player(GamePanel aGP, KeyHandler aKeyHandler, boolean isLoadGame) {
        super(aGP);
        this.keyHandler = aKeyHandler;
        this.isLoadGame = isLoadGame;
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

        // System.out.println("Initial Health: " + currentHealth);
        getPlayerImage();
        scaleImages(scale);

        this.isPoisoned = false;

        // getPlayerAttackImage(); when sprites are ready remove the COMMENTS!
    }

    public void setDefaultValues() {
        Lighting.currentDay = 1;
        Lighting.dayState = 0;
        Lighting.filterAlpha = 0f;
        Lighting.dayCounter = 0;
        worldX = gp.tileSize * 23; // initial y
        worldY = gp.tileSize * 21; // initial x

        if (!isLoadGame) {
            level = 1;
            haveKey = false;
            strength = 1; // the more strength => more damage
            dexterity = 1; // the more dexterity => less damage taken
            exp = 0;
            expToNextLevel = 5;
            coin = 0;
            this.speed = 6; // initial movement speed
            this.direction = "down"; // initial direction where the player looks

            maxHealth = 100; // maximum health a player can have
            currentHealth = maxHealth; // initial health equals to max health ( 100 )

            maxHunger = 100; // maximum hunger a player can have
            currentHunger = maxHunger; // initial hunger equalts to max hunger ( 100 )

            maxThirst = 100; // maximum thirst a player can have
            currentThirst = maxThirst; // initial thirst equalts to max thirst ( 100 )
        }
        invincible = false;
        invincibilityTimer = 0;

    }

    /*
     * This method is used to set the default position of the player.
     */
    public void setDefaultPosition() {
        worldX = gp.tileSize * 23; // initial y
        worldY = gp.tileSize * 21; // initial x
        direction = "down";
    }

    /*
     * This method is used to restore the player's life.
     */
    public void restoreLife() {
        currentHealth = maxHealth;
        invincible = false;
    }

    /*
     * This method is used to restart the player.
     */
    public void restartPlayer() {
        setDefaultPosition();
        restoreLife();
        inventory = new Inventory(gp);
    }

    /*
     * This method is used to get the player's images.
     */
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

        scaleImages(scale);
    }

    /*
     * This method is used to get the sleeping image of the player. No matter which
     * direction the player looks, it turns into a shelter image to visualize sleep.
     */
    public void getSleepingImage() {
        BufferedImage shelterImage = null;
        try {
            shelterImage = ImageIO.read(getClass().getResourceAsStream("/res/Objects/shelter/shelter.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.up1 = shelterImage;
        this.up2 = shelterImage;
        this.up3 = shelterImage;
        this.up4 = shelterImage;
        this.down1 = shelterImage;
        this.down2 = shelterImage;
        this.down3 = shelterImage;
        this.down4 = shelterImage;
        this.left1 = shelterImage;
        this.left2 = shelterImage;
        this.left3 = shelterImage;
        this.left4 = shelterImage;
        this.right1 = shelterImage;
        this.right2 = shelterImage;
        this.right3 = shelterImage;
        this.right4 = shelterImage;

        this.idle1 = shelterImage;
        this.idle2 = shelterImage;
        this.idle3 = shelterImage;
        this.idle4 = shelterImage;

        scaleImages(scale);
    }

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

        if (harvestCooldown > 0) {
            harvestCooldown--;
        }

        // Update attack value based on equipped weapon
        Item equippedItem = inventory.getItem(inventory.getSelectedSlot());
        /*
         * if (equippedItem != null && (equippedItem.name.equals("Axe") ||
         * equippedItem.name.equals("Spear"))) {
         * this.attack = equippedItem.attackValue;
         * } else {
         * this.attack = 0; // No weapon equipped means no attack power
         * }
         */

        // decreasing hunger over time
        hungerDecreaseCounter++;

        if (hungerDecreaseCounter >= hungerDecreaseInterval) {
            if (currentHunger > 0) {
                currentHunger--;
            } else {
                currentHealth--;
            }

            hungerDecreaseCounter = 0;
        }

        // decreasing thirst over time
        thirstDecreaseCounter++;

        if (thirstDecreaseCounter >= thirstDecreaseInterval) {
            if (currentThirst > 0) {
                currentThirst--;
            } else {
                currentHealth--;
            }

            thirstDecreaseCounter = 0;
        }

        if (isPoisoned) {
            poisonCounter++;

            if (poisonCounter >= poisonInterval) {
                if (poisonCounter > 0) {
                    currentHealth -= 3;
                }
                poisonCounter = 0;
                poisonTurn++;

                if (poisonTurn >= 2) {
                    isPoisoned = false;
                }
            }
        }

        // Check object collision
        int objectIndex = gp.cChecker.checkObject(this, true);
        if (objectIndex != 999) {
            WorldObject object = gp.obj[objectIndex];
            gp.ui.showTooltip = true;

            // If f is pressed
            if (keyHandler.fPressed) {
                keyHandler.fPressed = false; // Consume the key press

                if (object instanceof Interactable) {
                    ((Interactable) object).interact(object, this);
                } else if (object instanceof Item) {
                    Item item = (Item) object;
                    if (pickUpObject(item)) {
                        gp.obj[objectIndex] = null;
                    }
                }
            }
        } else {
            // Check NPC collision only if no object is being interacted with
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            if (npcIndex != 999) {
                gp.ui.showTooltip = true;
                if (keyHandler.fPressed) {
                    keyHandler.fPressed = false;
                    if (gp.npc[npcIndex] instanceof Interactable) {
                        ((Interactable) gp.npc[npcIndex]).interact(gp.npc[npcIndex], this);
                    }
                }
            } else {
                gp.ui.showTooltip = false;
            }
        }

        if (keyHandler.leftClicked) {
            if (objectIndex != 999 && gp.obj[objectIndex] instanceof Harvestable) {
                if (equippedItem != null && equippedItem.name.equals("Axe")) {
                    if (harvestCooldown == 0) {
                        ((Harvestable) gp.obj[objectIndex]).harvest();
                        harvestCooldown = harvestCooldownDuration;
                    }
                } else {
                    gp.ui.addMessage("You need an axe to harvest trees!");
                }
            }
            // only start new attack if not already attacking
            else if (!attacking) {
                attacking = true;
                keyHandler.leftClicked = false;
            }
        }

        if (keyHandler.gPressed) {
            dropSelectedItem();
            keyHandler.gPressed = false;
        }

        if (keyHandler.ePressed) {
            consumeSelectedItem();
            keyHandler.ePressed = false;
        }

        if (gp.gameState == gp.dialogueState && gp.keyH.enterPressed) {
            if (gp.ui.currentDialoguePage < gp.ui.dialoguePages.size() - 1) {
                gp.ui.currentDialoguePage++;
                gp.ui.dialogueChanged = true;
            } else {
                gp.gameState = gp.playState;
                gp.ui.currentDialoguePage = 0;
                gp.ui.dialoguePages.clear();
                dialogueCooldown = cooldownDuration;
            }
            gp.keyH.enterPressed = false;
        }

        if (attacking) {
            attacking();
            attackFrameCount++;
            if (attackFrameCount >= attackDuration) {
                attacking = false;
                attackFrameCount = 0;
                hasDamagedMonster = false;
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
                gp.cChecker.checkEntity(this, gp.npc);

                // Check monster collision
                // int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);

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
        if (currentHealth <= 0 || hasBoat) {
            gp.gameState = gp.gameOverState;
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

            if (monsterIndex != 999 && !hasDamagedMonster) {
                if (gp.monster[monsterIndex] instanceof Attackable) {
                    ((Attackable) gp.monster[monsterIndex]).attack(this.attack);
                    hasDamagedMonster = true;
                }
            }

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

    public void contactMonster(int damage) {
        if (currentHealth > 0 && !invincible) {
            int actualDamage = damage - this.defense;
            if (actualDamage < 0) {
                actualDamage = 0;
            }
            currentHealth -= actualDamage;
            if (currentHealth < 0) {

                currentHealth = 0;
            }
            invincible = true;
            invincibilityTimer = invincibilityDuration;
            System.out.println("Health: " + currentHealth + ", Invincible: " + invincible); // debug statement remove if
                                                                                            // issue fixed please. }
        }
    }

    /*
     * This method is used to pick up the item near the player.
     */
    private boolean pickUpObject(Item item) {
        if (item.isPickable) {
            boolean addedToStack = false;
            // Try to add to existing stack
            if (item.isStackable) {
                for (int j = 0; j < inventory.size(); j++) {
                    if (inventory.get(j) != null && inventory.get(j).name.equals(item.name)) {
                        inventory.get(j).quantity += item.quantity;
                        addedToStack = true;
                        break;
                    }
                }
            }

            // If not stacked, add to an empty slot
            if (!addedToStack) {
                for (int j = 0; j < inventory.size(); j++) {
                    if (inventory.get(j) == null) {
                        inventory.set(j, item);
                        addedToStack = true;
                        break;
                    }
                }
            }

            if (addedToStack) {
                if (item instanceof OBJ_KEY) {
                    haveKey = true;
                }
                gp.ui.addMessage("Picked up " + item.name + "!");
                return true;
            } else {
                gp.ui.addMessage("Your inventory is full!");
                return false;
            }
        } else {
            gp.ui.addMessage("You can't pick up this item!");
            return false;
        }
    }

    /*
     * This method is used to harvest the item near the player.
     */
    public void harvestItem(int i) {
        if (gp.obj[i] instanceof Harvestable && getCurrentItem("Axe") instanceof OBJ_AXE) {
            Harvestable item = (Harvestable) gp.obj[i];
            item.harvest();
        }
    }

    /*
     * This method is used to use/consume the selected item in the inventory
     */
    private void consumeSelectedItem() {
        int selectedSlot = inventory.getSelectedSlot();
        Item selectedItem = inventory.getItem(selectedSlot);

        if (selectedItem == null) {
            gp.ui.addMessage("No item selected.");
            return;
        }

        switch (selectedItem.itemType) {
            case CONSUMABLE:
                selectedItem.use(this);
                if (selectedItem.quantity <= 0) {
                    inventory.setItem(selectedSlot, null);
                }
                break;
            case OTHER:
                gp.ui.addMessage("Item is not consumable.");
                break;
            case TOOL:
                gp.ui.addMessage("Item is not consumable.");
                break;
        }
    }

    /*
     * This method is used to drop the selected item in the inventory
     */
    public void dropSelectedItem() {
        int selectedSlot = inventory.getSelectedSlot();
        Item selectedItem = inventory.getItem(selectedSlot);
        if (selectedItem instanceof OBJ_KEY) {
            this.haveKey = false;
        }

        if (selectedItem == null) {
            gp.ui.addMessage("No item selected.");
            return;
        }

        int dropX = worldX;
        int dropY = worldY;

        switch (direction) {
            case "up":
                dropY -= gp.tileSize;
                break;
            case "down":
                dropY += gp.tileSize;
                break;
            case "left":
                dropX -= gp.tileSize;
                break;
            case "right":
                dropX += gp.tileSize;
                break;
        }
        selectedItem.worldX = dropX;
        selectedItem.worldY = dropY;
        Item tempItem = selectedItem.clone();
        tempItem.worldX = dropX;
        tempItem.worldY = dropY;

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) {
                tempItem.quantity = 1;
                gp.obj[i] = tempItem;

                if (selectedItem.quantity > 1) {
                    selectedItem.quantity--;
                } else {
                    inventory.setItem(selectedSlot, null);
                    gp.removeObject(selectedItem);
                }

                gp.ui.addMessage("Dropped " + tempItem.name);

                return;
            }
        }
        gp.ui.addMessage("No space to drop item!");
    }

    /*
     * This method is used to check if the player is near a fire.
     */
    public boolean isNearFire() {

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null && gp.obj[i] instanceof OBJ_CAMPFIRE) {
                int objLeft = gp.obj[i].worldX;
                int objRight = gp.obj[i].worldX + gp.tileSize;
                int objTop = gp.obj[i].worldY;
                int objBottom = gp.obj[i].worldY + gp.tileSize;

                int playerLeft = worldX;
                int playerRight = worldX + gp.tileSize;
                int playerTop = worldY;
                int playerBottom = worldY + gp.tileSize;

                // Check simple distance
                if (playerRight > objLeft - gp.tileSize && playerLeft < objRight + gp.tileSize &&
                        playerBottom > objTop - gp.tileSize && playerTop < objBottom + gp.tileSize) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * This method is used to check if the player is near a water source.
     */
    public boolean isNearWater() {
        int playerCol = worldX / gp.tileSize;
        int playerRow = worldY / gp.tileSize;

        int[][] mapTileNum = gp.tileM.getMapTileNum();

        int upTile = mapTileNum[playerCol][playerRow - 1];
        int downTile = mapTileNum[playerCol][playerRow + 1];
        int leftTile = mapTileNum[playerCol - 1][playerRow];
        int rightTile = mapTileNum[playerCol + 1][playerRow];

        if (upTile == 1 || downTile == 1 || leftTile == 1 || rightTile == 1) {
            gp.ui.addMessage("Filled bucket with water ");
            return true;
        }
        return false;
    }

    // Getter and setters for player's attributes

    public BufferedImage getCurrentImage() {
        return idle1;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxThirst() {
        return maxThirst;
    }

    public int getCurrentThirst() {
        return currentThirst;
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

    public void setCurrentHealth(int health) {
        this.currentHealth = health;
        if (this.currentHealth > maxHealth) {
            this.currentHealth = maxHealth;
        } else if (this.currentHealth < 0) {
            this.currentHealth = 0;
        }
    }

    public void setCurrentHunger(int hunger) {
        this.currentHunger = hunger;
        if (this.currentHunger > maxHunger) {
            this.currentHunger = maxHunger;
        } else if (this.currentHunger < 0) {
            this.currentHunger = 0;
        }
    }

    public void setCurrentThirst(int thirst) {
        this.currentThirst = thirst;
        if (this.currentThirst > maxThirst) {
            this.currentThirst = maxThirst;
        } else if (this.currentThirst < 0) {
            this.currentThirst = 0;
        }
    }

    public void setPoisonStatus() {
        this.poisonTurn = 0;
        this.poisonCounter = 0;
        this.isPoisoned = true;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setExpToNextLevel(int expToNextLevel) {
        this.expToNextLevel = expToNextLevel;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setMaxHunger(int maxHunger) {
        this.maxHunger = maxHunger;
    }

    public void setMaxThirst(int maxThirst) {
        this.maxThirst = maxThirst;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Item getCurrentItem(String itemName) {
        // Check all inventory slots for the specified item
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.getItem(i);
            if (item != null && item.name.equals(itemName)) {
                return item;
            }
        }
        return null;
    }

}
