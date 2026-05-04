package utils;

public class Geometry {

    /**
     * Checks if a target point is within a directional cone (sector) originating from a player point.
     * 
     * @param playerX The X coordinate of the player (center)
     * @param playerY The Y coordinate of the player (center)
     * @param direction The direction the player is facing ("up", "down", "left", "right")
     * @param radius The maximum distance of the cone (attack range)
     * @param angleRange The total angle of the cone in degrees (e.g., 90 for a quarter circle)
     * @param targetX The X coordinate of the target (center)
     * @param targetY The Y coordinate of the target (center)
     * @return true if the target is within the cone, false otherwise
     */
    public static boolean isInCone(int playerX, int playerY, String direction, double radius, double angleRange, int targetX, int targetY) {
        double distance = Math.sqrt(Math.pow(targetX - playerX, 2) + Math.pow(targetY - playerY, 2));

        if (distance > radius) {
            return false;
        }

        // Calculate angle from player to target
        // Math.atan2 returns angle in radians from -PI to PI
        double angleToTarget = Math.toDegrees(Math.atan2(targetY - playerY, targetX - playerX));

        // Define player's facing angle based on direction
        // "right" is 0 degrees, "down" is 90, "left" is 180 or -180, "up" is -90
        double facingAngle = 0;
        if (direction != null) {
            switch (direction) {
                case "up":
                    facingAngle = -90;
                    break;
                case "down":
                    facingAngle = 90;
                    break;
                case "left":
                    facingAngle = 180; 
                    break;
                case "right":
                    facingAngle = 0;
                    break;
                default:
                    facingAngle = 0;
                    break;
            }
        }

        // Calculate the absolute difference between the two angles
        double angleDifference = Math.abs(angleToTarget - facingAngle);
        
        // Normalize the difference to be between 0 and 180 degrees
        if (angleDifference > 180) {
            angleDifference = 360 - angleDifference;
        }

        // Check if the difference is within half of the total angle range
        if (angleDifference <= angleRange / 2.0) {
            return true;
        }

        return false;
    }
}
