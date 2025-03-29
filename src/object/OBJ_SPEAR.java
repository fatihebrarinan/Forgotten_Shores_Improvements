package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_SPEAR extends SuperObject{

    //constructor 
    public OBJ_SPEAR () {
        this.name = "Spear";
        this.scale = 1.2f;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/spear/spear.png")); // spear object will be added
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
