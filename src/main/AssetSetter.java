package main;

import object.OBJ_CAMPFIRE;

public class AssetSetter 
{

    GamePanel gp;


    public AssetSetter ( GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        
        gp.obj[0] = new OBJ_CAMPFIRE();
        gp.obj[0].worldX = 23 * gp.tileSize;
        gp.obj[0].worldY = 7 * gp.tileSize;
        
    }

}
