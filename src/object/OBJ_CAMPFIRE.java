package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_CAMPFIRE extends SuperObject{
    
    //constructor
    public OBJ_CAMPFIRE () {
        this.name = "Camp Fire";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("")); // camp fire object will be added
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
