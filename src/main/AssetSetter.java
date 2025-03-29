package main;

import object.OBJ_AXE;
import object.OBJ_CAMPFIRE;
import object.OBJ_KEY;
import object.OBJ_SPEAR;
import object.OBJ_TORCH;

public class AssetSetter 
{

    GamePanel gp;


    public AssetSetter ( GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        //temporary locations for all objects
        gp.obj[0] = new OBJ_CAMPFIRE();
        gp.obj[0].worldX = 23 * gp.tileSize;
        gp.obj[0].worldY = 7 * gp.tileSize;
        
        gp.obj[1] = new OBJ_KEY();
        gp.obj[1].worldX = 23 * gp.tileSize;
        gp.obj[1].worldY = 8 * gp.tileSize;

        gp.obj[2] = new OBJ_KEY();
        gp.obj[2].worldX = 24 * gp.tileSize;
        gp.obj[2].worldY = 8 * gp.tileSize;

        gp.obj[3] = new OBJ_AXE();
        gp.obj[3].worldX = 21 * gp.tileSize;
        gp.obj[3].worldY = 6 * gp.tileSize;

        gp.obj[4] = new OBJ_SPEAR();
        gp.obj[4].worldX = 20 * gp.tileSize;
        gp.obj[4].worldY = 5 * gp.tileSize;

        gp.obj[6] = new OBJ_TORCH();
        gp.obj[6].worldX = 22 * gp.tileSize;
        gp.obj[6].worldY = 6 * gp.tileSize;
        
        
    }

}
