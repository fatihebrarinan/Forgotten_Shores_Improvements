package map;

public enum Biome {
    OCEAN(1, 0.0, 0.0, 0.0, 0.0, 0.0), // Tile 1 is Water
    BEACH(2, 0.0, 0.0, 0.0, 0.0, 0.0), // Tile 2 is Sand
    PLAINS(0, 0.002, 0.005, 0.001, 0.002, 0.002), // Tile 0 is Grass
    FOREST(0, 0.015, 0.01, 0.002, 0.001, 0.003);

    public final int primaryTile; // Default tile for the biome
    
    // Spawn probabilities per tile
    public final double treeChance;
    public final double bushChance;
    public final double stoneChance;
    public final double pigChance;
    public final double mobChance;

    Biome(int primaryTile, double treeChance, double bushChance, double stoneChance, double pigChance, double mobChance) {
        this.primaryTile = primaryTile;
        this.treeChance = treeChance;
        this.bushChance = bushChance;
        this.stoneChance = stoneChance;
        this.pigChance = pigChance;
        this.mobChance = mobChance;
    }
    
    // In the future, this can be expanded to return different tiles based on noise or coordinates
    // for biomes that have multiple default tiles (e.g., Mountains with grass and rock).
    public int getTile(int globalCol, int globalRow) {
        return primaryTile;
    }
}
