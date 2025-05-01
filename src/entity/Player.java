package entity;

import environment.Lighting;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.Inventory;
import main.KeyHandler;
import monster.MON_Island_Native;
import monster.MON_Pig;
import object.Item;
import object.OBJ_APPLE_TREE;
import object.OBJ_CAMPFIRE;
import object.OBJ_RAW_MEAT;
import object.OBJ_SHELTER;
import object.OBJ_SHIELD_WOOD;
import object.OBJ_SWORD_NORMAL;
import object.OBJ_WATER_BUCKET;
import tile_interactive.IT_DryTree;
import tile_interactive.InteractiveTile;

public class Player extends Entity {

    // dialogue cooldown timer to combat with dialogue state being re-triggered
    // again
    private int dialogueCooldown = 0;
    private final int cooldownDuration = 120;

    // private int damageCooldown = 0;
    // private final int damageCooldownDuration = 30;

    public boolean attacking = false;
    private boolean isAttackingForCollision = false; // New flag
    private boolean hasDamagedTile = false; // New flag to prevent multiple damages per attack

    // to track attack frames & duration of each frame
    private int attackFrameCount = 0;
    private final int attackDuration = 25;

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
    private final int hungerDecreaseInterval = 80; // 60 seconds or 1 minute for now further to be maybe changed

    private int thirstDecreaseCounter = 0;
    private final int thirstDecreaseInterval = 50; // 60 seconds or 1 minute for now further to be maybe changed

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
    private Entity currentLight;

    public boolean lightUpdated = false;

    private int defense;
    private int attack;

    public boolean canSleep = false; // An indicator for player to determine if he/she can sleep -- if it is night,
                                     // player will be able to sleep

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
        scaleImages(scale);

        this.isPoisoned = false;

