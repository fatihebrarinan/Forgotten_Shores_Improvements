package map;

import main.GamePanel;
import utils.PerlinNoise;

public class WorldGenerator {
    private PerlinNoise elevationNoise;

    public WorldGenerator(long seed) {
        elevationNoise = new PerlinNoise(seed);
    }

    public void generate(Chunk chunk, GamePanel gp) {
        int chunkSize = gp.chunkManager.getChunkSize();

        for (int r = 0; r < chunkSize; r++) {
            for (int c = 0; c < chunkSize; c++) {
                int globalCol = chunk.chunkX * chunkSize + c;
                int globalRow = chunk.chunkY * chunkSize + r;

                // Scale for noise coordinates to control the zoom/frequency of the terrain
                double scale = 0.05;

                // Get noise value for elevation (range approx -1.0 to 1.0)
                double elevation = elevationNoise.eval(globalCol * scale, globalRow * scale);

                if (elevation < -0.1) {
                    // Deep/shallow water
                    chunk.mapTileNum[c][r] = 1; // Water tile
                } else if (elevation >= -0.1 && elevation < 0.0) {
                    // Coastline
                    chunk.mapTileNum[c][r] = 2; // Sand tile
                } else {
                    // Inland
                    chunk.mapTileNum[c][r] = 0; // Grass tile
                }
            }
        }

        // Populate items/entities (trees, rocks, animals, etc.)
        gp.aSetter.populateChunk(chunk);
    }
}
