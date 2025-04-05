package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class KeyHandler implements KeyListener, MouseListener{
    GamePanel gp;

    public boolean upPressed;
    public boolean downPressed;
    public boolean rightPressed;
    public boolean leftPressed;
    public boolean fPressed;
    public boolean leftClicked = false;
    public boolean cPressed = false;
    public boolean escapePressed = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    public void keyTyped(KeyEvent e) {
        // TODO
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_F) {
            fPressed = true;
        }
        if (code == KeyEvent.VK_C) {
            cPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE) 
        {
            if (gp.gameState == gp.playState) 
            {
                gp.gameState = gp.pauseState;
            } else if (gp.gameState == gp.pauseState) 
            {
                gp.gameState = gp.playState;
            } else if (gp.gameState == gp.dialogueState) 
            {
                gp.gameState = gp.playState;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_F) {
            fPressed = false;
        }
        if (code == KeyEvent.VK_C) {
            cPressed = false;
        }
    }

    @Override
    public void mouseClicked (MouseEvent e) 
    {
        if (e.getButton() == MouseEvent.BUTTON1) 
        { 
            leftClicked = true; 
        }
    }

    @Override
    public void mousePressed(MouseEvent e) 
    {}
    @Override
    public void mouseReleased(MouseEvent e) 
    {}
    @Override
    public void mouseEntered(MouseEvent e) 
    {}
    @Override
    public void mouseExited(MouseEvent e) 
    {}

}
