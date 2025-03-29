package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class UI 
{
    GamePanel gp;
    Font arial_40;
    Font arial_80B;

    public UI(GamePanel gp)
    {
        this.gp = gp;
        arial_40 = new Font("Arial",Font.PLAIN,40);
        arial_80B = new Font("Arial",Font.BOLD,80);
    }
    Graphics2D g2;
    public void draw(Graphics2D g2)
    {
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.WHITE);

        if(gp.gameState == gp.playState)
        {

        }
        if(gp.gameState == gp.pauseState)
        {
            drawPauseScreen();
        }   
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
}
