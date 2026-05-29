package main;

import environment.Lighting;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import object.Item;
import entity.Entity;
import entity.Pig;
import entity.Mob;
import player.Player;

public class UI {
    public BufferedImage heartImage;
    BufferedImage foodImage;
    BufferedImage thirstImage;

    GamePanel gp;
    Font arial_40;
    Font arial_80B;
    public boolean showTooltip = false;

    // dimensions of the heart and hunger images
    private final int heartImageWidth = 32;
    private final int heartImageHeight = 32;
    private final int hungerImageWidth = 20;
    private final int hungerImageHeight = 20;
    private final int thirstImageWidth = 20;
    private final int thirstImageHeight = 20;

    public Font customFont;
    public Font customFontBold;

    private String currentMessage = "";
    private int messageTimer = 0;
    private final int MESSAGE_DURATION = 120; // 2 seconds at 60 FPS

    int counter = 0;

    private boolean dayIncreased = false;

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        try {
            // custom fonts that we will be using might change in time
            InputStream is = getClass().getResourceAsStream("/res/fonts/Jersey15-Regular.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(30f);

            InputStream isBold = getClass().getResourceAsStream("/res/fonts/Jersey15-Regular.ttf");
            customFontBold = Font.createFont(Font.TRUETYPE_FONT, isBold).deriveFont(60f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            ge.registerFont(customFontBold);

            is.close();
            isBold.close();
        } catch (Exception e) {

            // backup arial font if custom font fails
            e.printStackTrace();
            customFont = new Font("Arial", Font.PLAIN, 40);
            customFontBold = new Font("Arial", Font.BOLD, 80);
        }

        // reading the heart and food images
        try {
            heartImage = ImageIO.read(getClass().getResourceAsStream("/res/gameUI/heart.png"));
            foodImage = ImageIO.read(getClass().getResourceAsStream("/res/gameUI/hunger.png"));
            thirstImage = ImageIO.read(getClass().getResourceAsStream("/res/gameUI/thirst.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    Graphics2D g2;

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(customFont);
        g2.setColor(Color.WHITE);

        if (gp.gameState == gp.playState) {
            if (showTooltip) {
                drawToolTip();
            }

            drawHealthBar(g2);
            drawHungerBar(g2);
            drawThirstBar(g2);

            drawInventory();

            drawFPS();

            if (gp.showChunkBorders) {
                drawChunkCoordinates();
            }

        }

        if (gp.gameState == gp.pauseState) {
        }
        if (gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }

        if (gp.gameState == gp.sleepState) {
            drawSleepScreen();
        }

        if (gp.gameState == gp.craftingState) {
            gp.craftingScreen.draw(g2);
        }

        drawMessage();

        drawDayStateAndCount();
    }

    public void drawDayStateAndCount() {
        String dayStr = gp.lightManager.lighting.getDayStateAndCount("");
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(50f));
        g2.drawString(dayStr, gp.screenWidth - 250, 50);
    }

    private void drawChunkCoordinates() {
        int playerCol = gp.player.worldX / gp.tileSize;
        int playerRow = gp.player.worldY / gp.tileSize;
        int chunkX = Math.floorDiv(playerCol, gp.chunkManager.CHUNK_SIZE);
        int chunkY = Math.floorDiv(playerRow, gp.chunkManager.CHUNK_SIZE);

        String chunkStr = "Chunk: [" + chunkX + ", " + chunkY + "]";
        g2.setColor(Color.WHITE);
        g2.setFont(customFont.deriveFont(40f));
        g2.drawString(chunkStr, gp.screenWidth - 250, 90);
    }

    public void drawToolTip() {
        g2.setFont(customFont.deriveFont(Font.BOLD, 43));
        String text = "Press F to interact";
        int x = gp.screenWidth / 2 - 175;
        int y = gp.screenHeight - 350;

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x - 10, y - 20, 350, 30, 15, 15);
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y + 10);
    }

    public int getXForCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

    private void drawHealthBar(Graphics2D g2) {
        int radius = 20;

        // positions of health bar can be adjusted
        int x = 10;
        int y = gp.screenHeight - 20 - 5 * radius;

        int diameter = 5 * radius;

        double healthPercentage = (double) gp.player.getCurrentHealth() / gp.player.getMaxHealth();

        int outlineThickness = 5;
        g2.setColor(new Color(175, 0, 0));
        g2.fillOval(x - outlineThickness, y - outlineThickness, diameter + 2 * outlineThickness,
                diameter + 2 * outlineThickness);

        g2.setColor(Color.GRAY);
        g2.fillOval(x, y, diameter, diameter);

        int fillHeight = (int) (diameter * healthPercentage);

        Shape circle = new Ellipse2D.Double(x, y, diameter, diameter);

        Shape fillRect = new Rectangle2D.Double(x, y + (diameter - fillHeight), diameter, fillHeight);

        Area filledArea = new Area(circle);
        filledArea.intersect(new Area(fillRect));

        g2.setColor(Color.RED);
        g2.fill(filledArea);

        int heartX = x + diameter / 2 - heartImageWidth / 2;
        int heartY = y + diameter / 2 - heartImageHeight / 2;

        g2.drawImage(heartImage, heartX, heartY, heartImageWidth, heartImageHeight, null);

    }

    private void drawHungerBar(Graphics2D g2) {
        int radius = 20;
        // position of the hunger bar can be adjusted
        int x = 165;
        int y = gp.screenHeight - 20 - 5 * radius;

        int diameter = 5 * radius;

        double hungerPercentage = (double) gp.player.getCurrentHunger() / gp.player.getMaxHunger();

        int outlineThickness = 5;
        g2.setColor(new Color(0, 127, 14));
        g2.fillOval(x - outlineThickness, y - outlineThickness, diameter + 2 * outlineThickness,
                diameter + 2 * outlineThickness);

        g2.setColor(Color.GRAY);
        g2.fillOval(x, y, diameter, diameter);

        int fillHeight = (int) (diameter * hungerPercentage);

        Shape circle = new Ellipse2D.Double(x, y, diameter, diameter);

        Shape fillRect = new Rectangle2D.Double(x, y + (diameter - fillHeight), diameter, fillHeight);

        Area filledArea = new Area(circle);
        filledArea.intersect(new Area(fillRect));

        g2.setColor(Color.GREEN);
        g2.fill(filledArea);

        int foodX = x + diameter / 2 - hungerImageWidth / 2;
        int foodY = y + diameter / 2 - hungerImageHeight / 2;

        g2.drawImage(foodImage, foodX, foodY, hungerImageWidth, hungerImageHeight, null);
    }

    private void drawThirstBar(Graphics2D g2) {
        int radius = 20;

        // position of the hunger bar can be adjusted
        int x = 320;
        int y = gp.screenHeight - 20 - 5 * radius;

        int diameter = 5 * radius;

        double thirstPercentage = (double) gp.player.getCurrentThirst() / gp.player.getMaxThirst();

        int outlineThickness = 5;
        g2.setColor(new Color(0, 200, 255));
        g2.fillOval(x - outlineThickness, y - outlineThickness, diameter + 2 * outlineThickness,
                diameter + 2 * outlineThickness);

        g2.setColor(Color.GRAY);
        g2.fillOval(x, y, diameter, diameter);

        int fillHeight = (int) (diameter * thirstPercentage);

        Shape circle = new Ellipse2D.Double(x, y, diameter, diameter);

        Shape fillRect = new Rectangle2D.Double(x, y + (diameter - fillHeight), diameter, fillHeight);

        Area filledArea = new Area(circle);
        filledArea.intersect(new Area(fillRect));

        g2.setColor(new Color(0, 148, 255));
        g2.fill(filledArea);

        int thirstX = x + diameter / 2 - thirstImageWidth / 2 - 5;
        int thirstY = y + diameter / 2 - thirstImageHeight / 2 - 5;

        g2.drawImage(thirstImage, thirstX, thirstY, thirstImageWidth + 10, thirstImageHeight + 10, null);
    }

    private void drawInventory() {
        int slotSize = 100; // each slot size currently (100x100)
        int spacing = 10; // space between each slot currently (10 pixels)
        int startX = gp.screenWidth / 2 - 2 * slotSize - 50 - 2 * spacing; // startig x position
        int y = gp.screenHeight - 120; // starting y position

        for (int i = 0; i < 5; i++) {
            int x = startX + i * (slotSize + spacing);

            // slot backgrounds is gray
            g2.setColor(Color.GRAY);
            g2.fillRect(x, y, slotSize, slotSize);

            // highlighting the selected slot with yellow
            if (i == gp.player.getInventory().getSelectedSlot()) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(x, y, slotSize, slotSize);
                g2.setStroke(new BasicStroke(1));
            }

            Item item = gp.player.getInventory().getItem(i);
            if (item != null && item.image != null) {
                int imgX = x + 2;
                int imgY = y + 2;
                int imgWidth = slotSize - 4;
                int imgHeight = slotSize - 4;
                g2.drawImage(item.image, imgX, imgY, imgWidth, imgHeight, null);

                if (item.quantity > 1) {
                    g2.setFont(customFont.deriveFont(30f));
                    g2.setColor(Color.WHITE);
                    String quantityText = String.valueOf(item.quantity);
                    int textX = x + slotSize - g2.getFontMetrics().stringWidth(quantityText) - 5;
                    int textY = y + slotSize - 5;
                    g2.drawString(quantityText, textX, textY);
                }
            }
        }
    }

