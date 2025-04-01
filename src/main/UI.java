package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class UI 
{

    BufferedImage heartImage;
    BufferedImage foodImage;


    GamePanel gp;
    Font arial_40;
    Font arial_80B;
    public boolean showTooltip = false; 

    // dimensions of the heart and hunger images
    private final int heartImageWidth = 32; 
    private final int heartImageHeight = 32; 
    private final int hungerImageWidth = 20; 
    private final int hungerImageHeight = 20; 

    public UI(GamePanel gp)
    {
        this.gp = gp;
        arial_40 = new Font("Arial",Font.PLAIN,40);
        arial_80B = new Font("Arial",Font.BOLD,80);

        // reading the heart and food images
        try {
            heartImage = ImageIO.read(getClass().getResourceAsStream("/res/ui/health/heart.png"));
            foodImage = ImageIO.read(getClass().getResourceAsStream("/res/ui/hunger/hunger.png"));
        } catch (IOException e) {
            e.printStackTrace();
    }
    }

    Graphics2D g2;
    public void draw(Graphics2D g2)
    {
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.WHITE);

        if(gp.gameState == gp.playState)
        {
            if (showTooltip)
            {
                drawToolTip();
            }

            drawHealthBar(g2);
            drawHungerBar(g2);

        }
        if(gp.gameState == gp.pauseState)
        {
            drawPauseScreen();
        }   
    }

    public void drawToolTip() 
    {
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        String text = "Press F to interact";
        int x = gp.screenWidth / 2 - 50;
        int y = gp.screenHeight - 100;

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x - 10, y - 20, 150, 30, 10, 10);
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }

    public void drawPauseScreen()
    {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED GAME";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text,x,y);
    }

    public int getXForCenteredText(String text)
    {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

    private void drawHealthBar( Graphics2D g2 ) 
    {
        int radius = 25;

        // positions of health bar can be adjusted
        int x = 350; 
        int y = gp.screenHeight - 20 - 2 * radius;

        int diameter = 2 * radius;
    
        double healthPercentage = ( double ) gp.player.getCurrentHealth() / gp.player.getMaxHealth();
    
        g2.setColor( Color.GRAY );
        g2.fillOval( x, y, diameter, diameter );
    

        int fillHeight = ( int ) ( diameter * healthPercentage );

        Shape circle = new Ellipse2D.Double( x, y, diameter, diameter );
    
        Shape fillRect = new Rectangle2D.Double( x, y + ( diameter - fillHeight ), diameter, fillHeight );
    

        Area filledArea = new Area ( circle );
        filledArea.intersect( new Area ( fillRect ) );
    

        g2.setColor( Color.RED );
        g2.fill( filledArea );
    
        int heartX = x + radius - heartImageWidth / 2;
        int heartY = y + radius - heartImageHeight / 2;

        g2.drawImage( heartImage, heartX, heartY, heartImageWidth, heartImageHeight, null );

    }

    private void drawHungerBar( Graphics2D g2 ) 
    {
        int radius = 25;

        // position of the hunger bar can be adjusted
        int x = 650; 
        int y = gp.screenHeight - 20 - 2 * radius;

        int diameter = 2 * radius;
    
        double hungerPercentage = ( double ) gp.player.getCurrentHunger() / gp.player.getMaxHunger();
    

        g2.setColor( Color.GRAY );
        g2.fillOval( x, y, diameter, diameter );
    

        int fillHeight = ( int ) ( diameter * hungerPercentage ); 
    

        Shape circle = new Ellipse2D.Double( x, y, diameter, diameter );
    

        Shape fillRect = new Rectangle2D.Double( x, y + ( diameter - fillHeight ), diameter, fillHeight );
    
        Area filledArea = new Area ( circle );
        filledArea.intersect( new Area ( fillRect ) );
    

        g2.setColor( Color.GREEN );
        g2.fill( filledArea );
    

        int foodX = x + radius - hungerImageWidth / 2;
        int foodY = y + radius - hungerImageHeight / 2;

        g2.drawImage( foodImage, foodX, foodY, hungerImageWidth, hungerImageHeight, null );
    } 
}
