package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_SHELTER extends SuperObject{

    //constructor
    public OBJ_SHELTER () {
        this.name = "Shelter";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("")); // shelter object will be added
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
