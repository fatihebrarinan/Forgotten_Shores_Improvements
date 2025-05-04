package save;

import java.io.Serializable;
import java.util.ArrayList;

import entity.Entity;

public class Storage implements Serializable{
    
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
    Entity currentWeapon;
    Entity currentShield;

    String direction;
    
    int defense;
    int attack;

    ArrayList <String> itemNames = new ArrayList<String>();
    ArrayList <Integer> itemAmounts = new ArrayList<Integer>();

    String mapObjectNames [][] ;
    int mapObjectWorldX [][] ; 
    int mapObjectWorldY [][] ;
    boolean[][] treeIsHarvestable;

    String[][] iTileNames;
    int[][] iTileWorldX;
    int[][] iTileWorldY;
    int[][] interactiveTreeImageIndex;

    int playerWorldX;
    int playerWorldY;

    ArrayList<String> monsterNames = new ArrayList<>();
    ArrayList<Integer> monsterWorldX = new ArrayList<>();
    ArrayList<Integer> monsterWorldY = new ArrayList<>();
    ArrayList<Integer> monsterHealth = new ArrayList<>();

    int currentDay;
    int dayState;
    int dayCounter;
    float filterAlpha;
}
