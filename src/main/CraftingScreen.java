package main;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import object.Item;
import object.OBJ_AXE;
import object.OBJ_BOAT;
import object.OBJ_CAMPFIRE;
import object.OBJ_KEY;
import object.OBJ_SHELTER;
import object.OBJ_SPEAR;
import object.OBJ_STONE;
import object.OBJ_TORCH;
import object.OBJ_WATER_BUCKET;
import object.OBJ_WOOD;

public class CraftingScreen {
    private GamePanel gp;
    
    // Core logic parameters
    public List<CraftingCategory> craftingCategories = new ArrayList<>();
    
    // UI tracking parameters
    private int selectedCategoryIndex = 0;
    private int selectedItemIndex = 0;
    private boolean isCrafting = false;

    private int lastSelectedCategoryIndex = -1;
    private int lastSelectedItemIndex = -1;
    private boolean inventoryChanged = false;
    
    // Visual Buffer
    private BufferedImage craftingMenuBuffer;

    public CraftingScreen(GamePanel gp) {
        this.gp = gp;
        
        int menuWidth = (int) (gp.screenWidth * 0.8);
        int menuHeight = (int) (gp.screenHeight * 0.8);
        this.craftingMenuBuffer = new BufferedImage(menuWidth, menuHeight, BufferedImage.TYPE_INT_ARGB);
        
        setupCraftingRecipes();
    }

    private void setupCraftingRecipes() {
        // Add the tools category to the crafting menu.
        CraftingCategory tools = new CraftingCategory("Tools");

        // Add torch recipe
        Item torch = new OBJ_TORCH(gp);
        List<Material> torchMaterials = new ArrayList<>();
        torchMaterials.add(new Material(new OBJ_WOOD(gp), 2));
        tools.addRecipe(new CraftingRecipe(torch, torchMaterials));

        // Add axe recipe
        Item axe = new OBJ_AXE(gp);
        List<Material> axeMaterials = new ArrayList<>();
        axeMaterials.add(new Material(new OBJ_WOOD(gp), 2));
        axeMaterials.add(new Material(new OBJ_STONE(gp), 2));
        tools.addRecipe(new CraftingRecipe(axe, axeMaterials));

        // Add spear recipe
        Item spear = new OBJ_SPEAR(gp);
        List<Material> spearMaterials = new ArrayList<>();
        spearMaterials.add(new Material(new OBJ_WOOD(gp), 1));
        spearMaterials.add(new Material(new OBJ_STONE(gp), 2));
        tools.addRecipe(new CraftingRecipe(spear, spearMaterials));

        craftingCategories.add(tools);

        // Add the necessities category to the crafting menu.
        CraftingCategory necessities = new CraftingCategory("Necessities");

        // Add shelter recipe
        Item shelter = new OBJ_SHELTER(gp);
        List<Material> shelterMaterials = new ArrayList<>();
        shelterMaterials.add(new Material(new OBJ_WOOD(gp), 4));
        shelterMaterials.add(new Material(new OBJ_STONE(gp), 4));
        necessities.addRecipe(new CraftingRecipe(shelter, shelterMaterials));

        // Add bucket recipe
        Item bucket = new OBJ_WATER_BUCKET(gp);
        List<Material> bucketMaterials = new ArrayList<>();
        bucketMaterials.add(new Material(new OBJ_STONE(gp), 2));
        necessities.addRecipe(new CraftingRecipe(bucket, bucketMaterials));

        // Add key recipe
        Item key = new OBJ_KEY(gp);
        List<Material> keyMaterials = new ArrayList<>();
        keyMaterials.add(new Material(new OBJ_STONE(gp), 2));
        necessities.addRecipe(new CraftingRecipe(key, keyMaterials));

        // Add campfire recipe
        Item campfire = new OBJ_CAMPFIRE(gp);
        List<Material> campfireMaterials = new ArrayList<>();
        campfireMaterials.add(new Material(new OBJ_WOOD(gp), 2));
        campfireMaterials.add(new Material(new OBJ_TORCH(gp), 1));
        necessities.addRecipe(new CraftingRecipe(campfire, campfireMaterials));

        Item boat = new OBJ_BOAT(gp);
        List<Material> boatMaterials = new ArrayList<>();
        boatMaterials.add(new Material(new OBJ_WOOD(gp), 15));
        boatMaterials.add(new Material(new OBJ_STONE(gp), 15));
        necessities.addRecipe(new CraftingRecipe(boat, boatMaterials));

        craftingCategories.add(necessities);
    }
    
    public void hoverReset() {
        isCrafting = false;
    }

