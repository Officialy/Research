package mods.officialy.researchmod;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Node {

    private final ResourceLocation researchName;
    private final List<Node> prerequisites;
    private boolean activated;
    private final List<Pair<Ingredient, Integer>> prerequisiteItems;

    Node(ResourceLocation name, List<Pair<Ingredient, Integer>> prerequisiteItems) {
        this.researchName = name;
        this.prerequisites = new ArrayList<>();
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

    public List<Node> getPrerequisites() {
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