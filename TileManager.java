package tile;

import java.io.BufferedReader;
import java.io.InputStream;

public class TileManager
{
    Panel gp;
    Tile[] tile;
    int mapTileNum[][]; // to store the indexes of each tile

    public TileManager(Panel aGP)
    {
        this.gp = aGP;

        tile = new Tile[10];
        mapTileNum = new int[this.gp.maxScreenCol][this.gp.maxScreenRow];

        getTileImage(); // to load the tiles
        loadMap("/maps/map01.txt"); // should be added to folder
    }

    public void getTileImage()
    {
        try
        {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png")); // URL will be changed

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png")); // URL will be changed

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png")); // URL will be changed
        }catch(IOException e){
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

            while(col < this.gp.maxScreenCol && row < this.gp.maxScreenRow)
            {
                String line = br.readLine();

                while(col < this.gp.maxScreenCol)
                {
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }

                if(col == this.gp.maxScreenCol)
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
        int x = 0;
        int y = 0;

        while(col < gp.maxScreenCol && row < gp.maxScreenRow)
        {
            int tileNum = mapTileNum[col][row];

            g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            col++;
            x += gp.tileSize;

            if(col == gp.maxScreenCol)
            {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }

}