package main;

import entity.NPC_Mysterious_Stranger;
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

    BufferedImage heartImage;
    BufferedImage foodImage;
    BufferedImage parchmentSprite;

    GamePanel gp;
    Font arial_40;
    Font arial_80B;
    public boolean showTooltip = false;

    // dimensions of the heart and hunger images
    private final int heartImageWidth = 32;
    private final int heartImageHeight = 32;
    private final int hungerImageWidth = 20;
    private final int hungerImageHeight = 20;

    private Font customFont;
    private Font customFontBold;

    private String currentMessage = "";
    private int messageTimer = 0;
    private final int MESSAGE_DURATION = 120; // 2 seconds at 60 FPS

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        try {
            // custom fonts that we will be using might change in time
            InputStream is = getClass().getResourceAsStream("/res/fonts/RussoOne-Regular.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(40f);

            InputStream isBold = getClass().getResourceAsStream("/res/fonts/RussoOne-Regular.ttf");
            customFontBold = Font.createFont(Font.TRUETYPE_FONT, isBold).deriveFont(80f);

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
            parchmentSprite = ImageIO.read(getClass().getResourceAsStream("/res/parchment/parchment.png"));

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


            drawInventory();

        }

        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        if (gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        }

        if (gp.gameState == gp.characterState) {
            drawCharacterScreen();
        }

        drawMessage();
    }

    public void drawToolTip() {
        g2.setFont(customFont.deriveFont(Font.BOLD, 8f));
        String text = "Press F to interact";
        int x = gp.screenWidth / 2 - 50;
        int y = gp.screenHeight - 100;

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x - 10, y - 20, 160, 30, 10, 10);
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }

    public void drawPauseScreen() {
        g2.setFont(customFontBold);
        String text = "PAUSED GAME";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    public int getXForCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

    private void drawHealthBar(Graphics2D g2) {
        int radius = 25;

        // positions of health bar can be adjusted
        int x = 350;
        int y = gp.screenHeight - 20 - 2 * radius;

        int diameter = 2 * radius;

        double healthPercentage = (double) gp.player.getCurrentHealth() / gp.player.getMaxHealth();

        g2.setColor(Color.GRAY);
        g2.fillOval(x, y, diameter, diameter);

        int fillHeight = (int) (diameter * healthPercentage);

        Shape circle = new Ellipse2D.Double(x, y, diameter, diameter);

        Shape fillRect = new Rectangle2D.Double(x, y + (diameter - fillHeight), diameter, fillHeight);

        Area filledArea = new Area(circle);
        filledArea.intersect(new Area(fillRect));

        g2.setColor(Color.RED);
        g2.fill(filledArea);

        int heartX = x + radius - heartImageWidth / 2;
        int heartY = y + radius - heartImageHeight / 2;

        g2.drawImage(heartImage, heartX, heartY, heartImageWidth, heartImageHeight, null);

    }

    private void drawHungerBar(Graphics2D g2) {
        int radius = 25;

        // position of the hunger bar can be adjusted
        int x = 695;
        int y = gp.screenHeight - 20 - 2 * radius;

        int diameter = 2 * radius;

        double hungerPercentage = (double) gp.player.getCurrentHunger() / gp.player.getMaxHunger();

        g2.setColor(Color.GRAY);
        g2.fillOval(x, y, diameter, diameter);

        int fillHeight = (int) (diameter * hungerPercentage);

        Shape circle = new Ellipse2D.Double(x, y, diameter, diameter);

        Shape fillRect = new Rectangle2D.Double(x, y + (diameter - fillHeight), diameter, fillHeight);

        Area filledArea = new Area(circle);
        filledArea.intersect(new Area(fillRect));

        g2.setColor(Color.GREEN);
        g2.fill(filledArea);

        int foodX = x + radius - hungerImageWidth / 2;
        int foodY = y + radius - hungerImageHeight / 2;

        g2.drawImage(foodImage, foodX, foodY, hungerImageWidth, hungerImageHeight, null);
    }

    public void drawDialogueScreen() {

        int x = gp.tileSize * 2;
        int y = gp.tileSize * 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setFont(customFont.deriveFont(Font.PLAIN, 32f));

        String text = "";
        for (int i = 0; i < gp.npc.length; i++) {
            if ((gp.npc[i] != null) && (gp.cChecker.checkEntity(gp.player, gp.npc) == i)) {
                if (gp.npc[i] instanceof NPC_Mysterious_Stranger) {
                    text = ((NPC_Mysterious_Stranger) gp.npc[i]).dialogue;
                } else {
                    text = "backup dialogue"; // if no dialogue is found backup dialogue.
                }
                break;
            }
        }

        int textX = x + 20;
        int textY = y + 50;
        g2.drawString(text, textX, textY);
    }

    public void drawCharacterScreen() {

        final int bookX = gp.tileSize - 30;
        final int bookY = gp.tileSize;
        final int bookWidth = gp.tileSize * 22;
        final int bookHeight = gp.tileSize * 14;

        drawBookBackground(bookX, bookY, bookWidth, bookHeight);

        final int statsX = bookX + gp.tileSize;
        final int statsY = bookY + gp.tileSize;
        final int statsWidth = bookWidth - (gp.tileSize * 2);
        final int statsHeight = (bookHeight - (gp.tileSize * 2)) / 2;

        final int leftPageX = bookX + gp.tileSize;
        final int leftPageY = bookY + gp.tileSize + statsHeight;
        final int leftPageWidth = (bookWidth / 2) - (gp.tileSize * 2);
        final int leftPageHeight = (bookHeight - (gp.tileSize * 2)) / 2;

        final int rightPageX = bookX + (bookWidth / 2) + gp.tileSize;
        final int rightPageY = bookY + gp.tileSize + statsHeight;
        final int rightPageWidth = (bookWidth / 2) - (gp.tileSize * 2);
        final int rightPageHeight = (bookHeight - (gp.tileSize * 2)) / 2;

        drawStatsSection(statsX, statsY, statsWidth, statsHeight);
        drawCurrentEquipmentSection(leftPageX, leftPageY, leftPageWidth, leftPageHeight);
        drawWaysToEscapeSection(rightPageX, rightPageY, rightPageWidth, rightPageHeight);
    }

    private void drawBookBackground(int x, int y, int width, int height) {
        if (parchmentSprite != null) {

            g2.drawImage(parchmentSprite, x, y, width, height, null);
        } else {

            Color parchmentColor = new Color(245, 222, 179);
            g2.setColor(parchmentColor);
            g2.fillRect(x, y, width, height);

            Color spineShadow = new Color(139, 69, 19, 100);
            g2.setColor(spineShadow);
            g2.fillRect(x + (width / 2) - 5, y, 10, height);

            g2.setColor(new Color(139, 69, 19));
            g2.setStroke(new BasicStroke(5));
            g2.drawRect(x + 5, y + 5, width - 10, height - 10);
        }
    }

    private void drawCurrentEquipmentSection(int x, int y, int width, int height) {

        g2.setFont(customFont.deriveFont(18f));
        g2.setColor(Color.BLACK);
        String title = "CURRENT EQUIPMENT";
        int titleX = x + (width - (int) g2.getFontMetrics().getStringBounds(title, g2).getWidth()) / 2;
        g2.drawString(title, titleX + 50, y + 50);

        g2.setFont(customFont.deriveFont(16f));
        int textX = x + 100;
        int textY = y + 90;
        int lineHeight = 40;

        g2.drawString("Weapon", textX, textY);
        String weaponName = gp.player.getCurrentWeapon().name;
        int valueX = x + width - 8 - (int) g2.getFontMetrics().getStringBounds(weaponName, g2).getWidth();
        g2.drawString(weaponName, valueX, textY);
        textY += lineHeight;

        g2.drawString("Shield", textX, textY);
        String shieldName = gp.player.getCurrentShield().name;
        valueX = x + width - 23 - (int) g2.getFontMetrics().getStringBounds(shieldName, g2).getWidth();
        g2.drawString(shieldName, valueX, textY);
    }

    private void drawStatsSection(int x, int y, int width, int height) {

        g2.setFont(customFont.deriveFont(24f));
        g2.setColor(Color.BLACK);
        String title = "STATS";
        int titleX = x + (width - (int) g2.getFontMetrics().getStringBounds(title, g2).getWidth()) / 2;
        g2.drawString(title, titleX, y + 40);

        String[] labels = {
                "Level", "Life",
                "Strength", "Dexterity",
                "Attack", "Defense",
                "Exp", "Exp To Next Level"
        };
        String[] values = {
                String.valueOf(gp.player.getLevel()),
                gp.player.getCurrentHealth() + "/" + gp.player.getMaxHealth(),
                String.valueOf(gp.player.getStrength()),
                String.valueOf(gp.player.getDexterity()),
                String.valueOf(gp.player.getAttack()),
                String.valueOf(gp.player.getDefense()),
                String.valueOf(gp.player.getExp()),
                String.valueOf(gp.player.getExpToNextLevel())
        };

        g2.setFont(customFont.deriveFont(16f));

        final int cellWidth = 380; // change this value to resize all cells
        final int paddingBetweenColumns = 40;
        final int totalGridWidth = (2 * cellWidth) + paddingBetweenColumns;

        int gridX = x + (width - totalGridWidth) / 2;
        int gridY = y + 80;
        int cellHeight = (height - 80) / 5;
        int col = 0;
        int row = 0;

        for (int i = 0; i < labels.length; i++) {
            int cellX = gridX + (col * (cellWidth + paddingBetweenColumns));
            int cellY = gridY + (row * cellHeight);

            g2.drawString(labels[i], cellX, cellY);

            int valueWidth = (int) g2.getFontMetrics().getStringBounds(values[i], g2).getWidth();
            int valueX = cellX + cellWidth - valueWidth;
            g2.drawString(values[i], valueX, cellY);

            col++;
            if (col >= 2) {
                col = 0;
                row++;
            }
        }

        String coinLabel = "Coin";
        String coinValue = String.valueOf(gp.player.getCoin());
        int coinY = gridY + (5 * cellHeight - 10);
        int coinLabelWidth = (int) g2.getFontMetrics().getStringBounds(coinLabel, g2).getWidth();
        int coinValueWidth = (int) g2.getFontMetrics().getStringBounds(coinValue, g2).getWidth();
        int coinLabelX = x + (width - (coinLabelWidth + coinValueWidth + 20)) / 2;
        int coinValueX = coinLabelX + coinLabelWidth + 20;

        g2.drawString(coinLabel, coinLabelX, coinY);
        g2.drawString(coinValue, coinValueX, coinY);
    }

    private void drawWaysToEscapeSection(int x, int y, int width, int height) {

        g2.setFont(customFont.deriveFont(18f));
        g2.setColor(Color.BLACK);
        String title = "WAYS TO ESCAPE";
        int titleX = x + (width - (int) g2.getFontMetrics().getStringBounds(title, g2).getWidth()) / 2;
        g2.drawString(title, titleX - 30, y + 50);

        // empty for now we can dedice what we can do
    }

    public int getXforAlignToRightText(String text, int tailX) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return tailX - length;
    }

    private void drawInventory() 
    {
        int slotSize = 42; // each slot size currently (42x42)
        int spacing = 10;  // space between each slot currently (10 pixels)
        int startX = 425;  // startig x position
        int y = 700;       // starting y position
    
        for ( int i = 0; i < 5; i++ ) 
        {
            int x = startX + i * ( slotSize + spacing );
    
            // slot backgrounds is gray
            g2.setColor( Color.GRAY );
            g2.fillRect( x, y, slotSize, slotSize );
    
            // highlighting the selected slot with yellow
            Item item = gp.player.inventory.getItem(i);
            if (item != null) 
            {
                if (item == gp.player.getCurrentWeapon() || item == gp.player.getCurrentShield()) 
                {
                    g2.setColor(Color.GREEN);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawRect(x, y, slotSize, slotSize);
                    g2.setStroke(new BasicStroke(1));
                }
            }

            if (i == gp.player.inventory.getSelectedSlot()) 
            {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(x, y, slotSize, slotSize);
                g2.setStroke(new BasicStroke(1));
            }
    
            if (item != null && item.image != null) 
            {
                int imgX = x + 2;
                int imgY = y + 2;
                int imgWidth = slotSize - 4;
                int imgHeight = slotSize - 4;
                g2.drawImage(item.image, imgX, imgY, imgWidth, imgHeight, null);
    
                if (item.quantity > 1) 
                {
                    g2.setFont(customFont.deriveFont(12f));
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
            g2.setFont(customFont.deriveFont(Font.BOLD, 20f));
            int x = gp.screenWidth / 2 - 100;
            int y = gp.screenHeight - 50;

            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRoundRect(x - 10, y - 25, 220, 35, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawString(currentMessage, x, y);
            messageTimer--;
        }
    }
}
