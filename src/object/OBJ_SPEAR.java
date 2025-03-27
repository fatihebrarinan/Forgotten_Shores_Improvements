package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_SPEAR extends SuperObject{

    //constructor 
    public OBJ_SPEAR () {
        this.name = "Camp Fire";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("")); // spear object will be added
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
