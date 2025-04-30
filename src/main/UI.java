package main;

import environment.Lighting; 
import entity.Entity;
import entity.NPC_Mysterious_Stranger;
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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import object.Item;

public class UI {

    BufferedImage heartImage;
    BufferedImage foodImage;
    BufferedImage thirstImage;
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
    private final int thirstImageWidth = 20;
    private final int thirstImageHeight = 20;

    private Font customFont;
    private Font customFontBold;

    private String currentMessage = "";
    private int messageTimer = 0;
    private final int MESSAGE_DURATION = 120; // 2 seconds at 60 FPS

    int counter = 0;

    private int selectedCategoryIndex = 0;
    private int selectedItemIndex = 0;
    private boolean isCrafting = false;
    private float craftingProgress = 0;
    private final float CRAFTING_TIME = 120;

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
            drawThirstBar(g2);

            drawInventory();

        }

        if (gp.gameState == gp.pauseState) 
        {
        }

        if (gp.gameState == gp.dialogueState) 
        {
            drawDialogueScreen();
        }

        if (gp.gameState == gp.characterState) 
        {
            drawCharacterScreen();
        }

        if (gp.gameState == gp.gameOverState) 
        {
            drawGameOverScreen();
        }

        if(gp.gameState == gp.sleepState)
        {
            drawSleepScreen();
        }

        if (gp.gameState == gp.craftingState) 
        {
            drawCraftingScreen();
        }

        drawMessage();
    }

    private void drawCraftingScreen() {
        int menuWidth = (int) (gp.screenWidth * 0.8);
        int menuHeight = (int) (gp.screenHeight * 0.8);
        int menuX = (gp.screenWidth - menuWidth) / 2;
        int menuY = (gp.screenHeight - menuHeight) / 2;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);

        int leftWidth = (int) (menuWidth * 0.4);
        int rightWidth = menuWidth - leftWidth;
        int leftX = menuX + 10;
        int rightX = menuX + leftWidth + 10;

        List<CraftingCategory> categories = gp.craftingCategories;
        if (categories.isEmpty()) {
            g2.setColor(Color.WHITE);
            g2.drawString("No crafting recipes available", menuX + 50, menuY + 50);
            return;
        }

        int tabWidth = 120;
        int tabHeight = 50;
        int tabX = leftX;
        g2.setFont(customFont.deriveFont(20f));
        for (int i = 0; i < categories.size(); i++) {
            g2.setColor(i == selectedCategoryIndex ? Color.YELLOW : new Color(100, 100, 100));
            g2.fillRoundRect(tabX, menuY + 10, tabWidth, tabHeight, 10, 10);
            g2.setColor(Color.WHITE);
            String categoryName = categories.get(i).name;
            int textX = tabX + (tabWidth - g2.getFontMetrics().stringWidth(categoryName)) / 2;
            int textY = menuY + 10 + (tabHeight + g2.getFontMetrics().getAscent()) / 2 - g2.getFontMetrics().getDescent();
            g2.drawString(categoryName, textX, textY);
            tabX += tabWidth + 10;
        }

        CraftingCategory selectedCategory = categories.get(selectedCategoryIndex);
        List<CraftingRecipe> recipes = selectedCategory.recipes;
        int itemX = leftX;
        int itemY = menuY + tabHeight + 20;
        int itemSize = 64;
        int itemSpacing = 10;
        int itemsPerRow = 3;
        for (int i = 0; i < recipes.size(); i++) {
            Item item = recipes.get(i).result;
            boolean canCraft = checkCanCraft(recipes.get(i));
            if (!canCraft) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            if (item.image != null) {
                g2.drawImage(item.image, itemX, itemY, itemSize, itemSize, null);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            if (i == selectedItemIndex) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(itemX, itemY, itemSize, itemSize);
                g2.setStroke(new BasicStroke(1));
            }
            g2.setColor(canCraft ? Color.GREEN : Color.RED);
            if (canCraft) {
                g2.fillRect(itemX + itemSize - 10, itemY + 2, 8, 8);
            } else {
                g2.drawLine(itemX + 2, itemY + 2, itemX + itemSize - 2, itemY + itemSize - 2);
                g2.drawLine(itemX + 2, itemY + itemSize - 2, itemX + itemSize - 2, itemY + 2);
            }
            itemX += itemSize + itemSpacing;
            if ((i + 1) % itemsPerRow == 0) {
                itemX = leftX;
                itemY += itemSize + itemSpacing;
            }
        }

        if (selectedItemIndex < recipes.size()) {
            CraftingRecipe selectedRecipe = recipes.get(selectedItemIndex);
            Item selectedItem = selectedRecipe.result;
            boolean canCraft = checkCanCraft(selectedRecipe);
            int imageX = rightX + 20;
            int imageY = menuY + 20;
            int imageSize = 128;
            if (!canCraft) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            if (selectedItem.image != null) {
                g2.drawImage(selectedItem.image, imageX, imageY, imageSize, imageSize, null);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.setColor(Color.WHITE);
            g2.setFont(customFont.deriveFont(24f));
            String itemName = selectedItem.name;
            int nameX = imageX + (imageSize - g2.getFontMetrics().stringWidth(itemName)) / 2;
            g2.drawString(itemName, nameX, imageY + imageSize + 30);

            int materialsY = imageY + imageSize + 60;
            g2.setFont(customFont.deriveFont(18f));
            for (Material mat : selectedRecipe.materials) {
                int available = gp.player.inventory.getTotalQuantity(mat.item.name);
                String text = mat.item.name + ": " + available + "/" + mat.quantity;
                g2.setColor(available >= mat.quantity ? Color.GREEN : Color.RED);
                g2.drawString(text, rightX + 20, materialsY);
                materialsY += 30;
            }

            int craftX = rightX + 20;
            int craftY = materialsY + 20;
            int craftWidth = 100;
            int craftHeight = 40;
            if (canCraft) {
                if (isCrafting) {
                    g2.setColor(new Color(50, 50, 50));
                    g2.fillRect(craftX, craftY, craftWidth, craftHeight);
                    int progressWidth = (int) (craftWidth * (craftingProgress / CRAFTING_TIME));
                    g2.setColor(Color.GREEN);
                    g2.fillRect(craftX, craftY, progressWidth, craftHeight);
                    g2.setColor(Color.WHITE);
                    g2.drawString("Crafting...", craftX + 10, craftY + 25);
                } else {
                    g2.setColor(new Color(0, 100, 200));
                    g2.fillRect(craftX, craftY, craftWidth, craftHeight);
                    g2.setColor(Color.WHITE);
                    g2.drawString("Craft", craftX + 30, craftY + 25);
                }
            } else {
                g2.setColor(new Color(50, 50, 50));
                g2.fillRect(craftX, craftY, craftWidth, craftHeight);
                g2.setColor(Color.GRAY);
                g2.drawString("Craft", craftX + 30, craftY + 25);
            }
        }
    }

    private boolean checkCanCraft(CraftingRecipe recipe) {
        for (Material mat : recipe.materials) {
            if (!gp.player.inventory.hasEnough(mat.item.name, mat.quantity)) {
                return false;
            }
        }
        return true;
    }

    private CraftingRecipe getSelectedRecipe() {
        if (selectedCategoryIndex < gp.craftingCategories.size()) {
            List<CraftingRecipe> recipes = gp.craftingCategories.get(selectedCategoryIndex).recipes;
            if (selectedItemIndex < recipes.size()) {
                return recipes.get(selectedItemIndex);
            }
        }
        return null;
    }

    private void craftItem() {
        CraftingRecipe recipe = getSelectedRecipe();
        if (recipe != null && checkCanCraft(recipe)) {
            for (Material mat : recipe.materials) {
                gp.player.inventory.consumeItem(mat.item.name, mat.quantity);
            }
            Item craftedItem = recipe.result.clone();
            craftedItem.quantity = 1;
            if (gp.player.inventory.addItem(craftedItem)) {
                gp.ui.addMessage("Crafted " + craftedItem.name + "!");
            } else {
                gp.ui.addMessage("Inventory full!");
            }
        }
    }

    public void updateCrafting() {
        List<CraftingCategory> categories = gp.craftingCategories;
        if (categories.isEmpty()) {
            return;
        }

        if (gp.keyH.upPressed) {
            selectedCategoryIndex = (selectedCategoryIndex - 1 + categories.size()) % categories.size();
            selectedItemIndex = 0;
            gp.keyH.upPressed = false;
        }
        if (gp.keyH.downPressed) {
            selectedCategoryIndex = (selectedCategoryIndex + 1) % categories.size();
            selectedItemIndex = 0;
            gp.keyH.downPressed = false;
        }

        List<CraftingRecipe> recipes = categories.get(selectedCategoryIndex).recipes;
        if (!recipes.isEmpty()) {
            if (gp.keyH.leftPressed) {
                selectedItemIndex = (selectedItemIndex - 1 + recipes.size()) % recipes.size();
                gp.keyH.leftPressed = false;
            }
            if (gp.keyH.rightPressed) {
                selectedItemIndex = (selectedItemIndex + 1) % recipes.size();
                gp.keyH.rightPressed = false;
            }
        }

        if (gp.keyH.enterPressed && !isCrafting) {
            CraftingRecipe recipe = getSelectedRecipe();
            if (recipe != null && checkCanCraft(recipe)) {
                isCrafting = true;
            } 
            gp.keyH.enterPressed = false;
        }

        if (isCrafting) {
            craftingProgress += 1;
            if (craftingProgress >= CRAFTING_TIME) {
                craftItem();
                craftingProgress = 0;
                isCrafting = false;
            }
        }
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
        int radius = 25;

        // positions of health bar can be adjusted
        int x = 50;
        int y = gp.screenHeight - 20 - 5 * radius;

        int diameter = 5 * radius;

        double healthPercentage = (double) gp.player.getCurrentHealth() / gp.player.getMaxHealth();

        int outlineThickness = 5; 
        g2.setColor(new Color(175,0,0));
        g2.fillOval(x - outlineThickness, y - outlineThickness, diameter + 2 * outlineThickness, diameter + 2 * outlineThickness);

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
        int radius = 25;
        // position of the hunger bar can be adjusted
        int x = 225;
        int y = gp.screenHeight - 20 - 5 * radius;

        int diameter = 5 * radius;

        double hungerPercentage = (double) gp.player.getCurrentHunger() / gp.player.getMaxHunger();

        int outlineThickness = 5; 
        g2.setColor(new Color(0, 127, 14));
        g2.fillOval(x - outlineThickness, y - outlineThickness, diameter + 2 * outlineThickness, diameter + 2 * outlineThickness);

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
        int radius = 25;

        // position of the hunger bar can be adjusted
        int x = 400;
        int y = gp.screenHeight - 20 - 5 * radius;

        int diameter = 5 * radius;

        double thirstPercentage = (double) gp.player.getCurrentThirst() / gp.player.getMaxThirst();

        int outlineThickness = 5; 
        g2.setColor(new Color(0, 200, 255));
        g2.fillOval(x - outlineThickness, y - outlineThickness, diameter + 2 * outlineThickness, diameter + 2 * outlineThickness);

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

    public void drawDialogueScreen() {

        int x = gp.tileSize * 2;
        int y = gp.tileSize * 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 7;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setFont(customFont.deriveFont(Font.PLAIN, 32f));

        String text = "";
        for (int i = 0; i < gp.npc.length; i++) {
            if ((gp.npc[i] != null) && (gp.cChecker.checkEntity(gp.player, gp.npc) == i)) {
                if (gp.npc[i] instanceof NPC_Mysterious_Stranger) 
                {
                    text = ((NPC_Mysterious_Stranger) gp.npc[i]).dialogue;
                } 
                else 
                {
                    text = "backup dialogue"; // if no dialogue is found backup dialogue.
                }
                break;
            }
        }

        int textX = x + 20;
        int textY = y + 50;
        String[] lines = text.split("\n"); // \n karakterine göre ayır
        int lineHeight = g2.getFontMetrics().getHeight();

        for (String line : lines) 
        {
            g2.drawString(line, textX, textY);
            textY += lineHeight; // her satırdan sonra y'yi kaydır
        }
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

        g2.setFont(customFont.deriveFont(24f));
        g2.setColor(Color.BLACK);
        String title = "CURRENT EQUIPMENT";
        int titleX = x + (width - (int) g2.getFontMetrics().getStringBounds(title, g2).getWidth()) / 2;
        g2.drawString(title, titleX + 50, y + 50);

        g2.setFont(customFont.deriveFont(22f));
        int textX = x + 100;
        int textY = y + 90;
        int lineHeight = 40;

        g2.drawString("Weapon", textX, textY);
        Entity currentWeapon = gp.player.getCurrentWeapon();
        String weaponName;
        if (currentWeapon != null) 
        {
            weaponName = currentWeapon.name;
        } 
        else 
        {
            weaponName = "Normal Sword";
        }

        int valueX = x + width - 8 - (int) g2.getFontMetrics().getStringBounds(weaponName, g2).getWidth();
        g2.drawString(weaponName, valueX, textY);
        textY += lineHeight;

        g2.drawString("Shield", textX, textY);
        Entity currentShield = gp.player.getCurrentShield();
        String shieldName;

        if (currentShield != null) 
        {
            shieldName = currentShield.name;
        } 
        else 
        {
            shieldName = "Wood Shield";
        }
        valueX = x + width - 23 - (int) g2.getFontMetrics().getStringBounds(shieldName, g2).getWidth();
        g2.drawString(shieldName, valueX, textY);
        textY += lineHeight;

        g2.drawString("Lighting", textX, textY);
        Entity currentLighting = gp.player.getCurrentLighting();
        String lightingName;
        if(currentLighting != null)
        {
            lightingName = currentLighting.name;
        }
        else
        {
            lightingName = "No lighting";
        }
        valueX = x + width - 8 - (int) g2.getFontMetrics().getStringBounds(weaponName, g2).getWidth();
        g2.drawString(lightingName, valueX, textY);
    }

    private void drawStatsSection(int x, int y, int width, int height) {

        g2.setFont(customFont.deriveFont(30f));
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

        g2.setFont(customFont.deriveFont(22f));

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

        g2.setFont(customFont.deriveFont(24f));
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
        int slotSize = 100; // each slot size currently (100x100)
        int spacing = 10;  // space between each slot currently (10 pixels)
        int startX = gp.screenWidth / 2 - 2 * slotSize - 50 - 2 * spacing; // startig x position
        int y = gp.screenHeight - 120; // starting y position
    
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

    private void drawSleepScreen()
    {
        String sleepingMessage = "Zzzzz...";
        g2.setFont(customFont.deriveFont(100f));
        g2.setColor(Color.WHITE);

        int sleepingTextX = (gp.screenWidth / 2) - (gp.tileSize / 2) - 75;
        int sleepingTextY = (gp.screenWidth / 2) - (gp.tileSize / 2) - 150;
        g2.drawString(sleepingMessage, sleepingTextX, sleepingTextY);

        counter++;

        if(counter < 120)
        {
            gp.eManager.lighting.filterAlpha += 0.01f;
            if(gp.eManager.lighting.filterAlpha > 1f)
            {
                gp.eManager.lighting.filterAlpha = 1f;
            }
        }

        if(counter >= 120)
        {
            gp.eManager.lighting.filterAlpha -= 0.01f;

            if(!dayIncreased)
            {
                if (Lighting.currentDay < Lighting.maxDay) 
                { 
                    Lighting.currentDay++; 
                } 
                else 
                { 
                    gp.gameState = gp.gameOverState; 
                } 
                dayIncreased = true; // to prevent multiple day increase
            }
            
            if(gp.eManager.lighting.filterAlpha <= 0f)
            {
                gp.eManager.lighting.filterAlpha = 0f;
                counter = 0;
                dayIncreased = false; // to prevent multiple day increase
                gp.eManager.lighting.dayState = gp.eManager.lighting.day;
                gp.eManager.lighting.dayCounter = 0;
                gp.gameState = gp.playState;
                gp.player.getPlayerImage();
            } 
        }
    }

    private void drawGameOverScreen()
    {
       g2.setColor(new Color(0,0,0,150));
       g2.fillRect(0,0, gp.screenWidth, gp.screenHeight); 
       int x;
       int y;
       String text;

       g2.setFont(customFontBold);
       if(gp.player.getCurrentHealth() == 0) 
       { 
            text = "You Died"; 
       } 
       else 
       { 
            text = "CONGRATULATIONS, YOU WON!"; 
       }

       g2.setColor(Color.BLACK);
       x = getXForCenteredText(text);
       y = gp.tileSize * 4;
       g2.drawString(text, x ,y);

       g2.setColor(Color.WHITE);
       g2.drawString(text, x - 4 ,y - 4);

       g2.setFont(customFontBold.deriveFont(60f));
       text = "Restart (R)";
       x = getXForCenteredText(text);
       y += gp.tileSize * 4;
       g2.drawString(text, x, y);

       text = "Exit game (ESC)";
       x = getXForCenteredText(text);
       y += 55;
       g2.drawString(text, x ,y);

    }

}
