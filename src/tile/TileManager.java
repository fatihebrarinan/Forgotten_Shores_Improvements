package tile;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    GamePanel gp;
    private Tile[] tile;
    public TileManager(GamePanel aGP) {
        this.gp = aGP;

        tile = new Tile[10];

        getTileImage(); // to load the tiles
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
            tile[1].collision = true; 
        } catch (IOException e) {
            System.err.println("Failed to load tile images:");
            e.printStackTrace();
        }
    }

    public Tile[] getTile() {
        return tile;
    }
}