    public void addMessage(String message) {
        currentMessage = message;
        messageTimer = MESSAGE_DURATION;
    }

    private void drawMessage() {
        if (messageTimer > 0) {
            g2.setFont(customFont.deriveFont(Font.BOLD, 36f));
            int x = gp.screenWidth / 2 - 150;
            int y = (gp.screenWidth / 2) - (gp.tileSize / 2) - 30;

            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRoundRect(x - 10, y - 25, 350, 35, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawString(currentMessage, x, y);
            messageTimer--;
        }
    }

    private void drawSleepScreen() {
        String sleepingMessage = "Zzzzz...";
        g2.setFont(customFont.deriveFont(100f));
        g2.setColor(Color.WHITE);

        int sleepingTextX = (gp.screenWidth / 2) - (gp.tileSize / 2) - 75;
        int sleepingTextY = (gp.screenWidth / 2) - (gp.tileSize / 2) - 150;
        g2.drawString(sleepingMessage, sleepingTextX, sleepingTextY);

        counter++;

        if (counter < 120) {
            Lighting.filterAlpha += 0.01f;
            if (Lighting.filterAlpha > 1f) {
                Lighting.filterAlpha = 1f;
            }
        }

        if (counter >= 120) {
            Lighting.filterAlpha -= 0.01f;

            if (!dayIncreased) {
                if (Lighting.currentDay < Lighting.maxDay) {
                    Lighting.currentDay++;
                } else {
                    gp.gameState = gp.gameOverState;
                }
                dayIncreased = true; // to prevent multiple day increase
            }

            if (Lighting.filterAlpha <= 0f) {
                Lighting.filterAlpha = 0f;
                counter = 0;
                dayIncreased = false; // to prevent multiple day increase
                Lighting.currentDayState = gp.lightManager.lighting.day;
                Lighting.dayCounter = 0;
                gp.gameState = gp.playState;
                gp.player.getPlayerImage();
            }
        }
    }

    private void drawGameOverScreen() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        int x;
        int y;
        String text;

        g2.setFont(customFontBold);
        if (gp.player.getCurrentHealth() == 0) {
            text = "You Died";
        } else {
            text = "CONGRATULATIONS, YOU WON!";
        }

        g2.setColor(Color.BLACK);
        x = getXForCenteredText(text);
        y = gp.tileSize * 4;
        g2.drawString(text, x, y);

        g2.setColor(Color.WHITE);
        g2.drawString(text, x - 4, y - 4);

        g2.setFont(customFontBold.deriveFont(60f));
        text = "Restart (R)";
        x = getXForCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);

