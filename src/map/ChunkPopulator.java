package map;

import entity.Entity;
import entity.Mob;
import entity.Pig;
import entity.WorldObject;
import main.GamePanel;
import object.OBJ_APPLE_TREE;
import object.OBJ_BUSH;
import object.OBJ_CHEST;
import object.OBJ_STONE;
import object.OBJ_TREE;

public class ChunkPopulator {

    private GamePanel gp;

    public ChunkPopulator(GamePanel gp) {
        this.gp = gp;
    }

    public void populateChunk(Chunk chunk) {
        int startCol = chunk.chunkX * gp.chunkManager.CHUNK_SIZE;
        int startRow = chunk.chunkY * gp.chunkManager.CHUNK_SIZE;

        // Populate items based on per-tile biome probabilities
        for (int c = 0; c < gp.chunkManager.CHUNK_SIZE; c++) {
            for (int r = 0; r < gp.chunkManager.CHUNK_SIZE; r++) {
                Biome biome = chunk.biomeMap[c][r];

                if (biome == null) continue;

                int worldX = (startCol + c) * gp.tileSize;
                int worldY = (startRow + r) * gp.tileSize;

                // Try spawning Tree
                if (Math.random() < biome.treeChance) {
                    if (isValidSpawnPosition(chunk, worldX, worldY)) {
                        WorldObject tree = (Math.random() < 0.3) ? new OBJ_APPLE_TREE(gp) : new OBJ_TREE(gp);
                        tree.worldX = worldX;
                        tree.worldY = worldY;
                        chunk.objList.add(tree);
                    }
                } 
                // Try spawning Bush
                else if (Math.random() < biome.bushChance) {
                    if (isValidSpawnPosition(chunk, worldX, worldY)) {
                        WorldObject bush = new OBJ_BUSH(gp);
                        bush.worldX = worldX;
                        bush.worldY = worldY;
                        chunk.objList.add(bush);
                    }
                }
                // Try spawning Stone
                else if (Math.random() < biome.stoneChance) {
                    if (isValidSpawnPosition(chunk, worldX, worldY)) {
                        WorldObject stone = new OBJ_STONE(gp);
                        stone.worldX = worldX;
                        stone.worldY = worldY;
                        chunk.objList.add(stone);
                    }
                }
                // Try spawning Pig
                else if (Math.random() < biome.pigChance) {
                    if (isValidSpawnPosition(chunk, worldX, worldY)) {
                        Pig pig = new Pig(gp);
                        pig.worldX = worldX;
                        pig.worldY = worldY;
                        chunk.entityList.add(pig);
                    }
                }
                // Try spawning Mob
                else if (Math.random() < biome.mobChance) {
                    if (isValidSpawnPosition(chunk, worldX, worldY)) {
                        Mob mob = new Mob(gp);
                        mob.worldX = worldX;
                        mob.worldY = worldY;
                        chunk.entityList.add(mob);
                    }
                }
            }
        }
        
        // Retain the hardcoded chest for chunk 0,0 as a test item
        if (chunk.chunkX == 0 && chunk.chunkY == 0) {
            int chestWorldX = 23 * gp.tileSize;
            int chestWorldY = 14 * gp.tileSize;
            if (isValidSpawnPosition(chunk, chestWorldX, chestWorldY)) {
                OBJ_CHEST chest = new OBJ_CHEST(gp);
                chest.worldX = chestWorldX;
                chest.worldY = chestWorldY;
                chunk.objList.add(chest);
            }
        }
    }

    private boolean isValidSpawnPosition(Chunk chunk, int worldX, int worldY) {
        int localCol = (worldX / gp.tileSize) % gp.chunkManager.CHUNK_SIZE;
        int localRow = (worldY / gp.tileSize) % gp.chunkManager.CHUNK_SIZE;

        if (localCol < 0) localCol += gp.chunkManager.CHUNK_SIZE;
        if (localRow < 0) localRow += gp.chunkManager.CHUNK_SIZE;

        // Check if there's already an object at this location
        for (WorldObject obj : chunk.objList) {
            if (obj.worldX == worldX && obj.worldY == worldY) {
                return false;
            }
        }

        // Check if there's already an entity at this location
        for (Entity entity : chunk.entityList) {
            if (entity.worldX == worldX && entity.worldY == worldY) {
                return false;
            }
        }

        // Prevent spawning on water (tile 1)
        if (chunk.mapTileNum[localCol][localRow] == 1) {
            return false;
        }

        return true;
    }
}
