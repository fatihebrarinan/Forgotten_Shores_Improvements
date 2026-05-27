package map;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileSpriteManager {
    GamePanel gp;
    private Tile[] tileArr;

    public TileSpriteManager(GamePanel aGP) {
        this.gp = aGP;

        tileArr = new Tile[10];

        getTileImages(); // to load the tiles
    }

    private void getTileImages() {
        try {
            tileArr[0] = new Tile();
            tileArr[0].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/grass.png"));

            // longer grass variations for aesthetics
            tileArr[3] = new Tile();
            tileArr[3].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/grass2.png"));

            tileArr[4] = new Tile();
            tileArr[4].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/grass3.png"));

            tileArr[5] = new Tile();
            tileArr[5].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/grass4.png"));

            tileArr[6] = new Tile();
            tileArr[6].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/grass5.png"));

            tileArr[1] = new Tile();
            tileArr[1].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/water.png")); // URL will be
                                                                                                     // changed

            tileArr[2] = new Tile();
            tileArr[2].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/sand.png")); // URL will be
                                                                                                    // changed
            tileArr[1].collision = true;
        } catch (IOException e) {
            System.err.println("Failed to load tile images:");
            e.printStackTrace();
        }
    }

    public Tile getTile(int index) {
        return tileArr[index];
    }
}
