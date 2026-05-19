package save;

import java.io.Serializable;
import java.util.ArrayList;

public class Storage implements Serializable {

    int maxHealth;
    int maxHunger;
    int maxThirst;

    int health;
    int hunger;
    int thirst;
    int speed;
    int level;
    int strength;
    int dexterity;
    int exp;
    int expToNextLevel;
    int coin;
    String direction;

    ArrayList<String> itemNames = new ArrayList<String>();
    ArrayList<Integer> itemAmounts = new ArrayList<Integer>();

    int playerWorldX;
    int playerWorldY;

    int currentDay;
    int dayState;
    int dayCounter;
    float filterAlpha;
    boolean haveKey;
}
