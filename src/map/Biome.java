package map;

public enum Biome {
    OCEAN(1, 0.0, 0.0, 0.0, 0.0, 0.0), // Tile 1 is Water
    BEACH(2, 0.0, 0.0, 0.0, 0.0, 0.0), // Tile 2 is Sand
    PLAINS(0, 0.1, 0.3, 0.2, 0.4, 0.4), // Low tree chance, higher animal chance
    FOREST(0, 0.8, 0.6, 0.4, 0.1, 0.6); // High tree chance, lower animal chance

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
