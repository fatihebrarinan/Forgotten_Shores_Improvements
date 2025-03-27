package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager
{
    GamePanel gp;
    private Tile[] tile;
    private int mapTileNum[][]; // to store the indexes of each tile

    public TileManager(GamePanel aGP)
    {
        this.gp = aGP;

        tile = new Tile[10];
        mapTileNum = new int[this.gp.maxWorldCol][this.gp.maxWorldRow];

        getTileImage(); // to load the tiles
        loadMap("/res/maps/world01.txt"); // should be added to folder
    }

    public void getTileImage()
    {
        try
        {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/grass.png")); 

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/water.png")); // URL will be changed

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/res/Tiles/sand.png")); // URL will be changed
            tile[2].collision = true; // WE WILL NEED TO ADD THIS STATEMENT TO PROPER TILES WHICH ARE SOLID (NOT PASSABLE) !!!
        }catch(IOException e)
        {
            System.err.println("Failed to load tile images:");
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath)
    {
        try
        {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < this.gp.maxWorldCol && row < this.gp.maxWorldRow)
            {
                String line = br.readLine();

                while(col < this.gp.maxWorldCol)
                {
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }

                if(col == this.gp.maxWorldCol)
                {
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2)
    {   
        int col = 0;
        int row = 0;
        

        while(col < gp.maxWorldCol && row < gp.maxWorldRow)
        {
            int tileNum = mapTileNum[col][row];

            int worldX = col * gp.tileSize;
            int worldY = row * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY= worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)
            {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }

            col++;

            if(col == gp.maxWorldCol)
            {
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
