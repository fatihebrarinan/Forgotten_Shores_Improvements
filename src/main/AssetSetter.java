package main;

import entity.Entity;
import java.util.ArrayList;
import entity.NPC_Mysterious_Stranger;
import monster.MON_Island_Native;
import object.*;

public class AssetSetter {

    GamePanel gp;
    int objectCounter = 0;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        // Other manually placed objects:
        OBJ_CAMPFIRE campfire1 = new OBJ_CAMPFIRE(gp);
        addObject(campfire1, 23, 7);

        OBJ_KEY key1 = new OBJ_KEY(gp);
        addObject(key1, 23, 8);

        OBJ_AXE axe1 = new OBJ_AXE(gp);
        addObject(axe1, 23, 9);

        OBJ_SPEAR spear1 = new OBJ_SPEAR(gp);
        addObject(spear1, 23, 10);

        OBJ_TORCH torch1 = new OBJ_TORCH(gp);
        addObject(torch1, 23, 11);

        // Instead of manually placing trees, call the random tree and bush generator.
        setRandomTrees();
        setRandomBushes();
    }

    public void addObject(Entity obj, int x, int y) {
        obj.worldX = x * gp.tileSize;
        obj.worldY = y * gp.tileSize;
        gp.obj[objectCounter] = obj;
        objectCounter++;
    }

    // Method to randomly generate bushes on the map (completely random, fixed
    // count).
    public void setRandomBushes() {
        // Define how many bushes you want to generate.
        int numberOfBushes = 30; // increased the number because some won't spam because of water tiles.

        // Choose a starting index for bushes.

        // Loop to create bushes at random positions.
        for (int i = 0; i < numberOfBushes; i++) {
            int randomCol = (int) (Math.random() * gp.maxWorldCol);
            int randomRow = (int) (Math.random() * gp.maxWorldRow);


            //if the tile we are trying to spawn a tree is not water. 
            //We may add other statements in the future if we will have other tiles that we do not want bushes on them.
            if(gp.tileM.getMapTileNum()[randomCol][randomRow] != 1)
            {
                int worldX = randomCol * gp.tileSize;
                int worldY = randomRow * gp.tileSize;

                gp.obj[objectCounter] = new OBJ_BUSH(gp);
                gp.obj[objectCounter].worldX = worldX;
                gp.obj[objectCounter].worldY = worldY;

                objectCounter++;
            }
        }
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

 
            //Only place a tree if our random chance succeeds.
            //and if the tile we are trying to spawn a tree is not water. 
            //We may add other statements in the future if we will have other tiles that we do not want bushes on them. 
            if (Math.random() < probability && gp.tileM.getMapTileNum()[randomCol][randomRow] != 1) 
            {
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
        for (Entity tree : trees) {
            // Ensure you don't exceed the array size.
            if (objectCounter >= gp.obj.length) {
                break;
            }
            gp.obj[objectCounter] = tree;
            objectCounter++;
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
