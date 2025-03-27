package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_AXE extends SuperObject{

    //constructor
    public OBJ_AXE () {
        this.name = "Axe";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("")); // axe object will be added
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
