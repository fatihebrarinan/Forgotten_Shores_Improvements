package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_KEY extends SuperObject{
    public OBJ_KEY () {
        this.name = "Key";
        this.scale = 1.2f;
        
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/res/Objects/key/key_temporary.png")); // temporary key object may be changed
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