        text = "Exit game (ESC)";
        x = getXForCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);

    }

    private void drawFPS() {
        String FPSText;
        int fpsX;
        int fpsY;
        g2.setColor(Color.WHITE);
        g2.setFont(customFont.deriveFont(50f));
        FPSText = "FPS: " + gp.currentFPS;
        fpsX = gp.screenWidth - 150;
        fpsY = gp.screenHeight - 100;
        g2.drawString(FPSText, fpsX, fpsY);
    }

    public void drawEntity(Graphics2D g2, Entity entity, boolean isPlayer, boolean isMoving) {
        this.g2 = g2;
        // skips draw if already dead.
        if (!entity.alive && !entity.dying) {
            return;
        }

        BufferedImage image = null;

        int screenX = entity.worldX;
        int screenY = entity.worldY;

        int tileSize = 48;

        if (gp != null && gp.player != null) {
            screenX = entity.worldX - gp.player.worldX + gp.player.screenX;
            screenY = entity.worldY - gp.player.worldY + gp.player.screenY;
            tileSize = gp.tileSize;
        }

        int scaledWidth = (int) (tileSize * entity.scale);
        int scaledHeight = (int) (tileSize * entity.scale);

        int adjustedScreenX = screenX - (scaledWidth - tileSize) / 2;
        int adjustedScreenY = screenY - (scaledHeight - tileSize) / 2;

        if (isPlayer) {
            if (!isMoving) {
                switch (entity.spriteManager.spriteNum) {
                    case 1:
                        if (!((Player) entity).attacking || entity.spriteManager.scaledAttackUp1 == null) {
                            image = entity.spriteManager.scaledIdle1;
                        } else {
                            image = entity.spriteManager.scaledAttackUp1;
                        }
                        break;
                    case 2:
                        if (!((Player) entity).attacking || entity.spriteManager.scaledAttackDown1 == null) {
                            image = entity.spriteManager.scaledIdle2;
                        } else {
                            image = entity.spriteManager.scaledAttackDown1;
                        }
                        break;
                    case 3:
                        if (!((Player) entity).attacking || entity.spriteManager.scaledAttackLeft1 == null) {
                            image = entity.spriteManager.scaledIdle3;
                        } else {
                            image = entity.spriteManager.scaledAttackLeft1;
                        }
                        break;
                    case 4:
                        if (!((Player) entity).attacking || entity.spriteManager.scaledAttackRight1 == null) {
                            image = entity.spriteManager.scaledIdle4;
                        } else {
                            image = entity.spriteManager.scaledAttackRight1;
                        }
                        break;
                    default:
                        image = entity.spriteManager.scaledIdle1;
                        break;
                }
            } else {
                int walkingFrame = (entity.spriteManager.spriteNum == 1 || entity.spriteManager.spriteNum == 2) ? entity.spriteManager.spriteNum : 1;
                switch (entity.direction) {
                    case "up":
                        if (!((Player) entity).attacking || entity.spriteManager.scaledAttackUp1 == null) {
                            image = (walkingFrame == 1) ? entity.spriteManager.scaledUp1 : entity.spriteManager.scaledUp2;
                        } else {
                            image = (walkingFrame == 1) ? entity.spriteManager.scaledAttackUp1 : entity.spriteManager.scaledAttackUp2;
                        }
                        break;
                    case "down":
                        if (!((Player) entity).attacking || entity.spriteManager.scaledAttackDown1 == null) {
                            image = (walkingFrame == 1) ? entity.spriteManager.scaledDown1 : entity.spriteManager.scaledDown2;
                        } else {
                            image = (walkingFrame == 1) ? entity.spriteManager.scaledAttackDown1 : entity.spriteManager.scaledAttackDown2;
                        }
                        break;
                    case "left":
                        if (!((Player) entity).attacking || entity.spriteManager.scaledAttackLeft1 == null) {
                            image = (walkingFrame == 1) ? entity.spriteManager.scaledLeft1 : entity.spriteManager.scaledLeft2;
                        } else {
                            image = (walkingFrame == 1) ? entity.spriteManager.scaledAttackLeft1 : entity.spriteManager.scaledAttackLeft2;
                        }
                        break;
                    case "right":
                        if (!((Player) entity).attacking || entity.spriteManager.scaledAttackRight1 == null) {
                            image = (walkingFrame == 1) ? entity.spriteManager.scaledRight1 : entity.spriteManager.scaledRight2;
                        } else {
                            image = (walkingFrame == 1) ? entity.spriteManager.scaledAttackRight1 : entity.spriteManager.scaledAttackRight2;
                        }
                        break;
                    default:
                        image = entity.spriteManager.scaledIdle1;
                        break;
                }
            }
        }

        else if (entity.isMovingEntity) {
            int frame;
            if (entity.spriteManager.spriteNum == 1) {
                frame = 1;
            } else {
                frame = 2;
            }

            switch (entity.direction) {
                case "up":
                    if (frame == 1) {
                        image = entity.spriteManager.scaledUp1;
                    } else {
                        image = entity.spriteManager.scaledUp2;
                    }
                    break;
                case "down":
                    if (frame == 1) {
                        image = entity.spriteManager.scaledDown1;
                    } else {
                        image = entity.spriteManager.scaledDown2;
                    }
                    break;
                case "left":
                    if (frame == 1) {
                        image = entity.spriteManager.scaledLeft1;
                    } else {
                        image = entity.spriteManager.scaledLeft2;
                    }
                    break;
                case "right":
                    if (frame == 1) {
                        image = entity.spriteManager.scaledRight1;
                    } else {
                        image = entity.spriteManager.scaledRight2;
                    }
                    break;
                default:
                    image = entity.spriteManager.scaledDown1;
                    break;
            }
        } else {
            switch (entity.spriteManager.spriteNum) {
                case 1:
                    image = entity.spriteManager.scaledIdle1;
                    break;
                case 2:
                    image = entity.spriteManager.scaledIdle2;
                    break;
                case 3:
                    image = entity.spriteManager.scaledIdle3;
                    break;
                default:
                    image = entity.spriteManager.scaledIdle1;
                    break;
            }
        }

        // Enemy Health Bar
        if ((entity instanceof entity.Mob || entity instanceof Pig)) {
            drawEnemyHealthBar(entity, adjustedScreenX, adjustedScreenY, scaledWidth);
        }

        if (image != null) {
            if (entity.invincible) {

                if (isPlayer || entity instanceof Mob || entity instanceof Pig) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                    g2.drawImage(image, adjustedScreenX, adjustedScreenY, scaledWidth, scaledHeight, null);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }

                if (!isPlayer && (entity instanceof Mob || entity instanceof Pig)) {
                    entity.spriteManager.hpBarStatus = true;
                    entity.spriteManager.hpBarCounter = 0;
                }
            } else if (entity.dying) {
                drawDyingAnimation(entity, image, adjustedScreenX, adjustedScreenY, scaledWidth, scaledHeight);
            } else {
                g2.drawImage(image, adjustedScreenX, adjustedScreenY, scaledWidth, scaledHeight, null);
            }
        }

        // DRAW HITBOXES FOR DEBUG
        if (gp.drawHitboxes) {
            drawHitbox(entity, adjustedScreenX, adjustedScreenY, isPlayer);
        }
    }

    private void drawEnemyHealthBar(Entity entity, int adjustedScreenX, int adjustedScreenY, int scaledWidth) {
        int currentHp = 0;
        int maxHp = 1;
        if (entity instanceof Mob) {
            currentHp = ((Mob) entity).getHealth();
            maxHp = ((Mob) entity).maxLife;
        } else {
            currentHp = ((Pig) entity).getHealth();
            maxHp = 3; // Pig maxLife is 3
        }

        if (currentHp > 0) {
            int healthBarDiameter = 40;
            int healthBarX = adjustedScreenX + (scaledWidth - healthBarDiameter) / 2;
            int healthBarY = adjustedScreenY - healthBarDiameter - 10;

            g2.setColor(Color.GRAY);
            g2.fillOval(healthBarX, healthBarY, healthBarDiameter, healthBarDiameter);

            double healthPercentage = (double) currentHp / maxHp;
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
            if (gp != null && gp.ui != null && gp.ui.heartImage != null) {
                g2.drawImage(gp.ui.heartImage, heartX, heartY, heartSize, heartSize, null);
            }
        }
    }

    private void drawHitbox(Entity entity, int adjustedScreenX, int adjustedScreenY, boolean isPlayer) {
        g2.setColor(Color.RED);
        int hitboxX = adjustedScreenX + (int) (entity.solidArea.x * entity.scale);
        int hitboxY = adjustedScreenY + (int) (entity.solidArea.y * entity.scale);
        int hitboxWidth = (int) (entity.solidArea.width * entity.scale);
        int hitboxHeight = (int) (entity.solidArea.height * entity.scale);
        g2.drawRect(hitboxX, hitboxY, hitboxWidth, hitboxHeight);

        if (isPlayer && ((Player) entity).attacking) {
            g2.setColor(Color.BLUE);
            int attackX = adjustedScreenX;
            int attackY = adjustedScreenY;
            switch (entity.direction) {
                case "up":
                    attackY -= (int) (entity.attackArea.height * entity.scale);
                    break;
                case "down":
                    attackY += gp.tileSize;
                    break;
                case "left":
                    attackX -= (int) (entity.attackArea.width * entity.scale);
                    break;
                case "right":
                    attackX += gp.tileSize;
                    break;
            }

            int attackWidth = (int) (entity.attackArea.width * entity.scale);
            int attackHeight = (int) (entity.attackArea.height * entity.scale);
            g2.drawRect(attackX, attackY, attackWidth, attackHeight);
        }
    }

    public void drawDyingAnimation(Entity entity, BufferedImage image, int x, int y, int width, int height) {
        float default_float = 1.0f;

        if (entity.spriteManager.dyingCounter <= 5) {
            default_float = 0.0f;
        } else if (entity.spriteManager.dyingCounter < 5 && entity.spriteManager.dyingCounter <= 10) {
            default_float = 1.0f;
        } else if (entity.spriteManager.dyingCounter < 10 && entity.spriteManager.dyingCounter <= 15) {
            default_float = 0.0f;
        } else if (entity.spriteManager.dyingCounter < 15 && entity.spriteManager.dyingCounter <= 20) {
            default_float = 1.0f;
        } else if (entity.spriteManager.dyingCounter < 20 && entity.spriteManager.dyingCounter <= 25) {
            default_float = 0.0f;
        } else if (entity.spriteManager.dyingCounter < 25 && entity.spriteManager.dyingCounter <= 30) {
            default_float = 1.0f;
        } else if (entity.spriteManager.dyingCounter < 30 && entity.spriteManager.dyingCounter <= 35) {
            default_float = 0.0f;
        } else if (entity.spriteManager.dyingCounter < 35 && entity.spriteManager.dyingCounter <= 40) {
            default_float = 1.0f;
        } else if (entity.spriteManager.dyingCounter > 40) {
            entity.dying = false;
            entity.alive = false;
            return;
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, default_float));
        g2.drawImage(image, x, y, width, height, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
}
