package main;

import entity.Entity;
import object.OBJ_TREE;
import object.OBJ_APPLE_TREE;
import java.util.ArrayList;
import entity.NPC_Mysterious_Stranger;
import monster.MON_Island_Native;
import object.OBJ_AXE;
import object.OBJ_CAMPFIRE;
import object.OBJ_KEY;
import object.OBJ_SPEAR;
import object.OBJ_TORCH;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        // Other manually placed objects:
        gp.obj[0] = new OBJ_CAMPFIRE(gp);
        gp.obj[0].worldX = 23 * gp.tileSize;
        gp.obj[0].worldY = 7 * gp.tileSize;

        gp.obj[1] = new OBJ_KEY(gp);
        gp.obj[1].worldX = 23 * gp.tileSize;
        gp.obj[1].worldY = 8 * gp.tileSize;

        gp.obj[3] = new OBJ_AXE(gp);
        gp.obj[3].worldX = 21 * gp.tileSize;
        gp.obj[3].worldY = 6 * gp.tileSize;

        gp.obj[4] = new OBJ_SPEAR(gp);
        gp.obj[4].worldX = 20 * gp.tileSize;
        gp.obj[4].worldY = 5 * gp.tileSize;

        gp.obj[6] = new OBJ_TORCH(gp);
        gp.obj[6].worldX = 22 * gp.tileSize;
        gp.obj[6].worldY = 6 * gp.tileSize;

        // Instead of manually placing trees, call the random tree generator.
        setRandomTrees();
    }

    // This method generates trees based on location weights.
    public void setRandomTrees() {
        // We'll collect the trees in a temporary list
        ArrayList<Entity> trees = new ArrayList<>();

        // Define a region where trees spawn more frequently
        // Here we can get the coordinates of the places where we want more trees.
        int highDensityMinCol = 30;
        int highDensityMaxCol = 50;
        int highDensityMinRow = 30;
        int highDensityMaxRow = 50;

        // Define probabilities for each area.
        // For high density areas, we use a higher chance.
        double highDensityProbability = 0.4; // 100% chance to place a tree at a given attempt.
        double lowDensityProbability = 0.02; // 10% chance outside the high density area.

        // Number of random attempts. Increasing this number will result in more trees
        // overall.
        int totalAttempts = 700;

        for (int i = 0; i < totalAttempts; i++) {
            int randomCol = (int) (Math.random() * gp.maxWorldCol);
            int randomRow = (int) (Math.random() * gp.maxWorldRow);

            // Determine the probability based on whether the cell is in the high density
            // area.
            double probability = lowDensityProbability;
            if (randomCol >= highDensityMinCol && randomCol <= highDensityMaxCol
                    && randomRow >= highDensityMinRow && randomRow <= highDensityMaxRow) {
                probability = highDensityProbability;
            }

            // Only place a tree if our random chance succeeds.
            if (Math.random() < probability) {
                int worldX = randomCol * gp.tileSize;
                int worldY = randomRow * gp.tileSize;

                // Randomly choose which type of tree to spawn.
                Entity tree;
                if (Math.random() < 0.3) {
                    tree = new OBJ_APPLE_TREE(gp);
                } else {
                    tree = new OBJ_TREE(gp);
                }
                tree.worldX = worldX;
                tree.worldY = worldY;
                trees.add(tree);
            }
        }

        // Now, place the trees into your gp.obj array.
        // For example, if you've already manually placed objects in indices 0 to 6,
        // start adding trees at index 7.
        int treeIndex = 7;
        for (Entity tree : trees) {
            // Ensure you don't exceed the array size.
            if (treeIndex >= gp.obj.length) {
                break;
            }
            gp.obj[treeIndex] = tree;
            treeIndex++;
        }
    }

    public void setNPC() {
        gp.npc[0] = new NPC_Mysterious_Stranger(gp);
        gp.npc[0].worldX = gp.tileSize * 21;
        gp.npc[0].worldY = gp.tileSize * 21;
    }

    public void setMonster() {
        gp.monster[0] = new MON_Island_Native(gp);
        gp.monster[0].worldX = gp.tileSize * 25;
        gp.monster[0].worldY = gp.tileSize * 25;

        gp.monster[1] = new MON_Island_Native(gp);
        gp.monster[1].worldX = gp.tileSize * 26;
        gp.monster[1].worldY = gp.tileSize * 26;
    }
}
