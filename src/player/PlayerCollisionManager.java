package player;

import entity.Entity;
import main.GamePanel;
import java.util.List;

public class PlayerCollisionManager {
    GamePanel gp;

    public PlayerCollisionManager(GamePanel aGP) {
        this.gp = aGP;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.chunkManager.getTileId(entityLeftCol, entityTopRow);
                tileNum2 = gp.chunkManager.getTileId(entityRightCol, entityTopRow);
                if (gp.tileM.getTile(tileNum1).collision || gp.tileM.getTile(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.chunkManager.getTileId(entityLeftCol, entityBottomRow);
                tileNum2 = gp.chunkManager.getTileId(entityRightCol, entityBottomRow);
                if (gp.tileM.getTile(tileNum1).collision || gp.tileM.getTile(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                if (entityLeftCol >= 0 && entityTopRow >= 0 && entityBottomRow >= 0) {
                    tileNum1 = gp.chunkManager.getTileId(entityLeftCol, entityTopRow);
                    tileNum2 = gp.chunkManager.getTileId(entityLeftCol, entityBottomRow);
                    if (gp.tileM.getTile(tileNum1).collision || gp.tileM.getTile(tileNum2).collision) {
                        entity.collisionOn = true;
                    }
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                if (entityRightCol >= 0 && entityTopRow >= 0 && entityBottomRow >= 0) {
                    tileNum1 = gp.chunkManager.getTileId(entityRightCol, entityTopRow);
                    tileNum2 = gp.chunkManager.getTileId(entityRightCol, entityBottomRow);
                    if (gp.tileM.getTile(tileNum1).collision || gp.tileM.getTile(tileNum2).collision) {
                        entity.collisionOn = true;
                    }
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) {

        int index = 999;

        for (int i = 0; i < gp.objList.size(); i++) {
            if (gp.objList.get(i) != null) {

                // Get entity's solid area position
                entity.solidArea.x += entity.worldX;
                entity.solidArea.y += entity.worldY;

                // Get the object's solid area position
                gp.objList.get(i).solidArea.x += gp.objList.get(i).worldX;
                gp.objList.get(i).solidArea.y += gp.objList.get(i).worldY;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.objList.get(i).solidArea)) {
                            if (gp.objList.get(i).collision == true) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.objList.get(i).solidArea)) {
                            if (gp.objList.get(i).collision == true) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.objList.get(i).solidArea)) {
                            if (gp.objList.get(i).collision == true) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.objList.get(i).solidArea)) {
                            if (gp.objList.get(i).collision == true) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.objList.get(i).solidArea.x = gp.objList.get(i).solidAreaDefaultX;
                gp.objList.get(i).solidArea.y = gp.objList.get(i).solidAreaDefaultY;
            }
        }
        return index;
    }

    public int checkEntity(Entity entity, List<Entity> target) {
        int index = 999;

        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != null && target.get(i) != entity && target.get(i).solidArea != null) {

                // Get entity's solid area position
                entity.solidArea.x += entity.worldX;
                entity.solidArea.y += entity.worldY;

                // Get the object's solid area position
                target.get(i).solidArea.x += target.get(i).worldX;
                target.get(i).solidArea.y += target.get(i).worldY;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(target.get(i).solidArea)) {
                            entity.collisionOn = true;
                            target.get(i).collisionOn = true;
                            index = i;
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(target.get(i).solidArea)) {
                            entity.collisionOn = true;
                            target.get(i).collisionOn = true;
                            index = i;
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(target.get(i).solidArea)) {
                            entity.collisionOn = true;
                            target.get(i).collisionOn = true;
                            index = i;
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(target.get(i).solidArea)) {
                            entity.collisionOn = true;
                            target.get(i).collisionOn = true;
                            index = i;
                        }
                        break;
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target.get(i).solidArea.x = target.get(i).solidAreaDefaultX;
                target.get(i).solidArea.y = target.get(i).solidAreaDefaultY;
            }
        }
        return index;
    }
}