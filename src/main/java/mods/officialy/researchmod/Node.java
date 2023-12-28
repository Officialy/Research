package mods.officialy.researchmod;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node {

    private final ResourceLocation researchName;
    private final List<ResourceLocation> prerequisites;
    private boolean activated;
    private final List<Pair<Ingredient, Integer>> prerequisiteItems;

    Node(ResourceLocation name, List<ResourceLocation> prerequisites, List<Pair<Ingredient, Integer>> prerequisiteItems) {
        this.researchName = name;
        this.prerequisites = prerequisites;
        this.activated = false;
        this.prerequisiteItems = prerequisiteItems;
    }

    boolean isActivated() {
        return activated;
    }

    void activate() {
        this.activated = true;
    }

    void consumeItems() {
        // Logic to consume items
        System.out.println("Items consumed for research: " + researchName);
    }

    // Getters

    public ResourceLocation getResearchName() {
        return researchName;
    }

    public List<ResourceLocation> getPrerequisites() {
        return prerequisites;
    }

    public List<Pair<Ingredient, Integer>> getPrerequisiteItems() {
        return prerequisiteItems;
    }

    @Override
    public String toString() {
        List<String> preReqs = new ArrayList<>();
        prerequisiteItems.forEach(ingredientIntegerPair -> preReqs.add(Arrays.toString(ingredientIntegerPair.getFirst().getItems()) + " Count: " + ingredientIntegerPair.getSecond()));
        return "Node{" +
                "researchName=" + researchName +
                ", prerequisites=" + prerequisites +
                ", activated=" + activated +
                ", prerequisiteItems=" + preReqs +
                '}';
    }
}