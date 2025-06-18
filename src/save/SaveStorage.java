package save;

import entity.Entity;
import entity.Mob;
import entity.Pig;
import entity.WorldObject;
import environment.Lighting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import main.GamePanel;
import object.Item;
import object.OBJ_APPLE;
import object.OBJ_APPLE_TREE;
import object.OBJ_AXE;
import object.OBJ_BUSH;
import object.OBJ_CAMPFIRE;
import object.OBJ_CHEST;
import object.OBJ_KEY;
import object.OBJ_RAW_MEAT;
import object.OBJ_SHELTER;
import object.OBJ_SPEAR;
import object.OBJ_STONE;
import object.OBJ_TORCH;
import object.OBJ_WATER_BUCKET;
import object.OBJ_WOOD;
import object.OBJ_TREE;

public class SaveStorage {
    GamePanel gp;

    public SaveStorage(GamePanel gp) {
        this.gp = gp;
    }

    public Item getItem(String itemName) {
        Item obj = null;
        switch (itemName) {
            case "Apple":
                obj = new OBJ_APPLE(gp);
                break;
            case "Axe":
                obj = new OBJ_AXE(gp);
                break;
            case "Camp Fire":
                obj = new OBJ_CAMPFIRE(gp);
                break;
            case "Key":
                obj = new OBJ_KEY(gp);
                break;
            case "Meat":
                obj = new OBJ_RAW_MEAT(gp);
                break;
            case "Shelter":
                obj = new OBJ_SHELTER(gp);
                break;
            case "Spear":
                obj = new OBJ_SPEAR(gp);
                break;
            case "Stone":
                obj = new OBJ_STONE(gp);
                break;
            case "Torch":
                obj = new OBJ_TORCH(gp);
                break;
            case "Water Bucket":
                obj = new OBJ_WATER_BUCKET(gp);
                break;
            case "Wood":
                obj = new OBJ_WOOD(gp);
                break;
            case "stone":
                obj = new OBJ_STONE(gp);
                break;
            case "bush":
                obj = new OBJ_BUSH(gp);
                break;
            case "apple tree":
                obj = new OBJ_APPLE_TREE(gp);
                break;
            case "Chest":
                obj = new OBJ_CHEST(gp);
                break;
        }

        return obj;
    }

    private Entity getMonster(String name) {
        switch (name) {
            case "Pig":
                return new Pig(gp);
            case "Island Native":
                return new Mob(gp);
            default:
                return null;
        }
    }

