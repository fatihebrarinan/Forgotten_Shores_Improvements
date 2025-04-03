package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class Entity
{
    public GamePanel gp;
    public int worldX; // x-coordinate of entity
    public int worldY; // y-coordinate of entity
    public int speed; // movement speed of entity
    public float scale =1.0f;
    public int actionLockCounter = 0;

    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, left1, left2, left3, left4, right1, right2, right3, right4; // representing the images which will swap during movements
    public BufferedImage idle1, idle2, idle3, idle4; // idle animation variables
    public BufferedImage attackUp1, attackUp2, attackLeft1, attackLeft2, attackDown1, attackDown2, attackRight1, attackRight2;
    public String direction; // where entity looks

    public int spriteCounter = 0; // 
    public int spriteNum = 1; // for example: is it up1 or up2

    public Rectangle solidArea; // part that cannot colide
    public Rectangle attackArea;
    public int solidAreaDefaultX;
    public int solidAreaDefaultY;
    public boolean collisionOn = false;
    public boolean collision = false;

    public boolean isMovingEntity = false;

    public BufferedImage image;
    public String name;

    public boolean invincible = false;
    public int invincibilityTimer = 0;
    public int invincibilityDuration = 30;


    public Entity(GamePanel gp)
    {
        this.gp = gp;
    }

    // NPC movement method for future NPCs implementation
    public void update()
    {
        if ( isMovingEntity )
        {
            setAction();
            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkObject(this, false);
            gp.cChecker.checkEntity(this, gp.monster);
            gp.cChecker.checkPlayer(this);

            if ( !this.collisionOn ) 
            {
                if ( this.direction != null )
                {
                    if (this.direction.equals("up")) {
                        this.worldY -= this.speed;
                    } else if (direction.equals("down")) {
                        this.worldY += this.speed;
                    } else if (direction.equals("left")) {
                        this.worldX -= this.speed;
                    } else if (direction.equals("right")) {
                        this.worldX += this.speed;
                    }
                }  
            }
        }

        this.spriteCounter++;
        if (this.spriteCounter > 12) {
            if (this.spriteNum == 1) {
                this.spriteNum = 2;
            } else if (this.spriteNum == 2) {
                this.spriteNum = 1;
            }
            this.spriteCounter = 0;
        }

        
        if (invincibilityTimer > 0) 
        {
            invincibilityTimer--;

            if (invincibilityTimer == 0) 
            {
                invincible = false; 
            }
        }
    }

    public void draw( Graphics2D g2, boolean isPlayer, boolean isMoving ) 
    {
        BufferedImage image = null;

        int screenX = worldX;
        int screenY = worldY;

        int tileSize = 48; 

        if (gp != null && gp.player != null) 
        {
            screenX = worldX - gp.player.worldX + gp.player.screenX;
            screenY = worldY - gp.player.worldY + gp.player.screenY;
            tileSize = gp.tileSize;
        }

        int scaledWidth = (int) (tileSize * scale);
        int scaledHeight = (int) (tileSize * scale);
        
        int adjustedScreenX = screenX - (scaledWidth - tileSize) / 2;
        int adjustedScreenY = screenY - (scaledHeight - tileSize) / 2;

        int tempScreenX = adjustedScreenX;
        int tempScreenY = adjustedScreenY;

        if (isPlayer) 
        {
            if (!isMoving) 
            {
                switch (spriteNum) 
                {
                    case 1: 
                        if(!((Player)this).attacking)
                        {
                            image = idle1;
                        } 
                        else
                        {
                            image = attackUp1;
                        }
                        break;
                    case 2:
                        if(!((Player)this).attacking)
                        {
                            image = idle2;
                        } 
                        else
                        {
                            image = attackDown1;
                        }
                        break;
                    case 3:
                        if(!((Player)this).attacking)
                        {
                            image = idle3;
                        } 
                        else
                        {
                            image = attackLeft1;
                        }
                        break;
                    case 4: 
                        if(!((Player)this).attacking)
                        {
                            image = idle4;
                        } 
                        else
                        {
                            image = attackRight1;
                        }
                        break;
                    default: image = idle1; break;
                }
            } else 
            {
                int walkingFrame = (spriteNum == 1 || spriteNum == 2) ? spriteNum : 1;
                switch (direction) 
                {
                    case "up":
                        if(!((Player)this).attacking)
                        {
                            image = (walkingFrame == 1) ? up1 : up2;
                        }
                        else
                        {
                            tempScreenY = screenY - gp.tileSize;
                            image = (walkingFrame == 1) ? attackUp1 : attackUp2;
                        }
                        break;
                    case "down":
                        if(!((Player)this).attacking)
                        {
                            image = (walkingFrame == 1) ? down1 : down2;
                        }
                        else
                        {
                            image = (walkingFrame == 1) ? attackDown1 : attackDown2;
                        }
                        break;
                    case "left":
                        if(!((Player)this).attacking)
                        {
                            image = (walkingFrame == 1) ? left1 : left2;
                        }
                        else
                        {
                            tempScreenX = screenX - gp.tileSize;
                            image = (walkingFrame == 1) ? attackLeft1 : attackLeft2;
                        }
                        break;
                    case "right":
                        if(!((Player)this).attacking)
                        {
                            image = (walkingFrame == 1) ? right1 : right2;
                        }
                        else
                        {
                            image = (walkingFrame == 1) ? attackRight1 : attackRight2;
                        }
                        break;
                    default:
                        image = idle1;
                        break;
                }
            }
        } else if ( isMovingEntity ) 
        {  
            int frame;
            if (spriteNum == 1)
            {
                frame = 1;
            } else 
            {
                frame = 2;
            }

            switch (direction) 
            {
                case "up":
                    if (frame == 1)
                    {
                        image = up1;
                    } else 
                    {
                        image = up2;
                    }
                    break;
                case "down":
                    if (frame == 1) 
                    {
                        image = down1;
                    } else 
                    {
                        image = down2;
                    }
                    break;
                case "left":
                    if (frame == 1) 
                    {
                        image = left1;
                    } else 
                    {
                        image = left2;
                    }
                    break;
                case "right":
                    if (frame == 1) 
                    {
                        image = right1;
                    } else 
                    {
                        image = right2;
                    }
                    break;
                default:
                    image = down1;
                    break;
            }
        } else {  
            switch (spriteNum) {
                case 1: image = idle1; break;
                case 2: image = idle2; break;
                case 3: image = idle3; break;
                default: image = idle1; break;
            }
        }

        if (image != null) 
        {
            if (invincible) 
            {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                g2.drawImage(image, adjustedScreenX, adjustedScreenY, scaledWidth, scaledHeight, null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            } else 
            {
                g2.drawImage(image, adjustedScreenX, adjustedScreenY, scaledWidth, scaledHeight, null);
            }
        }
    }

    public void setAction()
    {
    
    }

    public boolean isInvincible() 
    {
        return invincible;
    }

    /* 
    Direction picker method for to be used for other NPCs.

    public void setAction()
    {
        actionLockCounter ++;

        if (actionLockCounter == 120)
        {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if ( i <= 25)
            {
                direction = "up";
            }

            if ( i > 25 && i <= 50)
            {
                direction = "down";
            }

            if ( i > 50 && i <= 75)
            {
                direction = "left";
            }

            if ( i > 75 && i <= 100)
            {
                direction = "right";
            }
        }

        actionLockCounter = 0;

    }

    */

}
