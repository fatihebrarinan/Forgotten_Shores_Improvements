package main;

import environment.Lighting;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import object.Item;

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
            if (i == gp.player.inventory.getSelectedSlot()) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(x, y, slotSize, slotSize);
                g2.setStroke(new BasicStroke(1));
            }

            Item item = gp.player.inventory.getItem(i);
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
                Lighting.dayState = gp.eManager.lighting.day;
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

}
