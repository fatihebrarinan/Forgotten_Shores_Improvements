package map;

import java.io.Serializable;
import java.util.ArrayList;

public class ChunkStorage implements Serializable {
    private static final long serialVersionUID = 1L;

    public int chunkX;
    public int chunkY;
    public int[][] mapTileNum = new int[32][32];

    public ArrayList<String> objNames = new ArrayList<>();
    public ArrayList<Integer> objWorldX = new ArrayList<>();
    public ArrayList<Integer> objWorldY = new ArrayList<>();
    public ArrayList<Integer> objHealth = new ArrayList<>(); // for trees, bushes, etc.

    public ArrayList<String> entityNames = new ArrayList<>();
    public ArrayList<Integer> entityWorldX = new ArrayList<>();
    public ArrayList<Integer> entityWorldY = new ArrayList<>();
    public ArrayList<Integer> entityHealth = new ArrayList<>();
}
