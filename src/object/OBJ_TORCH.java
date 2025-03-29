package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_TORCH extends SuperObject{

    //constructor
    public OBJ_TORCH () {
        this.name = "Torch";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("")); // torch object will be added
        }
        catch (IOException e){
            e.printStackTrace();
        }
}

}