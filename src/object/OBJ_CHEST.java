package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_CHEST extends SuperObject{
    public OBJ_CHEST () {
        this.name = "Chest";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("")); // chest object will be added
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
