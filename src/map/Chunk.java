package map;

import entity.Entity;
import entity.WorldObject;
import java.util.ArrayList;
import java.util.List;

public class Chunk {
    public int chunkX;
    public int chunkY;
    public int[][] mapTileNum;
    public List<WorldObject> objList;
    public List<Entity> entityList;

    public Chunk(int chunkX, int chunkY) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.mapTileNum = new int[32][32];
        this.objList = new ArrayList<>();
        this.entityList = new ArrayList<>();
    }
}
