package main;

import object.Item;

public class Material {
    public Item item;
    public int quantity;

    public Material(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }
}