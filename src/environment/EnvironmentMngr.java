package environment;

import java.awt.Graphics2D;

import main.GamePanel;

public class EnvironmentMngr 
{
    GamePanel gp;
    public Lighting lighting;
    public static final int diameter = 500; // may change, if you want a bigger lighting circle, increase the number

    public EnvironmentMngr(GamePanel aGP)
    {
        this.gp = aGP;
    }

    public void setup()
    {
        lighting = new Lighting(gp);
    }

    public void update()
    {
        lighting.update();
    }

    public void draw(Graphics2D g2)
    {
        lighting.draw(g2);
    }
}
