package main;

import java.util.ArrayList;
import java.util.List;

public class CraftingCategory {
    public String name;
    public List<CraftingRecipe> recipes = new ArrayList<>();

    public CraftingCategory(String name) {
        this.name = name;
    }

    public void addRecipe(CraftingRecipe recipe) {
        recipes.add(recipe);
    }
}