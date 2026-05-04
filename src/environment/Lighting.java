package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;

import main.GamePanel;
import object.Item;

public class Lighting {
    GamePanel gp;

    public static float filterAlpha = 0f;

    public static int dayCounter;
    // Day states
    public final int day = 0;
    public final int dusk = 1;
    public final int night = 2;
    public final int dawn = 3;
    public static int currentDayState = 0;

    // constants for durations
    final int dayDuration = 3600; // seconds * 60
    final int nightDuration = 3600; // seconds * 60

    public static final int maxDay = 10;
    public static int currentDay = 1;

    public Lighting(GamePanel gp) {
        this.gp = gp;
    }

    public void checkDayStatus() {
        if (currentDayState == day) {
            gp.player.canSleep = false;
            dayCounter++;

            if (dayCounter > dayDuration) {
                currentDayState = dusk;
                dayCounter = 0;
            }
        }

        if (currentDayState == dusk) {
            gp.player.canSleep = false;
            filterAlpha += 0.001f; // as this increases, screen gets darker, if we want a smoother transition make
                                   // it 0.0001 . . .

            if (filterAlpha > 1f) {
                filterAlpha = 1f;
                currentDayState = night;
            }
        }

        if (currentDayState == night) {
            gp.player.canSleep = true;
            dayCounter++;

            if (dayCounter > nightDuration) {
                currentDayState = dawn;
                dayCounter = 0;
                if (currentDay < maxDay) {
                    currentDay++; // means current day ends, next day starts
                } else {
                    gp.gameState = gp.gameOverState;
                }
            }
        }

        if (currentDayState == dawn) {
            gp.player.canSleep = false;
            filterAlpha -= 0.001f; // as this increases, screen gets darker, if we want a smoother transition make
                                   // it 0.0001 . . .

            if (filterAlpha < 0f) {
                filterAlpha = 0;
                currentDayState = day;
            }
        }
    }

    public void update() {
        checkDayStatus();
    }

    public void draw(Graphics2D g2) {
        if (filterAlpha > 0f) {
            Item lightSource = gp.player.getLitTorch();
            if (lightSource == null) {
                g2.setColor(new Color(0, 0, 0, 0.85f * filterAlpha));
                g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            } else // player has a lit torch
            {
                // Get the center x and y of the light circle
                int centerX = gp.player.screenX + (gp.tileSize) / 2;
                int centerY = gp.player.screenY + (gp.tileSize) / 2;

                // Create a gradation effect
                Color color[] = new Color[6];
                float fraction[] = new float[6];

                color[0] = new Color(0, 0, 0.1f, 0.1f * filterAlpha);
                color[1] = new Color(0, 0, 0.1f, 0.5f * filterAlpha);
                color[2] = new Color(0, 0, 0.1f, 0.65f * filterAlpha);
                color[3] = new Color(0, 0, 0.1f, 0.75f * filterAlpha);
                color[4] = new Color(0, 0, 0.1f, 0.80f * filterAlpha);
                color[5] = new Color(0, 0, 0.1f, 0.85f * filterAlpha);

                // Decide when these colors shift
                fraction[0] = 0f;
                fraction[1] = 0.4f;
                fraction[2] = 0.6f;
                fraction[3] = 0.8f;
                fraction[4] = 0.9f;
                fraction[5] = 1f;

                int radius = (int) lightSource.lightRadius;

                // Create a gradation paint settings that enables us to draw our screen level by
                // level in terms of brightness and colors
                RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, radius,
                        fraction, color);

                // Fill solid color outside the light radius to prevent rendering a giant
                // gradient
                g2.setColor(color[5]);
                // Top
                g2.fillRect(0, 0, gp.screenWidth, centerY - radius);
                // Bottom
                g2.fillRect(0, centerY + radius, gp.screenWidth, gp.screenHeight - (centerY + radius));
                // Left
                g2.fillRect(0, centerY - radius, centerX - radius, radius * 2);
                // Right
                g2.fillRect(centerX + radius, centerY - radius, gp.screenWidth - (centerX + radius), radius * 2);

                // Set the gradient data on g2 and paint only the bounding square of the light
                g2.setPaint(gPaint);
                g2.fillRect(centerX - radius, centerY - radius, radius * 2, radius * 2);
            }
        }

    }

    public String getDayStateAndCount(String dayStateStr) {
        switch (currentDayState) {
            case day:
                dayStateStr = "Day";
                break;
            case dusk:
                dayStateStr = "Dusk";
                break;
            case night:
                dayStateStr = "Night";
                break;
            case dawn:
                dayStateStr = "Dawn";
                break;
        }
        return "Day: " + currentDay + " - " + dayStateStr;

    }
}
