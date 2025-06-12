package environment;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;
import object.Item;

public class Lighting 
{
    GamePanel gp;
    BufferedImage darknessFilter;
    public static int dayCounter;
    public static float filterAlpha = 0f;

    // Day states
    public final int day = 0;
    public final int dusk = 1;
    public final int night = 2;
    public final int dawn = 3;
    public static int dayState = 0;

    // constants for durations
    final int dayDuration = 600; // May change, for example this means 1800 / 60 = 30 seconds
    final int nightDuration = 600; // May change, for example this means 1800 / 60 = 30 seconds
	
	public static final int maxDay = 10;
	public static int currentDay = 1;

    public Lighting(GamePanel gp) 
    {
		this.gp = gp;
        setLightSource();
	}

    public void update()
    {
        if(gp.player.lightUpdated)
        {
            setLightSource();
            gp.player.lightUpdated = false;
        }

        // check the status of the day
        if(dayState == day)
        {
            gp.player.canSleep = false; // now it is not night so player cannot sleep
            dayCounter++;

            if(dayCounter > dayDuration)
            {
                dayState = dusk;
                dayCounter = 0;
            }
        }

        if(dayState == dusk)
        {
            gp.player.canSleep = false; // now it is not night so player cannot sleep
            filterAlpha += 0.001f; // as this increases, screen gets darker, if we want a smoother transition make it 0.0001 . . .

            if(filterAlpha > 1f)
            {
                filterAlpha = 1f;
                dayState = night;
            }
        }

        if(dayState == night)
        {
            gp.player.canSleep = true; // now it is night so player can sleep
            dayCounter++;

            if(dayCounter > nightDuration)
            {
                dayState = dawn;
                dayCounter = 0;
		        if(currentDay < maxDay) 
                { 
                    currentDay++; // means current day ends, next day starts
                } 
                else 
                { 
                    gp.gameState = gp.gameOverState; 
                } 
            }
        }

        if(dayState == dawn)
        {
            gp.player.canSleep = false; // now it is not night so player cannot sleep
            filterAlpha -= 0.001f; // as this increases, screen gets darker, if we want a smoother transition make it 0.0001 . . .

            if(filterAlpha < 0f)
            {
                filterAlpha = 0;
                dayState = day;
            }
        }
    }

    public void setLightSource()
    {
		// Create a buffered image
		darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D)darknessFilter.getGraphics();
        
        Entity lightSource = gp.player.getCurrentItem("Torch");
        if(lightSource == null)
        {   
            g2.setColor(new Color(0,0,0,0.98f));
        }
        else // player has a lighter
        {
            // Get the center x and y of the light circle
            int centerX = gp.player.screenX + (gp.tileSize)/2;
            int centerY = gp.player.screenY + (gp.tileSize)/2;
                
            // Create a gradation effect
            Color color[] = new Color[6];
            float fraction[] = new float[6];
            
            // Make the colors be brighter in the center
            /*
            * 0.01f's in the 3rd parameters are in order to make the transition a bit blueish (to give the feeling of night), if we won't like it, we will need
            * to change it to 0 as other parameters.
            */
            color[0] = new Color(0, 0, 0.1f, 0.1f);
            color[1] = new Color(0, 0, 0.1f, 0.5f);
            color[2] = new Color(0, 0, 0.1f, 0.7f);
            color[3] = new Color(0, 0, 0.1f, 0.85f);
            color[4] = new Color(0, 0, 0.1f, 0.95f);
            color[5] = new Color(0, 0, 0.1f, 0.98f);
            
            // Decide when these colors shift
            fraction[0] = 0f;
            fraction[1] = 0.4f;
            fraction[2] = 0.6f;
            fraction[3] = 0.8f;
            fraction[4] = 0.9f;
            fraction[5] = 1f;

            
            // Create a gradation paint settings that enables us to draw our screen level by level in terms of brightness and colors
            RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, ((Item)lightSource).lightRadius, fraction, color);
            
            // Set the gradient data on g2
            g2.setPaint(gPaint);
        }
		
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

		g2.dispose();	
    }

    public void draw(Graphics2D g2)
    {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
        g2.drawImage(darknessFilter, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // set back to 1f

        // Drawing the situation of the day, we may make it more fashionable in the future
        String daySituation = "";

        switch(dayState)
        {
            case day: 
                daySituation = "Day"; 
                break;
            case dusk: 
                daySituation = "Dusk"; 
                break;
            case night: 
                daySituation = "Night"; 
                break;
            case dawn: 
                daySituation = "Dawn"; 
                break;
        }

        // Drawing the state of the day in the right bottom, may change if we want to
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(50f));
        g2.drawString(daySituation, gp.screenWidth - 150, gp.screenHeight - 50);

	    // Drawing the current day to inform the player 
        String dayText = "Day: " + currentDay; 
        g2.drawString(dayText, gp.screenWidth - 150, 50); // drawing to the right top 
    }
}