    public void update() {
        if (craftingCategories.isEmpty()) return;

        int menuWidth = (int) (gp.screenWidth * 0.8);
        int menuHeight = (int) (gp.screenHeight * 0.8);
        int menuX = (gp.screenWidth - menuWidth) / 2;
        int menuY = (gp.screenHeight - menuHeight) / 2;

        // Process mouse clicks
        if (gp.keyH.leftClicked) {
            int mx = gp.keyH.mouseX;
            int my = gp.keyH.mouseY;

            // Check Category Tabs
            int tabWidth = 120;
            int tabHeight = 50;
            int tabX = menuX + 10;
            int tabY = menuY + 10;
            for (int i = 0; i < craftingCategories.size(); i++) {
                if (mx >= tabX && mx <= tabX + tabWidth && my >= tabY && my <= tabY + tabHeight) {
                    selectedCategoryIndex = i;
                    selectedItemIndex = 0;
                }
                tabX += tabWidth + 10;
            }

            // Check Recipes
            List<CraftingRecipe> currentRecipes = craftingCategories.get(selectedCategoryIndex).recipes;
            int itemX = menuX + 10;
            int itemY = menuY + tabHeight + 20;
            int itemSize = 64;
            int itemSpacing = 10;
            int itemsPerRow = 3;
            for (int i = 0; i < currentRecipes.size(); i++) {
                if (mx >= itemX && mx <= itemX + itemSize && my >= itemY && my <= itemY + itemSize) {
                    selectedItemIndex = i;
                }
                itemX += itemSize + itemSpacing;
                if ((i + 1) % itemsPerRow == 0) {
                    itemX = menuX + 10;
                    itemY += itemSize + itemSpacing;
                }
            }

            // Check Craft Button
            CraftingRecipe recipe = getSelectedRecipe();
            if (recipe != null) {
                int leftWidth = (int) (menuWidth * 0.4);
                int rightX = menuX + leftWidth + 10;
                int materialsY = menuY + 20 + 128 + 60; 
                int craftX = rightX + 20;
                int craftY = materialsY + 100;
                int craftWidth = 100;
                int craftHeight = 40;
                
                if (mx >= craftX && mx <= craftX + craftWidth && my >= craftY && my <= craftY + craftHeight) {
                    if (checkCanCraft(recipe) && !isCrafting) {
                        craftItem();
                        isCrafting = true; // debounce
                    }
                }
            }

            gp.keyH.leftClicked = false;
        } else if (!gp.keyH.leftMousePressed) {
            isCrafting = false; // release debounce
        }

        // Keyboard navigation
        if (gp.keyH.upPressed) {
            selectedCategoryIndex = (selectedCategoryIndex - 1 + craftingCategories.size()) % craftingCategories.size();
            selectedItemIndex = 0;
            gp.keyH.upPressed = false;
        }
        if (gp.keyH.downPressed) {
            selectedCategoryIndex = (selectedCategoryIndex + 1) % craftingCategories.size();
            selectedItemIndex = 0;
            gp.keyH.downPressed = false;
        }

        List<CraftingRecipe> recipes = craftingCategories.get(selectedCategoryIndex).recipes;
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

        if (gp.keyH.enterPressed) {
            CraftingRecipe recipe = getSelectedRecipe();
            if (recipe != null && checkCanCraft(recipe) && !isCrafting) {
                craftItem();
                isCrafting = true; // debounce keyboard holding
            }
        } else if (!gp.keyH.leftMousePressed) {
            isCrafting = false;
        }
    }
    
    public void notifyInventoryChange() {
        inventoryChanged = true;
    }

    public void draw(Graphics2D g2) {
        if (lastSelectedCategoryIndex != selectedCategoryIndex ||
                lastSelectedItemIndex != selectedItemIndex ||
                inventoryChanged ||
                craftingMenuBuffer == null) {
            updateCraftingMenuBuffer();
            lastSelectedCategoryIndex = selectedCategoryIndex;
            lastSelectedItemIndex = selectedItemIndex;
            inventoryChanged = false;
        }

        int menuWidth = (int) (gp.screenWidth * 0.8);
        int menuHeight = (int) (gp.screenHeight * 0.8);
        int menuX = (gp.screenWidth - menuWidth) / 2;
        int menuY = (gp.screenHeight - menuHeight) / 2;
        g2.drawImage(craftingMenuBuffer, menuX, menuY, menuWidth, menuHeight, null);

        CraftingRecipe selectedRecipe = getSelectedRecipe();
        if (selectedRecipe != null) {
            int leftWidth = (int) (menuWidth * 0.4);
            int rightX = menuX + leftWidth + 10;
            int materialsY = menuY + 20 + 128 + 60;
            int craftX = rightX + 20;
            int craftY = materialsY + 100;
            int craftWidth = 100;
            int craftHeight = 40;

            g2.setClip(craftX, craftY, craftWidth, craftHeight);
            boolean canCraft = checkCanCraft(selectedRecipe);
            
            // Wait to get font from the UI
            g2.setFont(gp.ui.customFont); 
            
            if (canCraft) {
                g2.setColor(new Color(0, 100, 200));
                g2.fillRect(craftX, craftY, craftWidth, craftHeight);
                g2.setColor(Color.WHITE);
                g2.drawString("Craft", craftX + 30, craftY + 25);
            } else {
                g2.setColor(new Color(50, 50, 50));
                g2.fillRect(craftX, craftY, craftWidth, craftHeight);
                g2.setColor(Color.GRAY);
                g2.drawString("Craft", craftX + 30, craftY + 25);
            }

            g2.setClip(null);
        }
    }

