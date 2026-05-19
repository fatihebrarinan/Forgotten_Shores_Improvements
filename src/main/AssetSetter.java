package main;

import entity.Mob;
import entity.Pig;
import object.*;

public class AssetSetter {

    GamePanel gp;
    int objectCounter = 0;
    int iTileCounter = 0;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void populateChunk(map.Chunk chunk) {
        int startCol = chunk.chunkX * gp.chunkManager.CHUNK_SIZE;
        int startRow = chunk.chunkY * gp.chunkManager.CHUNK_SIZE;

        // Hardcoded manual placements for specific chunks
        if (chunk.chunkX == 0 && chunk.chunkY == 0) {
            OBJ_CHEST chest = new OBJ_CHEST(gp);
            chest.worldX = 23 * gp.tileSize;
            chest.worldY = 14 * gp.tileSize;
            chunk.objList.add(chest);

            Pig pig1 = new Pig(gp);
            pig1.worldX = 25 * gp.tileSize;
            pig1.worldY = 23 * gp.tileSize;
            chunk.entityList.add(pig1);

            Pig pig2 = new Pig(gp);
            pig2.worldX = 27 * gp.tileSize;
            pig2.worldY = 25 * gp.tileSize;
            chunk.entityList.add(pig2);

            Mob mob1 = new Mob(gp);
            mob1.worldX = 25 * gp.tileSize;
            mob1.worldY = 25 * gp.tileSize;
            chunk.entityList.add(mob1);
            
            Mob mob2 = new Mob(gp);
            mob2.worldX = 26 * gp.tileSize;
            mob2.worldY = 26 * gp.tileSize;
            chunk.entityList.add(mob2);
        }

        if (chunk.chunkX == 1 && chunk.chunkY == 1) {
            Mob mob3 = new Mob(gp);
            mob3.worldX = 32 * gp.tileSize;
            mob3.worldY = 32 * gp.tileSize;
            chunk.entityList.add(mob3);
            
            Mob mob4 = new Mob(gp);
            mob4.worldX = 40 * gp.tileSize;
            mob4.worldY = 40 * gp.tileSize;
            chunk.entityList.add(mob4);
            
            Mob mob5 = new Mob(gp);
            mob5.worldX = 50 * gp.tileSize;
            mob5.worldY = 50 * gp.tileSize;
            chunk.entityList.add(mob5);
            
            Mob mob6 = new Mob(gp);
            mob6.worldX = 51 * gp.tileSize;
            mob6.worldY = 51 * gp.tileSize;
            chunk.entityList.add(mob6);
            
            Mob mob7 = new Mob(gp);
            mob7.worldX = 52 * gp.tileSize;
            mob7.worldY = 52 * gp.tileSize;
            chunk.entityList.add(mob7);
        }

        // Randomly spawn bushes (e.g. 1 per chunk chance)
        if (Math.random() < 0.5) {
            for(int i = 0; i < 2; i++) {
                int c = (int) (Math.random() * gp.chunkManager.CHUNK_SIZE);
                int r = (int) (Math.random() * gp.chunkManager.CHUNK_SIZE);
                if (chunk.mapTileNum[c][r] != 1) { // not water
                    OBJ_BUSH bush = new OBJ_BUSH(gp);
                    bush.worldX = (startCol + c) * gp.tileSize;
                    bush.worldY = (startRow + r) * gp.tileSize;
                    chunk.objList.add(bush);
                }
            }
        }

        // Randomly spawn trees
        int treeCount = (int) (Math.random() * 5); // 0 to 4 trees per chunk
        for (int i = 0; i < treeCount; i++) {
            int c = (int) (Math.random() * gp.chunkManager.CHUNK_SIZE);
            int r = (int) (Math.random() * gp.chunkManager.CHUNK_SIZE);
            if (chunk.mapTileNum[c][r] != 1) {
                Item tree;
                if (Math.random() < 0.3) {
                    tree = new OBJ_APPLE_TREE(gp);
                } else {
                    tree = new OBJ_TREE(gp);
                }
                tree.worldX = (startCol + c) * gp.tileSize;
                tree.worldY = (startRow + r) * gp.tileSize;
                chunk.objList.add(tree);
            }
        }

        // Randomly spawn stones
        if (Math.random() < 0.3) { // 30% chance for a stone patch in a chunk
            int patchCol = (int) (Math.random() * gp.chunkManager.CHUNK_SIZE);
            int patchRow = (int) (Math.random() * gp.chunkManager.CHUNK_SIZE);
            int stonesInPatch = 3 + (int)(Math.random() * 3);
            for(int j = 0; j < stonesInPatch; j++) {
                int c = patchCol + (int)(Math.random() * 3) - 1;
                int r = patchRow + (int)(Math.random() * 3) - 1;
                if (c >= 0 && c < gp.chunkManager.CHUNK_SIZE && r >= 0 && r < gp.chunkManager.CHUNK_SIZE) {
                    if (chunk.mapTileNum[c][r] != 1) {
                        OBJ_STONE stone = new OBJ_STONE(gp);
                        stone.worldX = (startCol + c) * gp.tileSize;
                        stone.worldY = (startRow + r) * gp.tileSize;
                        chunk.objList.add(stone);
                    }
                }
            }
        }
    }

}
