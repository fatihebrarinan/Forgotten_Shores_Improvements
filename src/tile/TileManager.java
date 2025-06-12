package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    GamePanel gp;
    private Tile[] tile;
    private int mapTileNum[][]; // to store the indexes of each tile

    public TileManager(GamePanel aGP) {
        this.gp = aGP;

        tile = new Tile[10];
        mapTileNum = new int[this.gp.maxWorldCol][this.gp.maxWorldRow];

        getTileImage(); // to load the tiles
        loadMap("/res/maps/world01.txt"); // should be added to folder
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/grass.png"));

            // longer grass variations for aesthetics
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/grass2.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/grass3.png"));

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/grass4.png"));

            tile[6] = new Tile();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/grass5.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/water.png")); // URL will be changed

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/sand.png")); // URL will be changed
            tile[1].collision = true; // WE WILL NEED TO ADD THIS STATEMENT TO PROPER TILES WHICH ARE SOLID (NOT
                                      // PASSABLE) !!!
        } catch (IOException e) {
            System.err.println("Failed to load tile images:");
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // Loop through each row of the map.
            for (int row = 0; row < this.gp.maxWorldRow; row++) {
                String line = br.readLine();

                // Check if the line is null (end of file)
                if (line == null) {
                    System.out.println("Reached end of file before expected row count.");
                    break;
                }

                // Split the line into individual number strings.
                String[] numbers = line.split(" ");

                // Make sure the number of columns in the line matches expectations.
                if (numbers.length < this.gp.maxWorldCol) {
                    System.out.println("Not enough columns in row " + row);
                    break;
                }

                // Process each column in the current row.
                for (int col = 0; col < this.gp.maxWorldCol; col++) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            int tileNum = mapTileNum[col][row];

            int worldX = col * gp.tileSize;
            int worldY = row * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // Add a small overlap to prevent gaps
            int overlap = 2; // 1 pixel overlap

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                    && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                    && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                    && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                int drawTileNum = tileNum;
                if (tileNum == 0) {
                    int seed = (col * 127 + row * 311) ^ (col + row * 521);
                    seed = seed * 31 + 17;
                    int randomIndex = Math.abs(seed) % 5;
                    drawTileNum = (randomIndex == 0) ? 0 : (randomIndex + 2);
                }
                // Draw the tile with a small overlap
                g2.drawImage(tile[drawTileNum].image,
                        screenX - overlap,
                        screenY - overlap,
                        gp.tileSize + overlap * 2,
                        gp.tileSize + overlap * 2,
                        null);
            }

            col++;

            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public Tile[] getTile() {
        return tile;
    }

    public int[][] getMapTileNum() {
        return mapTileNum;
    }

}