    private void updateCraftingMenuBuffer() {
        int menuWidth = (int) (gp.screenWidth * 0.8);
        int menuHeight = (int) (gp.screenHeight * 0.8);

        if (craftingMenuBuffer == null) {
            craftingMenuBuffer = new BufferedImage(menuWidth, menuHeight, BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D bufferG2 = craftingMenuBuffer.createGraphics();
        bufferG2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        bufferG2.setColor(Color.BLACK);
        bufferG2.fillRect(0, 0, menuWidth, menuHeight);
        bufferG2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        bufferG2.setFont(gp.ui.customFont);
        bufferG2.setColor(Color.WHITE);

        int menuY = 0;
        int leftWidth = (int) (menuWidth * 0.4);
        int rightX = leftWidth + 10;
        
        if (craftingCategories.isEmpty()) {
            bufferG2.setColor(Color.WHITE);
            bufferG2.drawString("No crafting recipes available", 50, 50);
            bufferG2.dispose();
            return;
        }

        int tabWidth = 120;
        int tabHeight = 50;
        int tabX = 10;
        bufferG2.setFont(gp.ui.customFont.deriveFont(20f));

        for (int i = 0; i < craftingCategories.size(); i++) {
            bufferG2.setColor(i == selectedCategoryIndex ? Color.LIGHT_GRAY : new Color(100, 100, 100));
            bufferG2.fillRoundRect(tabX, menuY + 10, tabWidth, tabHeight, 10, 10);
            bufferG2.setColor(Color.WHITE);
            String categoryName = craftingCategories.get(i).name;
            int textX = tabX + (tabWidth - bufferG2.getFontMetrics().stringWidth(categoryName)) / 2;
            int textY = menuY + 10 + (tabHeight + bufferG2.getFontMetrics().getAscent()) / 2
                    - bufferG2.getFontMetrics().getDescent();
            bufferG2.drawString(categoryName, textX, textY);
            tabX += tabWidth + 10;
        }

        CraftingCategory selectedCategory = craftingCategories.get(selectedCategoryIndex);
        List<CraftingRecipe> recipes = selectedCategory.recipes;
        int itemX = 10;
        int itemY = menuY + tabHeight + 20;
        int itemSize = 64;
        int itemSpacing = 10;
        int itemsPerRow = 3;
        
        for (int i = 0; i < recipes.size(); i++) {
            Item item = recipes.get(i).result;
            boolean canCraft = checkCanCraft(recipes.get(i));
            if (!canCraft) {
                bufferG2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            if (item.image != null) {
                bufferG2.drawImage(item.image, itemX, itemY, itemSize, itemSize, null);
            }
            bufferG2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            if (i == selectedItemIndex) {
                bufferG2.setColor(Color.YELLOW);
                bufferG2.setStroke(new BasicStroke(3));
                bufferG2.drawRect(itemX, itemY, itemSize, itemSize);
                bufferG2.setStroke(new BasicStroke(1));
            }
            bufferG2.setColor(canCraft ? Color.GREEN : Color.RED);
            if (canCraft) {
                bufferG2.fillRect(itemX + itemSize - 10, itemY + 2, 8, 8);
            } else {
                bufferG2.drawLine(itemX + 2, itemY + 2, itemX + itemSize - 2, itemY + itemSize - 2);
                bufferG2.drawLine(itemX + 2, itemY + itemSize - 2, itemX + itemSize - 2, itemY + 2);
            }
            itemX += itemSize + itemSpacing;
            if ((i + 1) % itemsPerRow == 0) {
                itemX = 10;
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
                bufferG2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            if (selectedItem.image != null) {
                bufferG2.drawImage(selectedItem.image, imageX, imageY, imageSize, imageSize, null);
            }
            bufferG2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            bufferG2.setColor(Color.WHITE);
            bufferG2.setFont(gp.ui.customFont.deriveFont(48f));
            String itemName = selectedItem.name;
            int nameX = imageX + (imageSize - bufferG2.getFontMetrics().stringWidth(itemName)) / 2;
            bufferG2.drawString(itemName, nameX, imageY + imageSize + 30);

            int materialsY = imageY + imageSize + 60;
            bufferG2.setFont(gp.ui.customFont.deriveFont(30f));
            for (Material mat : selectedRecipe.materials) {
                int available = gp.player.inventory.getTotalQuantity(mat.item.name);
                String text = mat.item.name + ": " + available + "/" + mat.quantity;
                bufferG2.setColor(available >= mat.quantity ? Color.GREEN : Color.RED);
                bufferG2.drawString(text, rightX + 20, materialsY);
                materialsY += 25;
            }
        }

        bufferG2.dispose();
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
        if (selectedCategoryIndex < craftingCategories.size()) {
            List<CraftingRecipe> recipes = craftingCategories.get(selectedCategoryIndex).recipes;
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
                if (craftedItem instanceof OBJ_KEY) {
                    gp.player.haveKey = true;
                }
                gp.ui.addMessage("Crafted " + craftedItem.name + "!");
                inventoryChanged = true;
            } else {
                gp.ui.addMessage("Inventory full!");
            }
        }
    }
}
