package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_DOOR extends SuperObject{
    public OBJ_DOOR () {
        this.name = "Door";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("")); // door object will be added
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
