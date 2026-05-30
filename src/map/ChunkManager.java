package map;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import entity.Attackable;
import entity.Entity;
import entity.Mob;
import entity.Pig;
import entity.WorldObject;
import main.GamePanel;
import object.BreakableItem;
import object.Item;

public class ChunkManager {
    GamePanel gp;
    public ConcurrentHashMap<String, Chunk> activeChunks;

    private WorldGenerator worldGenerator;

    public final int CHUNK_SIZE = 32;
    public final int RENDER_DISTANCE = 2; // Increased to 2 to load earlier

    private ExecutorService executor;
    private ConcurrentHashMap<String, Boolean> loadingChunks;

    public ChunkManager(GamePanel gp) {
        this.gp = gp;
        this.activeChunks = new ConcurrentHashMap<>();
        this.loadingChunks = new ConcurrentHashMap<>();
        this.executor = Executors.newFixedThreadPool(2);

        // Ensure saves/chunks directory exists
        File chunksDir = new File("saves/chunks");
        if (!chunksDir.exists()) {
            chunksDir.mkdirs();
        }

        worldGenerator = new WorldGenerator(12345L); // Default seed
    }

    public void update() {
        int playerCol = gp.player.worldX / gp.tileSize;
        int playerRow = gp.player.worldY / gp.tileSize;
        int playerChunkX = Math.floorDiv(playerCol, CHUNK_SIZE);
        int playerChunkY = Math.floorDiv(playerRow, CHUNK_SIZE);

        // Determine which chunks should be active (e.g., 3x3 grid around player)
        List<String> chunksToKeep = new ArrayList<>();

        for (int x = playerChunkX - RENDER_DISTANCE; x <= playerChunkX + RENDER_DISTANCE; x++) {
            for (int y = playerChunkY - RENDER_DISTANCE; y <= playerChunkY + RENDER_DISTANCE; y++) {
                String key = x + "_" + y;
                chunksToKeep.add(key);
                if (!activeChunks.containsKey(key) && !loadingChunks.containsKey(key)) {
                    loadingChunks.put(key, true);
                    final int cx = x;
                    final int cy = y;
                    executor.submit(() -> {
                        loadOrGenerateChunk(cx, cy);
                    });
                }
            }
        }

        // Unload chunks that are out of bounds
        Iterator<String> it = activeChunks.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (!chunksToKeep.contains(key)) {
                unloadChunk(key);
            }
        }
    }

    private void loadOrGenerateChunk(int x, int y) {
        String key = x + "_" + y;
        File chunkFile = new File("saves/chunks/chunk_" + key + ".dat");

        Chunk chunk = new Chunk(x, y);

        if (chunkFile.exists()) {
            // Load chunk from disk
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chunkFile))) {
                ChunkStorage cs = (ChunkStorage) ois.readObject();
                chunk.mapTileNum = cs.mapTileNum;

                // Reconstruct objects
                for (int i = 0; i < cs.objNames.size(); i++) {
                    Item item = gp.saveStorage.getItem(cs.objNames.get(i));
                    if (item != null) {
                        item.worldX = cs.objWorldX.get(i);
                        item.worldY = cs.objWorldY.get(i);
                        if (item instanceof BreakableItem) {
                            ((BreakableItem) item).setLife(cs.objHealth.get(i));
                        }
                        chunk.objList.add(item);
                    }
                }

                // Reconstruct entities
                for (int i = 0; i < cs.entityNames.size(); i++) {
                    Entity entity = gp.saveStorage.getMonster(cs.entityNames.get(i));
                    if (entity != null) {
                        entity.worldX = cs.entityWorldX.get(i);
                        entity.worldY = cs.entityWorldY.get(i);
                        if (entity instanceof Attackable) {
                            ((Attackable) entity).setHealth(cs.entityHealth.get(i));
                        }
                        chunk.entityList.add(entity);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Generate chunk
            generateChunk(chunk, x, y);
        }

        activeChunks.put(key, chunk);
        loadingChunks.remove(key);
    }

    private void generateChunk(Chunk chunk, int cx, int cy) {
        worldGenerator.generate(chunk, gp);
    }

    private void unloadChunk(String key) {
        Chunk chunk = activeChunks.get(key);
        if (chunk == null)
            return;

        ChunkStorage cs = new ChunkStorage();
        cs.chunkX = chunk.chunkX;
        cs.chunkY = chunk.chunkY;
        cs.mapTileNum = chunk.mapTileNum;

        for (WorldObject obj : chunk.objList) {
            cs.objNames.add(obj.name);
            cs.objWorldX.add(obj.worldX);
            cs.objWorldY.add(obj.worldY);
            if (obj instanceof BreakableItem) {
                cs.objHealth.add(((BreakableItem) obj).getLife());
            } else {
                cs.objHealth.add(0);
            }
        }

        for (Entity entity : chunk.entityList) {
            cs.entityNames.add(entity.name);
            cs.entityWorldX.add(entity.worldX);
            cs.entityWorldY.add(entity.worldY);
            if (entity instanceof Mob) {
                cs.entityHealth.add(((Mob) entity).health);
            } else if (entity instanceof Pig) {
                cs.entityHealth.add(((Pig) entity).health);
            } else {
                cs.entityHealth.add(0);
            }
        }

        activeChunks.remove(key);

        // Offload saving to disk
        executor.submit(() -> {
            File chunkFile = new File("saves/chunks/chunk_" + key + ".dat");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(chunkFile))) {
                oos.writeObject(cs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void draw(Graphics2D g2) {
        for (Chunk chunk : activeChunks.values()) {
            drawChunk(g2, chunk);
        }

        if (gp.showChunkBorders) {
            g2.setColor(java.awt.Color.RED);
            g2.setStroke(new java.awt.BasicStroke(2));
            for (Chunk chunk : activeChunks.values()) {
                int chunkScreenX = (chunk.chunkX * CHUNK_SIZE) * gp.tileSize - gp.player.worldX + gp.player.screenX;
                int chunkScreenY = (chunk.chunkY * CHUNK_SIZE) * gp.tileSize - gp.player.worldY + gp.player.screenY;
                g2.drawRect(chunkScreenX, chunkScreenY, CHUNK_SIZE * gp.tileSize, CHUNK_SIZE * gp.tileSize);
            }
            g2.setStroke(new java.awt.BasicStroke(1));
        }
    }

    private void drawChunk(Graphics2D g2, Chunk chunk) {
        for (int r = 0; r < CHUNK_SIZE; r++) {
            for (int c = 0; c < CHUNK_SIZE; c++) {
                int tileNum = chunk.mapTileNum[c][r];

                int worldX = (chunk.chunkX * CHUNK_SIZE + c) * gp.tileSize;
                int worldY = (chunk.chunkY * CHUNK_SIZE + r) * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                int overlap = 2;

                if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                        && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                        && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                        && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                    int drawTileNum = tileNum;
                    if (tileNum == 0) {
                        int globalCol = chunk.chunkX * CHUNK_SIZE + c;
                        int globalRow = chunk.chunkY * CHUNK_SIZE + r;
                        int seed = (globalCol * 127 + globalRow * 311) ^ (globalCol + globalRow * 521);
                        seed = seed * 31 + 17;
                        int randomIndex = Math.abs(seed) % 5;
                        drawTileNum = (randomIndex == 0) ? 0 : (randomIndex + 2);
                    }
                    g2.drawImage(gp.tileM.getTile(drawTileNum).image,
                            screenX - overlap,
                            screenY - overlap,
                            gp.tileSize + overlap * 2,
                            gp.tileSize + overlap * 2,
                            null);
                }
            }
        }
    }

    // Utility to get all loaded entities for collision/updates
    public List<Entity> getActiveEntities() {
        List<Entity> list = new ArrayList<>();
        for (Chunk c : activeChunks.values()) {
            for (Entity e : c.entityList) {
                if (e.alive) {
                    list.add(e);
                }
            }
        }
        return list;
    }

    public List<WorldObject> getActiveObjects() {
        List<WorldObject> list = new ArrayList<>();
        for (Chunk c : activeChunks.values()) {
            list.addAll(c.objList);
        }
        return list;
    }

    public void removeObject(WorldObject obj) {
        for (Chunk c : activeChunks.values()) {
            if (c.objList.remove(obj)) {
                break;
            }
        }
    }

    public int getTileId(int globalCol, int globalRow) {
        int cx = Math.floorDiv(globalCol, CHUNK_SIZE);
        int cy = Math.floorDiv(globalRow, CHUNK_SIZE);
        String key = cx + "_" + cy;
        Chunk c = activeChunks.get(key);
        if (c != null) {
            int localCol = Math.floorMod(globalCol, CHUNK_SIZE);
            int localRow = Math.floorMod(globalRow, CHUNK_SIZE);
            return c.mapTileNum[localCol][localRow];
        }
        return 1; // Default water if chunk not found
    }

    public void removeEntity(Entity entity) {
        for (Chunk c : activeChunks.values()) {
            if (c.entityList.remove(entity)) {
                break;
            }
        }
    }

    public void addObject(WorldObject obj) {
        int cx = Math.floorDiv(obj.worldX / gp.tileSize, CHUNK_SIZE);
        int cy = Math.floorDiv(obj.worldY / gp.tileSize, CHUNK_SIZE);
        String key = cx + "_" + cy;
        Chunk c = activeChunks.get(key);
        if (c != null) {
            c.objList.add(obj);
        }
    }

    public void addEntity(Entity entity) {
        int cx = Math.floorDiv(entity.worldX / gp.tileSize, CHUNK_SIZE);
        int cy = Math.floorDiv(entity.worldY / gp.tileSize, CHUNK_SIZE);
        String key = cx + "_" + cy;
        Chunk c = activeChunks.get(key);
        if (c != null) {
            c.entityList.add(entity);
        }
    }

    public int getChunkSize() {
        return CHUNK_SIZE;
    }

    public int getRenderDistance() {
        return RENDER_DISTANCE;
    }
}
