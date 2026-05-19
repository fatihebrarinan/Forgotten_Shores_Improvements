package entity;

public interface Attackable {
    void takeDamage(int damage);
    String getRequiredWeaponName();
}