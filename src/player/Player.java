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
import object.Breakable;
import object.Consumable;
import object.Interactable;
import object.Item;
import object.Pickable;

import object.OBJ_CAMPFIRE;

public class Player extends Entity {
    public boolean attacking = false;

    // to track attack frames & duration of each frame
    private int attackFrameCount = 0;
    private final int attackDuration = 10;
    private int defaultAttackDamage = 20;
    private int defaultAttackRange = gp.tileSize * (3);

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

    public boolean lightUpdated = false;

    public boolean isLoadGame;
    public boolean canSleep = false; // An indicator for player to determine if he/she can sleep -- if it is night,
                                     // player will be able to sleep

    public Inventory inventory = new Inventory(gp);

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
        Lighting.currentDayState = 0;
        Lighting.filterAlpha = 0f;
        Lighting.dayCounter = 0;
        worldX = gp.tileSize * 23; // initial y
        worldY = gp.tileSize * 21; // initial x

        if (!isLoadGame) {
            this.speed = 6; // initial movement speed
            this.direction = "down"; // initial direction where the player looks

            maxHealth = 100; // maximum health a player can have
            currentHealth = maxHealth; // initial health equals to max health ( 100 )

            maxHunger = 100; // maximum hunger a player can have
            currentHunger = maxHunger; // initial hunger equalts to max hunger ( 100 )

            maxThirst = 100; // maximum thirst a player can have
            currentThirst = maxThirst; // initial thirst equalts to max thirst ( 100 )
        }
    }

    public void setDefaultPosition() {
        worldX = gp.tileSize * 23; // initial y
        worldY = gp.tileSize * 21; // initial x
        direction = "down";
    }

    public void restoreLife() {
        currentHealth = maxHealth;
    }

    public void restartPlayer() {
        setDefaultPosition();
        restoreLife();
        inventory = new Inventory(gp);
    }

    public void getPlayerImage() {
        try {
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

        if (harvestCooldown > 0) {
            harvestCooldown--;
        }

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
                } else if (object instanceof Pickable) {
                    Pickable pickable = (Pickable) object;
                    if (pickable.pickUp(this)) {
                        gp.obj[objectIndex] = null;
                    }
                }
            }
        }

        if (keyHandler.leftClicked) {
            keyHandler.leftClicked = false;// Consume key press

            leftClicked();
        }

        if (keyHandler.gPressed) {
            dropSelectedItem();
            keyHandler.gPressed = false;
        }

        if (keyHandler.ePressed) {
            consumeSelectedItem();
            keyHandler.ePressed = false;
        }

        if (attacking) {
            handleAttackAnimation();
        } else {
            handleMovement();
        }

        if (currentHealth <= 0) {
            gp.gameState = gp.gameOverState;
        }
    }

    private void handleAttackAnimation() {
        playAttackAnimation();
        attackFrameCount++;
        if (attackFrameCount >= attackDuration) {
            attacking = false;
            attackFrameCount = 0;
        }
    }

    private void handleMovement() {
        boolean isMoving = (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed
                || keyHandler.rightPressed);

        if (isMoving || keyHandler.leftClicked) {
            if (!wasMoving) {
                spriteNum = 1;
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

    public void leftClicked() {
        // Update attack value based on equipped weapon
        Item equippedItem = inventory.getItem(inventory.getSelectedSlot());

        if (!attacking) {
            attacking = true;
        }

        int playerCenterX = worldX + solidArea.x + solidArea.width / 2;
        int playerCenterY = worldY + solidArea.y + solidArea.height / 2;

        boolean attacked = attackEntity(playerCenterX, playerCenterY);

        if (!attacked) {
            breakObject(playerCenterX, playerCenterY, equippedItem);
        }
    }

    private boolean attackEntity(int playerCenterX, int playerCenterY) {
        for (int i = 0; i < gp.monster.length; i++) {
            if (gp.monster[i] != null && gp.monster[i] instanceof Attackable) {
                int monsterCenterX = gp.monster[i].worldX + gp.monster[i].solidArea.x
                        + gp.monster[i].solidArea.width / 2;
                int monsterCenterY = gp.monster[i].worldY + gp.monster[i].solidArea.y
                        + gp.monster[i].solidArea.height / 2;
                double distance = Math.sqrt(
                        Math.pow(playerCenterX - monsterCenterX, 2) + Math.pow(playerCenterY - monsterCenterY, 2));

                if (distance <= defaultAttackRange) {
                    ((Attackable) gp.monster[i]).takeDamage(defaultAttackDamage);
                    return true;
                }
            }
        }
        return false;
    }

    private void breakObject(int playerCenterX, int playerCenterY, Item equippedItem) {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null && gp.obj[i] instanceof Breakable) {
                int objCenterX = gp.obj[i].worldX + gp.tileSize / 2;
                int objCenterY = gp.obj[i].worldY + gp.tileSize / 2;
                double distance = Math.sqrt(
                        Math.pow(playerCenterX - objCenterX, 2) + Math.pow(playerCenterY - objCenterY, 2));

                if (distance <= defaultAttackRange) {
                    Breakable breakableObj = (Breakable) gp.obj[i];
                    if (equippedItem != null && equippedItem.name.equals(breakableObj.getRequiredToolName())) {
                        if (harvestCooldown == 0) {
                            breakableObj.breakObject();
                            harvestCooldown = harvestCooldownDuration;
                            return;
                        }
                    } else {
                        gp.ui.addMessage(
                                "You need an " + breakableObj.getRequiredToolName() + " to break this!");
                        return;
                    }
                }
            }
        }
    }

    public void playAttackAnimation() {
        spriteCounter++;

        if (spriteCounter <= 5) // from 0th frame to 5th frame second sprite will be shown.
        {
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) // from 6th frame to 25th frame second sprite will be shown.
        {
            spriteNum = 2;
        }
        if (spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void takeDamage(int damage) {
        // TODO
    }

    public boolean pickUpItem(Item item) {
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
            gp.ui.addMessage("Picked up " + item.name + "!");
            return true;
        } else {
            gp.ui.addMessage("Your inventory is full!");
            return false;
        }
    }

    public void harvestItem(int i) {
        if (gp.obj[i] instanceof Breakable) {
            Breakable item = (Breakable) gp.obj[i];
            Item tool = getCurrentItem(item.getRequiredToolName());
            if (tool != null && tool.name.equals(item.getRequiredToolName())) {
                item.breakObject();
            }
        }
    }

    private void consumeSelectedItem() {
        int selectedSlot = inventory.getSelectedSlot();
        Item selectedItem = inventory.getItem(selectedSlot);

        if (selectedItem == null) {
            gp.ui.addMessage("No item selected.");
            return;
        }

        if (selectedItem instanceof Consumable) {
            boolean success = ((Consumable) selectedItem).consume(this);
            if (success) {
                selectedItem.quantity--;
                if (selectedItem.quantity <= 0) {
                    inventory.setItem(selectedSlot, null);
                    gp.removeObject(selectedItem);
                }
            }
        } else {
            gp.ui.addMessage("You cannot eat or use this!");
        }
    }

    public void dropSelectedItem() {
        int selectedSlot = inventory.getSelectedSlot();
        Item selectedItem = inventory.getItem(selectedSlot);
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

    // --- Getter and setters ---

    public void setCurrentHealth(int health) {
        this.currentHealth = health;
        if (this.currentHealth > maxHealth) {
            this.currentHealth = maxHealth;
        } else if (this.currentHealth < 0) {
            this.currentHealth = 0;
        }
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setCurrentHunger(int hunger) {
        this.currentHunger = hunger;
        if (this.currentHunger > maxHunger) {
            this.currentHunger = maxHunger;
        } else if (this.currentHunger < 0) {
            this.currentHunger = 0;
        }
    }

    public void setMaxHunger(int maxHunger) {
        this.maxHunger = maxHunger;
    }

    public void setCurrentThirst(int thirst) {
        this.currentThirst = thirst;
        if (this.currentThirst > maxThirst) {
            this.currentThirst = maxThirst;
        } else if (this.currentThirst < 0) {
            this.currentThirst = 0;
        }
    }

    public void setMaxThirst(int maxThirst) {
        this.maxThirst = maxThirst;
    }

    public void setPoisonStatus() {
        this.poisonTurn = 0;
        this.poisonCounter = 0;
        this.isPoisoned = true;
    }

    public BufferedImage getCurrentImage() {
        return idle1;
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

    public Item getLitTorch() {
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.getItem(i);
            if (item != null && item instanceof object.OBJ_TORCH) {
                if (((object.OBJ_TORCH) item).isLit()) {
                    return item;
                }
            }
        }
        return null;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentThirst() {
        return currentThirst;
    }

    public int getMaxThirst() {
        return maxThirst;
    }

    public int getCurrentHunger() {
        return currentHunger;
    }

    public int getMaxHunger() {
        return maxHunger;
    }
}
