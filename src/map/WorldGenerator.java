package map;

import main.GamePanel;
import utils.PerlinNoise;

public class WorldGenerator {
    private PerlinNoise elevationNoise;
    private PerlinNoise moistureNoise;

    public WorldGenerator(long seed) {
        elevationNoise = new PerlinNoise(seed);
        moistureNoise = new PerlinNoise(seed + 100); // Offset seed for moisture
    }

    public void generate(Chunk chunk, GamePanel gp) {
        int chunkSize = gp.chunkManager.getChunkSize();

        for (int r = 0; r < chunkSize; r++) {
            for (int c = 0; c < chunkSize; c++) {
                int globalCol = chunk.chunkX * chunkSize + c;
                int globalRow = chunk.chunkY * chunkSize + r;

                // Scale for noise coordinates to control the zoom/frequency of the terrain
                double scale = 0.05;

                // Get noise values
                double elevation = elevationNoise.eval(globalCol * scale, globalRow * scale);
                double moisture = moistureNoise.eval(globalCol * scale, globalRow * scale);

                Biome biome;
                if (elevation < -0.1) {
                    biome = Biome.OCEAN;
                } else if (elevation >= -0.1 && elevation < 0.0) {
                    biome = Biome.BEACH;
                } else {
                    // Inland: distinguish Plains and Forest by moisture
                    if (moisture > 0.1) {
                        biome = Biome.FOREST;
                    } else {
                        biome = Biome.PLAINS;
                    }
                }

                chunk.biomeMap[c][r] = biome;
                chunk.mapTileNum[c][r] = biome.getTile(globalCol, globalRow);
            }
        }

        // Populate items/entities (trees, rocks, animals, etc.)
        gp.chunkPopulator.populateChunk(chunk);
    }
}
