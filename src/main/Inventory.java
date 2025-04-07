package main;

import object.Item;

public class Inventory 
{
    private Item[] slots = new Item[5]; // 5 inventory slots
    private int selectedSlot = 0;

    public Item getItem( int index ) 
    {
        return (index >= 0 && index < slots.length) ? slots[index] : null;
    }

    public void setItem( int index, Item item ) 
    {
        if ( index >= 0 && index < slots.length ) 
        {
            slots[index] = item;
        }
    }

    public int getSelectedSlot()  
    {
        return selectedSlot;
    }

    public void setSelectedSlot( int index ) 
    {
        if ( index >= 0 && index < slots.length ) 
        {
            selectedSlot = index;
        }
    }

    public int getSlotCount() 
    {
        return slots.length;
    }

    public int size() {
        return slots.length;
    }

    public Item get(int index) {
        return getItem(index);
    }

    public void set(int index, Item item) {
        setItem(index, item);
    }
    public Item[] getSlots() {
        return this.slots;
    }
}