    public void saveGame() {
        try {
            try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(new File("data.dat")))) {
                Storage stor = new Storage();

                stor.health = gp.player.getCurrentHealth();
                stor.hunger = gp.player.getCurrentHunger();
                stor.thirst = gp.player.getCurrentThirst();

                stor.maxHealth = gp.player.getMaxHealth();
                stor.maxHunger = gp.player.getMaxHunger();
                stor.maxThirst = gp.player.getMaxThirst();
                stor.speed = gp.player.speed;
                stor.level = gp.player.getLevel();
                stor.strength = gp.player.getStrength();
                stor.dexterity = gp.player.getDexterity();
                stor.exp = gp.player.getExp();
                stor.expToNextLevel = gp.player.getExpToNextLevel();
                stor.coin = gp.player.getCoin();

                stor.direction = gp.player.direction;

                for (int i = 0; i < gp.player.inventory.getSlots().length; i++) {
                    if (gp.player.inventory.getSlots()[i] != null) {
                        stor.itemNames.add(gp.player.inventory.getSlots()[i].name);
                        stor.itemAmounts.add(gp.player.inventory.getSlots()[i].quantity);
                    } else {
                        stor.itemNames.add(null);
                        stor.itemAmounts.add(0);
                    }
                }

                stor.mapObjectNames = new String[gp.maxWorldCol][gp.maxWorldRow];
                stor.mapObjectWorldX = new int[gp.maxWorldCol][gp.maxWorldRow];
                stor.mapObjectWorldY = new int[gp.maxWorldCol][gp.maxWorldRow];
                stor.treeIsHarvestable = new boolean[gp.maxWorldCol][gp.maxWorldRow];
                stor.treeLife = new int[gp.maxWorldCol][gp.maxWorldRow];

                for (WorldObject obj : gp.obj) {
                    if (obj != null) {
                        int col = obj.worldX / gp.tileSize;
                        int row = obj.worldY / gp.tileSize;
                        stor.mapObjectNames[col][row] = obj.name;
                        stor.mapObjectWorldX[col][row] = obj.worldX;
                        stor.mapObjectWorldY[col][row] = obj.worldY;
                        if (obj instanceof OBJ_APPLE_TREE) {
                            //stor.treeIsHarvestable[col][row] = ((OBJ_APPLE_TREE) obj).getHarvestable();
                            stor.treeLife[col][row] = ((OBJ_APPLE_TREE) obj).life;
                        } else if (obj instanceof OBJ_TREE) {
                            stor.treeLife[col][row] = ((OBJ_TREE) obj).life;
                        }
                    }
                }

                stor.playerWorldX = gp.player.worldX;
                stor.playerWorldY = gp.player.worldY;

                for (Entity monster : gp.monster) {
                    if (monster != null && monster.alive) {
                        stor.monsterNames.add(monster.name);
                        stor.monsterWorldX.add(monster.worldX);
                        stor.monsterWorldY.add(monster.worldY);
                        if (monster instanceof Mob) {
                            stor.monsterHealth.add(((Mob) monster).life);
                        } else if (monster instanceof Pig) {
                            stor.monsterHealth.add(((Pig) monster).life);
                        }
                    }
                }
                stor.currentDay = Lighting.currentDay;
                stor.dayState = Lighting.dayState;
                stor.dayCounter = Lighting.dayCounter;
                stor.filterAlpha = Lighting.filterAlpha;
                stor.haveKey = gp.player.haveKey;
                stream.writeObject(stor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGame() {
        try {
            try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(new File("data.dat")))) {
                Storage s = (Storage) stream.readObject();

                gp.player.setCurrentHealth(s.health);
                gp.player.setCurrentHunger(s.hunger);
                gp.player.setCurrentThirst(s.thirst);

                gp.player.setMaxHealth(s.maxHealth);
                gp.player.setMaxHunger(s.maxHunger);
                gp.player.setMaxThirst(s.maxThirst);
                gp.player.speed = s.speed;
                gp.player.setLevel(s.level);
                gp.player.setStrength(s.strength);
                gp.player.setDexterity(s.dexterity);
                gp.player.setExp(s.exp);
                gp.player.setExpToNextLevel(s.expToNextLevel);
                gp.player.setCoin(s.coin);
                gp.player.setDirection(s.direction);

                gp.player.inventory.clearInventory();
                for (int i = 0; i < s.itemNames.size(); i++) {
                    String itemName = s.itemNames.get(i);
                    int quantity = s.itemAmounts.get(i);

                    /*if (itemName != null) {
                        Item item = Item.createItemByName(itemName, gp);
                        if (item != null) {
                            item.quantity = quantity;
                            gp.player.inventory.setItem(i, item);
                        }
                    } else {
                        gp.player.inventory.setItem(i, null);
                    }*/
                }

                for (int i = 0; i < gp.obj.length; i++) {
                    gp.obj[i] = null;
                }

                int counter = 0;
                for (int col = 0; col < s.mapObjectNames.length; col++) {
                    for (int row = 0; row < s.mapObjectNames[0].length; row++) {
                        String name = s.mapObjectNames[col][row];
                        if (name != null) {
                            Item obj = getItem(name);
                            if (obj != null) {
                                obj.worldX = s.mapObjectWorldX[col][row];
                                obj.worldY = s.mapObjectWorldY[col][row];
                                if (obj instanceof OBJ_APPLE_TREE) {
                                    //((OBJ_APPLE_TREE) obj).setHasApple(s.treeIsHarvestable[col][row]);
                                    ((OBJ_APPLE_TREE) obj).life = s.treeLife[col][row];
                                } else if (obj instanceof OBJ_TREE) {
                                    ((OBJ_TREE) obj).life = s.treeLife[col][row];
                                }
                                gp.obj[counter] = obj;
                                counter++;
                            }
                        }
                    }
                }

                gp.player.worldX = s.playerWorldX;
                gp.player.worldY = s.playerWorldY;

                for (int i = 0; i < gp.monster.length; i++) {
                    gp.monster[i] = null;
                }

                for (int i = 0; i < s.monsterNames.size(); i++) {
                    String name = s.monsterNames.get(i);
                    Entity monster = getMonster(name);
                    if (monster != null) {
                        monster.worldX = s.monsterWorldX.get(i);
                        monster.worldY = s.monsterWorldY.get(i);
                        if (monster instanceof Mob) {
                            ((Mob) monster).life = s.monsterHealth.get(i);
                        } else if (monster instanceof Pig) {
                            ((Pig) monster).life = s.monsterHealth.get(i);
                        }
                        gp.monster[i] = monster;
                    }
                }

                Lighting.currentDay = s.currentDay;
                Lighting.dayState = s.dayState;
                Lighting.dayCounter = s.dayCounter;
                Lighting.filterAlpha = s.filterAlpha;
                gp.player.haveKey = s.haveKey;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
