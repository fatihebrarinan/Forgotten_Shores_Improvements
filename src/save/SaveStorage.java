package save;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import main.GamePanel;

public class SaveStorage {
    GamePanel gp;

    public SaveStorage(GamePanel gp) {
        this.gp = gp;
    }

    @SuppressWarnings("resource")
    public void saveGame() {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(new File("data.dat")));

            Storage stor = new Storage();

            stor.health = gp.player.getCurrentHealth();
            stor.hunger = gp.player.getCurrentHunger();

            stor.level = gp.player.getLevel();
            stor.strength = gp.player.getStrength();
            stor.dexterity = gp.player.getDexterity();
            stor.exp = gp.player.getExp();
            stor.expToNextLevel = gp.player.getExpToNextLevel();
            stor.coin = gp.player.getCoin();
            // stor.currentWeapon = gp.player.getCurrentWeapon();
            // stor.currentShield = gp.player.getCurrentShield();

            stor.defense = gp.player.getDefense();
            stor.attack = gp.player.getAttack();

            // stor.inventory = gp.player.inventory;
            stream.writeObject(stor);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("resource")
    public void loadGame() {
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(new File("data.dat")));

            Storage s = (Storage) stream.readObject();

            gp.player.setCurrentHealth(s.health);
            gp.player.setCurrentHunger(s.hunger);

            gp.player.setLevel(s.level);
            gp.player.setStrength(s.strength);
            gp.player.setDexterity(s.dexterity);
            gp.player.setExp(s.exp);
            gp.player.setExpToNextLevel(s.expToNextLevel);
            gp.player.setCoin(s.coin);
            // gp.player.setCurrentWeapon(s.currentWeapon);
            // gp.player.setCurrentShield(s.currentShield);

            gp.player.setDefense(s.defense);
            gp.player.setAttack(s.attack);
            // gp.player.setInventory(s.inventory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
