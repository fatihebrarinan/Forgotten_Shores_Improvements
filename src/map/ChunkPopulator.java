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

    // Constants for maximum attempts per chunk.
    private final int MAX_TREE_ATTEMPTS = 15;
    private final int MAX_BUSH_ATTEMPTS = 15;
    private final int MAX_STONE_ATTEMPTS = 4;
    private final int MAX_PIG_ATTEMPTS = 5;
    private final int MAX_MOB_ATTEMPTS = 6;

    public ChunkPopulator(GamePanel gp) {
        this.gp = gp;
    }

    public void populateChunk(Chunk chunk) {
        int startCol = chunk.chunkX * gp.chunkManager.CHUNK_SIZE;
        int startRow = chunk.chunkY * gp.chunkManager.CHUNK_SIZE;

        // Perform random attempts for each resource type
        spawnObjects(chunk, startCol, startRow, MAX_TREE_ATTEMPTS, "TREE");
        spawnObjects(chunk, startCol, startRow, MAX_BUSH_ATTEMPTS, "BUSH");
        spawnObjects(chunk, startCol, startRow, MAX_STONE_ATTEMPTS, "STONE");
        spawnEntities(chunk, startCol, startRow, MAX_PIG_ATTEMPTS, "PIG");
        spawnEntities(chunk, startCol, startRow, MAX_MOB_ATTEMPTS, "MOB");

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

    private void spawnObjects(Chunk chunk, int startCol, int startRow, int attempts, String type) {
        for (int i = 0; i < attempts; i++) {
            int c = (int) (Math.random() * gp.chunkManager.CHUNK_SIZE);
            int r = (int) (Math.random() * gp.chunkManager.CHUNK_SIZE);
            Biome biome = chunk.biomeMap[c][r];
            if (biome == null) continue;

            double successChance = 0;
            switch(type) {
                case "TREE": successChance = biome.treeChance; break;
                case "BUSH": successChance = biome.bushChance; break;
                case "STONE": successChance = biome.stoneChance; break;
            }

            if (Math.random() < successChance) {
                int worldX = (startCol + c) * gp.tileSize;
                int worldY = (startRow + r) * gp.tileSize;

                if (isValidSpawnPosition(chunk, worldX, worldY)) {
                    WorldObject obj = null;
                    if (type.equals("TREE")) {
                        obj = (Math.random() < 0.3) ? new OBJ_APPLE_TREE(gp) : new OBJ_TREE(gp);
                    } else if (type.equals("BUSH")) {
                        obj = new OBJ_BUSH(gp);
                    } else if (type.equals("STONE")) {
                        obj = new OBJ_STONE(gp);
                    }

                    if (obj != null) {
                        obj.worldX = worldX;
                        obj.worldY = worldY;
                        chunk.objList.add(obj);
                    }
                }
            }
        }
    }

    private void spawnEntities(Chunk chunk, int startCol, int startRow, int attempts, String type) {
        for (int i = 0; i < attempts; i++) {
            int c = (int) (Math.random() * gp.chunkManager.CHUNK_SIZE);
            int r = (int) (Math.random() * gp.chunkManager.CHUNK_SIZE);
            Biome biome = chunk.biomeMap[c][r];
            if (biome == null) continue;

            double successChance = 0;
            switch(type) {
                case "PIG": successChance = biome.pigChance; break;
                case "MOB": successChance = biome.mobChance; break;
            }

            if (Math.random() < successChance) {
                int worldX = (startCol + c) * gp.tileSize;
                int worldY = (startRow + r) * gp.tileSize;

                if (isValidSpawnPosition(chunk, worldX, worldY)) {
                    Entity entity = null;
                    if (type.equals("PIG")) {
                        entity = new Pig(gp);
                    } else if (type.equals("MOB")) {
                        entity = new Mob(gp);
                    }

                    if (entity != null) {
                        entity.worldX = worldX;
                        entity.worldY = worldY;
                        chunk.entityList.add(entity);
                    }
                }
            }
        }
    }

    private boolean isValidSpawnPosition(Chunk chunk, int worldX, int worldY) {
        int localCol = (worldX / gp.tileSize) % gp.chunkManager.CHUNK_SIZE;
        int localRow = (worldY / gp.tileSize) % gp.chunkManager.CHUNK_SIZE;

        if (localCol < 0) localCol += gp.chunkManager.CHUNK_SIZE;
        if (localRow < 0) localRow += gp.chunkManager.CHUNK_SIZE;

        // Prevent spawning on water (tile 1)
        if (chunk.mapTileNum[localCol][localRow] == 1) {
            return false;
        }

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

        return true;
    }
}