        // getPlayerAttackImage(); when sprites are ready remove the COMMENTS!
    }

    public void setDefaultValues() {
        Lighting.currentDay = 1;
        worldX = gp.tileSize * 23; // initial y
        worldY = gp.tileSize * 21; // initial x
        this.speed = 4; // initial movement speed
        this.direction = "down"; // initial direction where the player looks

        maxHealth = 100; // maximum health a player can have
        currentHealth = maxHealth; // initial health equals to max health ( 100 )

        maxHunger = 100; // maximum hunger a player can have
        currentHunger = maxHunger; // initial hunger equalts to max hunger ( 100 )

        maxThirst = 100; // maximum thirst a player can have
        currentThirst = maxThirst; // initial thirst equalts to max thirst ( 100 )

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

    public void setDefaultPosition() {
        worldX = gp.tileSize * 23; // initial y
        worldY = gp.tileSize * 21; // initial x
        direction = "down";
    }

    public void restoreLife() {
        currentHealth = maxHealth;
        invincible = false;
    }

    public void restartPlayer() {
        setDefaultPosition();
        restoreLife();
        inventory = new Inventory();
    }

    public int getAttack() {
        if (currentWeapon != null) {

            return strength * currentWeapon.attackValue;
        } else {
            return strength * 1;
        }

    }

    public int getDefense() {
        if (currentShield != null) {
            return dexterity * currentShield.defenseValue;
        } else {
            return dexterity * 0;
        }
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

    // no matter which direction does the player look, it turns into a shelter image
    // to visualize sleep.
    public void getSleepingImage() {
        /*
         * there will be a better image for shelter in the future
         */

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
    }

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

        if (keyHandler.leftClicked) {
            // only start new attack if not already attacking
            if (!attacking) {
                attacking = true;
                keyHandler.leftClicked = false;
            }
        }

        if (keyHandler.ePressed) {
            Item selectedItem = inventory.getItem(inventory.getSelectedSlot());

            if (selectedItem instanceof OBJ_WATER_BUCKET) {
                OBJ_WATER_BUCKET bucket = (OBJ_WATER_BUCKET) selectedItem;
                bucket.consume(this); // Drink from the bucket
            } else if (selectedItem instanceof OBJ_SHELTER) {
                if (this.canSleep) {
                    gp.gameState = gp.sleepState;
                    gp.player.setCurrentHealth(gp.player.getCurrentHealth() + 10); // health increases.
                    gp.player.getSleepingImage();
                    int selectedSlot = inventory.getSelectedSlot();
                    Item shelter = inventory.getItem(selectedSlot);
                    inventory.setItem(selectedSlot, null);
                    gp.ui.addMessage("Used up " + shelter.name);
                } else {
                    gp.ui.addMessage("You cannot sleep until night...");
                }
            } else {
                useSelectedItem(); // Normal use for other items
            }

            keyHandler.ePressed = false;
        }

        if (keyHandler.gPressed) {
            dropSelectedItem();
            keyHandler.gPressed = false;
        }

        if (keyHandler.qPressed) {
            Item selectedItem = inventory.getItem(inventory.getSelectedSlot());
            if (selectedItem instanceof OBJ_WATER_BUCKET) {
                OBJ_WATER_BUCKET bucket = (OBJ_WATER_BUCKET) selectedItem;

                if (isNearWater() && !isNearFire()) {
                    bucket.fill(true);
                } else if (isNearFire()) {
                    bucket.purify(true);
                } else {
                    System.out.println("You are not near water or fire!");
                }
            } else if (selectedItem instanceof OBJ_RAW_MEAT) {
                OBJ_RAW_MEAT meat = (OBJ_RAW_MEAT) selectedItem;

                if (isNearFire()) {
                    meat.cook();
                }

                else {
                    System.out.println("You are not near water or fire!");
                }
            } else {
                System.out.println("This item can't be used with Q key!");
            }

            keyHandler.qPressed = false; // always reset after pressing
        }

        // Check object collision
        int objectIndex = gp.cChecker.checkObject(this, true);

        if (objectIndex != 999) {
            gp.ui.showTooltip = true;

            if (keyHandler.fPressed) {
                if (gp.obj[objectIndex] instanceof OBJ_SHELTER) {

                }
                pickUpObject(objectIndex);
                keyHandler.fPressed = false;
            }

        } else {
            gp.ui.showTooltip = false;
        }

        int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
        if (iTileIndex != 999 && gp.iTile[iTileIndex] != null && keyHandler.fPressed) {
            gp.iTile[iTileIndex].interact(this, iTileIndex);
            keyHandler.fPressed = false;
        }

        if (attacking) {
            attacking();
            attackFrameCount++;
            if (attackFrameCount >= attackDuration) {
                attacking = false;
                attackFrameCount = 0;
                hasDamagedTile = false;
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
                // int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);

                // Check interactive tile collision
                gp.cChecker.checkEntity(this, gp.obj);

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
        if (currentHealth <= 0) {
            gp.gameState = gp.gameOverState;
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

            // Only damage tile once per attack
            if (!hasDamagedTile) {
                int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
                System.out.println("Checking tile collision: iTileIndex = " + iTileIndex);
                damageInteractiveTile(iTileIndex);

                if (iTileIndex != 999) {
                    hasDamagedTile = true;
                }
            }

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
            hasDamagedTile = false;
        }
    }

    public void pickUpObject(int i) {
        // Debugger for error: System.out.println("Picking up: " + gp.obj[i].name);
        if (i != 999) {
            if (gp.obj[i] != null) {
                // Check if the object is actually an Item before trying to pick it up
                if (gp.obj[i] instanceof Item) {
                    pickUpObject((Item) gp.obj[i], i);
                } else if (gp.obj[i] instanceof OBJ_APPLE_TREE) {
                    ((OBJ_APPLE_TREE) gp.obj[i]).interact(this, i);
                } else {
                    // If it's not an Item, try to interact with it
                    gp.obj[i].interact(this, i);
                }
            }
        } else {
            gp.ui.addMessage("There is nothing to pick up!");
        }
        // Debugger for error: System.out.println("After pickup: " + gp.obj[i]);
    }

    public void pickUpObject(Item item, int i) {

        if (item.isStackable) {
            // Try to add to existing stack
            for (int j = 0; j < inventory.size(); j++) {
                if (inventory.get(j) != null && inventory.get(j).name.equals(item.name)) {
                    inventory.get(j).quantity += item.quantity;
                    if (!(gp.obj[i] instanceof OBJ_APPLE_TREE))
                        gp.obj[i] = null;
                    return;
                }
            }
        }
        // Try to add to empty slot
        for (int j = 0; j < inventory.size(); j++) {
            if (inventory.get(j) == null) {
                inventory.set(j, item);
                if (!(gp.obj[i] instanceof OBJ_APPLE_TREE))
                    gp.obj[i] = null;
                return;
            }
        }
        gp.ui.addMessage("Your inventory is full!");
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

    public void damageMonster(int i) {
        if (i != 999) {
            if (gp.monster[i] instanceof MON_Island_Native) {
                MON_Island_Native monster = (MON_Island_Native) gp.monster[i];

                if (!monster.invincible) {
                    monster.life -= this.attack;
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

            else if (gp.monster[i] instanceof MON_Pig) {
                MON_Pig pig = (MON_Pig) gp.monster[i];
                if (!pig.invincible) {
                    pig.life -= this.attack;
                    pig.invincible = true;
                    pig.invincibilityTimer = pig.invincibilityDuration;
                    pig.reactToDamage();
                    if (pig.life <= 0) {
                        pig.dying = true;
                        pig.dyingCounter = 0;
                    }
                }
            }
        }
    }

    public void damageInteractiveTile(int i) {
        IT_DryTree dryTree = null;
        if (i != 999) {
            if (gp.iTile[i] != null) {
                dryTree = (IT_DryTree) gp.iTile[i];
                if (i != 999 && dryTree.destructible && dryTree.isCorrectItem(this) && !dryTree.invincible) {
                    dryTree.life--;
                    dryTree.invincible = true;
                    dryTree.invincibilityTimer = dryTree.invincibilityDuration;

                    System.out.println("Tree at index " + i + " hit, life remaining: " + dryTree.life);

                    if (dryTree.life <= 0) {
                        dryTree.onDestroy(i);
                        InteractiveTile newTile = dryTree.getDestroyedForm();
                        gp.iTile[i] = newTile;
                    } else {
                        System.out.println("Not died yet, remaining life: " + dryTree.life);
                    }
                }
            }
        }
    }

    private void useSelectedItem() {
        int selectedSlot = inventory.getSelectedSlot();
        Item selectedItem = inventory.getItem(selectedSlot);

        if (selectedItem == null) {
            gp.ui.addMessage("No item selected.");
            return;
        }

        switch (selectedItem.itemType) {
            case WEAPON:
                if (currentWeapon == selectedItem) {
                    currentWeapon = null;
                    gp.ui.addMessage("Unequipped " + selectedItem.name);
                    selectedItem.isEquipped = false;
                } else {
                    currentWeapon = selectedItem;
                    gp.ui.addMessage("Equipped " + selectedItem.name);
                    selectedItem.isEquipped = true;
                }
                attack = getAttack();
                // Debugger: System.out.println("Strength:" + attack);
                break;

            case SHIELD:
                if (currentShield == selectedItem) {
                    currentShield = null;
                    gp.ui.addMessage("Unequipped " + selectedItem.name);
                    selectedItem.isEquipped = false;
                } else {
                    currentShield = selectedItem;
                    gp.ui.addMessage("Equipped " + selectedItem.name);
                    selectedItem.isEquipped = true;
                }
                defense = getDefense();
                break;

            case CONSUMABLE:
                selectedItem.use(this);
                if (selectedItem.quantity <= 0) {

                    if (selectedItem == currentWeapon) {
                        currentWeapon = null;
                        attack = getAttack();
                    }
                    if (selectedItem == currentShield) {
                        currentShield = null;
                        defense = getDefense();
                    }

                    inventory.setItem(selectedSlot, null);
                    gp.ui.addMessage("Used up " + selectedItem.name);
                } else {
                    gp.ui.addMessage("Used " + selectedItem.name);
                }
                break;
            case LIGHTER:
                if (currentLight == selectedItem) {
                    currentLight = null;
                } else {
                    currentLight = selectedItem;
                }
                lightUpdated = true;
                break;
            case OTHER:
                gp.ui.addMessage("Cannot use this item.");
                break;
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
        tempItem.alive = true;

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) {
                tempItem.quantity = 1;
                tempItem.isEquipped = false;
                gp.obj[i] = tempItem;

                if (selectedItem.quantity > 1) {
                    selectedItem.quantity--;
                } else {
                    if (selectedItem.isEquipped) {
                        unequipDroppedItem(selectedItem);
                    }
                    inventory.setItem(selectedSlot, null);
                    gp.removeObject(selectedItem);
                }

                gp.ui.addMessage("Dropped " + tempItem.name);

                return;
            }
        }
        gp.ui.addMessage("No space to drop item!");
    }

    public void unequipDroppedItem(Item droppedItem) {

        if (droppedItem == null) {
            gp.ui.addMessage("No item selected.");
            return;
        }

        switch (droppedItem.itemType) {
            case WEAPON:
                currentWeapon = null;
                attack = getAttack();
                // Debugger: System.out.println("Strength:" + attack);
                droppedItem.isEquipped = false;
                break;
            case SHIELD:
                currentShield = null;
                droppedItem.isEquipped = false;
                defense = getDefense();
                break;
            default:
                break;
        }
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

    public Entity getCurrentLighting() {
        return currentLight;
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

    public void setCurrentWeapon(Entity weapon) {
        this.currentWeapon = weapon;
        this.attack = getAttack();
    }

    public void setCurrentShield(Entity shield) {
        this.currentShield = shield;
        this.defense = getDefense();
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

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
