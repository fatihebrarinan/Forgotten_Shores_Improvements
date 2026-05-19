package entity;

public interface Attackable {
    void takeDamage(int damage);

    int getHealth();

    void setHealth(int health);

    String getRequiredWeaponName();
}