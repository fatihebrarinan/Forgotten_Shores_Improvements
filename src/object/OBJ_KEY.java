package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_KEY extends SuperObject{
    public OBJ_KEY () {
        this.name = "Key";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("")); // key object will be added
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
