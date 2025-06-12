package main;

import java.util.List;
import object.Item;

public class CraftingRecipe {
    public Item result;
    public List<Material> materials;

    public CraftingRecipe(Item result, List<Material> materials) {
        this.result = result;
        this.materials = materials;
    }
}