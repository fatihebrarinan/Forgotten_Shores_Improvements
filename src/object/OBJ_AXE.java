package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_AXE extends SuperObject{

    //constructor
    public OBJ_AXE () {
        this.name = "Axe";
        this.scale = 1.2f;
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/axe/axe.png")); // axe object will be added
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
